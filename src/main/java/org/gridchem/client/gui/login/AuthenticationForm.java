/**
 * 
 */
package org.gridchem.client.gui.login;

import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.LabelTextRows;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.exceptions.LoginException;
import org.gridchem.service.model.enumeration.AccessType;

/**
 * @author dooley
 * 
 */
public class AuthenticationForm extends JPanel {

	private AccessType type;

	private JLabel usernameFieldLabel = new JLabel("Username");
	private JLabel passwordFieldLabel = new JLabel("Password");
	private JLabel myproxyServerFieldLabel = new JLabel("MyProxy Server");
	private JLabel myproxyUsernameFieldLabel = new JLabel("MyProxy Username");
	private JLabel myproxyPasswordFieldLabel = new JLabel("MyProxy Password");

	private JTextField usernameTextField;
	private JTextField myproxyServerTextField;
	private JTextField myproxyUsernameTextField;

	private JPasswordField passwordField;
	private JPasswordField myproxyPasswordField;

	private int prefWidth = 580;
	private int prefHeight = 350;

	private LoginDialog loginDialog;

	/**
	 * @throws HeadlessException
	 */
	public AuthenticationForm(AccessType type, LoginDialog loginDialog)
			throws HeadlessException {
		super();

		this.type = type;
		this.loginDialog = loginDialog;

		initLayout();

	}

	private void initLayout() {
		Border leBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		Border eBorder1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border eBorder2 = BorderFactory.createEmptyBorder(0, 10, 0, 10);
		Border eBorder3 = BorderFactory.createEmptyBorder(0, 0, 5, 0);

		usernameTextField = new JTextField(Invariants.loginFieldSize);
		usernameFieldLabel.setLabelFor(usernameTextField);

		passwordField = new JPasswordField(Invariants.loginFieldSize);
		passwordField.setText("");
		passwordFieldLabel.setLabelFor(passwordField);
		
		if (Settings.gridchemusername!=null) {
			usernameTextField.setText(Settings.gridchemusername);
		} else {
			usernameTextField.setText("");
		}

		myproxyServerTextField = new JTextField(Invariants.loginFieldSize);
		myproxyServerFieldLabel.setLabelFor(myproxyServerTextField);

		myproxyUsernameTextField = new JTextField(Invariants.loginFieldSize);
		myproxyUsernameFieldLabel.setLabelFor(myproxyUsernameTextField);

		myproxyPasswordField = new JPasswordField(Invariants.loginFieldSize);
		myproxyPasswordFieldLabel.setLabelFor(myproxyPasswordField);

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		setBorder(eBorder1);

		JLabel[] labels = { usernameFieldLabel, passwordFieldLabel,
				myproxyServerFieldLabel, myproxyUsernameFieldLabel,
				myproxyPasswordFieldLabel };
		JTextField[] fields = { usernameTextField, passwordField,
				myproxyServerTextField, myproxyUsernameTextField,
				myproxyPasswordField };

		LabelTextRows.add(labels, fields, gbl, this);

		myproxyServerFieldLabel.setVisible(type.equals(AccessType.EXTERNAL));
		myproxyServerTextField.setVisible(type.equals(AccessType.EXTERNAL));

		myproxyUsernameFieldLabel.setVisible(type.equals(AccessType.EXTERNAL));
		myproxyUsernameTextField.setVisible(type.equals(AccessType.EXTERNAL));

		myproxyPasswordFieldLabel.setVisible(type.equals(AccessType.EXTERNAL));
		myproxyPasswordField.setVisible(type.equals(AccessType.EXTERNAL));

		myproxyUsernameFieldLabel.setVisible(type.equals(AccessType.TERAGRID));
		myproxyUsernameTextField.setVisible(type.equals(AccessType.TERAGRID));

		myproxyPasswordFieldLabel.setVisible(type.equals(AccessType.TERAGRID));
		myproxyPasswordField.setVisible(type.equals(AccessType.TERAGRID));
	}

	public void login() {
		// Login to the GMS using the authentication type set in the constructor
		HashMap<String, String> authMap = new HashMap<String, String>();

		if (type.equals(AccessType.EXTERNAL)
				|| type.equals(AccessType.TERAGRID)) {

			GridChem.accessType = AccessType.TERAGRID;
			GridChem.externalUsername = myproxyUsernameTextField.getText();
			
			if (type.equals(AccessType.TERAGRID)) {
				authMap.put("myproxy.server", "myproxy.teragrid.org");
			} else {
				authMap.put("myproxy.server", myproxyServerTextField.getText());
			}
			authMap.put("myproxy.username", myproxyUsernameTextField.getText());
			authMap.put("myproxy.password",
					new String(myproxyPasswordField.getPassword()));

		} else {
			Settings.authenticatedGridChem = true;
		}

		try {
			GMS3.login(usernameTextField.getText(),
					new String(passwordField.getPassword()), type, authMap);
			loginDialog.updateMessage("Login successful...");

			Thread.currentThread().sleep(500);
			GridChem.projects = GMS3.getProjects();
			// added -nik
			System.out.println(" The project size is"
					+ GridChem.projects.toString() + GridChem.projects.size()
					+ "\n");
			loginDialog.updateMessage("Retrieved projects...");

			Thread.currentThread().sleep(500);
			if (GridChem.projects.size() == 1) {
				Thread.currentThread().sleep(500);
				loginDialog.updateMessage("Setting session project...");
				GMS3.setCurrentProject(GridChem.projects.get(0));
				
				Thread.currentThread().sleep(500);
				loginDialog.updateMessage("Updating details...");
				GridChem.project = GMS3.getCurrentProject();
				
				/*System.out.println("#########################");
				for (ComputeBean cb : GridChem.project.getSystems()) {
					System.out.println(cb.getName());
					for (SoftwareBean sb : cb.getSoftware()) {
						System.out.println("\t" + sb.getName());
					}
				}
				System.out.println("#########################");*/

			} else {
				ProjectSelectionDialog psd = new ProjectSelectionDialog(
						GridChem.projects, loginDialog);
				if (psd.getProject() == null) {
					loginDialog.updateMessage("Login cancelled.");
					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							loginDialog.loginButton.setEnabled(true);
							return null;
						}
					};

					worker.start();
					return;
				} else {
					Thread.currentThread().sleep(500);
					loginDialog.updateMessage("Setting session project...");
					GMS3.setCurrentProject(psd.getProject());

					Thread.currentThread().sleep(500);
					loginDialog.updateMessage("Updating details...");
					GridChem.project = GMS3.getCurrentProject();
				}
			}

			Thread.currentThread().sleep(500);
			loginDialog.updateMessage("Retrieving user profile...");
			GridChem.user = GMS3.getProfile();
			System.out
					.println(" The User is" + GridChem.user.toString() + "\n");
			// loginDialog.updateMessage("Retrieving user resources...");
			// GridChem.systems = GMS3.getHardware(GridChem.project.getId());
			//
			// loginDialog.updateMessage("Retrieving user software...");
			// GridChem.software = GMS3.getSoftware();

			SwingWorker worker = new SwingWorker() {
				public Object construct() {
					loginDialog.completeLogin();

					return null;
				}
			};

			worker.start();

		} catch (final Exception e) {
			SwingWorker worker = new SwingWorker() {
				public Object construct() {
					loginDialog.loginButton.setEnabled(true);
					loginDialog.updateMessage(e.getLocalizedMessage());
					return null;
				}
			};
			worker.start();

			throw new LoginException(e.getLocalizedMessage());
		}
		// catch (UserException e) {
		// throw new LoginException(e.getLocalizedMessage());
		// } catch (UserException e) {
		// throw new LoginException(e.getLocalizedMessage());
		// }
	}

}
