package Gamess.gamessGUI;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Highlighter.HighlightPainter;

public class Cosmetics 
{
	//Underliners
	private static final WavedUnderlinePen ERROR_UNDERLINE_PEN = new WavedUnderlinePen(Color.RED);
	private static final WavedUnderlinePen WARNING_UNDERLINE_PEN = new WavedUnderlinePen(Color.YELLOW);
	
	//Attributes
	public static final SimpleAttributeSet NORMAL_ATTRIBUTE = new SimpleAttributeSet();
	public static final SimpleAttributeSet GROUP_ATTRIBUTE = new SimpleAttributeSet();
	public static final SimpleAttributeSet COMMENTS_ATTRIBUTE = new SimpleAttributeSet();
	public static final SimpleAttributeSet KEYWORD_ATTRIBUTE = new SimpleAttributeSet();
	public static final SimpleAttributeSet ERROR_UNDERLINE = new SimpleAttributeSet();
	public static final SimpleAttributeSet WARNING_UNDERLINE = new SimpleAttributeSet();
	
	//Colors
	public static final Color NORMAL_ATTRIBUTE_COLOR = Color.BLACK;
	public static final Color GROUP_ATTRIBUTE_COLOR = Color.RED;
	public static final Color KEYWORD_ATTRIBUTE_COLOR = Color.BLUE;
	
	static
	{
		//Normal AttributeSet
		StyleConstants.setForeground(NORMAL_ATTRIBUTE, NORMAL_ATTRIBUTE_COLOR);
		StyleConstants.setBold(NORMAL_ATTRIBUTE, false);

		
		//Group AttributeSet
		//StyleConstants.setForeground(GROUP_ATTRIBUTE, Color.getHSBColor(4.83456203f,.67f,.44f));
		StyleConstants.setForeground(GROUP_ATTRIBUTE, GROUP_ATTRIBUTE_COLOR);
		StyleConstants.setBold(GROUP_ATTRIBUTE, true);
		
		//Comment AttributeSet
		StyleConstants.setForeground(COMMENTS_ATTRIBUTE, Color.getHSBColor(1.35f, .63f, .71f));
		StyleConstants.setBold(COMMENTS_ATTRIBUTE, false);
		
		//Keyword attribute
		StyleConstants.setForeground(KEYWORD_ATTRIBUTE, KEYWORD_ATTRIBUTE_COLOR);
		StyleConstants.setBold(KEYWORD_ATTRIBUTE, false);
		
		//Error Underline
		ERROR_UNDERLINE.addAttribute("UNDERLINEPEN", ERROR_UNDERLINE_PEN);
		
		//Warning Underline
		WARNING_UNDERLINE.addAttribute("UNDERLINEPEN", WARNING_UNDERLINE_PEN);
	}
	
	public static final SimpleAttributeSet getCustomAttribute(String key, Object value)
	{
		SimpleAttributeSet toolTipAttribute = new SimpleAttributeSet();
		toolTipAttribute.addAttribute(key, value);
		return toolTipAttribute;
	}
	
	public static final SimpleAttributeSet getCustomAttribute(String key, Object value, AttributeSet existingAttribute)
	{
		SimpleAttributeSet toolTipAttribute = getCustomAttribute(key, value);
		if(existingAttribute != null)
		{
			toolTipAttribute.setResolveParent(existingAttribute);
		}
		return toolTipAttribute;
	}
	
	public static final void setCharacterAttributes(int startOffset , int length , AttributeSet attribute, boolean replace)
	{
		StyledDocument styledDocument = GamessGUI.inputFilePane.getStyledDocument();
		Element element = null;
		element = styledDocument.getCharacterElement(startOffset);

		//Do not do anything if the element already contains the same attribute
		if(element.getAttributes().containsAttributes(attribute) && replace == false)
			return;
		styledDocument.setCharacterAttributes(startOffset, length, attribute, replace);
	}
	
	public static final void setParagraphAttributes(int startOffset , int length , AttributeSet attribute)
	{
		StyledDocument styledDocument = GamessGUI.inputFilePane.getStyledDocument();
		Element element = null;
		element = styledDocument.getParagraphElement(startOffset);
		//Do not do anything if the element already contains the same attribute
		if(element.getAttributes().containsAttributes(attribute))
			return;
		styledDocument.setParagraphAttributes(startOffset, length, attribute, true);
		setCharacterAttributes(startOffset, length, attribute, true);
	}
	
	public static final void setUnderline(int startOffset , int length , AttributeSet attribute)
	{
		StyledDocument styledDocument = GamessGUI.inputFilePane.getStyledDocument();
		styledDocument.setCharacterAttributes(startOffset, length, getCustomAttribute("UNDERLINE", (HighlightPainter)attribute.getAttribute("UNDERLINEPEN")), false);
	}
	
	public static final void setTooltip(int startOffset , int length , String tooltip)
	{
		StyledDocument styledDocument = GamessGUI.inputFilePane.getStyledDocument();
		styledDocument.setCharacterAttributes(startOffset, length, getCustomAttribute("TOOLTIP", tooltip), false);
	}
	
	public static final String getFormattedToolTip(String tooltip)
	{
		//previous value was #E6BE8A
		return "<html><body bgcolor=#FFFAD2 rightmargin=0 topmargin=0 bottommargin=0 leftmargin=0>" + tooltip.replace("\n", "<br/>") + "</body></html>";
	}
	
	public static final String getInputFileToolTip(String tooltip)
	{
		return "<html><body>" + tooltip.replace("\n", "<br/>") + "</body></html>";
	}
	
	public static final String getMenuToolTip(String tooltip)
	{
		return "<html><body bgcolor=#FFFAD2 rightmargin=0 topmargin=0 bottommargin=0 leftmargin=0>" + tooltip.replace("\n", "<br/>") + "</body></html>";
	}
}
