import java.util.*;
/**
 * This class extends QuestionTreeBase for students to implement the buildTree
 * and findQuery methods.
 *
 * @author
 */
public class QuestionTree extends QuestionTreeBase {

	/**
	 * This method builds the tree of objects.
	 *
	 * @param objects an array of QuestionObjects
	 */

	@Override
	public void buildTree(java.util.ArrayList<QuestionObject> objects) {
        build(m_root, objects);
	}

    public TreeNode<String> build(TreeNode<String> now,
                                  java.util.ArrayList<QuestionObject> objects) {
        if (objects.size() == 0) return null;
        if (objects.size() == 1) return now;
        TreeSet<String> set = new TreeSet<>();
        for (int i = 0; i < objects.size(); i++) {
            Iterator it = objects.get(i).propertyIterator();
            //for (int j = 0; j < objects.get(i).getPropCount(); j++) {
            while(it.hasNext()) {
                set.add(it.next().toString());
            }
        }
        ArrayList <QuestionObject> q_list = new ArrayList<QuestionObject>();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            q_list.add(it.next());
        }
        ArrayList<QuestionObject> tmp = objects;
        for (int i = 0; i < q_list.size(); i++) {
            ArrayList<QuestionObject> a = new ArrayList<QuestionObject>();
            ArrayList<QuestionObject> b = new ArrayList<QuestionObject>();
            for (int j = 0; j  < tmp.size(); j++) {
                if (tmp.get(j).containsProperty(q_list.get(i))) {
                    a.add(q_list.get(i));
                } else {
                    b.add(q_list.get(i));
                }
            }

            if (a.size() != 0 && b.size() != 0) {
                now = new TreeNode(q_list.get(i));
                now.setLeft(build(now.getLeft(), b));
                now.setRight(build(now.getRight(), a));
                now.getLeft().setParent(now);
                now.getRight().setParent(now);
                return now;
            }
        }
    }

    /**
     * This method calculates the next question to ask in the game.
     * It finds a node in the tree that has at least n/3 objects as descendants, and
     * at most 2n/3 objects as descendants, where n is the total number of objects in
     * the tree.
     *
     * @return the next query that will eliminate at least n/3 objects in the tree
     */
    @Override
        public Query findQuery(){
            // Implement this method for Problem 1.2
            return null;
        }

}
