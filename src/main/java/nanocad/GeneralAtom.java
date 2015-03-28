package nanocad;

import java.awt.Color;

public class GeneralAtom extends atom
{
	//private data members
	private String name = new String("");
	private String symbol = new String("");
	private int atomicNumber = 0;
	private double mass = 0.0;
	private Color color = Color.black;
	private double covalentRadius = 0.0;
	private double vdwEnergy = 0.0;
	private double vdwRadius = 0.0;
	private int correctNumBonds = 0;

	public GeneralAtom() {
		; }
	public GeneralAtom(GeneralAtom g)
	{
	    super(g);
	    name = g.name;
	    symbol = g.symbol;
	    atomicNumber = g.atomicNumber;
        mass = g.mass;
	    color = g.color;
	    covalentRadius = g.covalentRadius;
	    vdwEnergy = g.vdwEnergy;
	    vdwRadius = g.vdwRadius;
	    correctNumBonds = g.correctNumBonds;
	}
	//copy function
	public GeneralAtom copy()
	{
		GeneralAtom copy = new GeneralAtom();
		copy.name = name;
		copy.symbol = symbol;
		copy.atomicNumber = atomicNumber;
		copy.mass = mass;
		copy.color = color;
		copy.covalentRadius = covalentRadius;
		copy.vdwEnergy = vdwEnergy;
		copy.vdwRadius = vdwRadius;
		copy.correctNumBonds = correctNumBonds;
		return copy;
	}

	//data acessing functions
	public int atomicNumber (){ return atomicNumber; }
	public Color color() { return color; }
	public int correctNumBonds(){return correctNumBonds;}
	public double covalentRadius(){return covalentRadius;}
	public double mass(){return mass;}
	public String name(){return name;}
	public String symbol(){return symbol;}
	public double vdwEnergy(){return vdwEnergy;}
	public double vdwRadius(){return vdwRadius();}

	//data setting functions
	public void setAtomicNumber(int newAtomicNumber){atomicNumber = newAtomicNumber;}
	public void setColor(Color newColor){color = newColor;}
	public void setCorrectNumBonds(int newCorrectNumBonds){correctNumBonds = newCorrectNumBonds;}
	public void setCovalentRadius(double newCovalentRadius){covalentRadius = newCovalentRadius;}
	public void setMass(double newMass){mass = newMass;}
	public void setName(String newName){name = newName;}
	public void setSymbol(String newSymbol){symbol = newSymbol;}
	public void setVdwEnergy(double newVdwEnergy){vdwEnergy = newVdwEnergy;}
	public void setVdwRadius(double newVdwRadius){vdwRadius = newVdwRadius;}
}
