/**
 * LinkedList
 * Description:  linked list implementation
 * CS2040 2018
 */


/**
 * Class: LinkedList
 * Description: linked list implementation
 * All the elements in the list are integers >= 0.
 */

public class LinkedList implements List {

    protected class Node {
        protected int data;
        protected Node next;

        public Node()
        {
            data = 0;
            next = null;
        }

        public Node(int data, Node n) {
            //todo
            this.data = data;
            next = n;
        }
    };

    Node head;

    public LinkedList() {
        head = null;
    }


    /**
     * Method: add appends a new integer to the end of the list
     * @param key the integer to add to the list
     * @return true if the add succeeds, and false otherwise
     * Outputs an error if the key is < 0
     */
    public final boolean add(int key) {
        if (head == null) {
            head = new Node(key, null);
            return true;
        }
        if (head.data == -key) {
            head = head.next;
        }
        Node cur = head;
        while (cur.next != null) {
            if (cur.next.data == -key) {
                cur.next = cur.next.next;
            }
            cur = cur.next;
        }
        cur.next = new Node(key, null);
        return true;
    }


    /**
     * Method: search checks whether the key is in the list
     * @param key the integer to search for in the list
     * @return true if key is in the list and false otherwise
     */
    public boolean search(int key) {
        Node cur = head;
        while (cur != null) {
            if (cur.data == key) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    /**
     * Method: Converts the list into a printable string
     * @return a string
     */
    public String toString() {
        String output = "";
        Node cur = head;
        while (cur != null) {
            if (cur.next == null) {
                output += cur.data;
            } else {
                output += cur.data + " ";
            }
            cur = cur.next;
        }
        return output;
    }

}
