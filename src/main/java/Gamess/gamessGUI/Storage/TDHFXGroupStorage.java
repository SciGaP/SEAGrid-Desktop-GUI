package Gamess.gamessGUI.Storage;


public class TDHFXGroupStorage extends DefaultGroupStorage {

	public TDHFXGroupStorage()
	{
		KeywordValueSeperator = " ";
	}
	
	@Override
	public boolean isAvailable(String Data) {
		return storage.containsKey(getDataSplit(Data)[0]);
	}
	
	@Override
	protected boolean isSeperatorPresent(String Data) 
	{
		return true;
	}
	
	@Override
	protected StorageHashTable ConvertGroupData(String GroupData) {
		//The group contains no data. Return null
		if(GroupData.trim().length() == 0)
			return null;
		StorageHashTable returnTable = new StorageHashTable();
		//Split the complete group with =
		String[] splitGroup = GroupData.trim().split("\n");
		for (int i = 0; i < splitGroup.length; i++) {
			String[] splitKeyword = getDataSplit(splitGroup[i].trim());
			returnTable.put(splitKeyword[0], splitKeyword[1]);
		}
		return returnTable;
	}
	
	@Override
	protected String[] getDataSplit(String Data) 
	{
		int splitIndex = Data.indexOf(KeywordValueSeperator);
		String[] splitString = new String[2];
		if(splitIndex == -1)
		{
			splitString[0] = Data;
			splitString[1] = "";
			return splitString;
		}
		splitString[0] = Data.substring(0, splitIndex).trim();
		splitString[1] = Data.substring(splitIndex).trim();
		return splitString;
	}
}
