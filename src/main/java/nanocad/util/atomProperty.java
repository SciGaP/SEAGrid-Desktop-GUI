package nanocad.util;

import java.util.*;

public class atomProperty
{
	//data
	private Vector pList = new Vector();
	private int size;
	private int anumber;
	private String name = new String("");
 
		
	public atomProperty()
	{
		size = 0;
		anumber = 255;
		return;
	}
	public atomProperty(int number, byte[] data)
	{
		anumber = 255;
		setANumber(number);
		size = data.length;
		//for(int q = 0; q < size; q++) System.out.println(data[q]);
		int i = 0;
		while(i < size)
		{
			Vector v = new Vector();//make vector for next property in the list
			v.addElement(new Byte(data[i]));//get property number
			i++;
			byte pLength = data[i]; //get property length
			v.addElement(new Byte(pLength));
			i++;
			for(int j = 0; j < pLength; j++)
			{
				v.addElement(new Byte(data[i]));
				i++;
			}
			pList.addElement(v);
		}
	}
	//add property with no parameter
	public void addProperty(byte iNum)
	{
		Vector data = new Vector(2);
		data.addElement(new Byte(iNum));
		data.addElement(new Byte((byte) 0));
		size = size + 2;
		pList.addElement(data);
	}
	//add property with float parameter
	public void addProperty(byte iNum, float param)
	{
	//done using cheezy to String conversion, should be reimplemented by someone who isn't lazy
	byte[] barray = (Float.toString(param)).getBytes();
	int aSize = barray.length;
	Vector data = new Vector(aSize+2);
	data.addElement(new Byte(iNum));
	data.addElement(new Byte((byte) aSize));
	for(int i = 0; i < aSize; i++)
	{
	    data.addElement(new Byte(barray[i]));
	}
	pList.addElement(data);
	}
	//add property with integer parameter
	public void addProperty(byte iNum, int param)
	{
		Vector data = new Vector(6);
		data.addElement(new Byte(iNum));
		data.addElement(new Byte((byte) 4));
		//encode integer
		int posParam = Math.abs(param);
		data.addElement(new Byte((byte) (posParam % 256)));
		data.addElement(new Byte((byte) (posParam/256 % 256)));
		data.addElement(new Byte((byte) (posParam/65536 % 256)));
		data.addElement(new Byte((byte) (param/16777216 % 256)));
		size = size + 6;
		try
		{
		    pList.addElement(data);
	}
	catch(Exception e)
	    {
		e.printStackTrace();

	    }
	}
	//add property with property parameter
	public void addProperty(byte iNum, atomProperty param)
	{
		byte[] paramArray = param.toByteArray();
		Vector data = new Vector(3 + paramArray.length);
		data.addElement(new Byte(iNum));
		data.addElement(new Byte((byte) paramArray.length));
		for(int i = 0; i < paramArray.length; i++)
			data.addElement(new Byte(paramArray[i]));
		size = size + 2 + paramArray.length;
		pList.addElement(data);
	}
	//add property
	public void addProperty(atomProperty param)
	{
		for (int i = 0; i < param.pList.size(); i++)
			pList.addElement(((Vector) param.pList.elementAt(i)).clone());
		size = size + param.size;
	}
	public int getANumber()
	{
		return anumber;
	}
	public String getName()
	{
		return new String(name);
	}
	public byte[] getParameter(int propertyNum)
	{
		Vector property = (Vector) pList.elementAt(propertyNum);
		byte[] returnVal = new byte[property.size() - 2];
		for(int i = 2; i < property.size(); i++)
			returnVal[i-2] = ((Byte) property.elementAt(i)).byteValue(); 
		return returnVal;
	}
	public int getParameterAsInt(int propertyNum)
	{
		return paramToInt(getParameter(propertyNum));
	}
	public atomProperty getParameterAsProperty(int propertyNum)
	{
		return new atomProperty(255, getParameter(propertyNum));
	}
	public byte getPropertyType(int propertyNum)
	{
		Vector property = (Vector) pList.elementAt(propertyNum);
		return ((Byte) property.elementAt(0)).byteValue();
	}
/**
 * Insert the method's description here.
 * Creation date: (6/20/00 11:20:19 AM)
 * @return int
 */
public int numberOfProperties() {
	return pList.size();
}
	//utility functions

	protected static int paramToInt(byte[] b)
	{
	return (unsignedByte(b[0]) +256*unsignedByte(b[1]) + 65536*unsignedByte(b[2]) + 16777216*b[3]);
	}
	protected void removeProperty(int i)
	{
		size = size - ((Vector) pList.elementAt(i)).size();
		pList.removeElementAt(i);
	}
	//add property with property parameter
	public void replacePropertyParameter(int indexOfPropertyToReplace, atomProperty param)
	{
		byte[] paramArray = param.toByteArray();
		Vector data = new Vector(3 + paramArray.length);
		Vector oldArray = (Vector) pList.elementAt(indexOfPropertyToReplace);
		data.addElement(oldArray.elementAt(0));
		data.addElement(new Byte((byte) paramArray.length));
		for(int i = 0; i < paramArray.length; i++)
			data.addElement(new Byte(paramArray[i]));
		size = size + 2 + paramArray.length - oldArray.size();
		pList.setElementAt(data, indexOfPropertyToReplace);
	}
	public void setANumber(int num)
	{
		if ((num > -1) && (num < 256))
			anumber = num;
		return;
	}
	public void setName(String n)
	{
		name = new String(n);
	}
	public byte[] toByteArray()
	{
		System.out.println("********SIZE************:   "+ size);
		byte[] output = new byte[size];
		int place = 0;
		for(int i = 0; i < pList.size(); i++)
		{
			Vector v = (Vector) pList.elementAt(i);
			for(int j = 0; j < v.size(); j++)
			{
				output[place] = ((Byte) v.elementAt(j)).byteValue();
				place++;
			}
		}
		return output;
	}
	public String toString()
	{
		return new String("atomProperty to String NYI");

	}
	protected static int unsignedByte(byte b)
	{
	if(b < 0)
	    return 256 + (int) b;
	return (int) b;
	}
}
