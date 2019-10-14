import java.util.*;

public class InversionCount {
  public static int solve(int l, int r, int[] A) {
    if (l == r) {
      return 0;
    }
    int mid = (l + r) >> 1;
    int ans = solve(l, mid, A) + solve(mid + 1, r, A);
    // System.out.println("--" + l + " - " + r);
    int[] left = Arrays.copyOfRange(A, l, mid + 1);
    int[] right = Arrays.copyOfRange(A, mid + 1, r + 1);
    // System.out.println("---" + l + " - " + r);
    int cnt = 0;
    int lptr = 0;
    int rptr = 0;
    int ptr = l;
    while (lptr <= mid - l && rptr <= r - mid - 1) {
      if (left[lptr] < right[rptr]) {
        A[ptr++] = left[lptr++];
        ans += cnt;
      } else {
        cnt++;
        A[ptr++] = right[rptr++];
      }
    }
    while (lptr <= mid - l) {
      A[ptr++] = left[lptr++];
    }
    while (rptr <= r - mid - 1) {
      A[ptr++] = right[rptr++];
    }
    return ans;
  }

  static public int countInversions(int[] A) {
    return solve(0, A.length - 1, A);
  }

  public InversionCount() {}
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    int[] arr = new int[n];
    for (int i = 0; i < n; i++) {
      arr[i] = in.nextInt();
    }
    System.out.println(countInversions(arr));
  }
}

