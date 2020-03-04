package utility;

public class MyUtils {
	
	public static int sequence(int in) {
		int result = 0;
		for (int i = 0; i < in; i++) {
			result = result + i;
		}
		return result;
	}

}
