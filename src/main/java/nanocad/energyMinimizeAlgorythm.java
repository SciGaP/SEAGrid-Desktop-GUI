package nanocad;

import java.lang.Math;
import java.util.Vector;
import java.util.Random;
import nanocad.*;
import nanocad.minimize.*;

public abstract class energyMinimizeAlgorythm
//superclass of energyMinimization functions
{
/*
	protected Vector templist = null;
	protected Vector temptermList = null;
	// Tells us whether the temps have been inited
	protected boolean tempsInited = false;
*/
	public static final int ITMAX = 200;
	public static final double EPS = 1.0e-10;
	public static final double GOLD = 1.618034;
	public static final double GLIMIT = 100.0;
	public static final double TINY = 1.0e-20;
	public static final double TOL = 2.0e-8;
	public static final double ZEPS = 1.0e-10;
	public static final double CGOLD = 0.3819660;
	protected double [] pcom;
	protected double [] xicom;
	protected nanocad.group groupToMinimize;
	protected java.util.Vector termList;
	protected newNanocad windowForUpdates;
	protected Vector atomList;
	protected int funEvals = 0;	// The number of function evaluations required.

//	public abstract void initTemps(Vector myAtomList);
	
	protected abstract void enumerateTerms();

	/**
	* Insert the method's description here.
	* Creation date: (8/7/2000 10:48:49 AM)
	* @return boolean
	*/
	public boolean defaultsUsed()
	{
	    for(int i = 0; i < termList.size(); i++)
	    {	if(((term) termList.elementAt(i)).defaultsUsed())
		{   System.out.println("Defaults used with "+
			((term) termList.elementAt(i)).name()+" term.");
		    return false;
		}

	    }
	    return true;
	}

	public double potentialOnly(newNanocad windowForUpdates)
	{	if (groupToMinimize.needsToEnumerateTerms()) enumerateTerms();
		double maxForce = 0;
		for(int i = 0; i < atomList.size(); i++)
		{ ((atom)(atomList.elementAt(i))).zeroForce();
		}

                System.out.println("Potential is:");
                for (int i = 0; i < termList.size(); i++)
                {       ((term) termList.elementAt(i)).computeForces();
                        System.out.println (((term) termList.elementAt(i)).name()+": "+
                                ((term) termList.elementAt(i)).getpotential());
                }

                System.out.println("Total sum of potentials:");
                String prevName = ((term) termList.elementAt(0)).name();
                double potentialSum = 0;
                for (int i = 0; i < termList.size(); i++)
                {
                    if (prevName.equals(((term) termList.elementAt(i)).name()))
                        potentialSum += ((term) termList.elementAt(i)).getpotential();
                    else
                    {   System.out.println (prevName+": "+potentialSum);
                        prevName = ((term) termList.elementAt(i)).name();
                        potentialSum = ((term) termList.elementAt(i)).getpotential();
                    }
                }
                System.out.println (prevName+": "+potentialSum);

		potentialSum = 0;
		for(int i = 0; i < termList.size(); i++)
			potentialSum += ((term)termList.elementAt(i)).getpotential();
		for(int i = 0; i < atomList.size(); i++)
		{	int j;
			double flensq, force;
			atom a = (atom) atomList.elementAt(i);
			for(j=0, flensq = 0.0; j < 3; j++)
			{	flensq += a.f[j]*a.f[j];
			}
			if(flensq > 0)
			{	// maxForce is max(magnitude of force,old maxForce)
				maxForce = FMAX(Math.sqrt(flensq),maxForce);
			}
		}
		maxForce *= 100000;
		maxForce = (int) maxForce;
		maxForce /= 100000;
		potentialSum *= 100000;
		potentialSum = (int) potentialSum;
		potentialSum /= 100000;
		windowForUpdates.atomInfo("Max force = "+maxForce+"  Potential = "+potentialSum);
		return potentialSum;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (7/25/2000 3:08:36 PM)
	 * @return java.util.Vector
	 */
	public java.util.Vector getTermList() {
		return termList;
	}

	public boolean minimizeGroup(newNanocad windowForUpdates, boolean useCG)
	{	if (groupToMinimize.needsToEnumerateTerms())
		{	//System.out.println("Needed to enumerate terms in minimizeGroup");
			enumerateTerms();
			potentialOnly(windowForUpdates);
		}
		// Set the types, and set resonance as well.
		// Here we can ask if Sulfur is +2, +4, or +6;
		// 		   if Oxygen is zeolite;
		//		   if Tungsten is +2 or +4.
		
		// ****************************************************************************
		// * Conjugate Gradient Algorithm.
		// ****************************************************************************
		if(useCG)
		{	calcval myCalc = SteepestDescent(100);  // We want to do this to get closer.
			double ftol = 0.01;
			myCalc = calculate(ftol);
			double thepotential = myCalc.thepotential;
			double theforce = myCalc.theforce;
			thepotential *= 100000;
			thepotential = (int) thepotential;
			thepotential /= 100000;
			theforce *= 100000;
			theforce = (int) theforce;
			theforce /= 100000;
			windowForUpdates.atomInfo("CG done. Max force = "+theforce+" Potential = "+thepotential);
			System.out.println(funEvals+" function evaluations in CG");
			System.out.println("Potential = "+myCalc.thepotential);
			System.out.println("Force = "+myCalc.theforce);
			System.out.println("-----------------------------------");
			for(int termsize = 0; termsize < termList.size(); termsize++){
				term tempTerm = (term)(termList.elementAt(termsize));
				System.out.println(tempTerm.repr()+" : "+tempTerm.getpotential());
			}
		}
		
		// ***************************************************************************
		// * Steepest Descent Algorithm.
		// ***************************************************************************

		else{
		  calcval myCalc = SteepestDescent(1000);
		  for (int i = 0; i < atomList.size(); i++)
		  {
			atom currAtom = (atom)atomList.elementAt(i);
			double[][] normal = new double[currAtom.bonds.size()-1][3];
			for (int k = 1; k < currAtom.bonds.size(); k++)
			{
				atom b1 = (atom) (((bond)currAtom.bonds.elementAt(0)).otherAtom(currAtom));
				atom b2 = (atom) (((bond)currAtom.bonds.elementAt(k)).otherAtom(currAtom));
				double pq[] = {0.0,0.0,0.0};
				double qr[] = {0.0,0.0,0.0};
				for (int f = 0; f < 3; f++)
				{
				    pq[f] = b1.x[f] - currAtom.x[f];
				    qr[f] = b2.x[f] - currAtom.x[f];
				}
				normal[k-1] = windowForUpdates.crossProduct(pq,qr);
				normal[k-1][0] /= Math.sqrt(windowForUpdates.dotProduct(normal[k-1],normal[k-1]));
				normal[k-1][1] /= Math.sqrt(windowForUpdates.dotProduct(normal[k-1],normal[k-1]));
				normal[k-1][2] /= Math.sqrt(windowForUpdates.dotProduct(normal[k-1],normal[k-1]));
			}
			if ((currAtom.bonds.size() > 2) &&
				(((normal[0][0] == normal[1][0]) && (normal[0][1] == normal[1][1])
					&& (normal[0][2] == normal[1][2])) ||
				((normal[0][0] == -normal[1][0]) && (normal[0][1] == -normal[1][1])
					&& (normal[0][2] == -normal[1][2])))
				&& currAtom.isTetrahedral)
			{	currAtom.x[0] += 0.001;
				currAtom.x[1] -= 0.005;
				currAtom.x[2] += 0.01;
				SteepestDescent(50);
			}
		  } 

		  double minPot = myCalc.thepotential;
		  double maxForce = myCalc.theforce;
		  double stepSize = myCalc.thestepsize;
		  System.out.println("Potential = "+myCalc.thepotential);
		  System.out.println("Force = "+maxForce);
		  System.out.println("Step size was "+stepSize);
		  maxForce *= 100000;
		  maxForce = (int) maxForce;
		  maxForce /= 100000;
		  minPot *= 100000;
		  minPot = (int) minPot;
		  minPot /= 100000;
		  windowForUpdates.atomInfo("SD done.  Max force = "+maxForce+" Potential = "+minPot);
		  System.out.println(funEvals+" function evaluations in SD");
		  System.out.println("-----------------------------------");
		  for(int termsize = 0; termsize < termList.size(); termsize++)
		  { term tempTerm = (term)(termList.elementAt(termsize));
			System.out.println(tempTerm.repr()+" : "+tempTerm.getpotential());
		  }
		}

		// ******************************************************************************
		// * End Steepest Descent Algorithm
		// ******************************************************************************

		return defaultsUsed(); 

	} //end minimize group function
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/25/2000 3:08:36 PM)
	 * @param newTermList java.util.Vector
	 */
	    public void setTermList(java.util.Vector newTermList) 
	    {	
		termList = newTermList;
	    }

	// Absolute value algorithm.
	  protected double fabs(double f)
	  { if(f >= 0.0) return f;
	    else return -f;
	  }

	// Returns a with b's sign (unless b is zero, then it returns a with negative.)
	  protected double SIGN(double a, double b)
	  { if(b>0.0) return fabs(a);
	    else return (- fabs(a));
	  }

	// returns the max of a and b.
	  protected double FMAX(double a, double b)
	  { return a > b ? a : b; // if a > b return a, otherwise return b.
	  }

	  /* p initially contains the starting point. 
	     The convergence tolerance on the function
	     value is input as ftol.
	     Returned quantities are p
	     (the location of the minimum) 
	     linmin is called to perform line minimizations. */
	public calcval calculate(double ftol)
	{
		double maxForce = 0.0;

	    // The following are convenient numerically for CG
	    double dgg = 0.0;				// |(gradient of next iteration)^2|
	    double gg = 0.0;				// |gradient^2|
	    int n = atomList.size() * 3;	// Size of the one-d array
	    double myMinPot = 0.0;		// Return value, needs to be
									// declared here so it can be
									// reported after too many iterations.

	    // Vectors needed to use CG... together, they represent an orthonormal basis
	    // from which to get the new direction.
	    double[] g  = new double[n];	// g is the -gradient vector
	    double[] h  = new double[n];	// h is the direction vector
	    double[] xi = new double[n];	// xi is the gradient vector

		for (int i = 0; i < atomList.size(); i++)
			((atom) atomList.elementAt(i)).zeroForce();
	   
	    // Starting force values
	    for (int i = 0; i < termList.size(); i++)
	    {
		   term t1 = (term) termList.elementAt(i);
	 	   double value = t1.computeForces();
	    }

	    // Get the max force
		for(int i = 0;  i < atomList.size(); i++){
			double flensq, force;
			int j;
			atom a = (atom) atomList.elementAt(i);
			for (j = 0, flensq = 0.0; j < 3; j++){
				flensq += a.f[j]*a.f[j];
			}
			if(flensq > 0.0){
				force = Math.sqrt(flensq);
			if (force > maxForce) maxForce = force;
		   }
		}

	     // xi contains the gradient, which was calculated as
	     // the force in the previous loop
	     for(int i=0; i<n ; i++)
	     {
			// direction vector for each term...
			// the %3 is used to get each term: x,y,z.
	   		xi[i] = ((atom) atomList.elementAt(i/3)).f[i%3];
	     }

	      for(int j=0; j<n; j++)
	      {
	        // initializes the vectors so that all of them point along -gradient
	        g[j] = -xi[j];
	        xi[j] = h[j] = g[j];
	      }

	      int its;
	      for(its = 0; its < ITMAX; its++)
	      {
	        // This does all the real work.  We already know the direction from the
	        // gradient, but this tells us how far along the gradient we go to minimize
	        // the particular dimension we're minimizing.  It's the reason CG works faster
	        // than SD.  Note that linmin alters the positions of the atoms so the molecule
			// is in the correct minimal-energy configuration.
	       	minval myVal = linmin(xi);

	        // Cleanup the past iteration, and begin anew with an update to the positions.
	        for (int i = 0; i < atomList.size(); i++)
			  ((atom) atomList.elementAt(i)).zeroForce();

	        myMinPot = 0.0;

	        for (int i = 0; i < termList.size(); i++)
	        {
			   // Recomputes the derivative.  Note that computeForces is mutable.
		 	   double value = ((term) termList.elementAt(i)).computeForces();
			   myMinPot += ((term) termList.elementAt(i)).getpotential();
	        }

			maxForce = 0.0;
			for(int i = 0; i < atomList.size(); i++){
				double flensq, force;
				int j;
				atom a = (atom) atomList.elementAt(i);
				for(j=0, flensq = 0.0; j < 3; j++){
					flensq += a.f[j]*a.f[j];
			}
			if(flensq > 0.0){
				force = Math.sqrt(flensq);
				if (force > maxForce) maxForce = force;
			}
		}
		//System.out.println("************************");
		//System.out.println("Calculate iteration "+its);
		//System.out.println("Max force is "+maxForce);
		//System.out.println("Potential is "+myMinPot);
		//System.out.println("************************");

		// Terminate?
		if((myMinPot < ftol) && (maxForce < ftol) && (myVal.xmin < 0.00001)){
			System.out.println(its+" iterations in calculate");
			System.out.println("xmin is "+myVal.xmin);
			return new calcval(maxForce, myMinPot);
		}

	        for(int i=0; i<n ; i++)
	        {
			  // Rediscover the direction, now that we know the gradient
		   	  xi[i] = ((atom) atomList.elementAt(i/3)).f[i%3];
	        }
	      
	        dgg = gg = 0.0;
	        for(int j=0; j<n; j++)
	        {
			  gg += g[j]*g[j];
			  dgg += (xi[j]+g[j])*xi[j];	// Polak-Ribiere (the good choice, for the real world)
	        }

	        if(gg == 0.0)
	        {
			  // Highly unlikely, but the gradient actually turned out
			  // to be 0.  Might be better to insert a tolerance here, so
			  // we don't waste computation time on taking a really small step.
			  System.out.println("How about that; gradient was exactly 0.0");
			  System.out.println(its+" iterations in calculate");
			  return new calcval(maxForce, myMinPot);
	        }
		
	        // Notation mumbo-jumbo that gets the computationally-convenient
	        // numbers into the proper algebraic form
	        double gam = dgg/gg;
	        for(int j=0; j<n; j++){
			  g[j] = -xi[j];
			  xi[j] = h[j] = g[j] + gam*h[j];
	        }
	 
	        if(windowForUpdates != null){
			  windowForUpdates.atomInfo("Force = "+maxForce+" Potential = "+myMinPot);
	        }
	      }
	      System.out.println("Too many iterations of calculate");
	      System.out.println(its+" iterations in calculate");
	      return new calcval(maxForce, myMinPot);
	  }


	  /************************************************************************
	   * Does all the real work.  Minimizes a function along a known direction.
	   ************************************************************************/
	public minval linmin(double xi[])
	{
	//	int n = p.length;
		int n = atomList.size() * 3;
	    pcom = new double[n];
	    xicom= new double[n]; 
	    
	    for(int j=0; j<n; j++)
	    {
	      pcom[j] = ((atom) atomList.elementAt(j/3)).x[j%3];
	      xicom[j]= xi[j];
	    }

	    // Uses a bracketing scheme to find the minimum value.
	    double ax = 0.0;	// minimum value guess for initial bracket
	    double xx = 1.0;	// maximum value guess for initial bracket

	    bracket myBrak = mnbrak(ax, xx); 
	    minval myValues = brent(myBrak.low, myBrak.mid, myBrak.high, TOL);

	    for(int j=0; j<n; j++)
	    {	// Updates the current position.
	        ((atom) atomList.elementAt(j/3)).x[j%3]  = pcom[j] + myValues.xmin*xi[j];
	    }

	    return myValues;
	}


	public minval brent(double ax, double bx, double cx, double tol)
	{
	    int iter;
	    double xmin;
	    double a,b,d=0.0,etemp,fu,fv,fw,fx,p,q,r,tol1,tol2,u,v,w,x,xm;
	    double e = 0.0;		// Distance moved on step before last
	    a = (ax < cx ? ax : cx);
	    b = (ax > cx ? ax : cx);
	    x = w = v = bx;
	    fw = fv = fx = f1dim(x);
	    for(iter = 1; iter <= ITMAX; iter++)
	    {
	      xm = 0.5 * ( a + b);
	      tol2 = 2.0 * (tol1 = tol * fabs(x) + ZEPS);
	      if(fabs(x-xm) <= (tol2-0.5*(b-a)))
	      {
			xmin = x;
			return new minval(fx,xmin);
	      }
	      if(fabs(e) > tol1)		// Try a parabolic fit
	      {
			r = (x-w)*(fx-fv);
			q = (x-v)*(fx-fw);
			p = (x-v)*q-(x-w)*r;
			q = 2.0*(q-r);
			if(q>0.0) p = -p;
			q = fabs(q);
			etemp = e;
			e = d;
	
			// Is the fit acceptable?
			if(fabs(p) >= fabs(0.5*q*etemp) || p <= q * (a-x) || p>= q*(b-x))
			   d = CGOLD * (e = (x >= xm ? a-x : b-x));

			
			else	// Take a golden section step
			{
			  d = p / q;
			  u = x + d;
			  if(u-a < tol2 || b-u < tol2)
			    d = SIGN(tol1, xm-x);
			}
	      }
	      else d = CGOLD * (e=(x>=xm ? a-x : b-x));
	      u = (fabs(d) >= tol1 ? x+d : x+SIGN(tol1,d));
	      fu = f1dim(u);
	      if(fu<=fx)
	      {
			if(u>=x) a = x;
			else b = x;
			// SHFT(v,w,x,u);
			v = w;
			w = x;
			x = u;

			// SHFT(fv, fw, fx, fu);
			fv = fw;
			fw= fx;
			fx= fu;
	      }
	      else
	      {
			if(u<x) a = u;
			else b = u;
			if(fu <= fw || w == x)
	        {
			  v = w;
			  w = u;
			  fv = fw;
			  fw = fu;
			}
			else if(fu <= fv|| v ==x || v ==w)
			{
			    v = u;
			    fv = fu;
			}
	      }    
	    } 

	    System.out.println("Too many iterations in line minimization");
	    xmin = x;
	    return new minval(fx,xmin); 
	}


	protected bracket mnbrak(double ax, double bx)
	{
	   double ulim, u, r, q, fu, dum;

		// fa and fb are the potential values.
	   double fa = f1dim(ax);
	   double fb = f1dim(bx);

	   // For ax = 0, fa should agree with the previous result from calculate.

	   if(fb > fa)
	   {
	     // swapping ax and bx
	     dum = ax;
	     ax = bx;
	     bx = dum;

	     // swapping fa and fb
	     dum = fb;
	     fb = fa;
	     fa = dum;
	     
	   }

	   // Golden section step
	   double cx = bx + GOLD * (bx - ax);
	   double fc = f1dim(cx);

	   int iter = 0;
	   while(fb > fc)
	   {
		    iter++;
		    r = (bx - ax) * (fb - fc);
		    q = (bx - cx) * (fb - fa);
		    u = bx - (( bx - cx) *q - (bx - ax) *r) / (2.0 * SIGN(FMAX(fabs(q-r), TINY),q-r));

		    ulim = bx + GLIMIT * (cx - bx);

			if((bx - u)*(u-cx) > 0.0)
		    {
				fu = f1dim(u);
				if(fu < fc)
		        {
					ax = bx;
					bx = u;
					fa = fb;
					fb = fu;
					return new bracket(ax,bx,cx,fa,fb,fc);
			    }
		        else if(fu > fb)
			    {
					cx = u;
				    fc = fu;
					return new bracket(ax,bx,cx,fa,fb,fc);
				}
	 	        u = cx+ GOLD * (cx - bx);
				fu = f1dim(u);
			}
			else if( (cx - u) * (u - ulim) > 0.0)
			{
				fu = f1dim(u);
				if(fu < fc)
				{	//SHFT(bx, cx, u, cx+GOLD*(cx-bx))
					bx = cx;
					cx = u;
					u = cx + GOLD * (cx - bx);

					// SHFT(fb, fc, fu, func(u))
					fb = fc;
					fc = fu;
					fu = f1dim(u);
				}
			}
			else if( (u-ulim) * (ulim - cx) >= 0.0)
			{
				u = ulim;
				fu = f1dim(u);
			}
			else
			{
				u = cx + GOLD * (cx - bx);
				fu = f1dim(u);
			}
			// SHFT(*ax, *bx, *cx, u)
			ax = bx;   
			bx = cx;
			cx = u;

			// SHFT(*fa, *fb, *fc, fu)
			fa = fb;  
			fb = fc;  
			fc = fu;
	   }   
	   return new bracket(ax,bx,cx,fa,fb,fc);
	}
	  
	protected double f1dim(double x)
	{
//	    if(!tempsInited) initTemps(atomList);

	    int n = atomList.size() * 3;
	    double f = 0.0;

// atomList -> templist
	    for(int i=0; i < n; i++)
	    {	// Test the new value of x, and see what result we get.
			((atom) atomList.elementAt(i/3)).x[i%3] = pcom[i] + x*xicom[i];
	    }

	    funEvals ++;
	    for (int i = 0; i < atomList.size(); i++)
			((atom) atomList.elementAt(i)).zeroForce();
	   
		// termList -> temptermList
		for (int i = 0; i < termList.size(); i++)
	    {	double value = ((term) termList.elementAt(i)).computeForces();
	 		f += ((term) termList.elementAt(i)).getpotential();
	    }

		// f is the sum of potentials of every element.
	    return f;
	}

	public calcval SteepestDescent(int numIterations)
	{	int loopCtr = 0;	// Control for the loop
		double stepSize = 0.15;	// Magnitude of the stepsize
		double maxForce = 0.0;  // max force seen after each iteration
		double minPot = 0.0;	// total potential seen at the end

		for (loopCtr = 0; loopCtr < numIterations; loopCtr++)
		{	double prevMaxForce = Double.POSITIVE_INFINITY;	// max force seen at prev. iteration
			double thePot = 0.0;	// potential seen after each iteration

			// Zero all forces
			for (int i = 0; i < atomList.size(); i++)
			    ((atom) atomList.elementAt(i)).zeroForce();
			funEvals ++;
			maxForce = 0.0;

			// Compute the forces and get the potential; intially, no change to
			// the atom, so this is the starting configuration
			for (int i = 0; i < termList.size(); i++)
			{	// The following line is only here to calculate potential
			    term currentTerm = ((term) termList.elementAt(i));
			    double value = currentTerm.computeForces();
			    thePot += currentTerm.getpotential();
			}
			// Analyze the force data, and step size
			for (int i = 0; i < atomList.size(); i++)
			{
				int j;
				double flensq = 0.0, force, m;
				atom a = (atom) atomList.elementAt(i);

				// computes the total force on each atom
				for (j = 0; j < 3; j++)
					flensq += a.f[j] * a.f[j];
				if (flensq > 0.0)
				{	force = Math.sqrt(flensq);
					// The stepsize is determined by the arbitrary multiplier
					// stepSize, and normalized so each component steps a
					// relative distance compared to the component of force
					// on the atom.
					if (force > stepSize)
						m = stepSize / force;
					else m = 1;
					for(j=0; j<3; j++)
					{	a.x[j] += m*a.f[j];
						// Move the atom in the steepest descent dir by m.
					}
					maxForce = FMAX(force,maxForce);
				}

				// Decrease the step size only if the current step actually
				// decreased the force.  Will lead to much slower convergence,
				// but might also reduce oscillations when we get near the
				// minimum.  Has the effect of minimizing larger components of
				// error (from the minimum), while oscillating around the smaller
				// components until the step size is small enough to get closer
				// to them.  Also keeps step size constant if it is sufficiently small.
				if((prevMaxForce >= maxForce) && (stepSize > 0.000002))
					stepSize *= 0.98;

				prevMaxForce = maxForce; // is this correct? -stenhous 12/9/02
			}

			//System.out.println("Iteration "+loopCtr);
			//System.out.println("Max force is "+maxForce);
			//System.out.println("Potential is "+thePot);
			if (maxForce < .001)
			{	System.out.println("Finished early");
				minPot = thePot;
				break;
			}
			if (loopCtr % 25 == 0) // print an update every 25 iterations
			{	windowForUpdates.atomInfo("Max force = " + maxForce+" Min pot = "+thePot); 
			}
			minPot = thePot;
		}
		// Positions may have changed since the last calculation, so update forces.
		// In particular, position has changed if we reach the maximum number
		// of iterations.
		maxForce = 0;
		minPot = 0;
		for(int i = 0; i < atomList.size(); i++)
		{ ((atom)(atomList.elementAt(i))).zeroForce();
		}
		for(int i = 0; i < termList.size(); i++)
		{	term t1 = (term)(termList.elementAt(i));
			t1.computeForces();
			minPot += t1.getpotential();
		}
		for(int i = 0; i < atomList.size(); i++)
		{	int j;
			double flensq, force;
			atom a = (atom) atomList.elementAt(i);
			for(j=0, flensq = 0.0; j < 3; j++)
			{	flensq += a.f[j]*a.f[j];
			}
			if(flensq > 0)
			{	// maxForce is max(magnitude of force,old maxForce)
				maxForce = FMAX(Math.sqrt(flensq),maxForce);
			}
		}
		return new calcval(maxForce, minPot, stepSize);
	}
}
