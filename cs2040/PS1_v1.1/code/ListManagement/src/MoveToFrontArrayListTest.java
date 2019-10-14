

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * MoveToFrontArrayListTest
 * A set of tests for the MoveToFrontArrayList implementation in this package
 * Note: This test framework uses a method toString implemented in the class
 * 		 ArrayList.
 * Note: This test framework is based on method 2 of MoveToFrontArrayList's search
 *
 * @author Goh Chun Teck
 */
public class MoveToFrontArrayListTest {

	/**
	 * @param length - length of the list to create
	 * @return a MoveToFrontArrayList object to test
	 */
	MoveToFrontArrayList getMTFList(int length) {
		return new MoveToFrontArrayList(length);
	}

	/**
	 * Test add method with simple example
	 */
	@Test
	public void testAdd()
	{
		MoveToFrontArrayList testObj = getMTFList(3);
		testObj.add(10);
		assertEquals(testObj.toString(), "10");
		testObj.add(12);
		assertEquals(testObj.toString(), "10 12");
	}

	/**
	 * Test search method with an item not in the list
	 */
	@Test
	public void searchTest1()
	{
		MoveToFrontArrayList testList = getMTFList(5);
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

	/**
	 * Test search method with an item in the list
	 */
	@Test
	public void searchTest2()
	{
		MoveToFrontArrayList testList = getMTFList(5);
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

	/**
	 * Test errors
	 */
	@Test
	public void errorTest1()
	{
		try
		{
			MoveToFrontArrayList testList = getMTFList(1);
			testList.add(0);
			testList.add(1);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			assertTrue(false);
		}
		catch(NullPointerException e)
		{
			assertTrue(false);
		}
		catch(Exception e)
		{
			// Ok if they throw a specific type of exception
		}
	}
}
