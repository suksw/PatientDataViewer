import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DataGatherer {

    private static String charset_UTF_8 = "UTF-8";

    public void getFemalePatients() throws ExceptionDataGatherer {
        String url = "http://hapi.fhir.org/baseDstu3/Patient?_format=json&_count=3&gender=female&given=a";
        writeToFile(url, "src/main/resources/femalePatients.json");
    }

    public void getMalePatients() throws ExceptionDataGatherer {
        String url = "http://hapi.fhir.org/baseDstu3/Patient?_format=json&_count=2&gender=male";
        writeToFile(url, "src/main/resources/malePatients.json");
    }

    private void writeToFile(String stringUrl, String storingFileName) throws ExceptionDataGatherer {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(stringUrl);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);


            inputStream = url.openStream();
            outputStream =
                    new FileOutputStream(new File(storingFileName),true);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Done!");
        } catch (IOException e) {
            throw new ExceptionDataGatherer(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }


}
