package csp;

import java.util.ArrayList;
import java.util.Iterator;

public class Variable implements Comparable{
	protected ArrayList lstDomain = new ArrayList();
	protected ArrayList lstConstraintVariables = new ArrayList();
	
	Variable(ArrayList lstDom){
		lstDomain = lstDom;
	}
	
	void addConstraint(Constraint constraint){
		lstConstraintVariables.add(constraint.getOtherVariable(this));
	}

	public void setValue(Value value) throws InvalidAssignmentException{
		Iterator constraintVarsIterator=lstConstraintVariables.iterator();
		while(constraintVarsIterator.hasNext()){
			Variable var=(Variable) constraintVarsIterator.next();
			if(var.lstDomain.contains(value)&&var.lstDomain.size()==1){
				throw new InvalidAssignmentException();
			}
		}
		lstDomain.clear();
		lstDomain.add(value);
		
	}

	public int compareTo(Object obj) {
		Variable var2=(Variable)obj;
		return lstDomain.size()-var2.lstDomain.size();
		
	}
	
}
