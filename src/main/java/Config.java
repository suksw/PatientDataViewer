public class Config {
    //URLs
    public static String femalePatientsUrl
            = "http://hapi.fhir.org/baseDstu3/Patient?_format=json&_count=1&given=hy";
    public static String malePatientsUrl
            = "http://hapi.fhir.org/baseDstu3/Patient?_format=json&_count=4&gender=male";
    public static String encountersUrl
            = "http://hapi.fhir.org/baseDstu3/Encounter?_format=json&patient=";
    public static String observationsUrl
            = "http://hapi.fhir.org/baseDstu3/Observation?_format=json&_count=10&encounter=";

    //File to store
    public static String patientsJsonFile = "src/main/resources/patients.json";
    public static String encountersJsonFile = "src/main/resources/encounters.json";
    public static String observationJsonFile = "src/main/resources/observation.json";
}
