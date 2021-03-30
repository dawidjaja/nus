package urlcrawler.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UrlSupplier {

    private static final Logger logger = LogManager.getLogger(UrlSupplier.class);

    private BufferedReader bufferedReader;

    /**
     * Initiate the UrlSupplier with the list from file list.txt.
     */
    public UrlSupplier(String initFilePath) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(initFilePath));
    }

    /**
     * Returns a hard-coded URL for a certain index.
     * For use in P3 to show datarace.
     */
    public synchronized Url consume(int index) {
        switch (index) {
        case 0:
            return new Url("http://blankwebsite.com", null, null);
        case 1:
            return new Url("http://google.com", null, null);
        default:
            return new Url("http://yahoo.com", null, null);
        }
    }
    
    public synchronized Url consume() {
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
