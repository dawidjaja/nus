

/**
 * This is a node class that encapsulates one node in the QuestionTree.
 * TreeNodes are used to hold object properties.
 * 
 * @author
 * @param <KType> the type of node values
 */
public class TreeNode<KType> {
	
	// Left child
	private TreeNode<KType> m_left = null;
	// Right child
	private TreeNode<KType> m_right = null;
	// Parent
	private TreeNode<KType> m_parent = null;
	
	// Each node represents either a property or an object (name).
	// Thus, one of these two should be set, and the other should be null.
	
	// Property or object name
	protected KType m_value = null;
	
	/**
	 * This constructor builds an empty node.
	 */
	public TreeNode(){
		m_left = null;
		m_right = null;
		m_parent = null;
		m_value=null;
	}
	
	/**
	 * This constructor builds a node with a key/value
	 * 
	 * @param v the value of the node
	 */
	public TreeNode(KType v){
		m_value = v;
	}
	
	/**
	 * This method returns the left child of the node
	 * 
	 * @return left child of the node
	 */
	public TreeNode<KType> getLeft(){
		return m_left;
	}
	
	/**
	 * This method returns the right child of the node
	 * 
	 * @return right child of the node
	 */
	public TreeNode<KType> getRight(){
		return m_right;
	}
	
	/**
	 * This method returns the parent of the node
	 * 
	 * @return parent of the node
	 */
	public TreeNode<KType> getParent(){
		return m_parent;
	}
	
	/**
	 * This method returns the property (value) of the node
	 * 
	 * @return property (value) of the node
	 */
	public KType getValue(){
		return m_value;
	}
	
	/**
	 * This method sets the input node as the left child of the node
	 * 
	 * @param newNode the left child of the node
	 */
	public void setLeft(TreeNode<KType> newNode){
		m_left = newNode;
	}
	
	/**
	 * This method sets the input node as the right child of the node
	 * 
	 * @param newNode the right child of the node
	 */
	public void setRight(TreeNode<KType> newNode){
		m_right = newNode;
	}
	
	/**
	 * This method sets the input node as the parent of the node
	 * 
	 * @param newNode the parent of the node
	 */
	public void setParent(TreeNode<KType> newNode){
		m_parent = newNode;
	}
	
	/**
	 * This method checks if the node is a leaf node
	 * 
	 * @return true if the node is a leaf node, false otherwise
	 */
	public boolean isLeaf(){
		if ((m_right == null) && (m_left == null)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * This method returns a string version of the node.
	 * 
	 * @Override
	 * @return the string version of the node
	 */
	public String toString(){
		return new String("(" + m_value + ")");
	}
	
	
}
