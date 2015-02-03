package helpers;

import java.util.Comparator;

public class PointComparator implements Comparator<int[]>{

	public int compare(int[] arg0, int[] arg1) {
		if (arg0[0]>arg1[0]){
			return 1;
		}else if(arg1[0]>arg0[0]){
			return -1;
		}
		return 0;
	}
	
}
