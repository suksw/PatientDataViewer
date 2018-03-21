import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ExceptionDataFetcher {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("log4j2.xml");
        context.setConfigLocation(file.toURI());

        OnlineDataFetcher onlineDataFetcher = new OnlineDataFetcher();
        DataSupplier dataSupplier = DataSupplier.getInstance();

        List<String> patientIds = onlineDataFetcher.fetchAndStorePatientData();
        List<String> encounterIds = onlineDataFetcher.fetchAndStoreEncounterData(patientIds);
        onlineDataFetcher.fetchAndStoreObservationData(encounterIds);

        System.out.println(dataSupplier.supplyPatientDataOffline());
        System.out.println(dataSupplier.supplyEncounterDataOffline());
        System.out.println(dataSupplier.supplyObservationDataOffline());
    }
}
