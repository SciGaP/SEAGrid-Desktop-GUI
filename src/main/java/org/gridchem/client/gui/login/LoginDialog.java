/**
 * 
 */
package org.gridchem.client.gui.login;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.gridchem.client.GetFile;
import org.gridchem.client.GridChem;
import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.optsComponent;
import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.exceptions.LoginException;
import org.gridchem.service.model.enumeration.AccessType;

import com.sun.java.help.impl.SwingWorker;

/**
 * @author dooley
 * 
 */
public class LoginDialog extends JDialog implements ActionListener {

	private JTabbedPane authTabbedPane;

	private static JLabel statusLabel;

	protected static JButton loginButton;
	private static JButton cancelButton;

	public LoginDialog(Frame frame, boolean modal) {
		super(frame, "GridChem Login", modal);

		initLayout();

		pack();

		Toolkit kit = this.getToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		Dimension windowSize = this.getSize();
		int windowWidth = windowSize.width;
		int windowHeight = windowSize.height;
		int upperLeftX = (screenWidth - windowWidth) / 2;
		int upperLeftY = (screenHeight - windowHeight) / 2;
		this.setLocation(upperLeftX, upperLeftY);

		setVisible(true);
	}

	private void initLayout() {

		Border buttonBorder = BorderFactory.createEmptyBorder(0, 10, 0, 10);

		authTabbedPane = new JTabbedPane();
		authTabbedPane.addTab("Community", new AuthenticationForm(
				AccessType.COMMUNITY, this));
		authTabbedPane.addTab("XSEDE", new AuthenticationForm(
				AccessType.TERAGRID, this));
		authTabbedPane.addTab("External", new AuthenticationForm(
				AccessType.EXTERNAL, this));

		loginButton = new JButton("Login");
		loginButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setBorder(buttonBorder);

		statusLabel = new JLabel("");
		statusLabel.setPreferredSize(new Dimension(authTabbedPane.getWidth(),
				25));
		statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(authTabbedPane);
		add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(statusLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(buttonPanel);
		add(mainPanel);

		// setPreferredSize(new Dimension(420, 200));
		setResizable(false);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				// clearLogin();
				super.windowClosed(e);

			}

		});

		this.getRootPane().setDefaultButton(loginButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			loginButton.setEnabled(false);
			final AuthenticationForm form = (AuthenticationForm) authTabbedPane
					.getSelectedComponent();
			try {
				updateMessage("Logging in...");
				Thread thread = new Thread() {
					public void run() {
						form.login();
					}
				};

				thread.start();

			} catch (LoginException ex) {
				updateMessage(ex.getLocalizedMessage());
				loginButton.setEnabled(true);
				clearLogin();
			}

		} else {
			//clearLogin();
			dispose();
		}
	}

	public void updateMessage(final String message) {
		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// statusLabel.setText(message);
		// }
		// });

		SwingWorker worker = new SwingWorker() {
			public Object construct() {
				statusLabel.setText(message);
				return null;
			}
		};
		worker.start();
	}

	public void completeLogin() {
		Settings.authenticated = true;
		optsComponent.messageBoard.append("Login sucessful!\n");
		updateMessage("Login successful!!");
		loginButton.setEnabled(true);
		GridChem.oc.updateAuthenticatedStatus();
		dispose();
	}

	public static void clearLogin() {
		try {
			Settings.nameOK = false;

			// clear username
			Settings.name.selectAll();
			Settings.name.replaceSelection("");

			// clear gridchemusername
			Settings.gridchemusername = "";

			// clear password
			Settings.pass.selectAll();
			Settings.pass.replaceSelection("");

			// clear hostname
			Settings.sshhost.selectAll();
			Settings.sshhost.replaceSelection("");

			// clear myproxy server
			Settings.myproxyServer.selectAll();
			Settings.myproxyServer.replaceSelection("");

			// clear myproxy tag
			Settings.myproxyTag.selectAll();
			Settings.myproxyTag.replaceSelection("");

			// reset kerberos realm
			Settings.kerberosRealm.setText("ncsa.edu");
		} catch (Exception e) {
		}

		GMS3.logout();

		// reset authentication parameters
		// Settings.session = null;
		// Settings.channel = null;
		Settings.authenticated = false;
		Settings.authenticatedSSH = false;
		Settings.authenticatedGlobus = false;
		Settings.authenticatedKerberos = false;
		Settings.authenticatedGridChem = false;
		Settings.authMethodSelection = "<unassigned>";

		GetFile.getfileisdone = false;

		// clear preferences
		Preferences preferences = Preferences.getInstance();
		try {
			preferences.clear();
		} catch (BackingStoreException e) {
		}

		// clear all user session data
		GridChem.user = null;
		GridChem.systems.clear();
		GridChem.applications.clear();
		GridChem.jobs.clear();
		GridChem.projects.clear();
		// GridChem.userVO = null;

		// reset all the button panels to force refeshes
		if (optsComponent.pw != null) {
			optsComponent.pw.setVisible(false);
			optsComponent.pw = null;
		}

		if (optsComponent.monitorWindow != null) {
			optsComponent.monitorWindow.dispose();
		}

		// hide and clear submit jobs window
		if (optsComponent.sjw != null) {
			optsComponent.sjw.setVisible(false);
			optsComponent.sjw = null;
			// shoudl probably clear jobs list from GridChem as well
			SubmitJobsWindow.jobQueue.clear();
			SubmitJobsWindow.jobSubmitted.clear();
		}

		// open a login prompt for the user to reauthenticate
		if (GridChem.oc != null) {
			GridChem.oc.updateAuthenticatedStatus();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame() {
			public Dimension getPreferredSize() {
				return new Dimension(200, 100);
			}
		};
		frame.setTitle("Debugging frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);

		JDialog dialog = new LoginDialog(frame, true);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}

			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});

	}
}
