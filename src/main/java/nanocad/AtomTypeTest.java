package nanocad;

import nanocad.util.*;

public class AtomTypeTest
{
	protected atomProperty currentProperty;
	protected atom atomToTest;
	protected group groupToTestIn;
	private boolean verbose = false;
	protected boolean showPopups = false;
/**
 * Insert the method's description here.
 * Creation date: (6/20/00 11:35:43 AM)
 * @param testingAtom nanocad.atom
 * @param testingGroup nanocad.group
 */
public AtomTypeTest(atom testingAtom, group testingGroup) {
	atomToTest = testingAtom;
	groupToTestIn = testingGroup;
       // System.out.println(" $$$%%%$$$ groupToTestIn = " + testingGroup);
}
protected boolean askMetalTransitionNumber(int propertyToTest) {
	int mtNumber = currentProperty.getParameterAsInt(propertyToTest);
	return yesNoDialog("Is this metal's transition number" + mtNumber + "?");
}

//Type Testers
protected boolean AtomicNumberEq(int propertyToTest)
{
	int aNumber = atomToTest.atomicNumber();
	int iparam = currentProperty.getParameterAsInt(propertyToTest);
	return (iparam == aNumber);
}

//sends the simple (non-ANDed) property to the appropiate function
public boolean evaluate(int propertyToTest) {
	byte propertyType = currentProperty.getPropertyType(propertyToTest);
	//if(verbose == true){
	//	System.out.println("Testing " + propertyType);//}
	switch (propertyType) {
		case 0 :
			return Metal(propertyToTest);
		case 1 :
			return isCyclic(propertyToTest);
		case 2 :
			return AtomicNumberEq(propertyToTest);
		case 3 :
			return NumBondsGr(propertyToTest);
		case 4 :
			return NumXBondsGr(1, propertyToTest);
		case 5 :
			return NumXBondsGr(2, propertyToTest);
		case 6 :
			return NumXBondsGr(3, propertyToTest);
		case 7 :
			return Not(propertyToTest);
		case 8 :
			//one adjacent Such That
			return XAdjST(1, propertyToTest);
		case 9 :
			//two adjacent Such That
			return XAdjST(2, propertyToTest);
		case 10 :
			//etc
			return XAdjST(3, propertyToTest);
		case 11 :
			//etc
			return XAdjST(4, propertyToTest);
		case 12 :
			return XatomsYbondedST(1, 2, propertyToTest);
		case 13 :
			return XatomsYbondedST(2, 2, propertyToTest);
		case 14 :
			return XatomsYbondedST(3, 2, propertyToTest);
		case 15 :
			return XatomsYbondedST(4, 2, propertyToTest);
		case 16 :
			return XatomsYbondedST(1, 3, propertyToTest);
		case 17 :
			return XatomsYbondedST(2, 3, propertyToTest);
		case 18 :
			return XatomsYbondedST(3, 3, propertyToTest);
		case 19 :
			return XatomsYbondedST(4, 3, propertyToTest);
		case 23 :
			return askMetalTransitionNumber(propertyToTest);
		case 24:
			return yesNoDialog("Is this atom a carbonium ion?");
		default :
			//System.out.println("Attempt to reference NYI property");
			return false;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (7/19/2000 2:07:46 PM)
 */
public void highlightCurrentAtom()
{
	atomToTest.setHighlighted(true);
	atomToTest.getGroup().paint();
}

/**
 * Insert the method's description here.
 * Creation date: (6/27/00 2:12:35 PM)
 * @return boolean
 * @param X int
 */
protected boolean isConnectedToCurrentAtomInXSteps(atom a, int x) {
	if (x == 0)
		return (a == atomToTest);
	if (a.isMarked())
		return false;
	a.setMarked(true);
	int i = 0;
	while (i < groupToTestIn.bondList.size()) {
		bond b = (bond) groupToTestIn.bondList.elementAt(i);
		atom oAtom = b.otherAtom(a);
		if (oAtom != null)
			if (isConnectedToCurrentAtomInXSteps(oAtom, x - 1))
				return true;
		i++;
	}
	return false;
}

/**
 * Insert the method's description here.
 * Creation date: (6/26/00 1:23:57 PM)
 * @return boolean
 * @param propertyToTest nanocad.util.atomProperty
 */
public boolean isCyclic(int propertyToTest) {
         //System.out.println("ANU !!! : groupToTestIn.atomList.size() = " + groupToTestIn.atomList.size() );
	for(int i = 0; i < groupToTestIn.atomList.size(); i++)  // For each atom
		((atom) groupToTestIn.atomList.elementAt(i)).setMarked(false);  // unmark everything
	return isConnectedToCurrentAtomInXSteps(atomToTest, currentProperty.getParameterAsInt(propertyToTest));
}

	protected boolean Metal(int propertyToTest) { return false; }
	protected boolean Not(int propertyToEvaluate)
	{
		atomProperty parameter = currentProperty.getParameterAsProperty(propertyToEvaluate);
		AtomTypeTest subTest = new AtomTypeTest(atomToTest, groupToTestIn);
		boolean subTestResult = subTest.property(parameter, showPopups);
		return (!subTestResult);
	}
protected boolean NumBondsGr(int propertyToEvaluate) {
	int bnum = currentProperty.getParameterAsInt(propertyToEvaluate);
	return (bnum < atomToTest.currentNumBonds());
}

protected boolean NumXBondsGr(int x, int propertyToEvaluate) {
	//returns true of the atom has more than parameter bonds of order x
	int i = 0;
	int bondnum = 0;
	int iparam = currentProperty.getParameterAsInt(propertyToEvaluate);
	while ((i < groupToTestIn.bondList.size()) && (bondnum <= iparam)) {
		if ((((bond) groupToTestIn.bondList.elementAt(i)).order() == x) &&
			(((bond) groupToTestIn.bondList.elementAt(i)).otherAtom(atomToTest) != null))
			bondnum++;
		i++;
	}
	return (bondnum > iparam);
}

// Another removal of same functions brought to you by stenhous 11/17/02
protected boolean property(atomProperty property, boolean popups) {
	if (popups) return propertyWithPopups(property);
	else return propertyWithoutPopups(property);
}

public boolean propertyWithoutPopups(atomProperty property) {
	showPopups = false;
	currentProperty = property;
	for (int i = 0;i < property.numberOfProperties(); i++) {
		if (!(evaluate(i))) {
			return false;
		}
	}
	return true;
}

public boolean propertyWithPopups(atomProperty property) {
	showPopups = true;
	currentProperty = property;
	for (int i = 0;i < property.numberOfProperties(); i++) {
		if (!(evaluate(i))) {
			return false;
		}
	}
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (7/19/2000 2:07:46 PM)
 */
public void unhighlightCurrentAtom()
{
	atomToTest.setHighlighted(false);
}

/**
 * Insert the method's description here.
 * Creation date: (6/27/00 2:43:04 PM)
 */
public void verbose() { verbose = true; }

protected boolean XAdjST(int x, int propertyToTest) {
	//returns true of there are x atoms adjacent with this property

	//this would not normally be the most efficient way to do this, but 
	// based on the way atom checks to see if somethingroupToTestIn is adjacent...
	int i = 0;
	int numFound = 0;
	AtomTypeTest subTest; 

//	if((groupToTestIn == null) || (groupToTestIn.bondList == null))
/*	if(groupToTestIn == null)
		System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@ ERROR!!!!!!!!! $$$$$$$$");
*/
	while ((i < groupToTestIn.bondList.size()) && numFound < x) {
		bond b = (bond) groupToTestIn.bondList.elementAt(i);
		atom oAtom = b.otherAtom(atomToTest);
		if (oAtom != null) {
			subTest = new AtomTypeTest(oAtom, groupToTestIn);
			if (subTest.property(currentProperty.getParameterAsProperty(propertyToTest), showPopups))
				numFound++;
		}
		i++;
	}
	return (numFound == x);
}

protected boolean XatomsYbondedST(int x, int y, int propertyToTest) {
	//returns true of there are x atoms connected with y-order bonds that have this property

	//this would not normally be the most efficient way to do this, but 
	// based on the way atom checks to see if somethingroupToTestIn is adjacent...
	int i = 0;
	int numFound = 0;
	AtomTypeTest subTest;
	while ((i < groupToTestIn.bondList.size()) && numFound < x) {
		bond b = (bond) groupToTestIn.bondList.elementAt(i);
		if (b.order() == y) {
			atom oAtom = b.otherAtom(atomToTest);
			if (oAtom != null) {
				subTest = new AtomTypeTest(oAtom, groupToTestIn);
				if (subTest.property(currentProperty.getParameterAsProperty(propertyToTest), showPopups))
					numFound++;
			}
		}
		i++;
	}
	return (numFound == x);
}

protected boolean yesNoDialog(String question) {
	if(showPopups)
	{
		highlightCurrentAtom();
		YesNoDialog questionDialog = new YesNoDialog(question);
		questionDialog.ask();
		unhighlightCurrentAtom();
		return questionDialog.userAnsweredYes();
	}
	else
		return false;
}
}
