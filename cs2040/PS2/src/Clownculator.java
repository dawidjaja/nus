import java.util.*;

class Clownculator {

    private HerbertLog log;

    Clownculator(String filename) {
        log = new HerbertLog(filename);
    }

/*
 * ----- Qn 1.1 (20 marks) ------
 */


    /**
     * Performs the calculation of salary
     */
    public long calculateSalary(){
        int n = log.numMinutes();
        String lastName = "";
        long lastVal = 0;
        long total = 0;
        for (int i = 0; i < n; i++) {
            Record tmp = log.get(i);
            if (!tmp.getName().equals(lastName)) {
                lastName = tmp.getName();
                total += lastVal;
            }
            lastVal = tmp.getWages();
        }
        total += lastVal;
        return total;
    }

/*
 * ----- Qn 1.2 (10 marks) ------
 */

    /**
     *  Calculates the minimum number of minutes per job
     *  needed to reach the target income
     */
    public long calculateMinimumWork(long goalIncome){
        int n = log.numMinutes();
        String lastName = "";
        HashMap<Long,Long> ttl = new HashMap<Long,Long>();
        long maxKey = 0;
        long idx = 0;
        long lastVal = 0;
        for (int i = 0; i < n; i++) {
            Record tmp = log.get(i);
            long x = tmp.getWages();
            if (!tmp.getName().equals(lastName)) {
                idx = 0;
            } else {
                idx++;
                x -= lastVal;
                maxKey = Math.max(maxKey, idx);
            }
            if (!ttl.containsKey(idx)) {
                ttl.put(idx, x);
            } else {
                ttl.replace(idx, ttl.get(idx) + x);
            }
            lastVal = tmp.getWages();
            lastName = tmp.getName();
        }
        long tmp = 0;
        for (long i = 0; i <= maxKey; i++) {
            if (tmp >= goalIncome) {
                return i;
            }
            tmp += ttl.get(i);
        }
        if (tmp >= goalIncome) {
            return maxKey + 1;
        }
        return -1;
    }
}
