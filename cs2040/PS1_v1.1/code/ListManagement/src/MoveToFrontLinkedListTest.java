import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MoveToFrontLinkedListTest {
    @Test
    public void testSearch1() throws Exception {
        MoveToFrontLinkedList testList = new MoveToFrontLinkedList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(2500);

        String expected = "0 100 1000 13 4";
        assertEquals(result, false);
        assertEquals(testList.toString(), expected);
    }

    @Test
    public void testSearch2() throws Exception {
        MoveToFrontLinkedList testList = new MoveToFrontLinkedList();
        testList.add(3);
        testList.add(15);
        testList.add(25);
        testList.add(1000);
        testList.add(1500);

        boolean result = testList.search(1500);
        String expected1 = "1500 3 15 25 1000";
        assertEquals( result, true);
        assertEquals(testList.toString(), expected1);

        boolean result2 = testList.search(15);
        String expected2 = "15 1500 3 25 1000";
        assertEquals(result2, true);
        assertEquals(testList.toString(), expected2);

        boolean result3 = testList.search(1500);
        String expected3 = "1500 15 3 25 1000";
        assertEquals(result3, true);
        assertEquals(testList.toString(), expected3);
    }

}