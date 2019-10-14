

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;


public class QuestionGameTest {

	private final String FILE_DIR = "";
	private final String FILE_DB1 = FILE_DIR + "TestDB_00.txt";
	private final String FILE_DB2 = FILE_DIR + "TestDB_01.txt";
	private final String FILE_DB3 = FILE_DIR + "TestDB_02.txt";
	private final String FILE_DB4 = FILE_DIR + "TestDB_03.txt";
	private final String FILE_DB5 = FILE_DIR + "TestDB_04.txt";

	private QuestionGame getGame(String objectFileName) {
		return new QuestionGame(objectFileName);
	}

	// Throws FileNotFoundException if there's a problem
	private QuestionGame testGameConstruction(String objectFileName) {
		return getGame(objectFileName);
	}

	private void testGamePlay(QuestionGame game) throws Exception {
		game.playGame();
	}

	private void testGameFromDB(String objectFileName) throws Exception {
		QuestionGame game = testGameConstruction(objectFileName);
		testGamePlay(game);
	}

	// As you can see, I kinda gave up on coding-style here.
	// Cheers :-D
	private void testAllObjectsAdded(String objectFileName) throws Exception {
		QuestionGame game = getGame(objectFileName);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream out = System.out;
		System.setOut(ps);

		game.m_player.m_tree.printTree();

		System.out.flush();
		System.setOut(out);

		// Disgustingly inefficient code
		String printedTree = baos.toString();
		String[] treeObjects = printedTree.split("[\\n\\r]+|" +
				System.getProperty("line.separator"));

		// Check that we have the same number of objects
		assertEquals(game.m_objects.size(), treeObjects.length);

		// Check that no objects are repeated
		Set<Set<String>> setOfObjects = new HashSet<Set<String>>();
		for (int i = 0; i < treeObjects.length; i++) {
			Set<String> oneObject = new HashSet<String>();
			oneObject.addAll(Arrays.asList(treeObjects[i].split(" ")));
			assertFalse(setOfObjects.contains(oneObject));
			setOfObjects.add(oneObject);
		}
	}

	@Test
	public void testDB1() throws Exception {
		testGameFromDB(FILE_DB1);
	}

	@Test
	public void testDB2() throws Exception {
		testGameFromDB(FILE_DB2);
	}

	@Test
	public void testDB3() throws Exception {
		testGameFromDB(FILE_DB3);
	}

	@Test
	public void testDB4() throws Exception {
		testGameFromDB(FILE_DB4);
	}

	@Test
	public void testDB5() throws Exception {
		testGameFromDB(FILE_DB5);
	}

	@Test
	public void testAllObjectsAddedDB1() throws Exception {
		testAllObjectsAdded(FILE_DB1);
	}

	@Test
	public void testAllObjectsAddedDB2() throws Exception {
		testAllObjectsAdded(FILE_DB2);
	}

	@Test
	public void testAllObjectsAddedDB3() throws Exception {
		testAllObjectsAdded(FILE_DB3);
	}

	@Test
	public void testAllObjectsAddedDB4() throws Exception {
		testAllObjectsAdded(FILE_DB4);
	}

	@Test
	public void testAllObjectsAddedDB5() throws Exception {
		testAllObjectsAdded(FILE_DB5);
	}
}
