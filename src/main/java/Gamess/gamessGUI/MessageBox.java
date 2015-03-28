package Gamess.gamessGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.Incompatible;
import Gamess.gamessGUI.IncompatibilityPackage.RequiresIncompatibility;
import Gamess.gamessGUI.Storage.IDBChangeListener;

public class MessageBox extends JTabbedPane {

	private static final long serialVersionUID = 4069514782057521435L;
	
	public static UserNotes notes = null;
	public static ExcludeMessages excludes = null;
	public static RequiredMessages requires = null;
	private static JTabbedPane ThisPane = null;
	private final String NOTE_TITLE = "Note";
	private final String EXCLUDE_TITLE =  "Incompatible Inputs";
	private final String REQUIRE_TITLE =  "Required Inputs";
	//private final String ExcludeInformationFormat = "Exclude $%s because %s is selected";
	private final String ExcludeInformationFormat = "Exclude $%s";
	private final String RequiredInformationFormat = "Requires $%s";
	public MessageBox() 
	{
		ThisPane = this;
		
		notes = new UserNotes();
		excludes = new ExcludeMessages();
		requires = new RequiredMessages();
		
		addTab(NOTE_TITLE , notes);
		addTab(EXCLUDE_TITLE, excludes);
		addTab(REQUIRE_TITLE , requires);
		
	}

	public class UserNotes extends JPanel implements IDBChangeListener
	{
		private static final long serialVersionUID = 1L;
		private int messageCount = 0;
		private JTextArea msgDisplayArea= new JTextArea();
		private XPath xpath = XPathFactory.newInstance().newXPath();
		public UserNotes() 
		{
			setLayout(new BorderLayout());
			
	    	msgDisplayArea.setAutoscrolls(true);
	    	msgDisplayArea.setEditable(false);
	    	msgDisplayArea.setFont( msgDisplayArea.getFont().deriveFont(Font.BOLD) );
	    	
	    	TitledBorder msgDisplayPanelTitle = new TitledBorder("Message Display");
	    	msgDisplayPanelTitle.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
	    	msgDisplayArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
	    	
	        JScrollPane scrpane=new JScrollPane(msgDisplayArea);
	        scrpane.setAutoscrolls(true);
	        
	        super.add(scrpane,BorderLayout.CENTER);
	        super.setBorder(msgDisplayPanelTitle);
		}
		
		public void write(String note)
		{
			msgDisplayArea.insert(" ------------------------------------------------------------------------------\n", 0);
			msgDisplayArea.insert(note + "\n", 0);
			msgDisplayArea.setCaretPosition(0);
			ThisPane.setTitleAt( 0 , NOTE_TITLE + " (" + ++messageCount + ")");
		}

		public void DataAdded(String Data) {
			try
			{
				String Group,Keyword,Value;
		        String[] splitData = Data.split(" ");
		        
		        Group = (splitData.length > 0) ? "@Group='" + splitData[0] + "' and " : "not(@Group) and " ;
		        Keyword = (splitData.length > 1) ? "@Keyword='" + splitData[1] + "' and " : "not(@Keyword) and ";
		        Value = (splitData.length > 2) ? "@Value='" + splitData[2] + "'" : "not(@Value)";
				
		        String xpathCondExpr = "/root/UserNotes/UserNote[" + Group + Keyword + Value  + "]";

		        Node note = (Node)xpath.evaluate( xpathCondExpr ,GlobalParameters.userNotesAndToolTip, XPathConstants.NODE);
		        if(note != null)
		        	write("Note:\n    When you select : $" + Data + "\n" + note.getTextContent());
			}
			catch(XPathExpressionException e){e.printStackTrace();}
		}

		public void DataRemoved(String Data) {
			if(Data.equalsIgnoreCase("SYSTEM PARALL"))
			{
				write("Note:\n $SYSTEM PARALL=.TRUE. as is needed for distributed data parallel\nMP2 program to execute the parallel algorithm,\n even if you are running on only one node");
			}
		}
		public void DropDB() {}
	}
	
	public abstract class IncompatibilityMessages extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private JTextPane incompDisplay = new JTextPane();
		public ArrayList<String> incompList = null;
		private int currentSelectedIndex = 0;
		JButton next , previous;
		
		public IncompatibilityMessages() 
		{
			incompDisplay.setFont( incompDisplay.getFont().deriveFont(Font.BOLD));
			
			setLayout(new BorderLayout());
			//Add the next and previous button
			next = new JButton(">>");
			next.addActionListener(this);
			next.setActionCommand("next");
			//next.setPreferredSize(new Dimension(25,25));
		
			previous = new JButton("<<");
			previous.addActionListener(this);
			previous.setActionCommand("previous");
			//previous.setPreferredSize(new Dimension(25,25));
			
			JPanel buttonPannel = new JPanel();
			buttonPannel.add(previous, BorderLayout.CENTER);
			buttonPannel.add(next, BorderLayout.CENTER);
			
			add(buttonPannel , BorderLayout.NORTH);
			
			//Add the TextArea to the center
			JScrollPane scrollPane = new JScrollPane(incompDisplay);
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			add(scrollPane, BorderLayout.CENTER);
			
			resetList(new ArrayList<String>());
		}

		public void actionPerformed(ActionEvent evt) 
		{
			if(evt.getActionCommand().equalsIgnoreCase("NEXT"))
			{
				currentSelectedIndex++;
				incompDisplay.setText(incompList.get(currentSelectedIndex));
				updateNextAndPrevious();
			}
			else
			{
				currentSelectedIndex--;
				incompDisplay.setText(incompList.get(currentSelectedIndex));
				updateNextAndPrevious();
			}
		}
		
		private void updateNextAndPrevious()
		{
			if(currentSelectedIndex < incompList.size() - 1)
				next.setEnabled(true);
			else
				next.setEnabled(false);
			
			if(currentSelectedIndex > 0)
				previous.setEnabled(true);
			else
				previous.setEnabled(false);
		}
		
		protected void resetList(ArrayList<String> newList) 
		{
			incompList = newList;
			if(incompList.size() == 0)
			{
				currentSelectedIndex = 0;
				incompDisplay.setText("No incompatibilities currently identified");
				updateNextAndPrevious();
			}
			else
			{
				currentSelectedIndex = 0;
				incompDisplay.setText(incompList.get(currentSelectedIndex));
				updateNextAndPrevious();
			}
		}
		
		protected void UpdateTitle(String title, int TabLocation)
		{
			if(incompList.size() == 0)
			{
				ThisPane.setTitleAt(TabLocation, title);
			}
			else
			{
				ThisPane.setTitleAt(TabLocation, title + " (" + incompList.size() + ")");
			}
		}
	}
	
	public class ExcludeMessages extends IncompatibilityMessages
	{
		private static final long serialVersionUID = 1L;

		public void UpdateList()
		{
			ArrayList<String> excludeList = new ArrayList<String>();
			ArrayList<Incompatible> list = ExcludeIncompatibility.getInstance().getIncompatibilityList();
			for (int i = 0; i < list.size(); i++) {
				//excludeList.add("Exclude $" + list.get(i).getInput() + " because " + list.get(i).getCondition().toString() + " is selected");
				excludeList.add( String.format(ExcludeInformationFormat, list.get(i).getInput()) ) ;
			}
			resetList(excludeList);
			UpdateTitle(EXCLUDE_TITLE, 1);
		}
	}
	
	public class RequiredMessages extends IncompatibilityMessages
	{
		private static final long serialVersionUID = 1L;

		public void UpdateList()
		{
			ArrayList<String> requireList = new ArrayList<String>();
			ArrayList<Incompatible> list = RequiresIncompatibility.getInstance().getIncompatibilityList();
			for (int i = 0; i < list.size(); i++) {
				//requireList.add("Require $" + list.get(i).getInput() + " because " + list.get(i).getCondition().toString() + " is selected");
				requireList.add( String.format(RequiredInformationFormat, list.get(i).getInput()) ) ;
			}
			resetList(requireList);
			UpdateTitle(REQUIRE_TITLE, 2);
		}
	}
}
