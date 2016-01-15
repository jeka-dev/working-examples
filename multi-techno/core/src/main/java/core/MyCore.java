package core;

import utility.MyUtility;

public class MyCore {
	
	private final MyUtility myUtility = new MyUtility();

	public int minusCount() {
		return -myUtility.count();
	}

}
