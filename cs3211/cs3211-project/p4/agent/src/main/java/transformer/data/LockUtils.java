package transformer;

// This class provides additioanl function for lock-datarace
public class LockUtils {
    // Return the hard coded url from id
    public static String getUrl(int urlId) {
        switch (urlId) {
            case 0:
                return "http://blankwebsite.com";
            case 1:
                return "http://google.com";
            default:
                return "http://yahoo.com";
        }
    }

    // Return the id of hard coded url
    public static int getUrlId(String url) {
        switch (url) {
            case "http://blankwebsite.com":
                return 0;
            case "http://google.com":
                return 1;
            default:
                return 2;
        }
    }

    // This util is created as we strangely can't insert assert with the current API
    public static void assertEvent(boolean condition) {
        assert condition : "Bad event detected!";
    }
}