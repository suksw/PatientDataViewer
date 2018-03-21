import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

public class OnlineDataFetcher {
    private static Logger log = LogManager.getLogger(OnlineDataFetcher.class.getName());
    private JSONParser jsonParser;

    OnlineDataFetcher() {
        this.jsonParser = new JSONParser();
    }

    public List<String> fetchAndStorePatientData() throws ExceptionDataFetcher {
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
            List<String> patientIds = collectIDs(allPatientsJsonArray);
            JSONObject patientsJsonObject = new JSONObject();
            patientsJsonObject.put(DemoConstants.patients, allPatientsJsonArray);
            writeJsonObjectToFile(patientsJsonObject, Config.patientsJsonFile);
            log.debug("Completed fetching and storing patient data");
            return patientIds;
        } catch (ParseException | IOException e) {
            String msg = "error while fetching and storing patient data";
            throw new ExceptionDataFetcher(msg, e);
        }
    }

    public List<String> fetchAndStoreEncounterData(List<String> patientIds) throws ExceptionDataFetcher {
        log.debug("fetching and storing encounter data....");
        List<String> encounterIds = new ArrayList<>();
        JSONObject encountersJsonObject = new JSONObject();
        for (String patientId : patientIds ) {
            String url = Config.encountersUrl + patientId ;
            try {
                JSONObject encountersJsonObjectPerPatient = (JSONObject) jsonParser.parse(readUrl(url));
                JSONArray encounterEntryJsonArray = (JSONArray) encountersJsonObjectPerPatient.get(DemoConstants.entry);
                if (encounterEntryJsonArray != null && !encounterEntryJsonArray.isEmpty()) {
                    encountersJsonObject.put(patientId.toString(), encounterEntryJsonArray);
                    encounterIds.addAll(collectIDs(encounterEntryJsonArray));
                }
            } catch (ParseException | IOException e) {
                String msg = "error while fetching and storing" + DemoConstants.encounters;
                throw new ExceptionDataFetcher(msg, e);
            }
        }
        try {
            writeJsonObjectToFile(encountersJsonObject, Config.encountersJsonFile);
        } catch (IOException e) {
            String msg = "error while writing to file" + Config.encountersJsonFile;
            throw new ExceptionDataFetcher(msg, e);
        }
        log.debug("Completed fetching and storing encounter data");
        return encounterIds;
    }

    public void fetchAndStoreObservationData(List<String> encounterIDs) throws ExceptionDataFetcher {
        log.debug("fetching and storing observation data....");
        JSONObject observationsJsonObject = new JSONObject();
        for (String encounterID : encounterIDs) {
            String url = Config.observationsUrl + encounterID ;
            try {
                JSONObject encountersJsonObjectPerPatient = (JSONObject) jsonParser.parse(readUrl(url));
                JSONArray subToStoreJsonArray = (JSONArray) encountersJsonObjectPerPatient.get(DemoConstants.entry);
                if (subToStoreJsonArray != null && !subToStoreJsonArray.isEmpty()) {
                    observationsJsonObject.put(encounterID,subToStoreJsonArray);
                }
            } catch (ParseException | IOException e) {
                String msg = "error while fetching and storing" + DemoConstants.observations;
                throw new ExceptionDataFetcher(msg, e);
            }
        }
        try {
            writeJsonObjectToFile(observationsJsonObject, Config.observationJsonFile);
        } catch (IOException e) {
            String msg = "error while writing to file" + Config.observationJsonFile;
            throw new ExceptionDataFetcher(msg, e);
        }
        log.debug("Completed fetching and storing observation data");
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
