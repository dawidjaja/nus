
/**
 * This is you, the player. The player is given the entire database of objects
 * to begin with. Then, the player asks a sequence of queries until ready to guess.
 * Most of the work, however, is delegated to the QuestionTree.
 * 
 * @author
 */
public class QuestionPlayer {
	
	// The player maintains a tree, which keeps track
	// of the current state of the game
	QuestionTree m_tree = null;
	
	/**
	 * This constructor builds a new question tree, based on the objects
	 * with which it is initialized.
	 * 
	 * @param objects the objects with which the tree will be initialized with
	 */
	public QuestionPlayer(java.util.ArrayList<QuestionObject> objects){
		m_tree = new QuestionTree();
		m_tree.buildTree(objects);
	}
	
	/**
	 * This method checks if the player is ready to guess
	 * (i.e. when there is only one possible object left in the tree).
	 * 
	 * @return true if there is only one possible object left in the tree, false otherwise
	 */
	public boolean readyToGuess(){
		return (m_tree.countObjects() == 1);
	}
	
	/**
	 * This method returns the single guess object when there is only one object left
	 * in the tree.
	 * 
	 * @return the name of the only object left in the tree
	 */
	public String guessObject(){
		if (!readyToGuess()){
			return null;
		}
		return m_tree.getOneObject();
	}
	
	/**
	 * This method delegates the query to the tree.
	 * The tree is responsible for finding a good query to return.
	 * 
	 * @return a query
	 */
	public Query nextGuess(){
		return m_tree.findQuery();
	}
	
	/**
	 * This method updates the tree according to the given query and answer.
	 * This method is delegated to the tree.
	 * 
	 * @param query that was performed
	 * @param answer whether it was satisfied or not by the chosen object
	 */
	public void update(Query query, boolean answer){
		m_tree.updateTree(query, answer);
	}

}
