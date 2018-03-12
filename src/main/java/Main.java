public class Main {
    public static void main(String[] args) throws ExceptionDataGatherer {
        DataGatherer dataGatherer = new DataGatherer();
        DataOrganizer dataOrganizer = new DataOrganizer();
        DataSimplifier dataSimplifier = new DataSimplifier();

        //dataGatherer.getMalePatients();
        //dataGatherer.getFemalePatients();
        //dataOrganizer.jsonFromFileToMap();
        System.out.println(dataSimplifier.getData());
        
        //JsonFlattener jsonFlattener = new JsonFlattener();
        //jsonFlattener.createMap();
    }
}
