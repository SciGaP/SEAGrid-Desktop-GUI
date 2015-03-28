package nanocad.minimize.mm3;

import java.util.Vector;
import nanocad.*;

public  class estaticterm extends nanocad.term
{
    private final double e0 = 1.5;            //8.854187817 * .000000000001;
    private double firstCharge;
    private double secondCharge;
    private double firstDistribution;
    private double secondDistribution;

    public estaticterm() {}

    public estaticterm(atom first1, atom first2, atom second1, atom second2)
    {
	myAtoms = new atom[4];
	myAtoms[0] = first1;
	myAtoms[1] = first2;
	myAtoms[2] = second1;
	myAtoms[3] = second2;
	setCalculationValues();
    }

    public void enumerate(Vector atomList, Vector termList)
    {
	Vector dipoleList = new Vector();
	int number = 0;

	System.out.println("enumerating");

	//make a list of all dipoles	
	for (int i = 0; i < atomList.size(); i++)
	{
	    atom tmpAtom = (atom)atomList.elementAt(i);
	    for (int j = 0; j < tmpAtom.bonds.size(); j++)
	    {
		dipole temp;
		if (((bond)tmpAtom.bonds.elementAt(j)).a1 == tmpAtom)
		    temp = new dipole(tmpAtom,((bond)tmpAtom.bonds.elementAt(j)).a2); 
		else
		    temp = new dipole(tmpAtom,((bond)tmpAtom.bonds.elementAt(j)).a1);

		boolean add = true;

		for (int k = 0; k < dipoleList.size(); k++)			//aviod adding duplicate bonds
		    if (temp.isSameDipole((dipole)dipoleList.elementAt(k)))
			add = false;

		if (add == true)
		    dipoleList.addElement(temp);
	    }
	}

	//add terms for all dipoles that don't share common atoms
	for (int i = 0; i < dipoleList.size(); i++)
	{
	    for (int j = i + 1; j < dipoleList.size(); j++)
	    {
		dipole temp1 = (dipole)dipoleList.elementAt(i);
		dipole temp2 = (dipole)dipoleList.elementAt(j);
		
		Vector v = new Vector();

		if (!temp1.sharesAtomWith(temp2))
		{
		    v.addElement(temp1);
		    v.addElement(temp2);
		    this.buildTerm(v, termList);
		    number++;
		}
	    }
	}
        System.out.println(number + " terms added");
    }

    protected void buildTerm(Vector v, Vector termList)
    {
	estaticterm e = new estaticterm( ((dipole)v.elementAt(0)).getFirst(), ((dipole)v.elementAt(0)).getSecond(),
						((dipole)v.elementAt(1)).getFirst(), ((dipole)v.elementAt(1)).getSecond());
	if (!((e.firstCharge == 0.0 && e.firstDistribution == 0.0) || (e.secondCharge == 0.0 && e.secondDistribution == 0.0)) )
	    termList.addElement(e);
    }

    public double computeForces()
    {
	double ab[], cd[], uv[], abdotab, abdotcd, abdotuv, cddotcd, cddotuv, uvdotuv, abnorm, cdnorm, r, x, thetai, thetaj, dedr, dedx, 
	dedthi, dedthj, drda[], drdb[], drdc[], drdd[], dxda[], dxdb[], dxdc[], dxdd[], dthida[], dthidb[], dthjdc[], dthjdd[];
	ab = new double[3]; cd = new double [3]; uv = new double[3]; drda = new double[3]; drdb = new double[3]; drdc = new double[3];
	drdd = new double[3]; dxda = new double[3]; dxdb = new double[3]; dxdc = new double[3]; dxdd = new double[3];
	dthida = new double[3]; dthidb = new double[3]; dthjdc = new double[3]; dthjdd = new double[3];

	for (int i = 0; i < 3; i++)
	{
	    ab[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
	    cd[i] = myAtoms[2].x[i] - myAtoms[3].x[i];
	    uv[i] = myAtoms[3].x[i] + secondDistribution * cd[i] - myAtoms[1].x[i] - firstDistribution * ab[i];
	}
	
	abdotab = ab[0]*ab[0] + ab[1]*ab[1] + ab[2]*ab[2];
	abdotcd = ab[0]*cd[0] + ab[1]*cd[1] + ab[2]*cd[2];
	abdotuv = ab[0]*uv[0] + ab[1]*uv[1] + ab[2]*uv[2];
	cddotcd = cd[0]*cd[0] + cd[1]*cd[1] + cd[2]*cd[2];
	cddotuv = cd[0]*uv[0] + cd[1]*uv[1] + cd[2]*uv[2];
	uvdotuv = uv[0]*uv[0] + uv[1]*uv[1] + uv[2]*uv[2];

	if(abdotab <= 0) abdotab = mm3MinimizeAlgorythm.TINY;
	if(cddotcd <= 0) cddotcd = mm3MinimizeAlgorythm.TINY;
	if(uvdotuv <= 0) uvdotuv = mm3MinimizeAlgorythm.TINY;

	abnorm = Math.sqrt(abdotab);
	cdnorm = Math.sqrt(cddotcd);

	//note : the variables x, thetai, and thetaj represent cos x, cos thetai and cos thetaj rather than the angles themselves.

	r = Math.sqrt(uvdotuv);
	x = abdotcd / (abnorm * cdnorm);
	thetai = abdotuv / (abnorm * r);
	thetaj = cddotuv / (cdnorm * r);

	dedr = -3 * firstCharge * secondCharge / (e0 * r*r*r*r) * (x - 3 * thetai * thetaj);
	dedx = firstCharge * secondCharge / (e0 * r*r*r) * Math.sin(Math.acos(x));
	dedthi = firstCharge * secondCharge / (e0 * r*r*r) * (-3 * thetaj * Math.sin(Math.acos(thetai)));
	dedthj = firstCharge * secondCharge / (e0 * r*r*r) * (-3 * thetai * Math.sin(Math.acos(thetaj)));

	for (int i = 0; i < 3; i++)
	{
	    drda[i] = - firstDistribution * uv[i] / r;
	    drdb[i] = - (1 - firstDistribution) * uv[i] / r;
	    drdc[i] = secondDistribution * uv[i] / r;
	    drdd[i] = (1 - secondDistribution) * uv[i] / r;

	    dxda[i] = (cd[i] - ab[i] * abdotcd / abdotab) / (abnorm * cdnorm);
	    dxdb[i] = -dxda[i];
	    dxdc[i] = (ab[i] - cd[i] * abdotcd / cddotcd) / (abnorm * cdnorm);
	    dxdd[i] = -dxdc[i];

	    dthida[i] = (uv[i] - firstDistribution * ab[i] - ab[i] * abdotuv / abdotab + uv[i] * abdotuv * firstDistribution / uvdotuv) / (abnorm * r);
	    dthidb[i] = (-uv[i] - (1 - firstDistribution) * ab[i] + ab[i] * abdotuv / abdotab + uv[i] * abdotuv * (1 - firstDistribution) / uvdotuv) / (abnorm * r);
	    
	    dthjdc[i] = (uv[i] + secondDistribution * cd[i] - cd[i] * cddotuv / cddotcd - uv[i] * cddotuv * secondDistribution / uvdotuv) / (cdnorm * r);
	    dthjdd[i] = (-uv[i] + (1 - secondDistribution) * cd[i] + cd[i] * cddotuv / cddotcd - uv[i] * cddotuv * (1 - secondDistribution) / uvdotuv) / (cdnorm * r);

	    myAtoms[0].f[i] += dedr * drda[i] + dedx * dxda[i] + dedthi * dthida[i];
	    myAtoms[1].f[i] += dedr * drdb[i] + dedx * dxdb[i] + dedthi * dthidb[i];
	    myAtoms[2].f[i] += dedr * drdc[i] + dedx * dxdc[i] + dedthj * dthjdc[i];
	    myAtoms[3].f[i] += dedr * drdd[i] + dedx * dxdd[i] + dedthj * dthjdd[i];
	}

	potential = computePotential(x,thetai,thetaj,r);
	return myAtoms[0].x[0];
    }

    public double computePotential(double x, double alpha1, double alpha2, double d){
	return (firstCharge*secondCharge*(x-3*alpha1*alpha2)/(e0*d*d*d));
    }

    public void setCalculationValues()
    {
	atom tempa;
	int temp;
	int atom1 = myAtoms[0].getMM3TypeNum();
	int atom2 = myAtoms[1].getMM3TypeNum();
	int atom3 = myAtoms[2].getMM3TypeNum();
	int atom4 = myAtoms[3].getMM3TypeNum();

	if (atom1 > atom2)
	{
	    temp = atom1;
	    atom1 = atom2;
	    atom2 = temp;
	    tempa = myAtoms[0];
	    myAtoms[0] = myAtoms[1];
	    myAtoms[1] = tempa;
	}

	if (atom3 > atom4)
	{
	    temp = atom3;
	    atom3 = atom4;
	    atom4 = temp;
	    tempa = myAtoms[2];
	    myAtoms[2] = myAtoms[3];
	    myAtoms[3] = tempa;
	}

	try
	{
	AtomDataFile dataFile= new AtomDataFile(newNanocad.txtDir + 
			newNanocad.fileSeparator + "mm3estaticdata.txt");
	//AtomDataFile dataFile= new AtomDataFile("mm3estaticdata.txt");
	if (dataFile.findData(atom1, atom2, 0, 1))
	{
	    firstCharge = dataFile.parseDouble(2);
	    firstDistribution = dataFile.parseDouble(3);
	}
	else
	{
	    //System.out.println("No data for " + atom1 + " " + atom2 + " dipole.");
	    firstCharge = 0.0;
	    firstDistribution = 0.0;
	}

	if (dataFile.findData(atom3, atom4, 0, 1))
	{
	    secondCharge = dataFile.parseDouble(2);
	    secondDistribution = dataFile.parseDouble(3);
	}
	else
	{
	    //System.out.println("No data for " + atom3 + " " + atom4 + " dipole.");
	    secondCharge = 0.0;
	    secondDistribution = 0.0;
	}
	}
	catch (java.io.IOException e)
	{
	    System.err.println("Dipole data lookup error");
	    e.printStackTrace();
	}
    }

    public int termLength()
    {
	return 4;
    }

    public String repr2()
    {	return " Electrostatic" + myAtoms[0].name() + "-" + myAtoms[1].name() + " " +
		 myAtoms[2].name() + "-" + myAtoms[3].name();
    }   
 
    public String name()
    {
	return "Electrostatic";
    }
}
