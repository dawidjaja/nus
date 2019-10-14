import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MarkNotFoundListTest {
    @Test
    public void testSearch1() throws Exception {
        MarkNotFoundList testList = new MarkNotFoundList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(2500);

        String expected = "-2500 0 100 1000 13 4";
        assertEquals(result, false);
        assertEquals(testList.toString(), expected);
    }

    @Test
    public void testSearch2() throws Exception {
        MarkNotFoundList testList = new MarkNotFoundList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(13);

        String expected = "13 0 100 1000 4";
        assertEquals(result, true);
        assertEquals(testList.toString(), expected);
    }

}