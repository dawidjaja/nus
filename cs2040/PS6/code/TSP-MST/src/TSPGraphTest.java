
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import java.util.*;

public class TSPGraphTest {
	
	public static double calculateMSTCost(String filename){
		TSPMap map = new TSPMap(filename);
		IApproximateTSP t = new TSPGraph();
		t.initialize(map);
		
		t.MST();
		
		// There should only be links for the MST now..
		
		Stack<Integer> stack = new Stack<Integer>();
		int numEdges = 0;
		double cost = 0;
		stack.add(0); // Start from node 0.
		
		for(int i = 0; i < map.getCount(); i++){		
			int link = map.getLink(i);
			if(link >= 0){
				double w = map.pointDistance(i, link);
				
				cost += w;
				numEdges++;
			}
		}
		
		// A MST has |V| - 1 edges.
		assertEquals(map.getCount() - 1, numEdges);
		
		return cost;
	}
	
	public void testApproximationInGoodRange(String filename) {
		double mstCost = calculateMSTCost(filename);
		
		// Use this to eyeball whether their MST algo has a correct cost.
		System.out.println("MST Cost for \"" + filename + "\" is " + mstCost);

		TSPMap map = new TSPMap(filename);
		IApproximateTSP t = new TSPGraph();
		t.initialize(map);

		t.TSP();
		assertTrue(mstCost <= t.tourDistance());
		assertTrue(t.tourDistance() <= (2 * mstCost));
		
	}


	@Test
	public void testApproximationInGoodRange() {
		String testFiles[] = {"twopoints.txt",
				  			  "tenpoints.txt",
							  "twentypoints.txt",
							  "hundredpoints.txt",
							  "tenclusters.txt",
				              "fiftypoints.txt",
				              "thousandpoints.txt",
		};
		for(String testFile : testFiles){
			testApproximationInGoodRange(testFile);
		}
	}

	/*
		MST Cost for "twopoints.txt" is 94.58647524884306
		MST Cost for "tenpoints.txt" is 276.209214636432
		MST Cost for "twentypoints.txt" is 338.54924498580004
		MST Cost for "hundredpoints.txt" is 675.140819184705
		MST Cost for "tenclusters.txt" is 816.6549911414284
		MST Cost for "fiftypoints.txt" is 489.3033923170707
		MST Cost for "thousandpoints.txt" is 2078.0997560235796
	 */

	@Test
	public void testMST1() {
		double cost = calculateMSTCost("twopoints.txt");
		double diff = Math.abs(cost - 94.58647524884306);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST2() {
		double cost = calculateMSTCost("tenpoints.txt");
		double diff = Math.abs(cost - 276.209214636432);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST3() {
		double cost = calculateMSTCost("twentypoints.txt");
		double diff = Math.abs(cost - 338.54924498580004);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST4() {
		double cost = calculateMSTCost("hundredpoints.txt");
		double diff = Math.abs(cost - 675.140819184705);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST5() {
		double cost = calculateMSTCost("tenclusters.txt");
		double diff = Math.abs(cost - 816.6549911414284);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST6() {
		double cost = calculateMSTCost("fiftypoints.txt");
		double diff = Math.abs(cost - 489.3033923170707);
		assertTrue(diff < 1e-3);
	}

	@Test
	public void testMST7() {
		double cost = calculateMSTCost("thousandpoints.txt");
		double diff = Math.abs(cost - 2078.0997560235796);
		assertTrue(diff < 1e-3);
	}
}
