import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;

public class SiteTask extends RecursiveTask<String> {

    String url;

    static CopyOnWriteArrayList<String> urls1 = new CopyOnWriteArrayList<>();

    public SiteTask(String url) {
        this.url = url.trim();
    }

    @Override
    protected String compute() {
        String format;
        String tab = "\t";

        if (splitter(url) > 2) {
            tab = tab.repeat(splitter(url));
            format = tab + url.trim();
        } else {
            format = url.trim();
        }

        StringBuilder sb = new StringBuilder(format + "\n");
        List<SiteTask> urls = new CopyOnWriteArrayList<>();
        try {
            Thread.sleep(150);
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0").get();
            Elements elements = doc.select("a[href]");

            for (Element element : elements) {
                String newUrl = element.absUrl("href");
                if (!newUrl.isEmpty() &&
                        newUrl.startsWith("https://www.dugaweld.ru/") &&
                        !newUrl.contains("#") &&
                        !urls1.contains(newUrl) &&
                        !newUrl.endsWith("jpg") &&
                        !newUrl.endsWith("png") &&
                        !newUrl.endsWith("jpeg") &&
                        !newUrl.endsWith("JPG")) {
                    System.out.println(newUrl);
                    SiteTask task = new SiteTask(newUrl);
                    task.fork();
                    urls.add(task);
                    urls1.add(newUrl);
                    System.out.println(urls1.size());
                }
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
//        urls.sort(Comparator.comparing((SiteTask o) -> o.url));
        for (SiteTask link : urls) {

            sb.append(link.join());
        }
        return sb.toString();
    }

    public static int splitter(String line) {
        String[] lines = line.split("/");
        return lines.length;
    }
}
