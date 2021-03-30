package urlcrawler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.data.UrlSupplier;
import urlcrawler.indexer.IndexBuildingThread;
import urlcrawler.indexer.IndexedUrlTree;

public class Main {
    final static Logger logger = LogManager.getLogger(Main.class);
    final static Properties config = new Properties();

    private static String inputFile;
    private static String outputFile;
    private static String configFile;
    private static String crawlerDuration;
    private static String numOfStoredPagesStr;
    private static long crawlerDurationInMillis;

    private static int totalBUL;
    private static int totalIBT;
    private static int numberOfCrawlerPerBUL;
    private static int bufferedUrlListSize;
    private static String resultIndexFolderPath;
    private static String resultContentFolderPath;
    private static String crawlerChoice;
    private static int politenessDelay = 100;

    private static ArrayList<BufferedUrlList> bufferedUrlLists = new ArrayList<>();
    private static ArrayList<IndexBuildingThread> indexBuildingThreads = new ArrayList<>();
    private static ArrayList<CrawlerRunnable> crawlers = new ArrayList<>();

    public static void main(String[] args) {
        Options options = specifyOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            readArgs(cmd);
            Runtime.getRuntime().addShutdownHook(new Cleanup());
            readConfig(configFile);
            startThreads(inputFile);
            cleanupAfterCrawlingDuration();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        } catch (IOException e) {
            logger.error("Error when reading url seed.", e);
        } catch (Exception e) {
            logger.error("Something went wrong.", e);
        }
    }

    private static Options specifyOptions() {
        Options options = new Options();

        Option duration = new Option("time", true, "The duration for which the crawler will run.");
        duration.setRequired(true);
        options.addOption(duration);

        Option input = new Option("input", true, "Path to file containing the initial url seed.");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("output", true, "Path to output file.");
        output.setRequired(true);
        options.addOption(output);

        Option storedPageNum = new Option("storedPageNum", true, "Number of pages to be stored.");
        storedPageNum.setRequired(true);
        options.addOption(storedPageNum);

        Option config = new Option("config", true, "Path to config file.");
        options.addOption(config);

        return options;
    }

    private static void readArgs(CommandLine cmd) throws IllegalStateException {
        inputFile = cmd.getOptionValue("input");
        outputFile = cmd.getOptionValue("output");
        crawlerDuration = cmd.getOptionValue("time");
        crawlerDurationInMillis = toMillis(crawlerDuration);
        configFile = cmd.getOptionValue("config", "src/main/resources/config.properties");
        numOfStoredPagesStr = cmd.getOptionValue("storedPageNum");

        Url.numOfAllowedContent = Integer.parseInt(numOfStoredPagesStr);
    }

    /**
     * Change the string format to milli second, only support integer followed by character h, m, or s.
     *
     * @param strDuration String format duration, e.g. 24h, 12m, 10s.
     * @return The number of milli second equivalent from the string.
     */
    private static long toMillis(String strDuration) throws IllegalStateException {
        int size = strDuration.length();
        int intPart = Integer.parseInt(strDuration.substring(0, size - 1));
        char timeUnit = strDuration.charAt(size - 1);
        switch (timeUnit) {
        case 'h':
            return intPart * 1000L * 3600L;
        case 'm':
            return intPart * 1000L * 60L;
        case 's':
            return intPart * 1000L;
        default:
            throw new IllegalStateException("Unexpected value: " + timeUnit);
        }
    }

    private static void readConfig(String configFilePath) {

        try {
            FileInputStream ip = new FileInputStream(configFilePath);
            config.load(ip);
            logger.info(String.format("Reading config from %s", configFilePath));
        } catch (IOException e) {
            logger.info("Get config from default value.");
        }

        totalBUL = Integer.parseInt(config.getProperty("totalBUL", "3"));
        totalIBT = Integer.parseInt(config.getProperty("totalIBT", "3"));
        numberOfCrawlerPerBUL = Integer.parseInt(config.getProperty("numberOfCrawlerPerBUL", "2"));
        bufferedUrlListSize = Integer.parseInt(config.getProperty("bufferedUrlListSize", "100"));

        resultIndexFolderPath = config.getProperty("resultIndexFolderPath", "data/result/index");
        resultContentFolderPath = config.getProperty("resultContentFolderPath", "data/result/content");

        crawlerChoice = config.getProperty("crawlerChoice", "jsoup");
        politenessDelay = Integer.parseInt(config.getProperty("politenessDelay", "0"));
    }

    private static CrawlerRunnable createCrawlerBasedOnChoice(UrlSupplier urlSupplier, BufferedUrlList bufferedUrlList,
                                                              IndexedUrlTree indexedUrlTree) throws Exception {
        CrawlerRunnable crawler;
        switch (crawlerChoice) {
        case "jsoup":
            crawler = new urlcrawler.crawler.jsoup.Crawler(urlSupplier, bufferedUrlList, indexedUrlTree);
            break;
        case "crawler4j":
            crawler = new urlcrawler.crawler.crawler4j.CrawlerController(urlSupplier, bufferedUrlList, indexedUrlTree);
            break;
        default:
            String errorMsg = String.format("The crawler choice `%s` is not supported", crawlerChoice);
            throw new Exception(errorMsg);
        }

        crawler.setPolitenessDelay(politenessDelay);
        return crawler;
    }

    private static void startThreads(String initFilePath) throws Exception {
        UrlSupplier urlSupplier = new UrlSupplier(initFilePath);
        IndexedUrlTree indexedUrlTree = new IndexedUrlTree(resultIndexFolderPath, resultContentFolderPath, outputFile);

        for (int i = 0; i < totalBUL; i++) {
            bufferedUrlLists.add(new BufferedUrlList(bufferedUrlListSize));
        }

        for (int i = 0; i < totalIBT; i++) {
            indexBuildingThreads.add(new IndexBuildingThread(bufferedUrlLists.get(i), indexedUrlTree));
        }

        int totalCrawler = totalBUL * numberOfCrawlerPerBUL;
        for (int i = 0; i < totalCrawler; i++) {
            // Modulo instead of division between i and numberOfCrawlerPerBUl to follow P2
            BufferedUrlList bufferedUrlList = bufferedUrlLists.get(i % totalBUL);
            CrawlerRunnable crawler = createCrawlerBasedOnChoice(urlSupplier, bufferedUrlList, indexedUrlTree);
            crawlers.add(crawler);
        }

        for (Integer i = 0; i < indexBuildingThreads.size(); ++i) {
            new Thread(indexBuildingThreads.get(i), i.toString()).start();
        }

        for (Integer i = 0; i < crawlers.size(); ++i) {
            new Thread(crawlers.get(i), i.toString()).start();
        }

        logger.info("Initialization complete!");
    }

    private static void cleanupAfterCrawlingDuration() throws InterruptedException {
        logger.error(String.format("Crawling for %s", crawlerDuration));
        Thread.sleep(crawlerDurationInMillis);
        logger.error("Times is up, Shutting down...");

        Runtime.getRuntime().halt(0);
    }

    private static class Cleanup extends Thread {
        public void run() {
            logger.error("Interrupt received. Shutting down...");

            for (CrawlerRunnable crawler : crawlers) {
                crawler.stop();
            }
            for (IndexBuildingThread indexThread : indexBuildingThreads) {
                indexThread.stop();
            }

            logger.error(String.format("TODO: convert the IUT to %s.", outputFile));
            logger.error("Shutdown complete.");
        }
    }
}
