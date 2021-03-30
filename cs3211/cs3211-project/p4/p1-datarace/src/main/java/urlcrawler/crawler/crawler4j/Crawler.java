package urlcrawler.crawler.crawler4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.indexer.IndexedUrlTree;

public class Crawler extends WebCrawler {
    final static Logger logger = LogManager.getLogger(Crawler.class);
    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;

    public Crawler(BufferedUrlList bufferedUrlList, IndexedUrlTree indexedUrlTree) {
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL webURL) {
        Url url = new Url(webURL.getURL(), referringPage.getWebURL().getURL(), null);
        return indexedUrlTree.checkAndMarkUrl(url);
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.info("Crawling page " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            try {
                bufferedUrlList.add(new Url(url, null, html));
            } catch (InterruptedException e) {
                logger.error("Something went wrong while adding data to the buffer", e);
            }
        } else {
            logger.info("Skipping a page because it is not an HTML.\nThe page is : " + page);
        }
    }
}
