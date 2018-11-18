package csp;

public class Value {
	private int value;

	public boolean equals(Object object) {
		Value objValue = (Value)object;
		if (value == objValue.value)
			return true;
		else
			return false;

	}
	
	Value(int i){
		value = i;
	}
	
	public String toString() {
		return ""+value;
	}

	public void setValue(int i) {
		value=i;
		
	}

}
