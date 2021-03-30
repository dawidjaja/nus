package merger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

public class Main {

    final static Properties config = new Properties();

    private static String configFile;

    private static String resultIndexFolderPath;
    private static String resultContentFolderPath;
    private static String resultMergeFilePath;
    private static int ignoredUrlCount = 0;
    private static int deadUrlCount = 0;
    private static int duplicateCount = 0;
    private static int validUrlCount = 0;
    private static int totalCount = 0;
    private static double averageUrlLength = 0;
    private static ArrayList<String> deadUrls = new ArrayList<>();
    private static ArrayList<String> ignoredUrls = new ArrayList<>();
    private static ArrayList<String> validUrls = new ArrayList<>();
    private static HashSet<String> urlSet = new HashSet<>();

    private static BufferedWriter bw;

    public static void main (String[] args) {

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            readArgs(cmd);
            readConfig(configFile);

            initWriter();

            mergeUrl();

            bw.close();

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        } catch (Exception e) {
            System.err.println("Something went wrong." + e);
        }
    }

    private static void initWriter() {
        File resultFile = new File(resultMergeFilePath);
        try {
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }

            FileWriter fw = new FileWriter(resultFile);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Merge the url from resultContentFolderPath into res.txt.
     */
    private static void mergeUrl() {
        readUrls();
        printStatistics();
        writeToFile();
    }

    private static void printStatistics() {

        validUrlCount = validUrls.size();
        ignoredUrlCount = ignoredUrls.size();
        deadUrlCount = deadUrls.size();

        System.out.println(String.format("%d total urls are crawled", totalCount));
        System.out.println(String.format("There are %d duplicate urls", duplicateCount));
        System.out.println(String.format("%d out of %d urls are stored", validUrlCount, totalCount));
        System.out.println(String.format("%d out of %d urls are ignored", ignoredUrlCount, totalCount));
        System.out.println(String.format("%d out of %d urls are dead url", deadUrlCount, totalCount));
        System.out.println(String.format("The average url length is %.2f characters", averageUrlLength));
        System.out.println("The total content size is " + getContentSize());
    }

    private static String getContentSize() {
        return FileUtils.byteCountToDisplaySize(FileUtils.sizeOfDirectory(new File(resultContentFolderPath)));
    }

    private static void readUrls() {
        File resultIndexDirectory = new File(resultIndexFolderPath);
        File[] directoryListing = resultIndexDirectory.listFiles();
        if (directoryListing != null) {
            for (File hash : directoryListing) {
                try (BufferedReader br = new BufferedReader(new FileReader(hash))) {
                    String url;
                    while ((url = br.readLine()) != null) {
                        String[] urls = url.split(" ");
                        String content;

                        if (urlSet.contains(urls[1])) {
                            duplicateCount++;
                            continue;
                        }
                        urlSet.add(urls[1]);
                        int urlLength = urls[1].length();
                        totalCount++;
                        averageUrlLength += (1.0 * urlLength - averageUrlLength) / totalCount ;

                        if (urls.length == 4) {
                            if (urls[3].equals("IGNORED")) {
                                content = urls[2] + " --> " +
                                    urls[1] + " : ignored\n";
                                ignoredUrls.add(content);
                            } else {
                                content = urls[2] + " --> " +
                                    urls[1] + " : dead-url\n";
                                deadUrls.add(content);
                            }
                        } else {
                            String location = resultContentFolderPath + "/" + hash.getName() + "/" + urls[0];
                            content = urls[2] + " --> " +
                                urls[1] + " : " +
                                location + "\n";
                            validUrls.add(content);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println(String.format("%s is not a directory", resultIndexDirectory));
        }
    }

    private static void writeToFile() {
        try {
            for (String content: validUrls) {
                bw.write(content);
            }
            for (String content: ignoredUrls) {
                bw.write(content);
            }
            for (String content: deadUrls) {
                bw.write(content);
            }
            System.out.println("res.txt successfully merged");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readArgs(CommandLine cmd) throws IllegalStateException {
        configFile = cmd.getOptionValue("config", "src/main/resources/config.properties");
    }

    private static void readConfig(String configFilePath) {

        try {
            FileInputStream ip = new FileInputStream(configFilePath);
            config.load(ip);
            System.out.println(String.format("Reading config from %s", configFilePath));
        } catch (IOException e) {
            System.out.println("Get config from default value.");
        }
        resultIndexFolderPath = config.getProperty("resultIndexFolderPath", "data/result/index");
        resultContentFolderPath = config.getProperty("resultContentFolderPath", "data/result/content");
        resultMergeFilePath = config.getProperty("resultMergeFilePath", "data/res.txt");
    }
}
