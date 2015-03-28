package Gamess.gamessGUI;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Gamess.gamessGUI.Storage.Repository;

public class ContentAssist{

	private static ContentAssist instance = null;
	public static void CreateInstance()
	{
		if(instance == null)
		{
			instance = new ContentAssist();
		}
	}
	
	private JTextPane inputPane = GamessGUI.inputFilePane;
	private StyledDocument inputStyles = GamessGUI.inputFilePane.getStyledDocument();
	private JScrollPane listScrollPane = null;
	private JList list = null;
	private DefaultListModel model = null;
	
	/**
	 * This is holds the complete list of the items currently displaying 
	 */
	private ArrayList<String> currentDisplayingList = new ArrayList<String>();
	/**
	 * This is holds the complete list of the items currently displaying 
	 */
	private ArrayList<String> currentFilteredDisplayingList = new ArrayList<String>();
	/**
	 * This is holds the complete list of the tooltips for <b>all</b> the items currently displaying
	 */
	private ArrayList<String> currentToolTipList = new ArrayList<String>();
	/**
	 * This is holds the complete list of the tooltips for the items currently displayed
	 */
	private ArrayList<String> currentDisplayToolTipList = new ArrayList<String>();
	private inputPaneListeners inputListeners = new inputPaneListeners();
	private listKeyAdapter listKeyListeners = new listKeyAdapter();
	private listMouseAdapter listMouseListeners = new listMouseAdapter();
	private XPath tooltipXpath = XPathFactory.newInstance().newXPath();
	boolean isControlKeyPressed = false;
	int currentDisplayedOffset = 0;
	String context = "";
	
	private ContentAssist()
	{
		//Create the model and add the items to it
		model = new DefaultListModel();
        for(int i=0;i < currentDisplayingList.size();i++)
        {
            model.addElement(currentDisplayingList.get(i));
        }
        //Add the model to the JList
		list = new JList(model);
		list.setCellRenderer(new AssistRenderer());
		list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(listMouseListeners);
		list.addKeyListener(listKeyListeners);
		list.setForeground(new java.awt.Color(0, 102, 0));
		
		//Create the scroll pane over the list displayed
		listScrollPane = new JScrollPane(list);
		listScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		listScrollPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 3, true));
		listScrollPane.setVisible(false);
		listScrollPane.setSize(92, 100);
		
		//add the scrollpabe to the inputFilePane
		GamessGUI.inputFilePane.add(listScrollPane);
		GamessGUI.inputFilePane.addKeyListener(inputListeners);
		GamessGUI.inputFilePane.addMouseListener(new inputPaneAdapter());

	}
	
	
	//This is used to show the assist in a new context or if it is not displayed
	private void fire(boolean isForced) 
	{             
		if(getParagraphLine(getCaretPostion()).trim().length() > 0 && inputStyles.getParagraphElement(getCaretPostion()).getAttributes().containsAttributes(Cosmetics.COMMENTS_ATTRIBUTE))
			return;
		setCurrentDisplayOffset();
		loadDisplayItems(isForced);
		//Check if the event is consumed
		if(inputListeners.currentEvent != null && inputListeners.currentEvent.isConsumed())
			return;
		//if everything is proper show the intellisance
		loadToolTipForDisplayItem();
		filter();
		showDropDownAtLocation();
    }                                        

	private void filter()
    {
		String filterString = getCurrentPassedText();
        model.removeAllElements();
        currentFilteredDisplayingList = new ArrayList<String>();
        currentDisplayToolTipList = new ArrayList<String>();
        for (int i = 0; i < currentDisplayingList.size(); i++) {
            if(currentDisplayingList.get(i).toUpperCase().startsWith(filterString.toUpperCase()) && currentDisplayingList.get(i).length() > filterString.length())
            {
            	currentFilteredDisplayingList.add(currentDisplayingList.get(i));
                currentDisplayToolTipList.add(currentToolTipList.get(i));
            }
        }
        for (int i = 0; i < currentFilteredDisplayingList.size(); i++) {
            model.addElement(currentFilteredDisplayingList.get(i));
        }
        if(currentFilteredDisplayingList.size() > 0)
            list.setSelectedIndex(0);
    }
	
	private void showDropDownAtLocation()
	{
		try
        {
			//Set the location of the dropdown to be showed
            Rectangle cursor = inputPane.modelToView(currentDisplayedOffset);
            //
            setDownVisibleLocations(cursor);
            if((cursor.y + cursor.height + listScrollPane.getHeight()) > inputPane.getHeight())
            {
                //Setting visible down is not possible
                //Try setting visible upside
                if((cursor.y - listScrollPane.getHeight()) < 0)
                {
                    //Setting visible upside is not possible
                    //Show it downwards
                    setDownVisibleLocations(cursor);
                }
                else
                {
                    //You can show the list upside
                    //Show the list upside
                    setUpVisibleLocations(cursor);
                }
            }
            
            //Show the dropdown
            listScrollPane.setVisible(true);
        }
        catch (BadLocationException e){ e.printStackTrace(); }
	}
	
	private void setUpVisibleLocations(Rectangle rect)
    {
        listScrollPane.setLocation(rect.x, rect.y - listScrollPane.getHeight());
    }

    private void setDownVisibleLocations(Rectangle rect)
    {
    	listScrollPane.setLocation(rect.x, rect.y + rect.height);
    }
	
    private int getCaretPostion()
    {
    	return inputPane.getCaretPosition();
    }
	
    private void setCurrentDisplayOffset()
    {
		int caretPostition = getCaretPostion();
        
        int paragraphStartingIndex = inputStyles.getParagraphElement(caretPostition).getStartOffset();
        
        currentDisplayedOffset = paragraphStartingIndex;
        
        String currentLine = getParagraphLine(caretPostition).substring(0, caretPostition - paragraphStartingIndex);

        //Get the last seperator
        //Check for =
        int equalIndex = paragraphStartingIndex + currentLine.lastIndexOf("=") + 1 ;
        //if = later than currentDisplayedOffset update it
        if(equalIndex > currentDisplayedOffset)
        	currentDisplayedOffset = equalIndex;
        
        //Check for ' '
        int spaceIndex = paragraphStartingIndex + currentLine.lastIndexOf(" ") + 1;
        
        //if ' ' later than currentDisplayedOffset update it
        if(spaceIndex > currentDisplayedOffset)
        	currentDisplayedOffset = spaceIndex;
    }
    
    private String getCurrentPassedText()
    {
    	try
    	{
	    	setCurrentDisplayOffset();
	    	return inputStyles.getText(currentDisplayedOffset, getCaretPostion() - currentDisplayedOffset);
    	}
    	catch(BadLocationException e){ e.printStackTrace(); }
    	return "";
    }
    
    
    private void loadDisplayItems(boolean isForced)
    {
    	//Get the contextinfo and load the details as needed
    	context = getContextInfo(currentDisplayedOffset);
    	//if context has nothing load groups
    	if(context.length() == 0)
    	{
    		loadGroups();
    		return;
    	}

    	context = context.toUpperCase();
    	String[] splitString = context.split(" ");

    	if(isForced)
    	{
    		Hashtable<String, JDialog> dialogs = Dictionary.dialogs;
    		if(splitString.length == 1 && dialogs.containsKey( Dictionary.getFormattedKeyword(splitString[0].substring(1))) )
    		{
    			if(inputListeners.currentEvent != null) inputListeners.currentEvent.consume();
    			dialogs.get( Dictionary.getFormattedKeyword(splitString[0].substring(1)) ).setVisible(true);
    			return;
    		}
    		else if(splitString.length == 2 && dialogs.containsKey( Dictionary.getFormattedKeyword(splitString[0].substring(1) + " " + splitString[1])) )
    		{
    			if(inputListeners.currentEvent != null) inputListeners.currentEvent.consume();
    			dialogs.get( Dictionary.getFormattedKeyword(splitString[0].substring(1) + " " + splitString[1]) ).setVisible(true);
    			return;
    		}
    	}

    	//if there is no group ending then include $END
    	//if context has group load keywords
    	if(splitString.length == 1)
    		loadKeywords(splitString[0].substring(1));
    	//if context has group and keywords load values
    	else loadValues(splitString[0].substring(1), splitString[1]);
    }
    
    private String getContextInfo(int location)
    {
    	//Search back for the $ sign
    	//if there is no $ word or the word is $END then return ""
    	String stringBefore = null;
		try {
			stringBefore = inputStyles.getText(0, location);
		} catch (BadLocationException e) {e.printStackTrace();}
		
    	int groupStartingIndex = stringBefore.lastIndexOf("$");
    	//if there is no $ symbol then send "" as group should be displayed
    	if(groupStartingIndex == -1)
    		return "";
    	
    	String Group = getWordAfter(groupStartingIndex);
    	//if the group is $END then send "" as group should be displayed
    	if(Group.equalsIgnoreCase("$END"))
    		return "";
    	
    	//if the word is other than $END load the group and keywords
    	//if the word just before this is = load values
    	int paraStartOffset = inputStyles.getParagraphElement(location).getStartOffset();
    	String wordLine = getParagraphLine(location);
    	int lineOffset = location - paraStartOffset;
    	wordLine = wordLine.substring(0, lineOffset);
    	
    	if(wordLine.trim().endsWith("="))
    	{
    		//to load the values
    		//Get the keyword
    		int equalIndex = wordLine.lastIndexOf("=");
    		wordLine = wordLine.substring(0, equalIndex).trim();
        	int keywordStarting = wordLine.lastIndexOf(" ");
        	keywordStarting++;
    		//return the group and the keyword
    		return Group + " " + wordLine.substring(keywordStarting);
    	}
    	else
    	{
    		//to load the keywords
    		//return the group
    		return Group;
    	}
    }
    
    
    private void loadGroups()
    {
    	Repository db = Repository.getInstance();
    	currentDisplayingList = new ArrayList<String>();
    	//Get all the groups
    	Element groupRoot = Dictionary.get();
    	if(groupRoot == null) return;
    	
    	NodeList groups = groupRoot.getChildNodes();
    	//Load the groups
    	for (int i = 0; i < groups.getLength(); i++) {
    		//Remove the group that is already available here
    		String GroupName = groups.item(i).getNodeName();
    		//Add if the group is not available
    		if( ! db.isAvailable(GroupName))
    			currentDisplayingList.add("$" + groups.item(i).getNodeName());
		}
    	Collections.sort(currentDisplayingList);
    }
    
    
    private void loadKeywords(String group)
    {
    	Repository db = Repository.getInstance();
    	currentDisplayingList = new ArrayList<String>();
    	//Check if the group has an $END else add it
    	String stringAfter = getText(getCaretPostion(), inputStyles.getLength());
    	int groupStartPosition = stringAfter.indexOf("$");
    	if(groupStartPosition == -1)
    		currentDisplayingList.add("$END");
    	else
		{
    		groupStartPosition +=  getCaretPostion();
    		//if the group at groupStartPosition is not $END then add it 
			if(!getWordAfter(groupStartPosition).equalsIgnoreCase("$END"))
					currentDisplayingList.add("$END");
		}
    	
    	//Get all the keywords for the groups
    	Element groupRoot = Dictionary.get(group);
    	if(groupRoot == null) return;
    	
    	NodeList groups = groupRoot.getChildNodes();
    	//Load the groups
    	for (int i = 0; i < groups.getLength(); i++) {
    		String loadingKeyword = groups.item(i).getNodeName();
			//Remove the group that is already available here
    		//load if the keyword is not a textvalue or a text dialog or a grid dialog
    		//And if keyword is not available
    		if( !(loadingKeyword.equals(Dictionary.TEXTBOX_VALUE) || loadingKeyword.equals(Dictionary.TEXT_DIALOG) || loadingKeyword.equals(Dictionary.GRID_DIALOG) || loadingKeyword.equals(Dictionary.CUSTOM_DIALOG)) )
    			if(!db.isAvailable(group, loadingKeyword))
    				currentDisplayingList.add(loadingKeyword.replace("__", "(").replace("_", ")"));
		}
    	Collections.sort(currentDisplayingList);
    }
    
    
    private void loadValues(String group, String keyword)
    {
    	currentDisplayingList = new ArrayList<String>();
    	//Check if the group has an $END else add it
    	String stringAfter = getText(getCaretPostion(), inputStyles.getLength());
    	int groupStartPosition = stringAfter.indexOf("$");
    	if(groupStartPosition == -1)
    		currentDisplayingList.add("$END");
    	else
		{
    		groupStartPosition +=  getCaretPostion();
    		//if the group at groupStartPosition does not start with $E then add it 
			if(!getWordAfter(groupStartPosition).equalsIgnoreCase("$END"))
				currentDisplayingList.add("$END");
		}
    	
    	//Get all the values for the keyword in the groups
    	Element groupRoot = Dictionary.get(group,keyword);
    	if(groupRoot == null) return;
    	
    	NodeList groups = groupRoot.getChildNodes();
    	//Load all the values
    	for (int i = 0; i < groups.getLength(); i++) {
    		String loadingValue = groups.item(i).getAttributes().getNamedItem("value").getNodeValue();
    		//load if the value is not a textvalue or a text dialog or a grid dialog
    		if( !(loadingValue.equals(Dictionary.TEXTBOX_VALUE) || loadingValue.equals(Dictionary.TEXT_DIALOG) || loadingValue.equals(Dictionary.GRID_DIALOG) || loadingValue.equals(Dictionary.CUSTOM_DIALOG)) )
    			currentDisplayingList.add(loadingValue);
		}
    	Collections.sort(currentDisplayingList);
    }
    
    private String getWordAfter(int location)
    {
    	String wordLine = getParagraphLine(location);

    	location -= inputStyles.getParagraphElement(location).getStartOffset();
    	
    	int wordEnding = wordLine.indexOf(" ", location);
	    wordEnding = (wordEnding == -1)? wordLine.length() - 1: wordEnding;
	    	
    	return wordLine.substring(location, wordEnding);
    }
    
    @SuppressWarnings("unused")
	private String getWordBefore(int location)
    {
    	String wordLine = getParagraphLine(location);
	    	
    	int wordStarting = wordLine.lastIndexOf(" ", location);
    	wordStarting++;
    	
    	return wordLine.substring(wordStarting, location);
    }
    
    private String getParagraphLine(int location)
    {
    	try {
	    	javax.swing.text.Element paraElement = inputStyles.getParagraphElement(location);
	    	String paraLine = inputStyles.getText(paraElement.getStartOffset(), paraElement.getEndOffset() - paraElement.getStartOffset());
	    	return paraLine;
    	} catch (BadLocationException e) {e.printStackTrace();}
    	return "";
    }
    
    private String getText(int startLoc , int EndLoc)
    {
    	try {
			return inputStyles.getText(startLoc, EndLoc - startLoc);
		} catch (BadLocationException e) {e.printStackTrace();}
		return "";
    }
    
    private void loadToolTipForDisplayItem()
    {
    	currentToolTipList = new ArrayList<String>();
    	
    	String Group = null ,Keyword = null;
    	String[] splitString = context.split(" ");
    	
    	if(context.length() != 0 && splitString.length > 0)
		{
			Group = splitString[0].substring(1);
		}
    	if(splitString.length > 1)
    	{
    		Keyword = splitString[1];
    	}
    	
    	for (int i = 0; i < currentDisplayingList.size(); i++) {
			String currentItem = currentDisplayingList.get(i);
			if(currentItem.equalsIgnoreCase("$END"))
			{
				currentToolTipList.add("Group ending");
				continue;
			}
			
	        String xpathCondExpr = "/root/ToolTips/ToolTip[";
	        
			if(Group == null)
			{
				//The item is a group
				xpathCondExpr += "@Group='" + currentItem.substring(1) + "' and not(@Keyword) and not(@Value)";
			}
			else if(Keyword == null)
			{
				//The item is a keyword
				xpathCondExpr += "@Group='" + Group + "' and @Keyword='" + Dictionary.getFormattedKeyword(currentItem) + "' and not(@Value)";
			}
			else
			{
				//The item is a value
				xpathCondExpr += "@Group='" + Group + "' and @Keyword='" + Dictionary.getFormattedKeyword(Keyword) + "' and @Value='" + currentItem + "'";
			}
			
			xpathCondExpr += "]";
			
			try
			{
				Node tooltipNode = (Node)tooltipXpath.evaluate( xpathCondExpr ,GlobalParameters.userNotesAndToolTip, XPathConstants.NODE);
				if(tooltipNode == null)
					currentToolTipList.add(null);
				else
					currentToolTipList.add( Cosmetics.getInputFileToolTip(tooltipNode.getTextContent().trim()));
			}
			catch (XPathExpressionException e) 
			{
				currentToolTipList.add(null);
			}
		}
    }
    
    private class inputPaneListeners extends KeyAdapter
	{
    	KeyEvent currentEvent = null;
		public void keyPressed(java.awt.event.KeyEvent evt) {
			currentEvent = evt;
			//if ENTER is pressed and the list is visible then get the
			//value selected in the list and insert it into the textpane
			//if ESC is pressed and the list is visible then close the list
	        //if UP or DOWN is pressed and the list is visible then move the selection
	        if(!isControlKeyPressed && 
	        		(evt.getKeyCode() == KeyEvent.VK_ENTER
	        				|| evt.getKeyCode() == KeyEvent.VK_TAB
	        				|| evt.getKeyCode() == KeyEvent.VK_ESCAPE
	        				|| evt.getKeyCode() == KeyEvent.VK_UP
	        				|| evt.getKeyCode() == KeyEvent.VK_DOWN
	        			)
	        	)
	        	listKeyListeners.keyPressed(evt);
	        
	        //if CTRL + ' ' is pressed force the ContentAssist to show 
        	if(isControlKeyPressed && evt.getKeyCode() == KeyEvent.VK_SPACE)
                fire(true);
	        
	        if(evt.getKeyCode() == KeyEvent.VK_CONTROL)
	            isControlKeyPressed = true;
	        else
	            isControlKeyPressed = false;
	        currentEvent = null;
        }
		
        public void keyReleased(java.awt.event.KeyEvent evt) {
        	currentEvent = evt;
        	//if the space or equalTo or an enter is pressed then
        	//display either Group or Keyword or Value based on the context
        	if(!isControlKeyPressed && 
        			 (evt.getKeyCode() == KeyEvent.VK_SPACE  // ' '
        					 || evt.getKeyCode() == KeyEvent.VK_EQUALS // =
        					 )
        		)
        	{
        		try {
					if(evt.getKeyCode() == KeyEvent.VK_SPACE && getCaretPostion() != inputStyles.getLength() && !inputStyles.getText(getCaretPostion(), 1).equals(" "))
						return;
				} catch (BadLocationException e) {}
				fire(false);
        	}

        	if(!(evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN))
        		listKeyListeners.keyPressed(evt);

        	currentEvent = null;
        }
	}
	
    private class inputPaneAdapter extends MouseAdapter
	{
		 public void mouseClicked(java.awt.event.MouseEvent evt) {
			 listScrollPane.setVisible(false);
         }
	}
    
	private class listKeyAdapter extends KeyAdapter
	{
		 public void keyPressed(java.awt.event.KeyEvent evt) {
			 if(!listScrollPane.isVisible())
		            return;
		        
		     if(evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB)
		     {
		            if(list.getSelectedValue() != null)
		            {
		            	try
		            	{
		            		String stringToInsert = list.getSelectedValue().toString();
		            		String filterString = getCurrentPassedText();
				            if(stringToInsert.toUpperCase().startsWith(filterString.toUpperCase()))
				                stringToInsert = stringToInsert.substring(filterString.length());
				            ((DefaultStyledDocument)inputStyles).replace(getCaretPostion(), 0 , stringToInsert, null);
				            
				            /*//This is to replace the remaining part of the keywords with the current keyword completely
				             * 
				            int paragraphEndPosition = inputStyles.getParagraphElement(getCaretPostion()).getEndOffset();
				            if(inputStyles.getLength() == 0) paragraphEndPosition = 0;
				            int endPosition = inputStyles.getText(getCaretPostion(), paragraphEndPosition - getCaretPostion()).indexOf(" ");
				            if(endPosition == -1) endPosition = paragraphEndPosition - 1 ;
				            else endPosition += getCaretPostion();
				            
				            ((DefaultStyledDocument)inputStyles).replace(getCaretPostion(), endPosition - getCaretPostion(), stringToInsert, null);
				            */
		            	}
		            	catch(Exception e){ e.printStackTrace(); }
		            	UndoRedoHandler.toggleGroupClassifier();
		            }
		            listScrollPane.setVisible(false);
		            inputPane.requestFocus();
		            evt.consume();
		            return;
		        }
		        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
		        {
		            listScrollPane.setVisible(false);
		            inputPane.requestFocus();
		            evt.consume();
		            return;
		        }
		        if(evt.getKeyCode() == KeyEvent.VK_DOWN)
		        {
		            int selectedIndex = (list.getSelectedIndex() < list.getModel().getSize() - 1)?list.getSelectedIndex() + 1:0;
		            list.setSelectedIndex(selectedIndex);
		            list.ensureIndexIsVisible(selectedIndex);
		            evt.consume();
		            return;
		        }
		        if(evt.getKeyCode() == KeyEvent.VK_UP)
		        {
		            list.setSelectedIndex((list.getSelectedIndex() > 0)?list.getSelectedIndex() - 1:list.getModel().getSize() - 1);
		            list.ensureIndexIsVisible(list.getSelectedIndex());
		            evt.consume();
		            return;
		        }
		        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		        {
		            if(getCaretPostion() < currentDisplayedOffset)
		            {
		            	listScrollPane.setVisible(false);
		            	evt.consume();
		            }
		            else
		            	filter();
		            return;
		        }
		        //if the list is visible and if it is not any of the actions above then filter the text
	            filter();
         }
		 
		 
	}
	
	private class listMouseAdapter extends MouseAdapter
	{
		 public void mouseClicked(java.awt.event.MouseEvent evt) {
			 if(evt.getClickCount() == 2)
		            listKeyListeners.keyPressed(new KeyEvent(list,0,0L,0,KeyEvent.VK_ENTER,'\n'));
         }
	}
	
	private class AssistRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = -5243578200443821114L;

		public AssistRenderer() 
		{
			setOpaque(true);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			if(isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				if(value != null &&  value.toString().startsWith("$"))
					setForeground(Cosmetics.GROUP_ATTRIBUTE_COLOR);
				else
					setForeground(list.getForeground());
			}
			//Set Text
			setText(value.toString());
			//Set the tool tips if present
			setToolTipText(currentDisplayToolTipList.get(index));
			return this;
		}
	}
}
