/***************************************
 *dipole.java
 *Andrew Knox 6/27/01
 *
 *This class was written as a way to hold information about dipoles
 *for estaticterm.java.  I'm not sure how usefull it would be as an 
 *actual model of a dipole.
 */

package nanocad;

public class dipole
{
    private atom firstAtom, secondAtom;
    private double chargeConst;
    private double distribution;
    private boolean valid;
    
    public dipole(atom first, atom second, double charge, double dist)
    {
	if (first.isBondedWith(second) == true)
	{
	    firstAtom = first;
	    secondAtom = second;
	    chargeConst = charge;
	    distribution = dist;
	    valid = true;
	}
	else
	    valid = false;
	
	if (charge == 0.0 || dist == 0.0)
	    valid = false;    
    }

    public dipole(atom first, atom second)
    {
	if (first.isBondedWith(second) == true)
	{
	    firstAtom = first;
	    secondAtom = second;
	}
	chargeConst = 0.0;
	distribution = 0.0;
	valid = false;
    }

    public dipole()
    {
	valid = false;
    }

    public boolean isValid()
    {
	return valid;
    }

    public void setConst(double charge, double dist)
    {
	chargeConst = charge;
	distribution = dist;
	valid = true;
    }

    public double getChargeConst()
    {
	return chargeConst;
    }

    public double getDistribution()
    {
	return distribution;
    }
    
    public boolean isSameDipole(atom a, atom b)
    {
	if ((a == firstAtom && b == secondAtom) || (a == secondAtom && b == firstAtom))
	    return true;
	else
	    return false;
    }

    public boolean isSameDipole(dipole d)
    {
	if ((firstAtom == d.firstAtom && secondAtom == d.secondAtom) || 
		(firstAtom == d.secondAtom && secondAtom == d.firstAtom))
	    return true;
	else
	    return false;
    }

    public boolean sharesAtomWith(dipole d)
    {
	if (firstAtom == d.firstAtom || firstAtom == d.secondAtom
		|| secondAtom == d.firstAtom || secondAtom == d.secondAtom)
	    return true;
	else
	    return false;
    }

    public atom getFirst()
    {
	return firstAtom;
    }

    public atom getSecond()
    {
	return secondAtom;
    }
}
