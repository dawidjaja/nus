
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

/**
 * TSPGraph
 * @author gilbert, modified by hsoh
 *
 */
public class TSPGraph implements IApproximateTSP {

  private final double INFINITY = Double.MAX_VALUE;

  // Here is our map
  private TSPMap m_map = null;



  // Empty constructor
  TSPGraph(){
  }

  private int n;
  private int[] vis;
  int last;
  double MSTLen = 0;

  @Override
  public void MST(){
    if (m_map == null) throw new IllegalStateException("Graph not initialized.");

    // your code comes here
    
    class Node implements Comparable<Node> {
      double dist;
      int cur;
      int from;
      public Node(double a, int b, int c) {
        dist = a;
        cur = b;
        from = c;
      }
      @Override
      public int compareTo(Node b) {
        if (this.dist - b.dist > 0) {
          return 1;
        } else if (this.dist - b.dist == 0) {
          return 0;
        }
        return -1;
      }
    }
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.add(new Node(0, 0, 0));
    int[] vis = new int[n];
    for (int i = 0; i < n; i++) {
      vis[i] = 0;
    }
    int cnt = 0;
    while (pq.size() > 0) {
      double dist = pq.peek().dist;
      int cur = pq.peek().cur;
      int from = pq.poll().from;
      if (vis[cur] == 0) {
        vis[cur] = 1;
        if (cur != from) {
          m_map.setLink(cur, from, false);
          MSTLen += m_map.pointDistance(cur, from);
          cnt++;
          if (cnt == n - 1) {
            pq.clear();
            break;
          }
        }
        for (int i = 0; i < n; i++) {
          if (vis[i] == 0) {
            double tmp = m_map.pointDistance(cur, i);
            pq.add(new Node(tmp, i, cur));
          }
        }
      }
    }

    // redraw map
    m_map.redraw();
  }


  private void dfs(int cur, int from) {
    vis[cur] = 1;
    if (last != -1) {
      m_map.setLink(last, cur, false);
    }
    last = cur;
    for (int i = 0; i < n; i++) {
      if (i != from && i != cur && m_map.getLink(i) == cur) {
        if (vis[i] == 0) {
          dfs(i, cur);
        }
      }
    }
  }

  @Override
  public void TSP(){
    if (m_map == null) throw new IllegalStateException("Graph not initialized.");

    // your code comes here
    this.MST();
    
    last = -1;
    for (int i = 0; i < n; i++) {
      vis[i] = 0;
    }
    dfs(0, -1);
    m_map.setLink(last, 0);

    // redraw map
    m_map.redraw();
  }

  @Override
  public void initialize(TSPMap map) {
    // your code comes here
    m_map = map;
    n = m_map.getCount();
    vis = new int[n];
    for (int i = 0; i < n; i++) {
      vis[i] = 0;
    }
  }

  @Override
  public boolean isValidTour() {
    // your code comes here
    for (int i = 0; i < n; i++) {
      vis[i] = 0;
    }
    int now = 0;
    for (int i = 0; i < n; i++) {
      if (vis[now] != 0) {
        return false;
      }
      vis[now] = 1;
      now = m_map.getLink(now);
    }
    return true;
  }

  @Override
  public double tourDistance() {
    // your code comes here
    double ans = 0;
    for (int i = 0; i < n; i++) {
      ans += m_map.pointDistance(i, m_map.getLink(i));
    }
    return ans;
  }

  public static void main(String[] args){
    TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../twopoints.txt");
    TSPGraph graph = new TSPGraph();
    graph.initialize(map);

    graph.MST();
    graph.TSP();
    //System.out.println(graph.MSTLen * 2);
    System.out.println(graph.isValidTour());
    System.out.println(graph.tourDistance());

  }

}
