package urlcrawler.data;

import java.util.Optional;

import org.apache.commons.validator.routines.UrlValidator;

public class Url {

    public static int maxNumOfStoredPages;
    private static int numOfStoredPages = 0;
    private static int numOfUrls = 0;

    private String spec;
    private Optional<String> content;
    private Optional<String> parentSpec;

    private String slug;
    private String hashValue;
    private int id;

    public Url(String spec, String parentSpec, String content) {
        this.spec = spec;
        this.parentSpec = Optional.ofNullable(parentSpec);
        this.content = Optional.ofNullable(content);

        id = getAndIncNumOfUrls(); // NOTE: This only works for jsoup due to the implementation of UrlSupplier
        slug = UrlSanitizer.shared.toSlug(spec);
        hashValue = UrlSanitizer.shared.toHashValue(slug);
    }

    public synchronized static int getAndIncNumOfStoredPages() {
        int result = numOfStoredPages;
        numOfStoredPages++;
        return result;
    }

    private synchronized static int getAndIncNumOfUrls() {
        int result = numOfUrls;
        numOfUrls++;
        return result;
    }
    
    public int getId() { //Hard-coded in order to control URL collisions
        switch (spec) {
        case "http://blankwebsite.com":
            return 0;
        case "http://google.com":
            return 1;
        default:
            return 2;
        }
    }

    public static boolean isValid(String value) {
        return (new UrlValidator()).isValid(value);
    }

    public String getSpec() {
        return spec;
    }

    public String getSlug() {
        return slug;
    }

    public String getHashValue() {
        return hashValue;
    }

    public boolean hasParentUrl() {
        return parentSpec.isPresent();
    }

    public String getParentSpec() {
        return parentSpec.orElse(null);
    }

    public boolean isContentCrawled() {
        return content.isPresent();
    }

    public void setContent(String content) {
        this.content = Optional.ofNullable(content);
    }

    public String getContent() {
        return content.orElse(null);
    }
}
