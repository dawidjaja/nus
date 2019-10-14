
/**
 * The main class for running the game
 *
 * @author
 *
 */
public class QuestionGame extends QuestionGameBase {

	/**
	 * The constructor reads in the objects from a specified file, and chooses
	 * a random object. It also initializes the player, sending it the list of possible objects.
	 *
	 * @param objectFileName the name of the object database
	 */
	QuestionGame(String objectFileName){
		super(objectFileName);
	}

	/**
	 * The main method instantiates a QuestionGame with a specified file, and plays the game.
	 *
	 * @param args null
	 */
	public static void main(String[] args){
		// Specify the database to be tested
		String database = "TestDB_04.txt";
		QuestionGame game = new QuestionGame(database);

		// Change the following code to determine the average and maximum number
		// of questions asked for Problem 1.4
		try {
			// Round 1
			int queries = game.playGame();
			System.out.println("Number of queries made in round 1: " + queries);

			// Restart the game for round 2
			game.RestartGame();
			queries = game.playGame();
			System.out.println("Number of queries made in round 2: " + queries);
		} catch (Exception e) {
			System.out.println("Loser exception: " + e);
		}
	}
}
