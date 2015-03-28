package nanocad.util;

import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (7/14/2000 12:25:34 PM)
 * @author: 
 */
public class YesNoDialog extends java.awt.Dialog implements java.awt.event.ActionListener {
	private Button yesButton = new Button("Yes");
	private Button noButton = new Button("No");
	private boolean answeredYes;
/**
 * Insert the method's description here.
 * Creation date: (7/14/2000 12:26:13 PM)
 */
public YesNoDialog(String question)
{
	super(new Frame(), "", true);
	addComponents(question);
	addActionListeners();
}
/**
 * Insert the method's description here.
 * Creation date: (7/14/2000 12:40:09 PM)
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed(java.awt.event.ActionEvent e)
{
	if(e.getSource() == yesButton)
		closeWithYes();
	if(e.getSource() == noButton)
		closeWithNo();	
}

private void addActionListeners()
{
	yesButton.addActionListener(this);
	noButton.addActionListener(this);	
}

private void addComponents(String question)
{
	setBounds(100,100,200,100);
	setLayout(new FlowLayout());
	add(new Label(question));
	add(yesButton);
	add(noButton);	
}
public void ask() 
{
	setVisible(true);	
}

private void closeWithNo() 
{
	answeredYes = false;
	setVisible(false);	
}

private void closeWithYes() 
{
	answeredYes = true;
	setVisible(false);	
}

public boolean userAnsweredNo() {
	return !answeredYes;
}

public boolean userAnsweredYes() {
	return answeredYes;
}
}
