package urlcrawler.crawler;

public interface CrawlerRunnable extends Runnable {

    public void markAsStopped();

    public void setPolitenessDelay(int politenessDelay);

}
