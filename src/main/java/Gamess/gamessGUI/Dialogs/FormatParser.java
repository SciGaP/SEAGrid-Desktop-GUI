package Gamess.gamessGUI.Dialogs;

import java.util.Hashtable;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import Gamess.gamessGUI.GlobalParameters;
import Gamess.gamessGUI.InputFileHandlers.InputFileReader;
import Gamess.gamessGUI.Storage.Repository;


public class FormatParser
{
	private static Hashtable<String, FormatParser> formatLookup = new Hashtable<String, FormatParser>();

	public static FormatParser getFormat(String Group, String Keyword, Node currentNode)
	{
		//Check if the format is already available
		FormatParser formatParser = formatLookup.get(Group + " " + Keyword);
		if(formatParser == null)
		{
			//the format is not available
			//create a new format
			formatParser = new FormatParser(Group,Keyword,currentNode);
			//add it to the lookup			
			formatLookup.put(Group + " " + Keyword, formatParser);
		}
		return formatParser;
	}

	/////////////////////////////////////////////////////////////
	
	private enum inputType {String,Int,Float,Double};
	private boolean isInfinity = false;
	inputType type = inputType.Int;
	String Count = null;
	String Minimum = null;
	String Maximum = null;
	String Default = null;
	String Group = null;
	String Keyword = null;
	String Reason = null;

	private FormatParser(String _Group, String _Keyword, Node currentNode)
	{
		Group = _Group;
		Keyword = _Keyword;
		//Type
		if(currentNode == null )
		{
			type = inputType.String;
			return;
		}
		NamedNodeMap attributes = currentNode.getAttributes();
		if(attributes.getNamedItem("Type") != null)
		{
			String valueType = attributes.getNamedItem("Type").getNodeValue();
			if(valueType.equalsIgnoreCase("String"))
			{
				type = inputType.String;
			}
			else if(valueType.equalsIgnoreCase("Int"))
			{
				type = inputType.Int;
			}
			else if(valueType.equalsIgnoreCase("Float"))
			{
				type = inputType.Float;
			}
			else if(valueType.equalsIgnoreCase("Double"))
			{
				type = inputType.Double;
			}
		}
		//Count
		if(attributes.getNamedItem("Count") != null)
			Count = attributes.getNamedItem("Count").getNodeValue();
		if(Count != null && Count.equals("..."))
			isInfinity = true;
		//Min
		if(attributes.getNamedItem("Min") != null)
			Minimum = attributes.getNamedItem("Min").getNodeValue();
		//Max
		if(attributes.getNamedItem("Max") != null)
			Maximum = attributes.getNamedItem("Max").getNodeValue();
		//Default
		Default = currentNode.getTextContent().trim();
	}
	
	public boolean isConsistent(String Value)
	{
		if(Value == null || Value.length() == 0)
			return true;

		//if it is a string return true
		if(type == inputType.String)
			return true;
		//otherwise it should be either Integer/Float/Double
		String[] values = Value.split(",");
		int count = getCount();
		//Check if it is infinity
		if(isInfinity)
			count = values.length;
		
		//Check if the count is matching with the no of values available
		//if not return false
		if(count != values.length)
		{
			//Not a proper format. Missing some values. Format is <Type>[count]
			Reason = "The must be " + count + " values seperated by comma.";
			return false;
		}
		
		//Test the values one by one and evaluate
		for (int i = 0; i < count; i++) {
			Number numberValue = null;
			try
			{
				numberValue = getValue(values[i].trim());
			}
			catch (NumberFormatException e) {
				//Not a proper format. Invalid <Type> value. Format is <Type>[count]
				Reason = "Please type a valid number format";
				return false;
			}
			if(!testRange(numberValue))
				return false;
		}
		
		return true;
	}
	
	public boolean isDefault(String Value)
	{
		//if the user is completely removing the value then it is set to default
		if(Value == null || Value.length() == 0)
			return true;
		//Check if it is string
		if(type == inputType.String)
		{
			if( Value.equalsIgnoreCase(Default))
				return true;
			return false;
		}
		//otherwise it should be either Integer/Float/Double
		String[] values = Value.split(",");
		String[] defaults = null;
		if(Default != null)
			defaults = getDefault(values.length).split(",");
		
		if(defaults.length == 0 || values.length != defaults.length)
			return false;
		
		int count = getCount();
		//Check if it is infinity
		if(isInfinity)
			count = values.length;
		
		//compare the values and the defaults
		for (int i = 0; i < count; i++) {
			Number inputValues = null , defaultValue = null;
			try
			{
				inputValues = getValue(values[i]);
				defaultValue = getValue(defaults[i]);
				switch (type) {
				case Int:
					if(inputValues.intValue() != defaultValue.intValue())
						return false;
					break;
				case Float:
					if(inputValues.floatValue() != defaultValue.floatValue())
						return false;
					break;
				case Double:
					if(inputValues.doubleValue() != defaultValue.doubleValue())
						return false;
					break;
				}
			}
			catch (NumberFormatException e) {
				//Default values are not in proper format
				return false;
			}
		}
		
		return true;
	}
	
	public String getDefault(int valueCount)
	{
		//Build the dynamic default value
		if(Default.endsWith("..."))
		{
			if(valueCount == -1)
				return Default;
			
			StringBuilder dynamicDefault = new StringBuilder();
			
			String CurrentDefault = Default;
			int recursionIndex = -1;
			int count = isInfinity?valueCount:getCount();
			
			//Check if there is a sub structure like 1,{0...
			if( (recursionIndex = Default.indexOf(",{")) != -1)
			{
				CurrentDefault = Default.substring(recursionIndex + 2);
				String InitialValues = Default.substring(0, recursionIndex);
				dynamicDefault.append(InitialValues);
				count -= InitialValues.split(",").length;
			}
			//
			String[] defaultValues = CurrentDefault.replace("...", "").split(",");

			for (int i = 0, currentDefaultValuePosition = 0; i < count; i++, 
			currentDefaultValuePosition = (currentDefaultValuePosition == defaultValues.length-1)?0:(currentDefaultValuePosition + 1)) 
			{
				if(dynamicDefault.length() != 0)
					dynamicDefault.append(",");
				dynamicDefault.append(defaultValues[currentDefaultValuePosition]);
			}
			return dynamicDefault.toString();
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//																																				 //
		
		//TODO HARDCODE VALUES HERE
		ValueReader reader = new ValueReader();
		//NRAD0 , NTHE0 , NPHI0
		if(Group.equalsIgnoreCase("DFT") && (Keyword.equalsIgnoreCase("NRAD0") || Keyword.equalsIgnoreCase("NTHE0") || Keyword.equalsIgnoreCase("NPHI0")))
		{
			String NRAD = reader.Read("DFT", "NRAD") , NTHE = reader.Read("DFT", "NTHE") , NPHI = reader.Read("DFT", "NPHI");
			int nNRAD = (NRAD == null)?96:Integer.parseInt(NRAD) , nNTHE = (NTHE == null)?12:Integer.parseInt(NTHE) , nNPHI = (NPHI == null)?24:Integer.parseInt(NPHI);
			if(Keyword.equalsIgnoreCase("NRAD0"))
			{
				if(nNRAD== 96 && nNTHE == 12 && nNPHI == 24)
					return "24";
				return Float.toString(Math.max(nNRAD/4, 24));
			}
			if(Keyword.equalsIgnoreCase("NTHE0"))
			{
				if(nNRAD== 96 && nNTHE == 12 && nNPHI == 24)
					return "8";
				return Float.toString(Math.max(nNTHE/3, 8));
			}
			if(Keyword.equalsIgnoreCase("NPHI0"))
			{
				if(nNRAD== 96 && nNTHE == 12 && nNPHI == 24)
					return "16";
				return Float.toString(Math.max(nNPHI/3, 16));
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(Group.equalsIgnoreCase("RELWFN") && Keyword.equalsIgnoreCase("QRTOL"))
		{
			if(reader.isAvailable("CONTRL", "RELWFN=RESC"))
			{
				String QMTTOL = reader.Read("RELWFN", "QMTTOL");
				float nQMTTOL = (QMTTOL == null)?0:getValue(QMTTOL).floatValue();
				return Float.toString((float)Math.min(1.0E-8, nQMTTOL));
			}
			else if(reader.isAvailable("CONTRL", "RELWFN=DK"))
			{
				return "1.0E-2";
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(Group.equalsIgnoreCase("STATPT") && Keyword.equalsIgnoreCase("TRMAX"))
		{
			if(reader.isAvailable("CONTRL","RUNTYP=OPTIMIZE"))
				return "0.5";
			if(reader.isAvailable("CONTRL","RUNTYP=SADPOINT"))
				return "0.3";
		}
		
		if(Group.equalsIgnoreCase("STATPT") && Keyword.equalsIgnoreCase("NNEG"))
		{
			if(reader.isAvailable("CONTRL","RUNTYP=OPTIMIZE"))
				return "0";
			if(reader.isAvailable("CONTRL","RUNTYP=SADPOINT"))
				return "1";
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(Group.equalsIgnoreCase("TRANST") && Keyword.equalsIgnoreCase("NSTATE"))
		{
			if(reader.isAvailable("TRANST","IROOTS"))
				return reader.Read("TRANST","IROOTS");
			else
				return "1";
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(Group.equalsIgnoreCase("CIDET") || Group.equalsIgnoreCase("CIGEN") || Group.equalsIgnoreCase("DET") || Group.equalsIgnoreCase("GEN"))
		{
			if(Keyword.equalsIgnoreCase("NSTGSS"))
			{
				if(reader.isAvailable(Group.toUpperCase(),"NSTATE"))
					return reader.Read(Group.toUpperCase(),"NSTATE");
				else
					return "1";
			}
			
			if(Keyword.equalsIgnoreCase("MXXPAN"))
			{
				if(reader.isAvailable(Group.toUpperCase(),"NSTATE"))
				{
					String NSTGSS = reader.Read(Group.toUpperCase(),"NSTGSS");
					int iNSTGSS = 0;
					if( (iNSTGSS = Integer.parseInt(NSTGSS) * 2) > 10)
						return String.valueOf(iNSTGSS);
					else
						return "10";
				}
				else
					return "10";
			}
		}
		
		//																																				 //
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Return the normal default
		return Default;
	}
	
	private int getCount()
	{
		//Check if this is referres some other key 
		if(Count != null && Count.startsWith("$"))
		{
			String[] dataSplit = Count.split(" ");
			if(dataSplit.length >= 2 )
			{
				//Get the referred value
				String referredValue = new ValueReader().Read(dataSplit[0].substring(1), dataSplit[1]);
				int returnValue = 0;
				if(referredValue == null)
				{
					referredValue = "0";
				}
				else
				{
					returnValue = Integer.parseInt(referredValue);
				}
				
				
				
				//////////////////////////////////////////////////////////////////////////////
				//																			//
				
				//TODO HARDCODED VALUES
				if(Group.equalsIgnoreCase("SCF") && Keyword.equalsIgnoreCase("CICOEF"))
					returnValue *= 2;
				
				//////////////////////////////////////////////////////////////////////////////
				
				if(Group.equalsIgnoreCase("EOMNIP") && Keyword.equalsIgnoreCase("MOACT"))
				{
					referredValue = new ValueReader().Read("EOMNIP", "NUACT");
					if(referredValue == null)
						referredValue = "0";
					
					returnValue += Integer.parseInt(referredValue);
				}
				
				//////////////////////////////////////////////////////////////////////////////
				
				if(Group.equalsIgnoreCase("TDHF") && Keyword.equalsIgnoreCase("FREQ"))
					returnValue = (new ValueReader().Read(dataSplit[0].substring(1), dataSplit[1]) == null)?1:returnValue;
				
				//////////////////////////////////////////////////////////////////////////////
				
				if(Group.equalsIgnoreCase("ORMAS") && dataSplit[1].equalsIgnoreCase("NSPACE"))
					returnValue = (referredValue == null)?1:returnValue;
				
				//																			//
				//////////////////////////////////////////////////////////////////////////////
				
				//Return the integer version of the referred value
				return returnValue;
			}
			else {
				return 0;
			}
		}
		//Check if the count is not null
		if(Count != null && !Count.equals("..."))
		{
			//Write the Hardcoded expressions here
			return Integer.parseInt(Count);
		}
		//if count is null return 1
		return 1;
	}
	private Number getValue(String value) throws NumberFormatException
	{
		switch (type) 
		{
			case Int:
				return Integer.parseInt(value);
			case Float:
				return Float.parseFloat(value);
			case Double:
				return Double.parseDouble(value);
			default:
				break;
		}
		return null;
	}
	
	private boolean testRange(Number value)
	{
		try
		{
			if(testMin(value) == false)
			{
				//Number is not within range
				Reason = "The number should be >= (greaterthan or equal to) " + Minimum + ".";
				return false;
			}
		}
		catch (NumberFormatException e) {
			//Minimum cannot be parsed
			return false;
		}
		try
		{
			if(testMax(value)== false)
			{
				//Number is not within range
				Reason = "The number should be <= (lessthan or equal to) " + Minimum + ".";
				return false;
			}
		}
		catch (NumberFormatException e) {
			//Maximum cannot be parsed
			return false;
		}

		return true;
	}
	
	private boolean testMin(Number Value) throws NumberFormatException
	{
		if(Minimum != null)
		{
			//TODO Hardcode values here. change the maximum here
			switch (type) 
			{
				case Int:
					//if the value is less than the minimum then return false
					if(Value.intValue() < getValue(Minimum).intValue())
						return false;
					return true;
				case Float:
					//if the value is less than the minimum then return false
					if(Value.floatValue() < getValue(Minimum).floatValue())
						return false;
					return true;
				case Double:
					//if the value is less than the minimum then return false
					if(Value.doubleValue() < getValue(Minimum).doubleValue())
						return false;
					return true;
			}
		}
		return true;
	}
	
	private boolean testMax(Number Value) throws NumberFormatException
	{
		if(Maximum != null)
		{
			//TODO Hardcode values here. change the maximum here
			switch (type) 
			{
				case Int:
					//if the value is greater than the maximum then return false
					if(Value.intValue() > getValue(Maximum).intValue())
						return false;
					return true;
				case Float:
					//if the value is greater than the maximum then return false
					if(Value.floatValue() > getValue(Maximum).floatValue())
						return false;
					return true;
				case Double:
					//if the value is greater than the maximum then return false
					if(Value.doubleValue() > getValue(Maximum).doubleValue())
						return false;
					return true;
			}
		}
		return true;
	}
	
	private class ValueReader
	{
		Repository DB = Repository.getInstance();
		InputFileReader reader = InputFileReader.getInstance();
		
		public boolean isAvailable(String Group, String Keyword)
		{
			//Check if the dialog is opened or is called from update file
			//if the dialog is opeaned then the application will be in provisional mode
			if(GlobalParameters.isProvisionalMode)
				return DB.isAvailable(Group, Keyword);
			else
				return reader.isAvailable(Group, Keyword);
		}
		
		public String Read(String Group, String Keyword)
		{
			//Check if the dialog is opened or is called from update file
			//if the dialog is opeaned then the application will be in provisional mode
			if(GlobalParameters.isProvisionalMode)
				return DB.Retrieve(Group, Keyword);
			else
				return reader.Read(Group, Keyword);
		}
	}
}