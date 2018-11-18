package csp;

import java.util.ArrayList;

public class Square extends Variable {
	int x;
	int y;

	public Square(ArrayList list, int i, int j) {
		super(list);
		x=i;
		y=j;
	}
	public String toString() {
		Value value = getValue();
		String strValue = "-";
		if(value!=null){
			strValue=value.toString();
		}
//		return "("+x+","+y+"):"+strValue;
		return strValue;
	}
	
	Value getValue(){
		if(lstDomain.size()==1){
			return (Value) lstDomain.get(0);
		}
		else{
			return null;
		}
	}

}
