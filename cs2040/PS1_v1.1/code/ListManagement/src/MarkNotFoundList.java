public class MarkNotFoundList extends LinkedList {
    /**
     * Method: search checks whether the key is in the list
     * @param key the integer to search for in the list
     * @return true if key is in the list and false otherwise
     */
    public boolean search(int key) {
        if (head == null) {
            return false;
        }
        if (head.data == key) {
            return true;
        }
        if (head.data == -key && key != 0) {
            return false;
        }
        Node prev = head;
        Node cur = head.next;
        while (cur != null) {
            if (cur.data == -key && key != 0) {
                prev.next = cur.next;
                cur.next = head;
                head = cur;
                return false;
            }
            if (cur.data == key) {
                prev.next = cur.next;
                cur.next = head;
                head = cur;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        if (key != 0) 
            head = new Node(-key, head);
        return false;
    }
}
