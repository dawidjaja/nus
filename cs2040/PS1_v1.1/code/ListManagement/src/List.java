

public interface List {
    /**
     * Method: add appends a new integer to the end of the list
     * @param key the integer to add to the list
     * @return true if the add succeeds, and false otherwise
     * Outputs an error if the key is < 0
     */
    boolean add(int key);


    /**
     * Method: search checks whether the key is in the list
     * @param key the integer to search for in the list
     * @return true if key is in the last and false otherwise
     */
    boolean search(int key);

    /**
     * Method: Converts the list into a printable string
     * @return a string
     */
    String toString();

}
