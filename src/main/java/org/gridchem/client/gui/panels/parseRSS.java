package org.gridchem.client.gui.panels;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;

public class parseRSS {
  
   public String parseRSS(URL feedURL) {
	   String announcementMessage = "";
        /** Pull text from rss feed and place in text box **/	
        RSSHandler hand = new RSSHandler();
        
        try{
    	    RSSParser.parseXmlFile(feedURL,hand,false);
    	    RSSChannel ch = hand.getRSSChannel();
    	 
	    	
    	LinkedList lst = hand.getRSSChannel().getItems();
    	for (int i = 0; i < 1; i++){
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
        	"Recent Notice Posted at: " + new SimpleDateFormat("h:mm a").format(postDate)  + 
    	    			" on " + new SimpleDateFormat("MMM d, yyyy").format(postDate) + 
    	    			 "</div><p>"+ itm.getDescription()+"</p><br></div><br>";
        }
   } catch(Exception e) {
	    e.printStackTrace();
	    announcementMessage += "<div style=\"background-color:#E7EEF6; color:#000000\">" +
    	"<div style=\"background-color:#A7B3C7; color:#F00000;\">" +
    	"Recent Notice: Unavailable due to RSS url exception</div></div><br>";
	     
	}	
    	return announcementMessage;
    }
  
 }