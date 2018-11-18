package csp;

public class Constraint {
	private Variable variableA, variableB;
	
	Constraint(Variable varA, Variable varB){
		variableA = varA;
		variableB = varB;
		
	}
	
	Variable getOtherVariable(Variable var){
		if(var.equals(variableA))
			return variableB;
		else if(var.equals(variableB))
			return variableA;
		else
			return null;
	}
	
	public boolean equals(Object obj) {
		Constraint constraint = (Constraint)obj;
		if(constraint.getOtherVariable(variableA)==null){
			return false;
		}
		if(constraint.getOtherVariable(variableA).equals(variableB))
			return true;
		return false;
	}
	public String toString() {
		return variableA.toString()+","+variableB.toString();
	}
	

}
