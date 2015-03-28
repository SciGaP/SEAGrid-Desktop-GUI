package nanocad.util;

import java.io.*;
import java.util.*;
import nanocad.*;
import nanocad.newNanocad;

public class AtomTypeFile implements Serializable
{
	//Data
	protected String filename;
	private Vector atomList;

	public AtomTypeFile()
	{
		atomList = new Vector(256); 
	}
	public void addProperty(atomProperty prop)
	{
		try
		{
			Vector propList = (Vector) atomList.elementAt(prop.getANumber());
			propList.addElement(prop.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public void demoteProperty(int pNum)
	//moves the property one step backward (that is, evaluated later)
	{
		int sizeSoFar = 0;
		int i = 0;
		Vector currentAtom = (Vector) atomList.elementAt(0);
		while(pNum >= (sizeSoFar + currentAtom.size()))
		{
			i++;
			sizeSoFar = sizeSoFar + currentAtom.size();
			currentAtom = (Vector) atomList.elementAt(i);
		}
		int startIndex = pNum-sizeSoFar;
		Object selectedItem = currentAtom.elementAt(startIndex);
		currentAtom.removeElementAt(startIndex);
		currentAtom.insertElementAt(selectedItem, startIndex + 1);
	}
	public atomProperty getProperty(int pNum)
	{
		int sizeSoFar = 0;
		int i = 0;
		Vector currentAtom = (Vector) atomList.elementAt(0);
		while(pNum >= (sizeSoFar + currentAtom.size()))
		{
			i++;
			sizeSoFar = sizeSoFar + currentAtom.size();
			currentAtom = (Vector) atomList.elementAt(i);
		}
		byte[] barray = (byte[]) currentAtom.elementAt(pNum-sizeSoFar);
		return new atomProperty(i, barray);
	}
	public int getType(group g, atom atomToType)
	{
	int total = 0;
	int aNumber = atomToType.atomicNumber();
	for(int i = 0; i < aNumber; i++)
	    total = total + ((Vector) atomList.elementAt(i)).size();

	Vector pList = (Vector) atomList.elementAt(aNumber);
	AtomTypeTest tester = new AtomTypeTest(atomToType, g);
	for(int i = 0; i < pList.size(); i++)
	{
		if((i+total) == 32) tester.verbose();
	    if(tester.propertyWithoutPopups(new atomProperty(255, (byte[]) pList.elementAt(i))))
		return i+total;
	}
	return -1;
	}
	public int getTypeWithoutPopups(group g, atom atomToType)
	{
		int total = 0;
		int aNumber = atomToType.atomicNumber();
		for(int i = 0; i < aNumber; i++)
		    total = total + ((Vector) atomList.elementAt(i)).size();

		Vector pList = (Vector) atomList.elementAt(aNumber);
		AtomTypeTest tester = new AtomTypeTest(atomToType, g);
		for(int i = 0; i < pList.size(); i++)
		{
			if((i+total) == 32) tester.verbose();
		    if(tester.propertyWithoutPopups(new atomProperty(255, (byte[]) pList.elementAt(i))))
			return i+total;
		}
		return -1;
	}
	public void promoteProperty(int pNum)
	//moves the property one step forward (that is, evaluated sooner)
	{
		int sizeSoFar = 0;
		int i = 0;
		Vector currentAtom = (Vector) atomList.elementAt(0);
		while(pNum >= (sizeSoFar + currentAtom.size()))
		{
			i++;
			sizeSoFar = sizeSoFar + currentAtom.size();
			currentAtom = (Vector) atomList.elementAt(i);
		}
		int startIndex = pNum-sizeSoFar;
		Object selectedItem = currentAtom.elementAt(startIndex);
		currentAtom.removeElementAt(startIndex);
		currentAtom.insertElementAt(selectedItem, startIndex - 1);
	}
	public void readFile(String newFilename)
	{
		readFileFromObject(newFilename);
	}
	public void readFileFromData(String newFilename)
	{
		filename = newFilename;
		try 
		{
			//RandomAccessFile f = new RandomAccessFile(filename + ".data.txt","r");
			RandomAccessFile f = new RandomAccessFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + filename + ".data.txt","r");
			long data;
			for(int i =0; i < 256; i++)
			{
			    Vector propList = new Vector();
			    data = f.readLong();
			    if(data > 0)
			{	//System.out.println("Nonzero data item dected.");
					long returnPoint = f.getFilePointer();
					f.seek(data);
					int xx = ubyteToInt(f.readByte());
					while( xx == i)
					{   int x = ubyteToInt(f.readByte());
						//System.out.print(i + "/" + x + ":");
					    byte[] barray = new byte[x];					    
					    f.readFully(barray);
					    //for(int q = 0; q < x; q++) System.out.print(ubyteToInt(barray[q]) + " "); 
						//System.out.println("");
					    propList.addElement(barray);
				 	    xx = ubyteToInt(f.readByte());
				 	    //xx = 300;
				 	    //System.out.println(data + " - IN:"+i+"["+barray.length+"]"+xx);
					}
					f.seek(returnPoint);
			    }
			    atomList.addElement(propList);
			}
		}
		catch (IOException e)
		{
			System.out.println("File read error! : " + e.getMessage());
			e.printStackTrace();
	    return;
		}
	System.out.println("Data read sucessful." + filename + ".data");
	}
	public void readFileFromObject(String newFileName)
	{
		filename = newFileName;
		System.out.println("filename = " + filename);
		try 
		{
			//InputStream inputStream = getClass().getResourceAsStream("/" + filename + ".object.txt");
			FileInputStream inputStream = new FileInputStream(new File(newNanocad.txtDir + 
					newNanocad.fileSeparator + filename + ".object.txt"));
			ObjectInputStream test = new ObjectInputStream(inputStream);
			AtomTypeFile newTypeFile = (AtomTypeFile) test.readObject();
			atomList = newTypeFile.atomList;
		}
		catch (Exception e)
		{
			System.err.println("File read error! : " + e.getMessage());
			e.printStackTrace();
	    return;
		}
	//System.out.println("Data read sucessful.  " + atomList.size() + " elements loaded.");
	}
	public void removeProperty(int pNum)
	{
	int sizeSoFar = 0;
		int i = 0;
		Vector currentAtom = (Vector) atomList.elementAt(0);
		while(pNum >= (sizeSoFar + currentAtom.size()))
		{
			i++;
			sizeSoFar = sizeSoFar + currentAtom.size();
			currentAtom = (Vector) atomList.elementAt(i);
		}
		currentAtom.removeElementAt(pNum-sizeSoFar);
	}
	public void replaceProperty(int pNum, atomProperty newProperty)
	{
		int sizeSoFar = 0;
		int i = 0;
		Vector currentAtom = (Vector) atomList.elementAt(0);
		while(pNum >= (sizeSoFar + currentAtom.size()))
		{
			i++;
			sizeSoFar = sizeSoFar + currentAtom.size();
			currentAtom = (Vector) atomList.elementAt(i);
		}
		currentAtom.setElementAt((Object) newProperty.toByteArray(), pNum-sizeSoFar);
	}
	public static int ubyteToInt(byte b)
	{
	if (b >= 0) return (int) b;
	return ((int)b + 256);
	}
protected void writeDataFile()
	{
		try
		{
			//backup old file
			//String dataFilename = filename + ".data.txt";
			String dataFilename = newNanocad.txtDir + newNanocad.fileSeparator + 
				filename + ".data.txt";
			File oldFile = new File(dataFilename + ".old");
	    if(oldFile.exists()) oldFile.delete();
	    File currentFile = new File(dataFilename);
			currentFile.renameTo(oldFile);

			long fileEnd = 2048; //length of atom directory
			
			RandomAccessFile f = new RandomAccessFile(new File(dataFilename),"rw");
		 	for(int i =0; i < 256; i++)
		 	{
		 	    Vector propList = (Vector) atomList.elementAt(i);
		 	    f.seek(i*8); //go to appropaite place in file directory
				if (propList.size() == 0)
				{
			 	    f.writeLong((long) 0); //zero indicates no entry
			 	}
			 	else
			 	{
					//System.out.println("Outputing " + propList.size() + "bytes for " + i);
				 	f.writeLong(fileEnd);
					
				 	f.seek(fileEnd);
				 	
				 	for(int j = 0; j < propList.size(); j++)
				 	{
				 	    f.writeByte(i);
				 	    byte[] barray = (byte[]) propList.elementAt(j);
				 	    f.writeByte((byte) barray.length);
				 	    f.write(barray);
				 	    //System.out.println(fileEnd+ "  - OUT:"+i+"["+barray.length+"]");
				 	    fileEnd = fileEnd + barray.length + 2;
				 	}
				}
			}
			f.seek(fileEnd);
			f.writeByte((byte)254);
			f.close();
			System.out.println("data write completed: " + dataFilename);
		}
		catch (IOException e)
		{
			System.out.println("File write error! : " + e.toString());
		}
	}
	public void writeFile()
	{
		writeDataFile();
		writeObjectFile();
	}
	protected void writeObjectFile()
	{
		try
		{
			//String objectFileName = filename + ".object.txt";
			String objectFileName = newNanocad.txtDir + newNanocad.fileSeparator 
				+ filename + ".object.txt";
			//backup old file
			File oldFile = new File(objectFileName +".old");
		    if(oldFile.exists()) oldFile.delete();
		    File currentFile = new File(objectFileName);
			if(currentFile.exists()) currentFile.renameTo(oldFile);

			ObjectOutputStream test = new ObjectOutputStream(new FileOutputStream(objectFileName));
			test.writeObject(this);
			test.close();
			System.out.println("object write completed: " + objectFileName);
		}
		catch (Exception e)
		{
			System.out.println("File write error! : " + e.toString());
		}
	}
}
