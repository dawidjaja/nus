package urlcrawler.crawler.jsoup;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import urlcrawler.DelayManager;
import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.data.UrlSupplier;
import urlcrawler.indexer.IndexedUrlTree;

public class Crawler implements CrawlerRunnable {
    private final static Logger logger = LogManager.getLogger(Crawler.class);

    private UrlSupplier urlSupplier;
    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;
    private Queue<Url> urlQueue = new LinkedList<>();

    private boolean stopped = false;

    public Crawler(UrlSupplier urlSupplier, BufferedUrlList bufferedUrlList,
                   IndexedUrlTree indexedUrlTree) {
        this.urlSupplier = urlSupplier;
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
    }

    // in milli seconds
    private int politenessDelay = 100;

    @Override
    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    @Override
    public void run() {
        try {
            String nextSeed;
            while (!stopped) {
                // [EVENT 01] ct_selecturl.0.0 && [EVENT 05] ct_selectUrl.1.0
                DelayManager.delayForBothCTEvents(1, 5);

                nextSeed = urlSupplier.consume();
                if (nextSeed == null) {
                    break;
                }

                if (!Url.isValid(nextSeed)) {
                    continue;
                }

                urlQueue.add(new Url(nextSeed, null, null));
                while (!urlQueue.isEmpty() && !stopped) {
                    Url url = urlQueue.poll();
                    if (!indexedUrlTree.checkAndMarkUrl(url)) {
                        continue;
                    }

                    Thread.sleep(politenessDelay);
                    visit(url);

                    // Each crawler only need to crawl once and stopped for this project
                    stopped = true;
                }
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
        try {
            // [EVENT 04] ct_writeToBUL_success.0.0  && [EVENT 12] ct_writeToBUL_success.1.0
            DelayManager.delayForBothCTEvents(4, 12);

            bufferedUrlList.add(url);
        } catch (InterruptedException e) {
            logger.error("Something went wrong while adding data to the buffer", e);
        }
    }
}
