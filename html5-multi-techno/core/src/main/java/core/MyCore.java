package core;

import utility.MyUtils;

public class MyCore {
	
	public int magicFormula(int input) {
		if (input < 0) {
			input = - input;
		}
		return MyUtils.sequence(input + 6) - (18 * input);
	}

}
