package urlcrawler.crawler;

public interface CrawlerRunnable extends Runnable {
    public void stop();
    public void setPolitenessDelay(int politenessDelay);
}
