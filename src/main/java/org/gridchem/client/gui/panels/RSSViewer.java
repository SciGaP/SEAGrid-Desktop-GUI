/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Mar 15, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;
/**
 * Panel to display RSS announcements from CCG web server or wiki pages.  This should
 * replace the functionality of the 'Announcements' button in optsComponent that 
 * launched a browser on the user's local machine.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class RSSViewer {
    public static JFrame frame;
     
    public RSSViewer(String rssFeed) {
        frame = new JFrame("Announcements");
        final RSSDisplayPanel rssPanel = new RSSDisplayPanel(rssFeed);
        rssPanel.setPreferredSize(new Dimension(350,500));
        frame.getContentPane().add(rssPanel);
        frame.pack();
		
        // Centering the frame on the screen
        Toolkit kit = frame.getToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension windowSize = rssPanel.getSize();
        int windowWidth = windowSize.width;
        int windowHeight = windowSize.height;
        int upperLeftX = (screenWidth - windowWidth)/2;
        int upperLeftY = (screenHeight - windowHeight)/2;   
        frame.setLocation(upperLeftX, upperLeftY);
        
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
    }
    
    public static void main(String[] args)
    {
    	 	RSSViewer viewer = new RSSViewer(args[0]);
    		viewer.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
        
    }
}

    
class RSSDisplayPanel extends JPanel implements ActionListener
{
    private JButton cancelButton;
    private JButton refreshButton;
    private JTextPane announcementBoard;
    private URL feedURL;
    
    public RSSDisplayPanel (String rssFeed) {
        try {
            feedURL = new URL(rssFeed);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        Container messageBox = createAnnouncementPanel(parseRSS(feedURL));
        
        JPanel buttonBox = createButtonPanel();
    	
        // set up the layout of the buttons
		setLayout(new BorderLayout());
		
		// add each button to the layout
        add(messageBox,BorderLayout.PAGE_START);
        add(buttonBox,BorderLayout.CENTER);
    }
    
    private Container createAnnouncementPanel(String announcementMessage) {
        
        announcementBoard = new JTextPane();
        announcementBoard.setEditorKit( new HTMLEditorKit() );
        announcementBoard.setText(announcementMessage);
        announcementBoard.setCaretPosition(0);
        
        JScrollPane jscrollpane = new JScrollPane(announcementBoard);
        jscrollpane.setWheelScrollingEnabled(true);
        jscrollpane.setPreferredSize(new Dimension(300,450));
        jscrollpane.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5,5,5,5),
                                    jscrollpane.getBorder()));
        
        return jscrollpane;
    }
    
    /**
     * Create the button panel and layout the buttons created in the
     * makeButtons() call.
     * 
     * @return
     */
    private JPanel createButtonPanel() {
        
        makeButtons();
        
        JPanel buttonInterior = new JPanel();
        buttonInterior.setLayout(new GridLayout(1,2,5,0));
        buttonInterior.add(refreshButton);
        buttonInterior.add(cancelButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonInterior);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,5));
        
        return buttonPanel;
    }
    
    /**
     * Create and format the buttons on the panel
     */
    private void makeButtons() {
        
        refreshButton = new JButton("Refresh");
        refreshButton.setVerticalTextPosition(AbstractButton.CENTER);
        refreshButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        refreshButton.setToolTipText(
                "Refresh the announcements.");
        refreshButton.addActionListener(this);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setVerticalTextPosition(AbstractButton.CENTER);
        cancelButton.setHorizontalTextPosition(AbstractButton.CENTER);
        cancelButton.setToolTipText("Click to close this window");
        cancelButton.addActionListener(this);
    }
    
    public String parseRSS(URL feedURL) {
        /** Pull text from rss feed and place in text box **/	
        RSSHandler hand = new RSSHandler();
        
        try{
    	    RSSParser.parseXmlFile(feedURL,hand,false);
    	    RSSChannel ch = hand.getRSSChannel();
    	} catch(Exception e) {
    	    e.printStackTrace();
    	}
	    	
    	String announcementMessage = "";
    	LinkedList lst = hand.getRSSChannel().getItems();
    	for (int i = 0; i < lst.size(); i++){
    	    RSSItem itm = (RSSItem)lst.get(i);
    	    // date returned from rss feed is of type 
    	    //"2006-03-14T11:49:26GMT-05:00"
    	    String rssDate = itm.getDate().substring(0,(itm.getDate()).lastIndexOf(":")+2);
    	    rssDate = rssDate.replaceFirst("T","");
    	    SimpleDateFormat format = new SimpleDateFormat(
    	            "yyyy-MM-ddK:mm:ss");
    	    Date postDate = new Date();
    		try {
    		    postDate = format.parse(rssDate);
    		} catch (ParseException e1) {
    		    e1.printStackTrace();
    		}
		
        	announcementMessage += "<div style=\"background-color:#E7EEF6; color:#000000\">" +
                    "<div style=\"background-color:#A7B3C7; color:#FFFFFF;\">" +
                    "Posted at: " + new SimpleDateFormat("h:mm a").format(postDate)  + 
    	    			" on " + new SimpleDateFormat("MMM d, yyyy").format(postDate) + 
    	    			"</div><p>" + itm.getDescription() + "</p><br></div><br>";
        }
	    	
    	return announcementMessage;
    }

    public void actionPerformed(ActionEvent evt) {
       		Object item1;
       		Object item2;
       		
       		if (evt.getSource() == refreshButton) {
       		    //announcementBoard.setEditorKit(new HTMLEditorKit())
                announcementBoard.setText(parseRSS(feedURL));
                
       		} else if (evt.getSource() == cancelButton) {
       		    RSSViewer.frame.setVisible(false);
       		}
    }
}
