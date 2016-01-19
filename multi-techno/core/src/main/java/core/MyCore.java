package core;

import utility.MyUtility;

public class MyCore {
	
	public int magicFormula(int input) {
		if (input < 0) {
			input = - input;
		}
		return MyUtility.sequence(input + 6) - (18 * input);
	}

}
