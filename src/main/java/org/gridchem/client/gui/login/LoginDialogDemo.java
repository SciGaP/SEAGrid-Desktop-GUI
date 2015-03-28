/*
 * LoginDialog.java
 */
 
package org.gridchem.client.gui.login;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.gridchem.client.util.GMS3;
import org.gridchem.service.model.enumeration.AccessType;

/** 
 * Sample login dialog
 */
public class LoginDialogDemo extends JDialog {
  
    /** 
     * Resource bundle with default locale 
     */
    private ResourceBundle resources = null;
    private JTextField userNameTextField;
    private JPasswordField passwordField;
    /**
     * Command string for a cancel action (e.g.,a button or menu item). 
     * This string is never presented to the user and should 
     * not be internationalized.
     */
    private String CMD_CANCEL = "cmd.cancel"/*NOI18N*/;

    /**
     * Command string for a help action (e.g.,a button or menu item). 
     * This string is never presented to the user and should 
     * not be internationalized.
     */
    private String CMD_HELP = "cmd.help"/*NOI18N*/;

    /**
     * Command string for a login action (e.g.,a button or menu item). 
     * This string is never presented to the user and should 
     * not be internationalized.
     */
    private String CMD_LOGIN = "cmd.login"/*NOI18N*/;

    private JButton loginButton = null;
  
    /**
     * Create a new LoginDialog
     */
    public LoginDialogDemo(Frame parent,boolean modal) {
        super(parent, modal);
        initResources();
        initComponents();
        pack();
    }

    /**
     * Loads locale-specific resources: strings, images, et cetera
     */
    private void initResources() {
//	Locale locale = Locale.getDefault();
//        resources = ResourceBundle.getBundle(
//	    "samples.resources.bundles.LoginDialogResources",
//	    locale);
    }

    /** 
     * This method is called from within the constructor to
     * initialize the dialog.
     * 

     * We use dynamic layout managers, so that layout is dynamic and will
     * adapt properly to user-customized fonts and localized text. The
     * GridBagLayout makes it easy to line up components of varying
     * sizes along invisible vertical and horizontal grid lines. It
     * is important to sketch the layout of the interface and decide
     * on the grid before writing the layout code. 
     * 


     * 

     * Here we actually use
     * our own subclass of GridBagLayout called StringGridBagLayout,
     * which allows us to use strings to specify constraints, rather
     * than having to create GridBagConstraints objects manually.
     * 


     * 

     * We use the JLabel.setLabelFor() method to connect
     * labels to what they are labeling. This allows mnemonics to work
     * and assistive to technologies used by persons with disabilities
     * to provide much more useful information to the user.
     * 


     */
    private void initComponents() {

        // Set properties on this dialog
	//
        Container contents = getContentPane();
        contents.setLayout(new GridLayout(4,2));
        setTitle ("Login Example");
        addWindowListener(new WindowAdapter () {
            public void windowClosing(WindowEvent event) {
		// user hit window manager close button
                windowAction(CMD_CANCEL);
            }
        });
  
	userNameTextField = new JTextField(); //needed below

	// user name label
        JLabel userNameLabel = new JLabel();
        userNameLabel.setDisplayedMnemonic('U');
        userNameLabel.setLabelFor(userNameTextField);
        userNameLabel.setText("Username");
        contents.add(
//        		"anchor=WEST,insets=[12,12,0,0]", 
        		userNameLabel);
  
	// user name text field
        userNameTextField.setToolTipText(
	    "Enter your GridChem or TeraGrid username");
        contents.add(
//	    "fill=HORIZONTAL,weightx=1.0,insets=[12,7,0,11]",
            userNameTextField);
  
        passwordField = new JPasswordField(); // needed below

	// password label
        JLabel passwordLabel = new JLabel();
        passwordLabel.setDisplayedMnemonic('P');
        passwordLabel.setText("Password");
        passwordLabel.setLabelFor(passwordField);
        contents.add(
//	    "gridx=0,gridy=1,anchor=WEST,insets=[11,12,0,0]",
	    passwordLabel);
  
	// password field
        passwordField.setToolTipText("Enter your GridChem or TeraGrid password");
        Font echoCharFont = new Font("Lucida Sans", Font.PLAIN, 12);
        passwordField.setFont(echoCharFont);
        passwordField.setEchoChar('\u2022');
        contents.add(
//	    "gridx=1,gridy=1,fill=HORIZONTAL,weightx=1.0,insets=[11,7,0,11]",
            passwordField);
  
        JPanel buttonPanel = createButtonPanel(); // sets global loginButton
        contents.add(
//	    "gridx=0,gridy=2,gridwidth=2,anchor=EAST,insets=[17,12,11,11]",
	    buttonPanel);

	getRootPane().setDefaultButton(loginButton);

    } // initComponents()
  
    /**
     * Creates the panel of buttons that goes along the bottom
     * of the dialog
     * 

     * Sets the global variable loginButton
     * 


     */
    private JPanel createButtonPanel() {

	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 0));
  
	// login button (global variable)
	loginButton = new JButton();
    loginButton.setText("Login");
	loginButton.setToolTipText("Click to login to the CCG");
	loginButton.setActionCommand(CMD_LOGIN);
	loginButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
	        windowAction(event);
	    }
	});
        panel.add(loginButton);
    
	// space
        panel.add(Box.createRigidArea(new Dimension(5,0)));

	// cancel button
	JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
	cancelButton.setActionCommand(CMD_CANCEL);
	cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
	        windowAction(event);
	    }
	});
        panel.add(cancelButton);
    
	// space
        panel.add(Box.createRigidArea(new Dimension(5,0)));
    
	// help button
	JButton helpButton = new JButton();
        helpButton.setMnemonic('H');
        helpButton.setText("Help");
	helpButton.setActionCommand(CMD_HELP);
	helpButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
	        windowAction(event);
	    }
	});
        panel.add(helpButton);

	Vector buttons = new Vector(3);
	buttons.add(cancelButton);
	buttons.add(helpButton);
	buttons.add(loginButton);
	equalizeComponentSizes(buttons);
	buttons.removeAllElements(); // simplify gc

	return panel;
    } // createButtonPanel()

    /**
     * 

     * Sets the JComponents in the list to be the same size.
     * This is done dynamically by setting each button's 
     * preferred and maximum sizes after the components have
     * been created. This way, the layout automatically adjusts 
     * to the locale-specific strings and customized fonts.
     * 


     * 

     * The sizes of the JComponents are NOT modified here. That is
     * done later by the layout manager.
     * 


     * @param components must contain only instances of JComponent
     */
    private void equalizeComponentSizes(java.util.List components) {

	// Get the largest width and height
	int i = 0;
	Dimension maxPreferred = new Dimension(0,0);
	JComponent oneComponent = null;
	Dimension thisPreferred = null;
	for (i = 0; i < components.size(); ++i) {
	    oneComponent = (JComponent)components.get(i);
	    thisPreferred = oneComponent.getPreferredSize();
	    maxPreferred.width = 
	        Math.max(maxPreferred.width, (int)thisPreferred.getWidth());
	    maxPreferred.height = 
	        Math.max(maxPreferred.height, (int)thisPreferred.getHeight());
	}
      
        // reset preferred and maximum size since BoxLayout takes both 
	// into account 
	for (i = 0; i < components.size(); ++i) {
	    oneComponent = (JComponent)components.get(i);
            oneComponent.setPreferredSize((Dimension)maxPreferred.clone());
            oneComponent.setMaximumSize((Dimension)maxPreferred.clone());
        }
    } // equalizeComponentSizes()

    /**
     * The user has selected an option. Here we close and dispose the dialog.
     * If actionCommand is an ActionEvent, getCommandString() is called,
     * otherwise toString() is used to get the action command.
     *
     * @param actionCommand may be null
     */
    private void windowAction(Object actionCommand) {
	String cmd = null;
        if (actionCommand != null) {
	    if (actionCommand instanceof ActionEvent) {
	        cmd = ((ActionEvent)actionCommand).getActionCommand();
	    } else {
	        cmd = actionCommand.toString();
	    }
	}
	if (cmd == null) {
	    // do nothing
	} else if (cmd.equals(CMD_CANCEL)) {
	    System.out.println("your cancel code here...");
	} else if (cmd.equals(CMD_HELP)) {
	    System.out.println("your help code here...");
	} else if (cmd.equals(CMD_LOGIN)) {
	    boolean result = GMS3.login(userNameTextField.getText(), new String(passwordField.getPassword()), AccessType.COMMUNITY, new HashMap<String,String>());
	    if (result) {
	    	System.out.println("login succeeded");
	    } else {
	    	System.out.println("login failed");
	    	return;
	    }
	}
	setVisible(false);
	dispose();
    } // windowAction()

    /**
     * This main() is provided for debugging purposes, to display a 
     * sample dialog.
     */
    public static void main(String args[]) {
	JFrame frame = new JFrame() {
	    public Dimension getPreferredSize() {
	        return new Dimension(200,100);
	    }
	};
	frame.setTitle("Debugging frame");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(false);

        JDialog dialog = new LoginDialogDemo(frame, true);
	dialog.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent event) {
	        System.exit(0);
	    }
	    public void windowClosed(WindowEvent event) {
	        System.exit(0);
	    }
	});
	dialog.pack();
	dialog.setVisible(true);
    } // main()
  
} // class LoginDialog
 