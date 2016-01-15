package bar;

import utility.MyUtility;

import com.google.common.base.MoreObjects;

public class MyBar {
	
	private final MyUtility myUtility = new MyUtility();
	
	public String bar() {
		System.out.println(MoreObjects.firstNonNull("66", "88"));
		return "bar-" + myUtility.count();
	}
	
	

}
