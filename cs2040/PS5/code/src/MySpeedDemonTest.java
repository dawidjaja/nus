import static org.testng.Assert.*;

public class MySpeedDemonTest {

    private StopWatch sw = new StopWatch();

    public void testSpeedMatch(String testDBFile, int expected) throws Exception {
        System.out.println("============ " + testDBFile + "============");
        sw.reset();
        sw.start();
        int result = MySpeedDemon.speedMatch(testDBFile);
        sw.stop();
        System.out.println("Execution time: " + sw.getTime() + " nanoseconds");
        assertEquals(result, expected);
    }

    @org.testng.annotations.Test
    public void testSpeedMatch1() throws Exception {
        testSpeedMatch("TestDB_1.in", 6);
    }

    @org.testng.annotations.Test
    public void testSpeedMatch2() throws Exception {
        testSpeedMatch("TestDB_2.in", 8);
    }

    @org.testng.annotations.Test
    public void testSpeedMatch3() throws Exception {
        testSpeedMatch("TestDB_3.in", 0);
    }

    @org.testng.annotations.Test
    public void testSpeedMatch4() throws Exception {
        testSpeedMatch("TestDB_4.in", 8126496);
    }

    @org.testng.annotations.Test
    public void testSpeedMatch5() throws Exception {
        testSpeedMatch("TestDB_5.in", 38);
    }


}