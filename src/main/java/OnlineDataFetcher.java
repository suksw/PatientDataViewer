import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class OnlineDataFetcher {
    private static Logger log = LogManager.getLogger(OnlineDataFetcher.class.getName());
    private JSONParser jsonParser;

    OnlineDataFetcher() {
        this.jsonParser = new JSONParser();
    }

    public void fetchAndStorePatientData() throws ExceptionDataFetcher {
        log.debug("fetching and storing patient data....");
        try {
            String femalePatientsString = readUrl(Config.femalePatientsUrl);
            String malePatientsString = readUrl(Config.malePatientsUrl);


            Object femalePatients = jsonParser.parse(femalePatientsString);
            Object malePatients = jsonParser.parse(malePatientsString);

            JSONObject femalePatientsJObject = (JSONObject) femalePatients;
            JSONObject malePatientsJObject = (JSONObject) malePatients;

            JSONArray femalePatientsJArray = (JSONArray) femalePatientsJObject.get(DemoConstants.entry);
            JSONArray malePatientsJArray = (JSONArray) malePatientsJObject.get(DemoConstants.entry);

            JSONArray allPatientsJsonArray = new JSONArray();
            if (femalePatientsJArray != null && !femalePatientsJArray.isEmpty()) {
                allPatientsJsonArray.addAll(femalePatientsJArray);
            }
            if (malePatientsJArray != null && !malePatientsJArray.isEmpty()) {
                allPatientsJsonArray.addAll(malePatientsJArray);
            }
            JSONObject patientsJsonObject = new JSONObject();
            patientsJsonObject.put(DemoConstants.patients, allPatientsJsonArray);
            writeJsonObjectToFile(patientsJsonObject, Config.patientsJsonFile);
            log.debug("Completed fetching and storing patient data");
        } catch (ParseException | IOException e) {
            String msg = "error while fetching and storing patient data";
            throw new ExceptionDataFetcher(msg, e);
        }
    }

    public void fetchAndStoreEncounterData(JSONObject patientsJsonObject) throws ExceptionDataFetcher {
        log.debug("fetching and storing encounter data....");
        fetchAndStoreDataUsingJsonObject(patientsJsonObject, DemoConstants.patients,
                Config.encountersUrl, DemoConstants.encounters, Config.encountersJsonFile);
        log.debug("Completed fetching and storing encounter data");
    }

    public void fetchAndStoreObservationData(JSONObject encountersJsonObject) throws ExceptionDataFetcher {
        log.debug("fetching and storing observation data....");
        fetchAndStoreDataUsingJsonObject(encountersJsonObject, DemoConstants.encounters,
                Config.observationsUrl, DemoConstants.observations, Config.observationJsonFile);
        log.debug("Completed fetching and storing observation data");
    }


    private void fetchAndStoreDataUsingJsonObject(JSONObject inputJsonObject, String inputResourceType,
                                                  String fetchingUrl, String toStoreResourceType,
                                                  String toStoreFileName) throws ExceptionDataFetcher {
        List<String> _IDs = collectIDs((JSONArray) inputJsonObject.get(inputResourceType));
        JSONArray toStoreJsonArray = new JSONArray();
        for (String _ID : _IDs ) {
            String url = fetchingUrl + _ID ;
            try {
                JSONObject subToStoreJsonObject = (JSONObject) jsonParser.parse(readUrl(url));
                JSONArray subToStoreJsonArray = (JSONArray) subToStoreJsonObject.get(DemoConstants.entry);
                if (subToStoreJsonArray != null && !subToStoreJsonArray.isEmpty()) {
                    toStoreJsonArray.addAll(subToStoreJsonArray);
                }
            } catch (ParseException | IOException e) {
                String msg = "error while fetching and storing" + toStoreResourceType;
                throw new ExceptionDataFetcher(msg, e);
            }
        }
        JSONObject toStoreJsonObject = new JSONObject();
        toStoreJsonObject.put(toStoreResourceType, toStoreJsonArray);
        try {
            writeJsonObjectToFile(toStoreJsonObject, toStoreFileName);
        } catch (IOException e) {
            String msg = "error while writing to file" + toStoreFileName;
            throw new ExceptionDataFetcher(msg, e);
        }
    }

    private static String readUrl(String urlString) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            return stringBuilder.toString();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    private static void writeJsonObjectToFile(JSONObject jsonObject, String fileName) throws IOException {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toJSONString());
            log.debug("Successfully Copied JSON Object to File: " + fileName);
        }
    }

    private List<String> collectIDs(JSONArray jsonArray){
        List<String> _IDs = new ArrayList<>();
        for(Object object : jsonArray){
            if ( object instanceof JSONObject ) {
                String id = (String)((JSONObject)((JSONObject) object)
                        .get(DemoConstants.resource))
                        .get(DemoConstants.id);
                _IDs.add(id);
            }
        }
        return _IDs;
    }

}
