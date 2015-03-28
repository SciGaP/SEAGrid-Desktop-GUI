/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Center for Computational Sciences, University of Kentucky 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/
/**
 * @author Shreeram
 * @author Michael Sheetz
 */

package Gamess.gamessGUI.Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Gamess.gamessGUI.Dictionary;
import Gamess.gamessGUI.UndoRedoHandler;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;


public class MatrixDialog extends JDialog 
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	//
	private JDialog thisDialog = null;
	private JFrame frame = new JFrame();
	private JPanel headingPanel = null;
	private JLabel headingLabel = null; 
	private JPanel btnPanel = new JPanel();
	private JButton doneBtn = new JButton("Done");
	private JButton clearBtn = new JButton("Clear");
	private JButton exitBtn = new JButton("Exit");
	private JScrollPane tableScrollPane = new JScrollPane();
	private String DefaultGroup = "";
	private int GridRowCount = 5;
	private JTable table = null;
	private DefaultTableModel model = null;
	private Class[] ColumnClasses = null;
	private String[] ColumnNames = null;
	private ActionListener buttonAction = null;
	private GridGroupRW GroupRW = null;
	
	public MatrixDialog( Frame parentFrame , Node referenceNode , String _DefaultGroup ) 
	{
		super(parentFrame);
		thisDialog = this;
		DefaultGroup = _DefaultGroup;
		if(referenceNode == null)
		{
			//Error:No referenceNode assigned
		}
		
		/*
		 * add the basic layouts and the basic frame contents
		 */
		this.setLayout(new BorderLayout());
		frame.setLayout(new BorderLayout());
		this.getContentPane().add(frame.getContentPane());
		//add button panel
		btnPanel.setLayout(new FlowLayout());
		btnPanel.add(doneBtn);
		btnPanel.add(clearBtn);
		btnPanel.add(exitBtn);
		//Add button listeners
		buttonAction = new ButtonListener();
		doneBtn.addActionListener(buttonAction);
		clearBtn.addActionListener(buttonAction);
		exitBtn.addActionListener(buttonAction);
		//
		frame.getContentPane().add(btnPanel,BorderLayout.SOUTH);
		this.setSize(400,300);
		this.setLocationRelativeTo(parentFrame);
		//
		
		NamedNodeMap mainAttributes = referenceNode.getAttributes();
		if(mainAttributes.getNamedItem("Title") != null)
		{
			setTitle(mainAttributes.getNamedItem("Title").getNodeValue());
		}
		if(mainAttributes.getNamedItem("Heading") != null)
		{
			headingPanel = new JPanel();
			headingLabel = new JLabel();
			headingLabel.setText(mainAttributes.getNamedItem("Heading").getNodeValue());
			headingPanel.add(headingLabel);
			frame.getContentPane().add(headingPanel,BorderLayout.NORTH);
		}
		if(mainAttributes.getNamedItem("RowCount") != null)
		{
			GridRowCount = Integer.parseInt( mainAttributes.getNamedItem("RowCount").getNodeValue());  
		}
		
		/*
		 * Form the table model here
		 */
		int GridColumnCount = 0;
		XPath xpathcolumnList;
		xpathcolumnList = XPathFactory.newInstance().newXPath();
		NodeList ColumnList = null;
		try {
			ColumnList = (NodeList)xpathcolumnList.evaluate("GridModel/column", referenceNode, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		//Get the number of columns
		GridColumnCount = ColumnList.getLength();
		
		//Initialize the column variables
		ColumnClasses = new Class[GridColumnCount];
		ColumnNames = new String[GridColumnCount];
		
		//Get the information about the particular Column
		for (int i = 0; i <GridColumnCount ; i++) {
			//Iterate over each column tag
			Node ColumnTag = ColumnList.item(i);
			
			//Get the Column Classes
			NamedNodeMap ColumnAttributes = ColumnTag.getAttributes();
			if(ColumnAttributes == null || ColumnAttributes.getNamedItem("DataType") == null)
			{
				ColumnClasses[i] = Integer.class;
			}
			else
			{
				String ColumnClass = ColumnAttributes.getNamedItem("DataType").getNodeValue();
				if(ColumnClass.equals("Int"))
				{
					ColumnClasses[i] = Integer.class;
				}
				else if(ColumnClass.equals("Bool"))
				{
					ColumnClasses[i] = Boolean.class;
				}
				else if(ColumnClass.equals("Float"))
				{
					ColumnClasses[i] = Float.class;
				}
				else if(ColumnClass.equals("Double"))
				{
					ColumnClasses[i] = Double.class;
				}
				else if(ColumnClass.equals("String"))
				{
					ColumnClasses[i] = String.class;
				}
			}
			
			//Get the column names 
			Node ColumnName = ColumnTag.getFirstChild();
			while(ColumnName != null && ColumnName.getNodeValue().trim().equals(""))
			{
				ColumnName = ColumnName.getNextSibling();
			}
			if(ColumnName == null)
			{
				ColumnNames[i] = "";
			}
			else
			{
				ColumnNames[i] = ColumnName.getNodeValue();
			}
		}
		
		//Create the table with Default table model
		this.SetNewModalForTable();
		table = new JTable(model);
		tableScrollPane.setViewportView(table);
		frame.add(tableScrollPane , BorderLayout.CENTER);
		this.addComponentListener(new DialogOpenClose());
		
		/*
		 * Set the properties of the table here
		 */
		table.setRowHeight(21);

		/*
		 * Get the keywords and their column list 
		 */
		XPath xpathGroupList;
		xpathGroupList = XPathFactory.newInstance().newXPath();
		NodeList GroupInfoList = null;
		try {
			GroupInfoList = (NodeList)xpathGroupList.evaluate("GridGroups/GridGroup", referenceNode, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		GroupRW = new GridGroupRW(GroupInfoList);
	}
	
	private void SetNewModalForTable()
	{
		model = new DefaultTableModel(ColumnNames , GridRowCount)
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public Class getColumnClass(int columnIndex) {
				return ColumnClasses[columnIndex];
			}
		};
		
		/*
		 * Add the table model listener so that when the user 
		 * enters a value in the lastrow a new row is added 
		 */
		model.addTableModelListener(new TableModelListener(){

			public void tableChanged(TableModelEvent e) 
			{
				/*
				 * DO NOT DELETE the line below. It is needed for adding ther row automatically
				 */
				if((e.getFirstRow()+1) == table.getRowCount() && e.getType() == TableModelEvent.UPDATE && table.getValueAt(e.getFirstRow(),e.getColumn()) != null)
				{
					model.addRow(new Vector());
				}
			}
			
		});
	}
	
	public boolean testValue(String group, String keyword, String value)
	{
		return GroupRW.testValue(group, keyword, value);
	}
	
	class ButtonListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == doneBtn)
			{
				//Perform Actions related to done button
				//Try to stop the editing in the grid.
				if(table.isEditing())
					table.getCellEditor(table.getEditingRow(), table.getEditingColumn()).stopCellEditing();
				//See if the grid is still editing. if it is then there is a error in the grid
				if(table.isEditing())
				{
					//Send a message saying that the grid is still editing so cannot commit
					return;
				}
				//Get the information from the table to the inputfile format
				GroupRW.Write();
				//Get the list of groups to be written
				//
				thisDialog.dispose();
			}
			if(e.getSource() == clearBtn)
			{
				//Perform Actions related to clear button
				SetNewModalForTable();
				table.setModel(model);
			}
			if(e.getSource() == exitBtn)
			{
				//Perform Actions related to exit button
				thisDialog.dispose();
			}
		}
	}
	
	class GroupFormat
	{
		public String CurrentGroup = "";
		public String Keyword = "";
		public String Value = "";
		private ArrayList<Integer> ColArrayList = new ArrayList<Integer>();
		private String RowSeperator = "";
		private String ColumnSeperator = "";
		private boolean isDynamic = false;
		
		public GroupFormat(Node GroupTag)
		{
			CurrentGroup = DefaultGroup;
			if(GroupTag == null)
			{
				//Error:No group information found
			}
			NamedNodeMap GroupAttributes = GroupTag.getAttributes();
			if(GroupAttributes.getNamedItem("GroupName")!=null)
			{
				CurrentGroup = GroupAttributes.getNamedItem("GroupName").getNodeValue();
			}
			if(GroupAttributes.getNamedItem("Keyword")==null)
			{
				//Error:No Keyword attribute found
			}
			Keyword = GroupAttributes.getNamedItem("Keyword").getNodeValue();
			if(GroupAttributes.getNamedItem("RowSeperator")==null)
			{
				//Error:No row Seperator found
			}
			RowSeperator = GroupAttributes.getNamedItem("RowSeperator").getNodeValue();
			if(GroupAttributes.getNamedItem("ColumnSeperator")==null)
			{
				//Error:No Column Seperator found
			}
			ColumnSeperator = GroupAttributes.getNamedItem("ColumnSeperator").getNodeValue();
			
			/*
			 * Set the column list value
			 */
			XPath ColumnList;
			ColumnList = XPathFactory.newInstance().newXPath();
			Node columnListValue = null;
			try {
				columnListValue = (Node)ColumnList.evaluate("ColumnList", GroupTag , XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			if(columnListValue==null)
			{
				//Error:No Keyword attribute found
			}
			columnListValue= columnListValue.getFirstChild();
			while(columnListValue != null && columnListValue.getNodeValue().trim().equals(""))
			{
				columnListValue = columnListValue.getNextSibling();
			}
			if(columnListValue == null)
			{
				//Error:No list found
			}
			SetColumnList(columnListValue.getNodeValue());
			
			//////////////////////////////////////////////////////////////////////
			//			Register the entity	with the Organized document		   	//
			//			This is used for the ContentAssist
				Dictionary.Register(CurrentGroup,Keyword,Dictionary.GRID_DIALOG);
				Dictionary.registerDialog(CurrentGroup + " " + Keyword, thisDialog);
			//																	//
			//////////////////////////////////////////////////////////////////////

		}
		
		private void SetColumnList(String _colList)
		{
			String[] ColNos = _colList.split(",");
			for(int i = 0 ; i < ColNos.length ; i++)
			{
				try
				{
					//Check if there is a range in the string
					if(ColNos[i].indexOf("..") != -1)
					{
						String[] ColRange = ColNos[i].split("\\.\\.");
						//the first string should be a number
						//get the starting range
						int StartRange = Integer.parseInt(ColRange[0]);
						int EndRange = 0;
						//Check if the second value starts with COL:
						//if it is then it is dynamic
						if(ColRange[1].startsWith("COL:"))
							isDynamic = true;
						//get the ending range
						//if it is dynamic then COL represents the Column from where we 
						//get the number of columns to search for the specific row.
						if(isDynamic)
							EndRange = Integer.parseInt(ColRange[1].substring("COL:".length()));
						else
							EndRange = Integer.parseInt(ColRange[1]);

						//Add the list of the column to the array
						for (int j = 0; j < (EndRange - StartRange + 1); j++) {
							ColArrayList.add(StartRange++);
						}
						continue;
					}
					ColArrayList.add(Integer.parseInt(ColNos[i]));
				}
				catch (NumberFormatException e) {
					throw new NumberFormatException("Improper number format at position " + i);
				}
			}
		}
		
		private ArrayList<Integer> GetColumnList()
		{
			return ColArrayList;
		}
		
		private ArrayList<Integer> GetDynamicColumnList(int row)
		{
			//check if the column list is dynamic 
			if(isDynamic)
			{
				//if the column list is dynamic then it comes here
				if(ColArrayList.size() == 0)
					return ColArrayList;
				//Start with the empty array list
				ArrayList<Integer> dynamicList = new ArrayList<Integer>();
				//Get the column no of the dynamic column
				int dynamicColNo = ColArrayList.get(ColArrayList.size() - 1);
				//Check if the column contains some value
				//if not return an emptylist
				if(table.getValueAt(row , dynamicColNo - 1) == null)
					return dynamicList;
				//The dynamic column contains the no of columns more on that row
				int noOfColumnsToAdd = Integer.parseInt(table.getValueAt(row , dynamicColNo - 1).toString());

				//HARDCODED value for IZMAT, IXZMAT, IRZMAT, IFZMAT
				if(Keyword.startsWith("I") && Keyword.contains("ZMAT"))
				{
					if(noOfColumnsToAdd == 1 || noOfColumnsToAdd == 2 || noOfColumnsToAdd == 3)
						noOfColumnsToAdd++;
					if(noOfColumnsToAdd == 5)
						noOfColumnsToAdd = 3;
					if(noOfColumnsToAdd == 6 || noOfColumnsToAdd == 7)
						noOfColumnsToAdd--;
				}
				
				//prepare the list of column numbers for that row
				for (int i = 0; i < noOfColumnsToAdd; i++) {
					dynamicList.add(++dynamicColNo);
				}
				return dynamicList;
			}
			//if the column list is not dynamic then it is going to be the same for all rows 
			//so return the static array list
			return ColArrayList;
		}
		
		public void Write()
		{
			ArrayList<Integer> ColList = null;
			StringBuilder ValueBuilder = new StringBuilder();
			for(int row = 0 ; row < table.getRowCount() ; row++)
			{
				boolean isValueSet = false;
				boolean isColumnValueChanged = false;
				ColList = GetColumnList();
				//Check if it is dynamic
				if(isDynamic)
				{
					//if it is then get the appropriate column list
					ColList = new ArrayList<Integer>(ColList);
					ColList.addAll(GetDynamicColumnList(row));
				}
				
				for(int colIndex = 0 ; colIndex < ColList.size() ; colIndex++)
				{
					int col = ColList.get(colIndex) - 1;
					if(table.getValueAt(row, col) != null && ! table.getValueAt(row, col).toString().trim().equals(""))
					{
						if(isColumnValueChanged)
							ValueBuilder.append(ColumnSeperator);
						ValueBuilder.append(table.getValueAt(row, col).toString());
						isColumnValueChanged = true;
						isValueSet = true;
					}
				}
				if(isValueSet)
				{
					ValueBuilder.append(RowSeperator);
				}
			}
			if(ValueBuilder.length() > 0)
			{
				Value = ValueBuilder.substring(0 , ValueBuilder.length() - RowSeperator.length());
			}
			//System.out.println("$" + CurrentGroup.toUpperCase() + " " + Keyword + "=" +  Value + " $END");
			UndoRedoHandler.toggleGroupClassifier();
			UndoRedoHandler.setLock();
			
			Repository.getInstance().Store(CurrentGroup, Keyword + "=" + Value);
			InputFileWriter.getInstance().Write(CurrentGroup, Keyword + "=" + Value);
			
			UndoRedoHandler.releaseLock();
		}
		
		public void Read()
		{
			//String matrixInput = "1,1,2 ,2,1,2,3 ,1,2,3";
			//String matrixInput = "1,1,2,2";
			String matrixInput = Repository.getInstance().Retrieve(CurrentGroup, Keyword);
			ReadInput(matrixInput);
		}
		
		public boolean testValue(String group, String keyword, String value)
		{
			if(!CurrentGroup.equalsIgnoreCase(group) || !Keyword.equalsIgnoreCase(keyword))
				return true;
			else
				return ReadInput(value);
		}
		
		private boolean ReadInput(String matrixInput)
		{
			if(matrixInput == null)
				return false;
			//split the complete list based on the seperator
			String[] matrixSplit = matrixInput.split(RowSeperator + "|" + ColumnSeperator);
			//Check if the split is empty or not
			if(matrixSplit.length == 0)
				return false;
			int splitLocation = 0;
			//Check if the table has sufficient rows
			if(table.getRowCount() == 0)
				model.addRow(new Vector());
			
			try
			{
				for (int row = 0; row < table.getRowCount() && splitLocation < matrixSplit.length; row++) 
				{
					//get the number of columns for that row.
					ArrayList<Integer> ColList = GetColumnList();
					for (int colIndex = 0; colIndex < ColList.size(); colIndex++) 
					{
						int col = ColList.get(colIndex) - 1;
						//if setValueOnGrid is true then set value to the table else test value
						//place the items on that row.
						table.setValueAt( getParsedValue( matrixSplit[splitLocation++].trim(), col) , row, col);
					}
					//see if the row is dynamic.
					if(isDynamic)
					{
						//if it is get dynamic column list again
						ArrayList<Integer> dynamicColList = GetDynamicColumnList(row);
						if(dynamicColList.size() != 0)
						{
							//start from the next column. and read the values
							for (int colIndex = 0; colIndex < dynamicColList.size(); colIndex++) 
							{
								int col = dynamicColList.get(colIndex) - 1;
								//if setValueOnGrid is true then set value to the table else test value
								//place the items on that row.
								table.setValueAt( getParsedValue(matrixSplit[splitLocation++].trim(), col) , row, col);
							}
						}
					}
					//if it is then check if there are rows for it. 
					//else add more rows
					if(row == (table.getRowCount() - 1))
						model.addRow(new Vector());
				}
			}
			catch(Exception ex)
			{
				//ex.printStackTrace();
				return false;
			}
			return true;
		}
		
		private Object getParsedValue(String value, int col) throws Exception
		{
			Class currentColumnClass = table.getColumnClass(col);
			
			if(currentColumnClass == Integer.class)
			{
				return Integer.parseInt(value);
			}
			else if(currentColumnClass == Boolean.class)
			{
				return Boolean.parseBoolean(value);
			}
			else if(currentColumnClass == Float.class)
			{
				return Float.parseFloat(value);
			}
			else if(currentColumnClass == Double.class)
			{
				return Double.parseDouble(value);
			}
			else if(currentColumnClass == String.class)
			{
				return value;
			}
			else
				return value;
		}
	}
	
	class GridGroupRW
	{
		GroupFormat[] GroupList = null;
		public GridGroupRW(NodeList GroupListTags) {
			if(GroupListTags == null)
			{
				//Error:No Group info tag found
				return;
			}
			GroupList = new GroupFormat[GroupListTags.getLength()];
			for(int i = 0 ; i < GroupListTags.getLength() ; i++)
			{
				GroupList[i] = new GroupFormat(GroupListTags.item(i));
			}
		}
		
		public void Write()
		{
			for(int i = 0 ; i < GroupList.length ; i++)
			{
				GroupList[i].Write();
			}
		}
		
		public void Read()
		{
			for(int i = 0 ; i < GroupList.length ; i++)
			{
				GroupList[i].Read();
			}
		}
		
		public boolean testValue(String group, String keyword, String value)
		{
			if(group == null || keyword == null || value == null)
				return false;
			for (int i = 0; i < GroupList.length; i++) 
			{
				if(GroupList[i].testValue(group, keyword, value) == false)
					return false;
			}
			return true;
		}
	}
	
	private class DialogOpenClose extends ComponentAdapter
	{
		@Override
		public void componentShown(ComponentEvent arg0) {
			SetNewModalForTable();
			table.setModel(model);
			GroupRW.Read();
		}
	}
}
