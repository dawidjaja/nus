import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MyFastListTest {

    @Test
    public void testSearch1() throws Exception {
        MyFastList testList = new MyFastList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(2500);
        assertEquals(result, false);
    }

    @Test
    public void testSearch2() throws Exception {
        MyFastList testList = new MyFastList();
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
        MyFastList testList = new MyFastList();
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
        MyFastList testList = new MyFastList();
        testList.add(0);
        testList.add(100);
        testList.add(1000);
        testList.add(13);
        testList.add(4);

        boolean result = testList.search(4);

        assertEquals(result, true);
    }

}