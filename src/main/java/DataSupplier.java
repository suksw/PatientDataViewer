import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class DataSupplier {
    private static DataSupplier dataSupplier = new DataSupplier();
    private OnlineDataFetcher onlineDataFetcher;

    public static DataSupplier getInstance() {
        return dataSupplier;
    }

    private DataSupplier() {
        this.onlineDataFetcher = new OnlineDataFetcher();
    }

    public JSONObject supplyPatientDataOffline() throws ExceptionDataFetcher {
        return readJsonObjectFromFile(Config.patientsJsonFile);
    }
    public JSONObject supplyEncounterDataOffline() throws ExceptionDataFetcher {
        return readJsonObjectFromFile(Config.encountersJsonFile);
    }
    public JSONObject supplyObservationDataOffline() throws ExceptionDataFetcher {
        return readJsonObjectFromFile(Config.observationJsonFile);
    }

    public JSONObject readJsonObjectFromFile(String fileName) throws ExceptionDataFetcher {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(new FileReader(fileName));
        } catch (IOException | ParseException e) {
            throw new ExceptionDataFetcher(e);
        }
    }






}

