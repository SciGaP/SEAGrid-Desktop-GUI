package Gamess.gamessGUI.Dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Gamess.gamessGUI.GlobalParameters;
import Gamess.gamessGUI.UndoRedoHandler;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;

public class MenuTableDialog extends JDialog
{
	private static final long serialVersionUID = -4769273198912927330L;
	ArrayList<IMenuTableCellTester> cellListenerList = new ArrayList<IMenuTableCellTester>();
	ArrayList<TableCellRenderer> cellRendererList = new ArrayList<TableCellRenderer>();
	ArrayList<TableCellEditor> cellEditorList = new ArrayList<TableCellEditor>();
	JDialog thisDialog = null;
	public MenuTable table = null;
	JButton doneButton = null, exitButton = null;
	
	public MenuTableDialog(String Group, String title, Node currentNode, Frame parentFrame) 
	{
		super(parentFrame, true);
		
		thisDialog = this;
		
		super.setTitle(title);
		
		//Set the layout
		this.setLayout(new BorderLayout());
		
		//Check if the current node is a groupframe node
		if(!currentNode.getNodeName().equalsIgnoreCase("GroupFrame"))
			return;
		
		NamedNodeMap currentAttributes = currentNode.getAttributes();

		//Set the title
		if(currentAttributes.getNamedItem("DisplayName") != null)
			setTitle(currentAttributes.getNamedItem("DisplayName").getNodeValue());
		
		//Group
		if(currentAttributes.getNamedItem("Group") != null)
			Group = currentAttributes.getNamedItem("Group").getNodeValue();
		
		//Create the table model for the table
		DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Keyword" , "Value"}, 0);
		table = new MenuTable(tableModel);
		
		//Keywords
		//Get all the keywordlist and create the menu table
		NodeList keywords = currentNode.getChildNodes();
		for (int i = 0; i < keywords.getLength() ; i++) {
			Node keyword = keywords.item(i);
			if(keyword == null || keyword.getNodeType() == Node.TEXT_NODE)
				continue;
			
			currentAttributes = keyword.getAttributes();
			String keywordText = null, valueType = null;
			
			if(currentAttributes.getNamedItem("DisplayName") != null)
				keywordText = currentAttributes.getNamedItem("DisplayName").getNodeValue();
			
			if(currentAttributes.getNamedItem("MenuType") != null)
				valueType = currentAttributes.getNamedItem("MenuType").getNodeValue();
			
			if(keywordText == null || valueType == null)
				continue;
			
			tableModel.addRow(new Object[]{keywordText , null});
			
			//add the value item here
			if(valueType.equalsIgnoreCase("COMBO"))
			{
				//add a combobox
				MenuTableComboBox menuCombo = new MenuTableComboBox(Group , keyword , cellListenerList);
				cellListenerList.add(menuCombo);
				cellRendererList.add(menuCombo);
				cellEditorList.add(menuCombo);
			}
			else if(valueType.equalsIgnoreCase("TEXTBOX"))
			{
				//add a textbox
				MenuTableTextBox menuText = new MenuTableTextBox(Group , keyword , cellListenerList, thisDialog);
				cellListenerList.add(menuText);
				cellRendererList.add(menuText);
				cellEditorList.add(menuText);
			}
		}
		
		//add buttons
		JPanel buttonPanel = new JPanel();
		doneButton = new JButton("Done");
		exitButton = new JButton("Exit");
		
		doneButton.setPreferredSize(new Dimension(75,25));
		exitButton.setPreferredSize(new Dimension(75,25));
		
		ActionListener buttonListener = new ButtonListener();
		
		doneButton.addActionListener(buttonListener);
		exitButton.addActionListener(buttonListener);
		
		buttonPanel.add(doneButton);
		buttonPanel.add(exitButton);
		
		//add listeners
		addComponentListener(new DialogOpenClose());
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				//Switch to normal mode when closing
				GlobalParameters.switchToNormalMode();
			}
		});
		
		//add table properties
		table.setRowHeight(25);
		
		//add all the items to the dialogs contentPane
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		//Set size
		int titleBarHeight = 37;
		int tableHeaderHeight = table.getTableHeader().getPreferredSize().height;
		int tableRowsHeight = table.getRowHeight() * (table.getRowCount());
		int buttonPanelHeight = buttonPanel.getPreferredSize().height;
		
		int Height = titleBarHeight + tableHeaderHeight + tableRowsHeight + buttonPanelHeight;
		Height = (Height > 450)?450:Height;
		
		int Width = 160 + 160;
		super.setSize(new Dimension(Width, Height));
		setLocationRelativeTo(parentFrame);
	}
	
	public class MenuTable extends JTable
	{
		private static final long serialVersionUID = 4279227050469207134L;

		public MenuTable(TableModel tableModel) {
			super(tableModel);
		}
		
		@Override
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(convertColumnIndexToModel(column) == 1)// && cellRendererList.get(row) instanceof MenuTableComboBox)
			{
				//return TableCellRenderer.class.cast(cellListenerList.get(row));
				return cellRendererList.get(row);
			}
			return super.getCellRenderer(row, column);
		}
		
		@Override
		public TableCellEditor getCellEditor(int row, int column) {
			if(convertColumnIndexToModel(column) == 1)
			{
				//return TableCellEditor.class.cast(cellListenerList.get(row));
				return cellEditorList.get(row);
			}
			return super.getCellEditor(row, column);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			if(column == 1)
				return true;
			return false;
		}
	}
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt) 
		{
			if(evt.getSource().equals(doneButton))
			{
				//Try to stop the editing of the cells
				if(table.isEditing())
					cellEditorList.get(table.getEditingRow()).stopCellEditing();
				//Check if there is some editor still editing
				if(table.isEditing())
				{
					JOptionPane.showMessageDialog(thisDialog, "Table cannot stop editing because of some inconsistency in the editing cell.");
					((JComponent)cellEditorList.get(table.getEditingRow())).requestFocus();
					return;
				}
				for (int i = 0; i < cellListenerList.size(); i++) {
					IMenuTableCellTester cellValues = cellListenerList.get(i);
					if(!cellValues.isConsistent())
					{
						//some value is inconsistent.
						//Ask the user if he needs to proceed or not
						int response = JOptionPane.showConfirmDialog(thisDialog, "Value to the keyword \"" + cellValues.getKeyword() + "\" is inconsistent in the table.\nDo you wany to proceed anyway?", "Inconsistency", JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE);
						//if the user does not need to proceed
						if(response == JOptionPane.NO_OPTION)
						{
							table.editCellAt(i, 1);
							((JComponent)cellValues).requestFocus();
							return;
						}
					}
				}
				
				GlobalParameters.switchToNormalMode();
				
				UndoRedoHandler.toggleGroupClassifier();
				UndoRedoHandler.setLock();
				
				for (int i = 0; i < cellListenerList.size(); i++) {
					IMenuTableCellTester cellValues = cellListenerList.get(i);
					if(!cellValues.isDefault())
					{
						//The value is not default. So add to the repository and write it to the input file
						String seperator = Repository.getInstance().getKeywordValueSeperator(cellValues.getGroup());
						Repository.getInstance().Store(cellValues.getGroup(), cellValues.getKeyword() + seperator + cellValues.getValue());
						InputFileWriter.getInstance().Write(cellValues.getGroup(), cellValues.getKeyword() + seperator + cellValues.getValue());
					}
					//The value is default. Remove the value if it is available
					else if(Repository.getInstance().isAvailable(cellValues.getGroup(), cellValues.getKeyword()) )
					{
						Repository.getInstance().Remove(cellValues.getGroup(), cellValues.getKeyword());
						InputFileWriter.getInstance().Remove(cellValues.getGroup(), cellValues.getKeyword());
					}
				}
				
				UndoRedoHandler.releaseLock();
				
				thisDialog.dispose();
			}
			else if(evt.getSource().equals(exitButton))
			{
				//drop the provisional instance
				Repository.getInstance().DropDB();
				//switch to normal mode
				GlobalParameters.switchToNormalMode();
				//close this
				thisDialog.dispose();
			}
		}
	}
	
	private class DialogOpenClose extends ComponentAdapter
	{
		@Override
		public void componentShown(ComponentEvent arg0) {
			//This is called when Dialog is opened
			//Reset all the values
			super.componentShown(arg0);
			ArrayList<String> dataToPushProvisionalDB = new ArrayList<String>();
			//Load the data from the main DB
			for (int i = 0; i < cellListenerList.size(); i++) {
				IMenuTableCellTester currentCellListenerList = cellListenerList.get(i);
				
				currentCellListenerList.resetValues();
				
				//if the cell value is not default add the list to the provisional DB
				if(!currentCellListenerList.isDefault())
					dataToPushProvisionalDB.add(currentCellListenerList.getGroup() + " " + currentCellListenerList.getKeyword() + Repository.getInstance().getKeywordValueSeperator(currentCellListenerList.getGroup()) + currentCellListenerList.getValue());
				
				currentCellListenerList.recomputeConsistency();
			}
			//Switch to provisional mode
			GlobalParameters.switchToProvisionalMode();
			Repository.getInstance().DropDB();
			for (int i = 0; i < dataToPushProvisionalDB.size(); i++) 
			{
				String FullData = dataToPushProvisionalDB.get(i);
				int splitIndex = FullData.indexOf(" ");
				Repository.getInstance().Store(FullData.substring(0, splitIndex), FullData.substring(splitIndex + 1));
			}
			
			//Repaint this dialog for updating it
			thisDialog.repaint();
		}
		
		@Override
		public void componentHidden(ComponentEvent arg0) {
			//This is called when the Dialog is closed
			super.componentHidden(arg0);
			//Switch to normal mode when closing
			GlobalParameters.switchToNormalMode();
		}
	}
}
