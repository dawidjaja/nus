package urlcrawler.crawler.jsoup;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.data.UrlSupplier;
import urlcrawler.indexer.IndexedUrlTree;
import urlcrawler.scheduler.EventScheduler;

public class Crawler implements CrawlerRunnable {
    private final static Logger logger = LogManager.getLogger(Crawler.class);
    private static int numOfCrawlers = 0;

    private UrlSupplier urlSupplier;
    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;
    private Queue<Url> urlQueue = new LinkedList<>();

    private int id;
    private volatile boolean stopped = false;

    public Crawler(UrlSupplier urlSupplier, BufferedUrlList bufferedUrlList,
                   IndexedUrlTree indexedUrlTree) {
        this.urlSupplier = urlSupplier;
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
        id = getAndIncNumOfCrawlers();
    }

    private synchronized static int getAndIncNumOfCrawlers() {
        int result = numOfCrawlers;
        numOfCrawlers++;
        return result;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                int enabled = EventScheduler.shared.requestPermissionWithPrefix(
                        "ct_selectUrl." + id + ".");
                
                if (enabled == -1) {
                    return; // return if never enabled
                }
                
                Url nextUrl = urlSupplier.consume(enabled);
                if (nextUrl == null) {
                    break;
                }

                EventScheduler.shared.releasePermission();

                // URL queue removed for simplicity

                if (!indexedUrlTree.checkAndMarkUrl(nextUrl, id)) {
                    continue;
                }

                Thread.sleep(100); // politeness delay
                visit(nextUrl);
            }
            if (stopped) {
                logger.error("Stop signal received!");
            }
        } catch (Exception e) {
            logger.error("There is an error while running crawler", e);
        }
    }

    /**
     * Initiates shutdown of the current crawling session.
     */
    @Override
    public void stop() {
        stopped = true;
    }

    private void visit(Url url) {
        logger.info("Crawling page " + url.getSpec());

        Document document;

        try {
            document = Jsoup.connect(url.getSpec())
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 " +
                            "Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .timeout(3000)
                    .get();
        } catch (Exception e) {
            logger.error("Error retrieving (" + url.getSpec() + ")", e);

            // Keep adding to the buffered URL List to be marked as broken URL
            addToBufferedUrlList(url);
            return;
        }

        Elements linkElements = document.select("a[href]");

        for (Element link : linkElements) {
            String targetUrlSpec = link.attr("abs:href");
            if (Url.isValid(targetUrlSpec)) {
                urlQueue.offer(new Url(targetUrlSpec, url.getSpec(), null));
            }
        }

        String html = document.toString();
        url.setContent(html);

        addToBufferedUrlList(url);
    }

    private void addToBufferedUrlList(Url url) {
        int enabled = EventScheduler.shared.requestPermission(new String[]{
                "ct_waitInBUL." + id + "." + url.getId(),
                "ct_writeToBUL_success." + id + "." + url.getId()});
        
        if (enabled == -1) {
            return; // terminate if never enabled
        }
        
        try {
            bufferedUrlList.add(url, enabled);
            EventScheduler.shared.releasePermission();
        } catch (InterruptedException e) {
            logger.error("Something went wrong while adding data to the buffer", e);
        }

    }
}
