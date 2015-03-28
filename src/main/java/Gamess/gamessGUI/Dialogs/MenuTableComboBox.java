package Gamess.gamessGUI.Dialogs;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Gamess.gamessGUI.Cosmetics;
import Gamess.gamessGUI.Dictionary;
import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.RequiresIncompatibility;
import Gamess.gamessGUI.Storage.Repository;

public class MenuTableComboBox extends JComboBox implements TableCellRenderer,TableCellEditor,IMenuTableCellTester 
{
	private static final long serialVersionUID = 1L;
	private String Group = null;
	private String Keyword = null;
	private ArrayList<String> OriginalValues = new ArrayList<String>();
	private String DEFAULT = "<default>";
	private String DefaultValue = null;
	private String DefaultToolTip = "<b>DefaultValue :</b> ";
	private ArrayList<CellEditorListener> cellListeners = new ArrayList<CellEditorListener>();
	private boolean isConsistent = true;
	private boolean isRecomputingConsistency = false;
	private Border consistentBorder = getBorder();
	private Border inConsistentBorder = BorderFactory.createLineBorder(Color.RED);
	ArrayList<IMenuTableCellTester> cellListenerList = null;
	private JComboBox rendererInstance = new JComboBox();

	//default constructor
	public MenuTableComboBox() 
	{
		super();
	}

	//default constructor
	public MenuTableComboBox(Object[] items) 
	{
		super(items);
	}
	
	//Constructor
	public MenuTableComboBox(String Group, Node currentNode, ArrayList<IMenuTableCellTester> cellListenerList) 
	{
		super();
		
		NamedNodeMap currentAttributes = currentNode.getAttributes();

		//Check if it is combobox node
		if(currentAttributes.getNamedItem("MenuType") == null || !currentAttributes.getNamedItem("MenuType").getNodeValue().equalsIgnoreCase("COMBO"))
		{
			//Error : Not a combobox node
			return;
		}
		
		this.cellListenerList = cellListenerList;
		
		if(currentAttributes.getNamedItem("Group") != null)
			Group = currentAttributes.getNamedItem("Group").getNodeValue();
		
		//Group
		this.Group = Group;
		
		//Keyword
		if(currentAttributes.getNamedItem("DisplayName") != null)
			Keyword = currentAttributes.getNamedItem("DisplayName").getNodeValue();
		
		String userToolTip = "";
		//ToolTip
		if(currentAttributes.getNamedItem("ToolTip") != null)
			userToolTip = currentAttributes.getNamedItem("ToolTip").getNodeValue();
		
		
		//Values
		NodeList valueList = currentNode.getChildNodes();
		for(int i = 0 ; valueList.getLength() > i ; i++)
		{
			Node valueNode;
			//if it is a text node skipit;
			if((valueNode = valueList.item(i)).getNodeType() == Node.TEXT_NODE)
				continue;
			String _Value = valueNode.getTextContent().trim();
			currentAttributes = valueNode.getAttributes();
			//Check if this value is the default value
			if(currentAttributes.getNamedItem("isDefault") != null && currentAttributes.getNamedItem("isDefault").getNodeValue().equalsIgnoreCase("True"))
				DefaultValue = _Value;
			//Add the value to the list
			OriginalValues.add(_Value);
			
			//////////////////////////////////////////////////////////////////////
			//			Register the entity	with the Organized document		   	//
			//			This is used for the ContentAssist
				Dictionary.Register(Group, Keyword, _Value);
			//																	//
			//////////////////////////////////////////////////////////////////////

		}
		//Add items
		addItem(DEFAULT);
		for (int i = 0; i < OriginalValues.size(); i++)
			addItem(OriginalValues.get(i));
		if(DefaultValue != null)
		{
			setSelectedItem(DEFAULT);
			DefaultToolTip += DefaultValue;
		}
		else
		{
			setSelectedIndex(-1);
			DefaultToolTip += "There is no default value set";
		}
		
		if(userToolTip.length() != 0)
		 DefaultToolTip = userToolTip + "<br/>" + DefaultToolTip;
		
		setToolTip(DefaultToolTip);
	}
	
	public void resetValues() 
	{
		isRecomputingConsistency = true;
		String valueInDB = Repository.getInstance().Retrieve(Group , Keyword);
		//if valueInDB is null then there is no value in the document. 
		if(valueInDB == null)
		{
			//reset it to the default value
			if(DefaultValue == null)
				setSelectedIndex(-1);
			else
				setSelectedItem(DefaultValue);
			isRecomputingConsistency = false;
			return;
		}
		//else set the value in the DB to be the current selected
		setSelectedItem(valueInDB);
		isRecomputingConsistency = false;
	}
	
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		rendererInstance.removeAllItems();
		rendererInstance.addItem((getSelectedItem() == null)?null:getSelectedItem());
		//if(isConsistent == false)
			//rendererInstance.setBorder(inConsistentBorder);
		//else
			//rendererInstance.setBorder(consistentBorder);
		//rendererInstance.setEnabled(super.isEnabled());
		return rendererInstance;
		//return new MenuTableComboBox(new String[]{(this.getSelectedItem() == null)?null:this.getSelectedItem().toString()});
	}

	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		return this;
	}

	public void addCellEditorListener(CellEditorListener listener) 
	{
		cellListeners.add(listener);
	}

	public void cancelCellEditing() {
		for (int i = 0; i < cellListeners.size(); i++) 
		{
			cellListeners.get(i).editingCanceled(new ChangeEvent(this));
		}
	}

	public Object getCellEditorValue() 
	{
		if(getSelectedItem() == null)
			return null;
		
		String CellValue = null;
		//if the selected value is "<default>" return the default value
		if(DEFAULT.equalsIgnoreCase(getSelectedItem().toString()))
			CellValue = DefaultValue;
		else
			CellValue = getSelectedItem().toString();
		
		Repository.getInstance().Store(Group, Keyword + "=" + CellValue);
		
		return CellValue;
	}

	public boolean isCellEditable(EventObject arg0) 
	{
		//if the combobox is empty then this is not editable 
		if(super.getItemCount() == 0)
			return false;
		return true;
	}

	public void removeCellEditorListener(CellEditorListener listener) 
	{
		cellListeners.remove(listener);
	}

	public boolean shouldSelectCell(EventObject arg0) 
	{
		return true;
	}

	public boolean stopCellEditing() 
	{
		for (int i = 0; i < cellListeners.size(); i++) 
		{
			cellListeners.get(i).editingStopped(new ChangeEvent(this));
		}
		return true;
	}

	public void recomputeConsistency() {
		isRecomputingConsistency = true;
		//Check if this group/keyword is excluded
		if(ExcludeIncompatibility.getInstance().isLikelyToBecomeIncompatible(Group) || ExcludeIncompatibility.getInstance().isLikelyToBecomeIncompatible(Group + " " + Keyword))
		{
			setComboBoxEnabled(false);
			setToolTip("This is disabled because it is likely to be excluded if selected.");
			isRecomputingConsistency = false;
			return;
		}
		setComboBoxEnabled(true);
		setToolTip(DefaultToolTip);
		
		String currentValue = (String)getSelectedItem();
		if(DEFAULT.equalsIgnoreCase(currentValue))
			currentValue = DefaultValue;
		//Remove all the items
		removeAllItems();
		addItem(DEFAULT);
		//check all the values in the original list
		for (int i = 0; i < OriginalValues.size(); i++) 
		{
			String Value = OriginalValues.get(i);
			//Check if this value is required
			//if required and if is not the current selected value then set the inconsistency to true
			if(RequiresIncompatibility.getInstance().isLikelyToBecomeIncompatible(Group + " " + Keyword + " " + Value) && !Value.equals(currentValue))
			{
				setConsistency(false);
				setToolTip(Value + " is currently required. Please select it");
			}
			//Check if this value is excluded
			//if excluded then remove it from the combobox else add it
			//This makes the the combobox consistent
			boolean isIncompatible = ExcludeIncompatibility.getInstance().isLikelyToBecomeIncompatible(Group + " " + Keyword + " " + Value);
			//the value is incompatible
			//check if the item is the current selected value
			if(isIncompatible == true && Value.equals(currentValue))
			{
				//The item is inconsistent
				addItem(Value);
				setConsistency(false);
				setToolTip("Current selected value is inconsistent. Please select another value");
			}
			if(!isIncompatible)
				addItem(Value);
			/*if(isIncompatible)
			{
				if(Value.equals(currentValue))
				{
					//it is the current selected value
					isConsistent = false;
					continue;
				}
				for (int j = 0; j < getItemCount(); j++) 
				{
					if(getItemAt(j).equals(Value))
					{
						removeItemAt(j);
						j--;
					}
				}
			}
			else
			{
				//This value is not incompatible.
				//Check if it is available in the list. if not add it.
				boolean isItemAvail = false;
				for (int j = 0; j < getItemCount(); j++) 
				{
					if(getItemAt(j).equals(Value))
						isItemAvail = true;
				}
				// find the position to insert
				if(!isItemAvail)
					addItem(Value);
			}*/
		}
		if(currentValue == null)
			setSelectedIndex(-1);
		else
		{
			if(currentValue.equalsIgnoreCase(DefaultValue))
				setSelectedItem(DEFAULT);
			else
				setSelectedItem(currentValue);
		}
		isRecomputingConsistency = false;
	}

	public boolean isConsistent() {
		return isConsistent;
	}

	public boolean isDefault() {
		//if nothing is selected or if it is the default value or if the value is "<default>" then it is default
		if(getSelectedItem() == null || getSelectedItem().toString().equalsIgnoreCase(DefaultValue) || getSelectedItem().toString().equalsIgnoreCase(DEFAULT))
			return true;
		return false;
	}

	public String getGroup() {
		return Group;
	}

	public String getKeyword() {
		return Keyword;
	}

	public String getValue() 
	{
		return (String)getSelectedItem();
	}
	
	public void setGroup(String Group) {
		this.Group = Group;
	}

	public void setKeyword(String Keyword) {
		this.Keyword = Keyword;
	}

	private void setConsistency(boolean Consistent)
	{
		isConsistent = Consistent;
		if(Consistent == false)
		{
			setBorder(inConsistentBorder);
			rendererInstance.setBorder(inConsistentBorder);
		}
		else
		{
			setBorder(consistentBorder);
			rendererInstance.setBorder(consistentBorder);
		}
	}
	
	@Override
	protected void selectedItemChanged() {
		if(isRecomputingConsistency == false)
		{
			//Propogate the values and retest all the incompatibility
			String value = null;
			//Get the previous item from the combobox
			if(getSelectedItem() != null)
				value = getSelectedItem().toString();
			if(DEFAULT.equalsIgnoreCase(value))
				value = DefaultValue;
			//delete the previous item from the repository
			if(value != null)
			{
				Repository.getInstance().Remove(Group, Keyword);
			}
			
			//set the new item
			super.selectedItemChanged();
			//some value is changed. Place it in the DB
			//Get the changed value
			if(getSelectedItem() != null)
				value = getSelectedItem().toString();
			if(DEFAULT.equalsIgnoreCase(value))
				value = DefaultValue;
			//Add it to the repository
			if(value != null && !value.equalsIgnoreCase(DefaultValue))
			{
				String seperator = Repository.getInstance().getKeywordValueSeperator(Group);
				Repository.getInstance().Store(Group, Keyword + seperator + value);
			}
			//fire test in all the peer component in this dialog
			for (int i = 0; i < cellListenerList.size(); i++) {
				cellListenerList.get(i).recomputeConsistency();
			}
		}
	}
	
	private void setToolTip(String tooltip)
	{
		tooltip = Cosmetics.getFormattedToolTip(tooltip);
		setToolTipText(tooltip);
		rendererInstance.setToolTipText(tooltip);
	}
	
	private void setComboBoxEnabled(boolean enabled)
	{
		setEnabled(enabled);
		rendererInstance.setEnabled(enabled);
	}
}
