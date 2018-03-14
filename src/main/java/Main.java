import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.json.simple.JSONObject;

import java.io.File;

public class Main {
    public static void main(String[] args) throws ExceptionDataFetcher {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("log4j2.xml");
        context.setConfigLocation(file.toURI());

        DataSupplier dataSupplier = DataSupplier.getInstance();

        //Realtime
        JSONObject patientData = dataSupplier.supplyPatientDataRealtime();
        JSONObject encounterData = dataSupplier.supplyEncounterDataRealtime(patientData);
        JSONObject observationsData = dataSupplier.supplyObservationDataRealtime(encounterData);

        //Offline (when the frontend and this module both exists within the same host)
        System.out.println(dataSupplier.supplyPatientDataOffline());
        System.out.println(dataSupplier.supplyEncounterDataOffline());
        System.out.println(dataSupplier.supplyObservationDataOffline());
    }
}
