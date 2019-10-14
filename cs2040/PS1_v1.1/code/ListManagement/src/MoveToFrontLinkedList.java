public class MoveToFrontLinkedList extends LinkedList {
    /**
     * Method: search checks whether the key is in the list
     * @param key the integer to search for in the list
     * @return true if key is in the list and false otherwise
     */
    public boolean search(int key) {
        if (head == null)
            return false;
        if (head.data == key)
            return true;
        Node cur = head.next;
        Node prev = head;
        while (cur != null) {
            if (cur.data == key) {
                prev.next = cur.next;
                cur.next = head;
                head = cur;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }
}
