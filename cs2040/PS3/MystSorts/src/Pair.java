public class Pair implements Comparable<Pair>{
	int first;
	int second;
	
	/*
	 * default constructor.
	 */
	public Pair(){
		first = 0;
		second = 0;
	}
	
	public Pair(int f, int s){
		first = f;
		second = s;
	}
	
	/**
	 * Compare according to the first number
	 */
	public int compareTo(Pair other){
		if(first - other.first > 0)
			return 1;
		else if(first - other.first < 0)
			return -1;
		else
			return -0;
	}
}
