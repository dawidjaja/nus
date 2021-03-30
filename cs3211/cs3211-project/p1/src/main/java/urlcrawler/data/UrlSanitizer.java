package urlcrawler.data;

import com.github.slugify.Slugify;

public class UrlSanitizer {
    private Slugify slugify;

    private UrlSanitizer() {
        slugify = new Slugify().withLowerCase(false);
    }

    public static final UrlSanitizer shared = new UrlSanitizer();

    public String toSlug(String s) {
        return slugify.slugify(s);
    }

    public String toHashValue(String s) {
        String hexHash = String.format("%08x", s.hashCode());
        return hexHash.substring(hexHash.length() - 3); // take only the last 3 chars
    }
}
