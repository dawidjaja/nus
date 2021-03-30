package urlcrawler.data;

import java.util.Optional;

import org.apache.commons.validator.routines.UrlValidator;

public class Url {

    public static int maxNumOfStoredPages;
    private static int numOfStoredPages = 0;

    private String spec;
    private Optional<String> content;
    private Optional<String> parentSpec;

    private String slug;
    private String hashValue;

    public Url(String spec, String parentSpec, String content) {
        this.spec = spec;
        this.parentSpec = Optional.ofNullable(parentSpec);
        this.content = Optional.ofNullable(content);

        slug = UrlSanitizer.shared.toSlug(spec);
        hashValue = UrlSanitizer.shared.toHashValue(slug);
    }

    public synchronized static int getAndIncNumOfStoredPages() {
        int result = numOfStoredPages;
        numOfStoredPages++;
        return result;
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
