package urlcrawler.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.data.Url;

public class StatisticReporter implements Runnable {
    private static final Logger logger = LogManager.getLogger(StatisticReporter.class);

    private String resultStatisticFilePath;

    public StatisticReporter(String resultStatisticFilePath) {
        this.resultStatisticFilePath = resultStatisticFilePath;
    }

    @Override
    public void run() {
        File file = new File(resultStatisticFilePath);

        try {
            // open file
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.printf("[%s] Number of crawled URLs: %d\n", new Date(), Url.getNumOfCrawledUrls());

            // close file
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            logger.error("Error in writing statistic", e);
        }
    }
}
