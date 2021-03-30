package urlcrawler.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UrlSupplier {
    private static final Logger logger = LogManager.getLogger(UrlSupplier.class);

    private volatile BufferedReader bufferedReader;

    /**
     * Initiate the UrlSupplier with the list from file list.txt.
     */
    public UrlSupplier(String initFilePath) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(initFilePath));
    }

    /**
     * Returns the next URL in the list.
     * Returns null if no more URL in the list.
     */
    public synchronized Url consume() throws IOException {
        if (bufferedReader == null) {
            return null;
        }

        try {
            String urlSpec = bufferedReader.readLine();
            while (urlSpec != null && !Url.isValid(urlSpec)) {
                urlSpec = bufferedReader.readLine();
            }
            if (urlSpec == null) {
                bufferedReader.close();
                bufferedReader = null;
                return null;
            }
            return new Url(urlSpec, null, null);

        } catch (IOException e) {
            logger.error("Error in reading list of seed urls.", e);
            bufferedReader = null;
            return null;
        }
    }
}
