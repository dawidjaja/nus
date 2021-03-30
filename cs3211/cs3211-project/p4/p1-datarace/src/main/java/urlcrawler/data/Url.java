package urlcrawler.data;

import org.apache.commons.validator.routines.UrlValidator;

public class Url {
    public static volatile int numOfAllowedContent;

    private String spec;
    private String content;
    private String parentSpec;

    private String slug;
    private String hashValue;

    private boolean deadUrl;

    public Url(String spec, String parentSpec, String content) {
        this.spec = spec;
        this.parentSpec = parentSpec;
        this.content = content;
        this.deadUrl = false;

        slug = UrlSanitizer.shared.toSlug(spec);
        hashValue = UrlSanitizer.shared.toHashValue(slug);
    }

    public synchronized static boolean getPermissionToSaveContent() {
        if (numOfAllowedContent > 0) {
            numOfAllowedContent--;
            return true;
        } else {
            return false;
        }
    }

    public void markAsDeadUrl() {
        deadUrl = true;
    }

    public boolean isDeadUrl() {
        return deadUrl;
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
        return parentSpec != null;
    }

    public String getParentSpec() {
        return parentSpec;
    }

    public boolean isContentCrawled() {
        return content != null;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
