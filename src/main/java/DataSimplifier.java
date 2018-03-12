import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class DataSimplifier {


    public JSONObject getData() throws ExceptionDataGatherer {
        JSONParser parser = new JSONParser();
        try {
            Object femalePatients = parser.parse(new FileReader(
                    "src/main/resources/femalePatients.json"));
            Object malePatients = parser.parse(new FileReader(
                    "src/main/resources/malePatients.json"));

            JSONObject femalePatientsJObject = (JSONObject) femalePatients;
            JSONArray femalePatientsJArray = (JSONArray) femalePatientsJObject.get("entry");

            JSONObject malePatientsJObject = (JSONObject) malePatients;
            JSONArray malePatientsJArray = (JSONArray) malePatientsJObject.get("entry");

            JSONArray allPatientsJArray = new JSONArray();
            allPatientsJArray.addAll(femalePatientsJArray);
            allPatientsJArray.addAll(malePatientsJArray);

            JSONObject allPatientsJObject = new JSONObject();
            allPatientsJObject.put("patients", allPatientsJArray);
            return allPatientsJObject;

        } catch (IOException | ParseException e) {
            throw new ExceptionDataGatherer(e);
        }
    }

}

