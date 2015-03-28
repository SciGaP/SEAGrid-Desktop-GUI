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
package Gamess.gamessGUI;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.Hashtable;
import java.util.Stack;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.MenuListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.*;

import Gamess.gamessGUI.Dialogs.MatrixDialog;
import Gamess.gamessGUI.Dialogs.MenuTableDialog;
import Gamess.gamessGUI.Dialogs.TextDialog;
import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;

public class MenuBuilder {

	private JMenuBar menuBar;
	private String menuFileLocation;
	private Node relativeNode;
	private Document doc = null;
	private Hashtable<Object,MenuEntity> MenuValueMapper = new Hashtable<Object,MenuEntity>();
	private MenuListener standardMenuListener = null;
	private ActionListener standardMenuItemListener = null;
	private Stack<MenuEntity> menuPath = new Stack<MenuEntity>() , menuPathImage = new Stack<MenuEntity>();
	private Frame parentFrame;
	private Hashtable<String, JDialog> CustomDialogs;
	private XPath toolTipXPath = XPathFactory.newInstance().newXPath();

	public MenuBuilder(JMenuBar _menuBar, Frame _parentFrame, String _menuFileLocation, Hashtable<String, JDialog> _CustomDialogs)
	{
		menuBar = _menuBar;
		menuFileLocation = _menuFileLocation;
		parentFrame = _parentFrame;
		CustomDialogs = _CustomDialogs;
	}

	public MenuBuilder(JMenuBar _menuBar, Frame _parentFrame, Node _relativeNode, Hashtable<String, JDialog> _CustomDialogs)
	{
		menuBar = _menuBar;
		relativeNode = _relativeNode;
		parentFrame = _parentFrame;
		CustomDialogs = _CustomDialogs;
	}
	
	public void BuildMenu()
	{
		/**
		 * The nodes that has to be parsed
		 */
		NodeList nodesToParse = null;
		
		/*
		 * if relativeNode is null the object has been called with filename
		 * In that case open file and get the root node
		 */
		if(relativeNode == null)
		{
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setIgnoringComments(true);
				dbFactory.setValidating(false);
				dbFactory.setIgnoringElementContentWhitespace(true);
				DocumentBuilder builder = dbFactory.newDocumentBuilder();
				doc = builder.parse(new File(menuFileLocation));
				Node root = doc.getFirstChild();

				//Check if this is the correct Document
				if(root.getNodeName().equals("gamessInput"))
				{
					nodesToParse = root.getChildNodes();
				}
				else
				{
//					Error:Throw exception malformed xml
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			nodesToParse = relativeNode.getChildNodes();
		}
		
		//Check if the standard menu Listener is Null 
		if(standardMenuItemListener == null)
		{
			standardMenuItemListener = new StandardMenuItemListener();
		}
		if(standardMenuListener == null)
		{
			standardMenuListener = new StandardMenuListener();
		}
		
		/*
		 * Pass each root node and build the menu
		 */
		for(int i = 0 ; i < nodesToParse.getLength() ; i++)
		{
			if(nodesToParse.item(i).getNodeType() != Node.TEXT_NODE)
				InsertMenu(nodesToParse.item(i) , null , "");
		}
	}
	
	/**
	 * Insert Menu
	 * @param currNode Current Node
	 * @param parentMenu
	 * @param parentGroup
	 */
	private void InsertMenu(Node currNode , JMenu parentMenu, String parentGroup)
	{
		NamedNodeMap CurrAttributes;
		Node LocalAttribute;
		String CurrentGroupName = parentGroup;
		
		//Get all the attributes of current node
		CurrAttributes = currNode.getAttributes();
		
		//if the current node has a group tag (Overriding)
		if((LocalAttribute = CurrAttributes.getNamedItem("Group")) != null)
		{
			CurrentGroupName = LocalAttribute.getNodeValue();
		}
		
		/*
		 * If the current tag is the new group tag 
		 * (all the items below it will be of this group unless overriden)  
		 */
		if(currNode.getNodeName().trim().equals("Group"))
		{
			Node LocalNode = null;
			if((LocalNode = CurrAttributes.getNamedItem("Name")) == null)
			{
				//Error:Throw name attribute not available
			}
			CurrentGroupName = LocalNode.getNodeValue();
			//Get all the node list under it and pass it with the new group
			NodeList ChildNodes = currNode.getChildNodes();
			for(int i = 0 ; i < ChildNodes.getLength() ; i++)
			{
				if(ChildNodes.item(i).getNodeType() != Node.TEXT_NODE)
					InsertMenu(ChildNodes.item(i), parentMenu, CurrentGroupName);
			}
			return;
		}
		
		
		/*
		 * Check if Current tag is either Menu or MenuItem or Seperator
		 * if neither then throw error
		 * The tags that occur in this recursion should be either menu or menuitem 
		 */
		if(currNode.getNodeName().equals("Menu"))
		{
			if(CurrentGroupName == null || CurrentGroupName.trim().length() == 0)
			{
				//Error:throw error malformed menu xml
			}
			
			//This node is of type menu and is a submenu to the parent menu
			//add a jmenu to the parentMenu
			JMenu CurrentMenu = new JMenu();
			if(parentMenu == null)
			{
				//add the new menu to the menubar
				menuBar.add(CurrentMenu);
			}
			else
			{
				parentMenu.add(CurrentMenu);
			}
			
			/*
			 * Set the properties of the new menu created
			 */
			
			//Basic properties
			CurrentMenu.setText(CurrAttributes.getNamedItem("DisplayName").getNodeValue());
			if(CurrAttributes.getNamedItem("ActionName") == null)
			{
				CurrentMenu.setActionCommand(CurrAttributes.getNamedItem("DisplayName").getNodeValue());
			}
			else
			{
				CurrentMenu.setActionCommand(CurrAttributes.getNamedItem("ActionName").getNodeValue());
			}
			if(CurrAttributes.getNamedItem("ToolTip") != null)
			{
				String toolTip = CurrAttributes.getNamedItem("ToolTip").getNodeValue();
				CurrentMenu.setToolTipText(getParsedToolTipForMenu(toolTip));
			}
			
			String MenuType = null;
			if(CurrAttributes.getNamedItem("MenuType") == null)
			{
				//Error:No MenuType attribute found in 
			}
			MenuType = CurrAttributes.getNamedItem("MenuType").getNodeValue(); 

			if(MenuType.equals("Dummy"))
				CurrentMenu.addMenuListener(new DummyMenuListener());
			
			//Additional properties
			if(MenuType.equals("Keyword") || MenuType.equals("Value") || MenuType.equals("Both") || MenuType.equals("Group"))
			{
				//Create the Entity Type
				MenuEntity Entity = new MenuEntity();
				Entity.Group = CurrentGroupName;
				Entity.MenuType = EntityType.GROUP;
				if(MenuType.equals("Keyword"))
				{
					Entity.Keyword =  CurrentMenu.getActionCommand();
					Entity.MenuType = EntityType.KEYWORD;
				}
				if(MenuType.equals("Value"))
				{
					Entity.Value =  CurrentMenu.getActionCommand();
					Entity.Keyword = CurrAttributes.getNamedItem("Keyword").getNodeValue();
					Entity.MenuType = EntityType.VALUE;
				}
				if(MenuType.equals("Both"))
				{
					String[] KW_V_item = CurrentMenu.getActionCommand().split("=");
					Entity.Keyword =  KW_V_item[0];
					Entity.Value =  KW_V_item[1];
					Entity.MenuType = EntityType.BOTH;
				}
				//Add the entity to the HashTable
				MenuValueMapper.put(CurrentMenu, Entity);
				
				//////////////////////////////////////////////////////////////////////
				//			Register the entity	with the Organized document		   	//
				//			This is used for the ContentAssist
					if(Entity.MenuType == EntityType.GROUP)
						Dictionary.Register(Entity.Group);
					else if(Entity.MenuType == EntityType.KEYWORD)
						Dictionary.Register(Entity.Group, Entity.Keyword);
					else
						Dictionary.Register(Entity.Group, Entity.Keyword, Entity.Value);
				//																	//
				//////////////////////////////////////////////////////////////////////
				
				//Set the listener to the menu
				CurrentMenu.addMenuListener(standardMenuListener);
				
			}
			if(MenuType.equals("GroupFrame") || MenuType.equals("Grid") || MenuType.equals("Custom") || MenuType.equals("Textbox"))
			{
				ItemLauncher LaunchListener = new ItemLauncher();
				LaunchListener.setGroup(CurrentGroupName);
				LaunchListener.setReferenceNode(currNode);
				if(MenuType.equals("GroupFrame"))
					LaunchListener.setLaunchType(LaunchType.GROUPFRAME);
				if(MenuType.equals("Grid"))
					LaunchListener.setLaunchType(LaunchType.GRID);
				if(MenuType.equals("Textbox"))
					LaunchListener.setLaunchType(LaunchType.TEXTBOX);
				if(MenuType.equals("Custom"))
				{
					LaunchListener.setLaunchType(LaunchType.CUSTOM);
					//Add the JDialog passed in
					LaunchListener.setCustomDialog(CustomDialogs.get(CurrentMenu.getActionCommand()), true);
				}
				CurrentMenu.addMouseListener(LaunchListener);
				
				//This is used for creating objects that are required for content assist
				LaunchListener.createObject();
				
				//These kind of menus are of Launch type so any menu having this will not be built further
				return;
			}
			//Call the same function recursively to all the child nodes of the current node
			NodeList ChildNodeList = currNode.getChildNodes();
			for(int i = 0; i < ChildNodeList.getLength() ; i++)
			{
				if(ChildNodeList.item(i).getNodeType() != Node.TEXT_NODE)
					InsertMenu(ChildNodeList.item(i), CurrentMenu, CurrentGroupName);
			}
			
			return;
		}
		else if(currNode.getNodeName().equals("MenuItem"))
		{
			if(parentMenu == null || CurrentGroupName == null || CurrentGroupName.trim().length() == 0)
			{
				//Error:throw error malformed menu xml
			}
			
			//create add a new menuitem
			JMenuItem CurrentMenuItem = new JMenuItem();
			parentMenu.add(CurrentMenuItem);
			
			/*
			 * Set the properties of the new menuitem created
			 */
			
			//Basic properties
			CurrentMenuItem.setText(CurrAttributes.getNamedItem("DisplayName").getNodeValue());
			if(CurrAttributes.getNamedItem("ActionName") == null)
			{
				CurrentMenuItem.setActionCommand(CurrAttributes.getNamedItem("DisplayName").getNodeValue());
			}
			else
			{
				CurrentMenuItem.setActionCommand(CurrAttributes.getNamedItem("ActionName").getNodeValue());
			}
			
			if(CurrAttributes.getNamedItem("ToolTip") != null)
			{
				String toolTip = CurrAttributes.getNamedItem("ToolTip").getNodeValue();
				CurrentMenuItem.setToolTipText(getParsedToolTipForMenu(toolTip));
			}
			
			String MenuType = null;
			if(CurrAttributes.getNamedItem("MenuType") == null)
			{
				//Error:No MenuType attribute found in 
			}
			MenuType = CurrAttributes.getNamedItem("MenuType").getNodeValue();
			
			//Additional Properties
			
			if(MenuType.equals("Keyword") || MenuType.equals("Value") || MenuType.equals("Both") || MenuType.equals("Group"))
			{
				//Create the Entity Type
				MenuEntity Entity = new MenuEntity();
				Entity.Group = CurrentGroupName;
				Entity.MenuType = EntityType.GROUP;
				if(MenuType.equals("Keyword"))
				{
					Entity.Keyword =  CurrentMenuItem.getActionCommand();
					Entity.MenuType = EntityType.KEYWORD;
				}
				if(MenuType.equals("Value"))
				{
					Entity.Value =  CurrentMenuItem.getActionCommand();
					Entity.Keyword = CurrAttributes.getNamedItem("Keyword").getNodeValue();
					Entity.MenuType = EntityType.VALUE;
				}
				if(MenuType.equals("Both"))
				{
					String[] KW_V_item = CurrentMenuItem.getActionCommand().split("=");
					Entity.Keyword =  KW_V_item[0];
					Entity.Value =  (KW_V_item.length != 2)?"":KW_V_item[1];
					Entity.MenuType = EntityType.BOTH;
				}
				//Add the entity to the HashTable
				MenuValueMapper.put(CurrentMenuItem, Entity);
				
				//////////////////////////////////////////////////////////////////////
				//			Register the entity	with the Organized document		   	//
			   /**/	if(Entity.MenuType == EntityType.GROUP)
			   /**/		Dictionary.Register(Entity.Group);
			   /**/	else if(Entity.MenuType == EntityType.KEYWORD)
			   /**/		Dictionary.Register(Entity.Group, Entity.Keyword);
			   /**/	else
			   /**/		Dictionary.Register(Entity.Group, Entity.Keyword, Entity.Value);
				//																	//
				//////////////////////////////////////////////////////////////////////
				
				//Set the action listener to the menu
				CurrentMenuItem.addActionListener(standardMenuItemListener);
			}
			if(MenuType.equals("GroupFrame") || MenuType.equals("Grid") || MenuType.equals("Custom") || MenuType.equals("Textbox"))
			{
				ItemLauncher LaunchListener = new ItemLauncher();
				LaunchListener.setGroup(CurrentGroupName);
				LaunchListener.setReferenceNode(currNode);
				if(MenuType.equals("GroupFrame"))
					LaunchListener.setLaunchType(LaunchType.GROUPFRAME);
				if(MenuType.equals("Grid"))
					LaunchListener.setLaunchType(LaunchType.GRID);
				if(MenuType.equals("Textbox"))
					LaunchListener.setLaunchType(LaunchType.TEXTBOX);
				if(MenuType.equals("Custom"))
				{
					LaunchListener.setLaunchType(LaunchType.CUSTOM);
					//Add the JDialog passed in
					LaunchListener.setCustomDialog(CustomDialogs.get(CurrentMenuItem.getActionCommand()), true);
				}
				CurrentMenuItem.addActionListener(LaunchListener);

				//This is used for creating objects that are required for content assist
				LaunchListener.createObject();
				
				//These kind of menus are of Launch type so any menu having this will not be built further
				return;
			}
			
			return;
		}
		else if(currNode.getNodeName().equals("Seperator"))
		{
			if(parentMenu == null)
			{
				//Error:throw error malformed menu xml
			}
			parentMenu.addSeparator();
			
			return;
		}
		else if(currNode.getNodeName().equals("CallLink"))
		{
			//Call the linking node to continue building
			if( CurrAttributes.getNamedItem("id") == null)
			{
				//Error:Throw Error malformed xml
			}
			//Get the link id to get the linking document
			String LinkID = CurrAttributes.getNamedItem("id").getNodeValue();
			
			//Get the linking node to continue
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node Linker = null;
			try {
				Linker = (Node)xpath.evaluate("/gamessInput/Link[@id='" + LinkID + "']", doc , XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			if( Linker == null)
			{
				//Error:Throw Linking tag not found
			}
			NodeList linkerNodeList = Linker.getChildNodes();
			for(int i = 0 ; i < linkerNodeList.getLength() ; i++)
			{
				if(linkerNodeList.item(i).getNodeType() != Node.TEXT_NODE)
					InsertMenu(linkerNodeList.item(i), parentMenu, CurrentGroupName);
			}
			
			//check if there are some items removed
			if(currNode.hasChildNodes())
			{
				//Then there are some items to disable
				NodeList localNodeList = currNode.getChildNodes();
				for(int i = 0 ; i < localNodeList.getLength() ; i++)
				{
					if(localNodeList.item(i).getNodeType() == Node.TEXT_NODE)
						continue;
					if(localNodeList.item(i).getNodeName().equals("Disable"))
					{
						NodeList disableMenuItems = localNodeList.item(i).getChildNodes();
						for(int j = 0 ; j < disableMenuItems.getLength() ; j++)
						{
							if(disableMenuItems.item(j).getNodeType() == Node.TEXT_NODE)
								continue;
							if(disableMenuItems.item(j).getNodeName().equals("item"))
							{
								NamedNodeMap localAttribute = disableMenuItems.item(j).getAttributes();
								if(localAttribute.getNamedItem("Name") != null)
								{
									String DisablePath = localAttribute.getNamedItem("Name").getNodeValue();
									String[] itemLocation = DisablePath.split("\\.");
									JMenu CurrentSearchMenu = parentMenu;
									for(int k = 0 ; k < itemLocation.length ; k++)
									{
										for( int l = 0 ; l < CurrentSearchMenu.getItemCount() ; l++)
										{
											Component TestComponent = CurrentSearchMenu.getMenuComponent(l);
											//Check if the item is either a menu or menuitem
											if(TestComponent.getClass().getName().endsWith("JMenu"))
											{
												JMenu TestItem = (JMenu)TestComponent;
												if( TestItem.getText().equals(itemLocation[k]))
												{
													CurrentSearchMenu = TestItem;
													if((k+1) == itemLocation.length)
													{
														TestItem.setEnabled(false);
														MenuValueMapper.get(TestItem).isExcluded = true;
														break;
													}
												}
												
											}
											else if(TestComponent.getClass().getName().endsWith("JMenuItem"))
											{
												JMenuItem TestItem = (JMenuItem)TestComponent;
												if(TestItem.getText().equals(itemLocation[k]))
												{
													TestItem.setEnabled(false);
													MenuValueMapper.get(TestItem).isExcluded = true;
													break;
												}
											}
										}
									}
								}
								else
								{
//									Error:Throw malformed xml
								}
							}
							else 
							{
								//Error:Throw malformed xml
							}
						}
					}
					else
					{
						//Error:Throw malformed xml
					}
				}
			}
			
			return;
		}
		else if(currNode.getNodeName().equals("Link"))
		{
			//Do Nothing
		}
		else
		{
			//Error::Throw error as malformed xml
		}
	}
	
	private String getParsedToolTipForMenu(String toolTip)
	{
		if(toolTip.startsWith("#$"))
		{
			try
			{
				String Group,Keyword,Value;
		        String[] splitData = toolTip.substring(2).split(" ");
		        
		        Group = (splitData.length > 0) ? "@Group='" + splitData[0] + "' and " : "not(@Group) and " ;
		        Keyword = (splitData.length > 1) ? "@Keyword='" + splitData[1] + "' and " : "not(@Keyword) and ";
		        Value = (splitData.length > 2) ? "@Value='" + splitData[2] + "'" : "not(@Value)";
				
		        String xpathCondExpr = "/root/ToolTips/ToolTip[" + Group + Keyword + Value  + "]";

				Node tooltipNode = (Node)toolTipXPath.evaluate( xpathCondExpr, GlobalParameters.userNotesAndToolTip, XPathConstants.NODE);
				if(tooltipNode != null)
				{
					return Cosmetics.getMenuToolTip(tooltipNode.getTextContent().trim());
				}
			}
			catch (XPathExpressionException e) {e.printStackTrace();}
		}
		return Cosmetics.getMenuToolTip(toolTip);
	}
	
	/**
	 * Write all the classes that are used in this object
	 */
	public enum EntityType	{ GROUP,KEYWORD,VALUE,BOTH };
	public enum LaunchType  { GRID,GROUPFRAME,TEXTBOX,CUSTOM};
	private class MenuEntity
	{
		public String Group = null, Keyword = null, Value = null;
		EntityType MenuType; 
		boolean isExcluded = false;
	}
	
	private class DummyMenuListener implements MenuListener
	{
		public void menuCanceled(javax.swing.event.MenuEvent evt) {
        }
        public void menuDeselected(javax.swing.event.MenuEvent evt) {
        }
		public void menuSelected(javax.swing.event.MenuEvent evt) {
        	//Test enabling or disabling the submenus here
        	JMenu parentMenu = (JMenu)evt.getSource();
        	ExcludeIncompatibility excludes = ExcludeIncompatibility.getInstance();
        	for (int i = 0; i < parentMenu.getItemCount(); i++) 
        	{
				JMenuItem childMenuItem = parentMenu.getItem(i);
				if(childMenuItem == null) continue;
				MenuEntity childEntity = MenuValueMapper.get(childMenuItem);
				if(childEntity != null && childEntity.isExcluded) continue;
				
				//check if this is excluded
				if(childEntity != null && (excludes.isLikelyToBecomeIncompatible(childEntity.Group) || excludes.isLikelyToBecomeIncompatible(childEntity.Group + " " + childEntity.Keyword ) ||  excludes.isLikelyToBecomeIncompatible(childEntity.Group + " " + childEntity.Keyword + " " + childEntity.Value)))
				{
					childMenuItem.setEnabled(false);
				}
				else
					childMenuItem.setEnabled(true);
				
				/*/check if this is required
				if(childEntity != null && RequiresIncompatibility.getInstance().testIncompatibility(childEntity.Group + " " + childEntity.Keyword + " " + childEntity.Value))
				{
					//
					//childMenuItem.setFont(childMenuItem.getFont().deriveFont(Font.BOLD));
				}
				else
					childMenuItem.setFont(childMenuItem.getFont().deriveFont(Font.PLAIN));
				*/
			}
        }
	}
	
	private class StandardMenuListener implements MenuListener
	{
		public void menuCanceled(javax.swing.event.MenuEvent evt) {
        }
        public void menuDeselected(javax.swing.event.MenuEvent evt) {
        	menuPath.pop();
        }
        @SuppressWarnings("unchecked")
		public void menuSelected(javax.swing.event.MenuEvent evt) {
        	MenuEntity CurrentSelectedMenu = MenuValueMapper.get(evt.getSource());
        	
        	//Test enabling or disabling the submenus here
        	JMenu parentMenu = (JMenu)evt.getSource();
        	ExcludeIncompatibility excludes = ExcludeIncompatibility.getInstance();
        	for (int i = 0; i < parentMenu.getItemCount(); i++) 
        	{
				JMenuItem childMenuItem = parentMenu.getItem(i);
				if(childMenuItem == null) continue;
				MenuEntity childEntity = MenuValueMapper.get(childMenuItem);
				if(childEntity != null && childEntity.isExcluded) continue;
				
				//check if this is excluded
				if(childEntity != null && (excludes.isLikelyToBecomeIncompatible(childEntity.Group) || excludes.isLikelyToBecomeIncompatible(childEntity.Group + " " + childEntity.Keyword ) ||  excludes.isLikelyToBecomeIncompatible(childEntity.Group + " " + childEntity.Keyword + " " + childEntity.Value)))
				{
					childMenuItem.setEnabled(false);
				}
				else
					childMenuItem.setEnabled(true);
				
				/*/check if this is required
				if(childEntity != null && RequiresIncompatibility.getInstance().testIncompatibility(childEntity.Group + " " + childEntity.Keyword + " " + childEntity.Value))
				{
					//
					//childMenuItem.setFont(childMenuItem.getFont().deriveFont(Font.BOLD));
				}
				else
					childMenuItem.setFont(childMenuItem.getFont().deriveFont(Font.PLAIN));
				*/
			}
        	menuPath.push(CurrentSelectedMenu);
			menuPathImage = (Stack<MenuEntity>)menuPath.clone();;
        }
	}

	private class StandardMenuItemListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			MenuEntity CurrentSelectedMenu = MenuValueMapper.get(evt.getSource());
        	menuPathImage.push(CurrentSelectedMenu);
        	//
        	//Set the parent lock and classify all the items added now under the same group. 
        	UndoRedoHandler.toggleGroupClassifier();
			UndoRedoHandler.setLock();
        	//from starting to the ending of the menupath
        	for(int i = 0 , j  ; i < menuPathImage.size() ; i++)
        	{
        		//Get each item
        		MenuEntity MenuItemInPath = menuPathImage.elementAt(i);
        		//if the menu is of type keyword or both then it to have a matching value 
        		if(MenuItemInPath.MenuType == EntityType.KEYWORD || MenuItemInPath.MenuType == EntityType.BOTH)
        		{
        			//Find the menu entity containing values for the corresponding keywords 
        			for(j = i ; j < menuPathImage.size() ; j++)
        			{
        				//Check for the value type and both type
        				MenuEntity MenuToCompare = menuPathImage.elementAt(j); 
        				if((MenuToCompare.MenuType == EntityType.VALUE || MenuToCompare.MenuType == EntityType.BOTH) && MenuToCompare.Keyword.equals(MenuItemInPath.Keyword))
        				{
        					InputFileWriter KeywordValueWriter = InputFileWriter.getInstance();
        					String seperator = Repository.getInstance().getKeywordValueSeperator(MenuToCompare.Group);
        					KeywordValueWriter.Write(MenuToCompare.Group, MenuToCompare.Keyword + seperator + MenuToCompare.Value);
        					Repository.getInstance().Store(MenuToCompare.Group, MenuToCompare.Keyword + seperator + MenuToCompare.Value);
        					break;
        				}
        			}
        			if(j == menuPathImage.size())
        			{
        				//Error:No matching value found for the keyword MenuItemInPath.Keyword
        			}
        		}
        	}
        	
			UndoRedoHandler.releaseLock();
        }
	}

	private class ItemLauncher extends MouseAdapter implements ActionListener
	{
		private LaunchType LauncherType = null;
		private Node referenceNode  = null;
		private JDialog LaunchingItem = null;
		private String Group = null;
		
		public ItemLauncher(){}
		
		public ItemLauncher(LaunchType LaunchTyp , Node refNode)
		{
			LauncherType = LaunchTyp;
			referenceNode = refNode.cloneNode(true);
		}
		
		public void setGroup(String _Group)
		{
			Group = _Group;
		}
		
		public void setLaunchType(LaunchType launchType)
		{
			LauncherType = launchType;
		}
		
		public void setReferenceNode(Node refNode)
		{
			if(refNode.hasChildNodes())
			{
				Node childNode = refNode.getFirstChild();
				while(childNode != null && childNode.getNodeType() == Node.TEXT_NODE)
				{
					childNode = childNode.getNextSibling();
				}
				if(childNode == null){
					//Error:No child type listed
				}
				referenceNode = childNode.cloneNode(true); 
			}
		}
		
		public void setCustomDialog(JDialog dialog , boolean isModalDialog)
		{
			if(LauncherType != null && LauncherType == LaunchType.CUSTOM)
			{
				LaunchingItem = dialog;
				if(isModalDialog && dialog != null)
					LaunchingItem.setModal(true);
			}
		}
		
		/*
		 * This is mainly used to create the objects of the launching types
		 * Eager instantiation is used to as there is a code to support the
		 * ContentAssist in the objects created 
		 */
		public void createObject()
		{
			if(LauncherType != LaunchType.CUSTOM)
				LoadDialog();
		}
		
		public void mouseClicked(java.awt.event.MouseEvent evt) 
		{
			LaunchItem();
        }
		
		public void actionPerformed(java.awt.event.ActionEvent evt) 
		{
			LaunchItem();
		}
		
		private void LaunchItem()
		{
			if(LaunchingItem == null)
			{
				//Load the Dialogue based on the launcher type and node information
				LoadDialog();
			}
			LaunchingItem.setVisible(true);
		}
		
		private void LoadDialog()
		{
			if(LauncherType == null)
			{
				//Error:No launching type specified
			}
			if(LauncherType != LaunchType.CUSTOM && referenceNode == null)
			{
				//Error:No information found for launching the item
			}
			
			//Create the jdialog object object here
			if(LauncherType == LaunchType.GRID)
			{
				LaunchingItem = new MatrixDialog(parentFrame , referenceNode , Group);
			}
			else if(LauncherType == LaunchType.GROUPFRAME)
			{
				NamedNodeMap GroupframeAttributes = referenceNode.getAttributes();
				LaunchingItem = new MenuTableDialog(Group,GroupframeAttributes.getNamedItem("DisplayName").getNodeValue(),referenceNode,parentFrame);
			}
			else if(LauncherType == LaunchType.TEXTBOX)
			{
				LaunchingItem = new TextDialog(parentFrame , Group , referenceNode);
			}
			else
			{
				//Error:Trying to launch a custom dialog which is not available
			}
			
			LaunchingItem.setModal(true);
			LaunchingItem.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}
	}
	

}
