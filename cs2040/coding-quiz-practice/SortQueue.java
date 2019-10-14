import java.util.*;

public class SortQueue {
  
  public void sortQueueDescending(Queue<Integer> q) {
    ArrayList <Integer> alist = new ArrayList<>();
    while (q.peek() != null) {
      alist.add(q.poll());
    }
    alist.sort((a, b) -> b - a);
    for (int i = 0; i < alist.size(); i++) {
      q.add(alist.get(i));
    }
  }

  public void sortQueueAscending(Queue<Integer> q) {
    int size = q.size();
    for (int i = 0; i < size; i++) {
      int a = q.poll();
      for (int j = 0; j < size - 1; j++) {
        int b = q.poll();
        if (a < b) {
          q.add(a);
          a = b;
        } else {
          q.add(b);
        }
      }
      q.add(a);
    }
  }
}
