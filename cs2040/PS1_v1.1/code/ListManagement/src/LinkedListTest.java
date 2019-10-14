import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LinkedListTest {

    @Test
    public void testAdd() throws Exception {
        LinkedList testObj = new LinkedList();
        testObj.add(10);
        assertEquals(testObj.toString(), "10");
        testObj.add(12);
        assertEquals(testObj.toString(), "10 12");
    }

    @Test
    public void testSearch1() throws Exception {
        LinkedList testList = new LinkedList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);jkj
        testList.add(4);jkjkjkjkj

        boolean result = testList.search(2500);
        assertEquals(result, false);
    }

    @Test
    public void testSearch2() throws Exception {
        LinkedList testList = new LinkedList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(1000);

        assertEquals(result, true);
    }

    @Test
    public void testSearch3() throws Exception {
        LinkedList testList = new LinkedList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(0);

        assertEquals(result, true);
    }

    @Test
    public void testSearch4() throws Exception {
        LinkedList testList = new LinkedList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(4);

        assertEquals(result, true);
    }

    @Test
    public void testToString1() throws Exception {
        LinkedList testList = new LinkedList();
        testList.add(3);
        testList.add(15);
        testList.add(25);
        testList.add(1000);
        testList.add(1500);

        String result = testList.toString();
        String expected = "3 15 25 1000 1500";
        assertEquals(result, expected);
    }

    @Test
    public void testToString2() throws Exception {
        LinkedList testList = new LinkedList();

        String result = testList.toString();
        String expected = "";
        assertEquals(result, expected);
    }


}