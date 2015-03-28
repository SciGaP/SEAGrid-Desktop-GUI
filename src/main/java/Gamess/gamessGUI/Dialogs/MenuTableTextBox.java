package Gamess.gamessGUI.Dialogs;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import Gamess.gamessGUI.Cosmetics;
import Gamess.gamessGUI.Dictionary;
import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.RequiresIncompatibility;
import Gamess.gamessGUI.Storage.Repository;

public class MenuTableTextBox extends JTextField implements
		TableCellRenderer, TableCellEditor, IMenuTableCellTester{

	private static final long serialVersionUID = 1L;
	private ArrayList<CellEditorListener> cellEditorListener = new ArrayList<CellEditorListener>();
	private String Group = null;
	private String Keyword = null;
	private String DEFAULT = "<default>";
	private String DefaultToolTip = "<b>Default value : </b>";
	private boolean isConsistent = true;
	private boolean isCellEditable = true;
	private boolean isDefault = false;
	private boolean isValueChanged = true;
	private FormatParser format = null;
	private Border consistentBorder = getBorder();
	private Border inConsistentBorder = BorderFactory.createLineBorder(Color.RED);
	ArrayList<IMenuTableCellTester> cellListenerList = null;
	private JTextField rendererInstance = new JTextField();
	
	public MenuTableTextBox() 
	{
		super();
	}
	
	public MenuTableTextBox(String Group , Node currentNode , ArrayList<IMenuTableCellTester> _cellListenerList, JDialog parentDialog) {
		super();
		
		NamedNodeMap currentAttributes = currentNode.getAttributes();

		//Check if it is combobox node
		if(currentAttributes.getNamedItem("MenuType") == null || !currentAttributes.getNamedItem("MenuType").getNodeValue().equalsIgnoreCase("TEXTBOX"))
		{
			//Error : Not a combobox node
			return;
		}
		
		cellListenerList = _cellListenerList;
		
		if(currentAttributes.getNamedItem("Group") != null)
			Group = currentAttributes.getNamedItem("Group").getNodeValue();
		
		//Group
		this.Group = Group;
		
		//Keyword
		if(currentAttributes.getNamedItem("DisplayName") != null)
			Keyword = currentAttributes.getNamedItem("DisplayName").getNodeValue();
		
		//Format
		Node optionNode = null;
		for(optionNode = currentNode.getFirstChild(); optionNode != null && optionNode.getNodeType() == Node.TEXT_NODE ; optionNode = optionNode.getNextSibling());
		format = FormatParser.getFormat(this.Group, Keyword, optionNode);
		
		//ToolTip
		if(currentAttributes.getNamedItem("ToolTip") != null)
			DefaultToolTip = currentAttributes.getNamedItem("ToolTip").getNodeValue() + "<br>" + DefaultToolTip;
		DefaultToolTip += ((format.getDefault(-1) == null || format.getDefault(-1).trim().length() == 0)?"There is no default value for this":format.getDefault(-1) );
		
		setToolTip(DefaultToolTip);
		
		//////////////////////////////////////////////////////////////////////
		//			Register the entity	with the Organized document		   	//
		//			This is used for the ContentAssist
			Dictionary.Register(Group, Keyword, Dictionary.TEXTBOX_VALUE);
			Dictionary.registerDialog(Group + " " + Keyword, parentDialog);
		//																	//
		//////////////////////////////////////////////////////////////////////
	}

	public void resetValues() 
	{
		String valueInDB = Repository.getInstance().Retrieve(Group , Keyword);
		
		//if the value in the textbox and the DB are different then set isValueChanged to true
		if(!getText().equalsIgnoreCase(valueInDB))
			isValueChanged = true;
		
		//if valueInDB is null then there is no value in the document. 
		if(valueInDB == null)
		{
			//reset it to the default value
				setText(DEFAULT);
				setToolTip(DefaultToolTip);
		}
		//else set the value in the DB to be the current selected
		setText(valueInDB);
	}
	
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,boolean arg2, boolean arg3, int arg4, int arg5) {
		//Check if the value is changed since last call to this function
		//if it is then check if the current value is default
		if(isValueChanged)
			isDefault = format.isDefault(getText());
		//change the flag to false
		isValueChanged = false;
		//if the current value is default then set the text as default
		if(isDefault)
			rendererInstance.setText(DEFAULT);
		//else set the current text value
		else
			rendererInstance.setText(getText());
		//if(isConsistent == false)
			//rendererInstance.setBorder(inConsistentBorder);
		//else
			//rendererInstance.setBorder(consistentBorder);
		return rendererInstance;
	}

	public Component getTableCellEditorComponent(JTable arg0, Object arg1,boolean arg2, int arg3, int arg4) {
		return this;
	}

	public void addCellEditorListener(CellEditorListener listener) {
		cellEditorListener.add(listener);
	}

	public void cancelCellEditing() {
		String previousText = rendererInstance.getText();
		if(DEFAULT.equals(previousText))
			setText(format.getDefault(-1));
		setText(previousText);
		for (int i = 0; i < cellEditorListener.size(); i++) {
			cellEditorListener.get(i).editingCanceled(new ChangeEvent(this));
		}
	}

	public Object getCellEditorValue() {
		String CellValue = null;
		if(format.isDefault(getText()))
		{
			CellValue = format.getDefault(-1);
		}
		else
			CellValue = super.getText();
		
		if(CellValue != null)
			Repository.getInstance().Store(Group, Keyword + "=" + CellValue);
		
		return CellValue;
	}

	public boolean isCellEditable(EventObject arg0) {
		return this.isCellEditable;
	}

	public void removeCellEditorListener(CellEditorListener listener) {
		cellEditorListener.remove(listener);
	}

	public boolean shouldSelectCell(EventObject arg0) {
		return true;
	}

	public boolean stopCellEditing() 
	{
		//parse the string
		setConsistency(format.isConsistent(getText()));
		if(isConsistent)
		{
			setToolTip(DefaultToolTip);
			isValueChanged = true;
			for (int i = 0; i < cellEditorListener.size(); i++)
				cellEditorListener.get(i).editingStopped(new ChangeEvent(this));
			valueChanged();
			return true;
		}

		setToolTip("There is an error in this field.<br/> <b>Reason :</b> " + format.Reason);
		//else set the inconsistency border
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
		return getText();
	}
	
	public boolean isConsistent() {
		return isConsistent;
	}

	public boolean isDefault() 
	{
		return format.isDefault(getText());
	}

	public void recomputeConsistency() 
	{
		//Test if the value is excluded
		ExcludeIncompatibility excludes = ExcludeIncompatibility.getInstance();
		setTextboxEnabled(!( excludes.isLikelyToBecomeIncompatible(Group) || excludes.isLikelyToBecomeIncompatible(Group + " " + Keyword) || excludes.isLikelyToBecomeIncompatible(Group + " " + Keyword + " " + getText())));
		if(isEnabled())
		{
			//Set the consistency if the textbox
			setConsistency(format.isConsistent(getText()));
			if(isConsistent)
				setToolTip(DefaultToolTip);
			else
				setToolTip("There is an error in this field.<br/> <b>Reason :</b> " + format.Reason);
			
			
			//Test if it is partially incompatible. test the Required Incompatibility
			String partialIncompatibilities = RequiresIncompatibility.getInstance().getPartialIncompatibility(Group + " " + Keyword);
			if(isConsistent && partialIncompatibilities != null && partialIncompatibilities.trim().length() != 0)
			{
				//This item is incompatible. 
				//But this is Required. 
				//So this should be enabled
				//Set an in consistency border and set the tooltip as required
				setConsistency(false);
				setToolTip("$" + Group + " " + Keyword + " = " + partialIncompatibilities + " is required");
			}
		}
	}

	private void setTextboxEnabled(boolean enabled) {
		setEnabled(enabled);
		rendererInstance.setEnabled(enabled);
		if(enabled)
			setToolTip(DefaultToolTip);
		else
			setToolTip("This is disabled because it is likely to be excluded if selected.");
	}

	public void setGroup(String Group) {
		this.Group = Group;

	}

	public void setKeyword(String Keyword) {
		this.Keyword = Keyword;
	}

	public void valueChanged() 
	{
		///Check if the value is consistent.
		if(isConsistent)
		{
			//if it is then recompute the consistency on all cells
			for (int i = 0; i < cellListenerList.size(); i++) 
			{
				cellListenerList.get(i).recomputeConsistency();
			}
		}
		
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
	
	private void setToolTip(String tooltip)
	{
		tooltip = Cosmetics.getFormattedToolTip(tooltip);
		setToolTipText(tooltip);
		rendererInstance.setToolTipText(tooltip);
	}
}
