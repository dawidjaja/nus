public class MyFastList extends ArrayList {
    /**
     * Method: search checks whether the key is in the list
     * @param key the integer to search for in the list
     * @return true if key is in the list and false otherwise
     */
    public MyFastList() {
        super(10000);
    }

    public MyFastList(int length) {
        super(length);
    }

    public boolean search(int key) {
        for (int i = 0; i  <= m_max; i++)
            if (m_list[i] == key)
                return true;
        return false;
    }
}
