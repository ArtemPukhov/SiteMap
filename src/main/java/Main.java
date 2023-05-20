import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static String fileName;
    static String url = "https://www.dugaweld.ru/";

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        try {
            fileName = new URL(url).getHost().replace(".", "_");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        SiteTask task = new SiteTask(url);
        String siteMap;

        siteMap = new ForkJoinPool().invoke(task);
        writeToFile(siteMap);
        System.out.println("Программа заняла " + ((System.currentTimeMillis() - start)/1000) + " сек.");
    }

    protected static void writeToFile(String string) {
        if (!Files.exists(Paths.get("src/main/resources"))) new File("src/main/resources").mkdir();
        String filePath = "src/main/resources".concat(fileName).concat(".").concat("txt");
        File file = new File(filePath);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert writer != null;
        writer.write(string);
        writer.flush();

    }
}

