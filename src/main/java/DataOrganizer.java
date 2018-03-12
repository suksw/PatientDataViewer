import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DataOrganizer {

    private JSONArray organizedPatientArray;

    DataOrganizer() {
        organizedPatientArray = new JSONArray();
    }

    public void jsonFromFileToMap(){
        ObjectMapper mapper = new ObjectMapper();
        JSONObject allPatientsJson = new JSONObject();

        try {
            Map<String, Object> femalePatientsMap = mapper.readValue(new File(
                    "src/main/resources/femalePatients.json"), new TypeReference<Map<String, Object>>() {
            });
            //System.out.println(femalePatientsMap.get("entry").getClass());
            organizeAllPatients((ArrayList) femalePatientsMap.get("entry"), allPatientsJson);

            Map<String, Object> malePatientsMap = mapper.readValue(new File(
                    "src/main/resources/malePatients.json"), new TypeReference<Map<String, Object>>() {
            });
            //System.out.println(malePatientsMap.get("entry"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void organizeAllPatients(ArrayList patients, JSONObject allPatientsJObject){
        JSONArray allPatientsJArray = new JSONArray();
        if(patients == null){
            return;
        }
        for (Object patient: patients) {
            JSONObject patientJson = organizeOnePatient((JSONObject) patient);
            allPatientsJArray.add(patientJson);
        }
        allPatientsJObject.put("simplifiedEntry", allPatientsJArray);
    }
    private JSONObject organizeOnePatient(JSONObject inputPatient){
        JSONObject rawPatient = (JSONObject) inputPatient.get("resource");
        JSONObject organizedPatient = new JSONObject();
        organizedPatient.put("id",rawPatient.get("id"));
        organizedPatient.put("name",rawPatient.get("name"));
        return organizedPatient;
    }

    public JSONArray getOrganizedPatientArray() {
        return organizedPatientArray;
    }
}
