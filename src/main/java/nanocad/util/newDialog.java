package nanocad.util;

import java.awt.*;
 
public class newDialog extends Dialog implements java.awt.event.ActionListener
{
	private List propertyList = new List();
	private Button upButton = new Button("Up");
	private Button downButton = new Button("Down");
	private Button okButton = new Button("OK");
	private Button cancelButton = new Button("Cancel");
	private boolean wasCancelled;

	public newDialog(Frame owner)
	{
		super(owner, true);
		setSize(400,400);
		setInternalComponentLayout();
		setActionListeners();
	}
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (e.getSource() == upButton)
			reorderUp();
		if (e.getSource() == downButton)
			reorderDown();
		if (e.getSource() == okButton)
			closeWithOK();
		if (e.getSource() == cancelButton)
			closeWithCancel();
	}
	public void addItem(String item)
	{
		if(item == null)
			return;
		int i = -1 ;
		int val = atomicNumberOf(item);
		while(i+1<propertyList.getItemCount())
		{
			i++;
			String lItem = propertyList.getItem(i);
			int compVal = atomicNumberOf(lItem);
			if (val < compVal)
			{
				propertyList.addItem(item,i);
				return;
			}	        
		}
		propertyList.addItem(item);

	}
	private void addItemToDialog(Component newComponet, GridBagLayout gridbag, GridBagConstraints c)
	{ 
		gridbag.setConstraints(newComponet, c);
		add(newComponet);
	}
	public static int atomicNumberOf(String aListItem)
	{
		return Integer.parseInt(aListItem.substring(0,aListItem.indexOf(" ")));
	}
	private void closeWithCancel()
	{
		setVisible(false);
		wasCancelled = true;
	}
	private void closeWithOK()
	{
		setVisible(false);
		wasCancelled = false;
	}
	public boolean dialogWasCancelled()
	{
		return wasCancelled;
	}
	public String getItem(int index)
	{
		return propertyList.getItem(index);
	}
	public int getItemCount()
	{
		return propertyList.getItemCount();
	}
	public int getSelectedIndex()
	{
		return propertyList.getSelectedIndex();
	}
	public static void main(String args[])
	{
		newDialog dialog = new newDialog(new Frame());
		dialog.setVisible(true);
	}
	public void openForReorder()
	{
		upButton.setVisible(true);
		downButton.setVisible(true);
		cancelButton.setVisible(false);
		setVisible(true);
	}
	public void openForSelect()
	{
		upButton.setVisible(false);
		downButton.setVisible(false);
		cancelButton.setVisible(true);
		setVisible(true);
	}
	public void removeItem(int index)
	{
		propertyList.delItem(index);
	}
	public void reorderDown() 
	{
		String itemToMove = propertyList.getSelectedItem();
		int currentIndex = getSelectedIndex();
		if (currentIndex == (propertyList.getItemCount() -1 ))
			return;
		if (atomicNumberOf(propertyList.getItem(currentIndex + 1 )) != atomicNumberOf(itemToMove))
			return;
		((Frame1) getParent()).getFile().demoteProperty(currentIndex);
		propertyList.delItem(currentIndex);
		propertyList.addItem(itemToMove, currentIndex+1);
		propertyList.select(currentIndex+1);
		return;
	}
	public void reorderUp()
	{
		String itemToMove = propertyList.getSelectedItem();
		int currentIndex = getSelectedIndex();
		if (currentIndex == 0)
			return;
		if (atomicNumberOf(propertyList.getItem(currentIndex - 1 )) != atomicNumberOf(itemToMove))
			return;
		((Frame1) getParent()).getFile().promoteProperty(currentIndex);
		propertyList.delItem(currentIndex);
		propertyList.addItem(itemToMove, currentIndex - 1);
		propertyList.select(currentIndex -1);
	}
	//Event Handling

	public void setActionListeners()
	{
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	//DIALOG LAYOUT

	private void setInternalComponentLayout()
	{
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);

		GridBagConstraints listBoxConstraints = new GridBagConstraints();
		listBoxConstraints.fill = GridBagConstraints.BOTH;
		listBoxConstraints.gridwidth = GridBagConstraints.RELATIVE;
		listBoxConstraints.gridheight = 8;
		listBoxConstraints.gridx = 0;
		listBoxConstraints.gridy = 0;
		listBoxConstraints.weightx = 1.0;
		listBoxConstraints.weighty = 1.0;
		addItemToDialog(propertyList, gridbag, listBoxConstraints);

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.BOTH;
		buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
		buttonConstraints.gridheight = 1;
		buttonConstraints.gridx = 1;

		buttonConstraints.gridy = 0;
		addItemToDialog(upButton, gridbag, buttonConstraints);

		buttonConstraints.gridy = 1;
		addItemToDialog(downButton, gridbag, buttonConstraints);

		buttonConstraints.gridy = 2;
		addItemToDialog(okButton, gridbag, buttonConstraints);

		buttonConstraints.gridy = 3;
		addItemToDialog(cancelButton, gridbag, buttonConstraints);
	}
}
