package urlcrawler.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import urlcrawler.DelayManager;

public class UrlSupplier {

    BufferedReader bufferedReader;
    ArrayList<List<String>> urlList;
    boolean getFromFile;

    /**
     * Initiate the UrlSupplier with the list from file list.txt.
     */
    public UrlSupplier(String initFilePath) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(initFilePath));
        urlList = new ArrayList<List<String>>();
        getFromFile = true;
    }

    /**
     * Returns the next URL in the list.
     * Returns null if no more URL in the list.
     */
    public synchronized String consume() throws IOException {
        // Always return the only url that will be used in this project.
        return "https://www.google.com/";
    }

    /**
     * Return the last url from the url list.
     *
     * @return last url if exist, null otherwise.
     */
    public String getLastUrl() {
        if (urlList.size() == 0) {
            return null;
        }
        List<String> urlSublist = urlList.get(urlList.size() - 1);

        String url = urlSublist.get(urlSublist.size() - 1);
        urlSublist.remove(urlSublist.size() - 1);

        if (urlSublist.isEmpty()) {
            urlList.remove(urlList.size() - 1);
        }

        return url;
    }

    /**
     * Adds the list of URLs to the back of the list. Does not check for duplicates.
     *
     * @param urls
     */
    public void supply(List<String> urls) {
        if (urls.size() != 0) {
            urlList.add(urls);
        }
    }
}
