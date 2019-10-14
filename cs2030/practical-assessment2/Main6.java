import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;
import java.util.*;

class Main {
  static Scanner sc = new Scanner(System.in);

  public static void main(String[] args) {
    ArrayList<Student> slist = new ArrayList<>();
    ArrayList<Integer> groups = new ArrayList<>();
    HashMap<String,Integer> scores = new HashMap<>();
    String plab;
    String id;
    int group;
    while (true) {
      plab = sc.next();
      if (plab.equals("end")) {
        break;
      }
      id = sc.next();
      group = sc.nextInt();
      slist.add(new Student(plab, id, group));
      groups.add(group);
    }
    //read score
    boolean first = true;
    int mini = 0;
    int maxi = 0;
    HashMap<Integer,Integer> freq = new HashMap<>();
    while (true) {
      plab = sc.next();
      if (plab.equals("end")) {
        break;
      }
      int tmp = sc.nextInt();
      scores.put(plab, tmp);
    }

    //output
    groups.sort((a, b) -> a - b);
    ArrayList<Integer> uniqueGroups = new ArrayList<>();
    for (int i = 0; i < groups.size(); i++){
      if (i == 0 || groups.get(i) != groups.get(i - 1)) {
        uniqueGroups.add(groups.get(i));
      }
    }
    System.out.printf("Groups(%d):", uniqueGroups.size());
    System.out.println(uniqueGroups);

    ArrayList<Student> absentees = new ArrayList<>();
    HashMap<Integer, HashMap<Integer,Integer>> fgroup = new HashMap<>();
    HashSet<Integer> activeGroup = new HashSet<>();
    for (int i = 0; i < slist.size(); i++) {
      int score;
      boolean absent = false;
      if (scores.containsKey(slist.get(i).getPlab())) {
        score = scores.get(slist.get(i).getPlab());
      } else {
        absent = true;
        score = 0;
        absentees.add(slist.get(i));
      }
      if (!absent) {
        if (freq.containsKey(score)) {
          if (fgroup.get(score).containsKey(slist.get(i).getGroup())) {
            fgroup.get(score).replace(slist.get(i).getGroup(), 
                fgroup.get(score).get(slist.get(i).getGroup()) + 1);
          } else {
            fgroup.get(score).put(slist.get(i).getGroup(), 1);
          }
          freq.replace(score, freq.get(score) + 1);
          activeGroup.add(slist.get(i).getGroup());
        } else {
          fgroup.put(score, new HashMap<Integer,Integer>());
          fgroup.get(score).put(slist.get(i).getGroup(), 1);
          freq.put(score, 1);
          activeGroup.add(slist.get(i).getGroup());
        }
        if (first) {
          mini = score;
          maxi = score;
          first = false;
        }
        mini = Math.min(mini, score);
        maxi = Math.max(maxi, score);
      }
      /*
         if (groupFreq[slist.get(i).getGroup()].containsKey(score)) {
         groupFreq[slist.get(i).getGroup()].replace(score, 
         groupFreq[slist.get(i).getGroup()].get(score) + 1);
         } else {
         groupFreq[slist.get(i).getGroup()].put(score, 1);
         }
         */
      System.out.println(slist.get(i) + "," + score);
    }

    System.out.println("List of absentees:");
    if (absentees.size() == 0) {
      System.out.println("None");
    }
    for (int i = 0; i < absentees.size(); i++) {
      System.out.println(absentees.get(i));
    }
    System.out.printf("Mark frequency from %d to %d\n", mini, maxi);
    for (int i = mini; i <= maxi; i++) {
      System.out.printf("%d : %d\n", i, freq.containsKey(i) ? freq.get(i) : 0);
    }
    for (int i = 0; i < uniqueGroups.size(); i++) {
      if (activeGroup.contains(uniqueGroups.get(i))) {
        System.out.printf("Group #%d...Mark frequency from %d to %d\n", uniqueGroups.get(i), mini, maxi);
        for (int j = mini; j <= maxi; j++) {
          System.out.printf("%d : %d\n", j, fgroup.containsKey(j) ? 
              (fgroup.get(j).containsKey(uniqueGroups.get(i)) ? fgroup.get(j).get(uniqueGroups.get(i)) : 0) : 0);
        }
      }
    }
  }
}
