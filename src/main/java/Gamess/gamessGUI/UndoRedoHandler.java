package Gamess.gamessGUI;

import java.util.Stack;

import javax.swing.AbstractButton;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UndoRedoHandler extends UndoManager implements UndoableEditListener{

	private static final long serialVersionUID = -3887860951075715652L;
	private Stack<Boolean> undoClassifierStack = new Stack<Boolean>();
	private Stack<Boolean> redoClassifierStack = new Stack<Boolean>();
	private static boolean currentGroupClassifier = true;
	private static boolean isLocked = false;
	
	public void undoableEditHappened(UndoableEditEvent e) 
	{
		super.addEdit(e.getEdit());
		undoClassifierStack.push(currentGroupClassifier);
	}
	
	@Override
	public synchronized void undo() throws CannotUndoException {
		//check if the changes can be undone
		if(!canUndo())
			return;
		//get the top classifier from undo stack
		boolean groupClassifier = undoClassifierStack.peek();
		//undo all the edits that are classified as the same
		//stop when the classifier changes
		while(undoClassifierStack.isEmpty() == false && undoClassifierStack.peek() == groupClassifier && canUndo() == true)
		{
			//push the classifiers into the redo stack
			redoClassifierStack.push(groupClassifier);
			//undo the edit
			super.undo();
			//remove the top item from the undo stack
			undoClassifierStack.pop();
		}
		
		UpdateInputFile.startUpdate();
		MessageBox.excludes.UpdateList();
    	MessageBox.requires.UpdateList();
    	ResetUndoRedoButton();
	}
	
	@Override
	public synchronized void redo() throws CannotRedoException {
		//check if the changes can be redone
		if(!canRedo())
			return;
		//get the top classifier from redo stack
		boolean groupClassifier = redoClassifierStack.peek();
		//redo all the edits that are classified as the same
		//stop when the classifier changes
		while(redoClassifierStack.isEmpty() == false && redoClassifierStack.peek() == groupClassifier && canRedo() == true)
		{
			//push the classifiers into the undo stack
			undoClassifierStack.push(groupClassifier);
			//redo the edit
			super.redo();
			//remove the top item from the redo stack
			redoClassifierStack.pop();
		}
		
		UpdateInputFile.startUpdate();
		MessageBox.excludes.UpdateList();
    	MessageBox.requires.UpdateList();
    	ResetUndoRedoButton();
	}
	
	public static void toggleGroupClassifier()
	{
		//toggle if this is not locked
		if(isLocked == false)
			currentGroupClassifier = !currentGroupClassifier;
	}
	
	public static void setLock()
	{
		isLocked = true;
	}
	
	public static void releaseLock()
	{
		isLocked = false;
		
		MessageBox.excludes.UpdateList();
    	MessageBox.requires.UpdateList();
	}
	
	public static boolean isLocked()
	{
		return isLocked;
	}
	
	AbstractButton undoButton = null;
	public void setUndoButton(AbstractButton _undoButton)
	{
		undoButton = _undoButton;
	}
	
	AbstractButton redoButton = null;
	public void setRedoButton(AbstractButton _redoButton)
	{
		redoButton = _redoButton;
	}
	
	public void ResetUndoRedoButton()
	{
		if(undoButton != null)
		{
			if(!this.canUndo())
				undoButton.setEnabled(false);
			else
				undoButton.setEnabled(true);
		}
		if(redoButton != null)
		{
			if(!this.canRedo())
				redoButton.setEnabled(false);
			else
				redoButton.setEnabled(true);
		}
	}
}
