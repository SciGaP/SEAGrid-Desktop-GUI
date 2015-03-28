/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory; 
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;


/** 
 * This is the GUI for the Save Data window.
 *
 * @author Xiaohai Li
 *
 */


public class SaveDataWindow {

    public static JFrame frame;
    public static InternalStuff4 si;
    
    public SaveDataWindow() {

        //JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Save Data");
        si = new InternalStuff4();
        si.setBorder(BorderFactory.createEmptyBorder(15,15,10,15));
        frame.getContentPane().add(si);
        frame.pack();
    
        // Centering the frame on the screen
        Toolkit kit = frame.getToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension windowSize = frame.getSize();
        int windowWidth = windowSize.width;
        int windowHeight = windowSize.height;
        int upperLeftX = (screenWidth - windowWidth)/2;
        int upperLeftY = (screenHeight - windowHeight)/2;   
        frame.setLocation(upperLeftX, upperLeftY);
    
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    public static void main(String[] args) {

        Settings settings = Settings.getInstance();
        //JFrame.setDefaultLookAndFeelDecorated(true);
        SaveDataWindow sdw = new SaveDataWindow();
        SaveDataWindow.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
   

class InternalStuff4 extends JComponent {

    JButton saveButton;
    JButton cancelButton;
    JButton browseButton;
    
    JTextField setPlace;
    
    JCheckBox file1CB;
    JCheckBox file2CB;
    
    String selectedDir;
    
    public InternalStuff4() {        

        //Border
        Border leBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border eBorder1 = BorderFactory.createEmptyBorder(0,0,5,0);
        Border eBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
        Border eBorder3 = BorderFactory.createEmptyBorder(10,0,5,0);
        
        //instructionPane
        JLabel instructionLabel = new JLabel(
                "Choose the flowing files to save to another place");
        JPanel instructionPane = new JPanel();
        instructionPane.setLayout(new GridLayout(1,1));
        instructionPane.setBorder(eBorder1);
        instructionPane.add(instructionLabel);
        
        //filesPane
        file1CB = new JCheckBox("qcrjm.hist");
        Preferences preferences = Preferences.getInstance();
        file2CB = new JCheckBox(preferences.getLocalPrefFilename());
        JPanel filesPane = new JPanel();
        filesPane.setLayout(new GridLayout(2,1));
        filesPane.setBorder(BorderFactory.createCompoundBorder(leBorder,eBorder2));
        filesPane.add(file1CB);
        filesPane.add(file2CB);
        
        //choosePane
        setPlace = new JTextField(25);
        String defaultdir = System.getProperty("user.home");
        setPlace.setText(defaultdir);
        browseButton = new JButton("Browse...");
        JPanel choosePane = new JPanel();
        //choosePane.setPreferredSize(new Dimension(400,5));
        choosePane.setLayout(new BorderLayout());
        choosePane.add(setPlace,BorderLayout.WEST);
        choosePane.add(browseButton,BorderLayout.EAST);
        choosePane.setBorder(eBorder2);
        JPanel choosePaneOuter = new JPanel();
        choosePaneOuter.add(choosePane);
        TitledBorder chooseOuterTitled = BorderFactory.createTitledBorder(
                leBorder, "Choose a place to save", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION);
        choosePaneOuter.setBorder(BorderFactory.createCompoundBorder(
                eBorder3,chooseOuterTitled));

        //buttonPane
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new GridLayout(1,2,5,0));
        buttonBox.add(saveButton);
        buttonBox.add(cancelButton);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(buttonBox);

        //Add all action listeners
        ActionListener bl = new ButtonListener();
        cancelButton.addActionListener(bl);
        saveButton.addActionListener(bl);
        browseButton.addActionListener(bl);

        //Final Layout
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //this.setPreferredSize(new Dimension(400,200));
        this.add(instructionPane);
        this.add(filesPane);
        this.add(choosePaneOuter);
        this.add(buttonPane);
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {

            if (evt.getSource() == cancelButton) {
                SaveDataWindow.frame.setVisible(false);
            }
            else if (evt.getSource() == saveButton) {
                if (!file1CB.isSelected() && !file2CB.isSelected()) {
                    JOptionPane.showMessageDialog(
                            SaveDataWindow.si,
                            "Please select a file...",
                            "Save Data",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
                else {
                    try {
                        String fileSeparator;
                        String toFileName;
                        String fromFileName;
                    
                        fileSeparator = System.getProperty("file.separator");
                    
                        if (file1CB.isSelected()) {
                            fromFileName = Settings.histFilename; 
                            toFileName = setPlace.getText() + fileSeparator +
                                         Settings.localhist;
                            doCopy(fromFileName,toFileName);
                        }
                        if (file2CB.isSelected()) {
                            Preferences preferences = Preferences.getInstance();
                            toFileName = setPlace.getText() + fileSeparator +
                                         preferences.getLocalPrefFilename();
                            preferences.makeLocalCopy(toFileName);
                        }
                        SaveDataWindow.frame.setVisible(false);
                    }
                    catch (IOException ioe) {
                        System.err.println("SaveDataWindow: Error copying files!");
                        System.err.println(ioe.toString());
                        ioe.printStackTrace();
                    }
                }
            }
            else if (evt.getSource() == browseButton) {
                
                JFileChooser chooser = new JFileChooser();
                int retVal = chooser.showOpenDialog(SaveDataWindow.si);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    selectedDir = chooser.getSelectedFile().getPath(); 
                    setPlace.setText(selectedDir);
                }
            }
        }
    }  // private class ButtonListener implements ActionListener

    /** 
     * Generic file copy.
     *
     * @param from the name of the source file.
     * @param to   the name of the destination file.
     * @throws IOException
     * @author Scott Brozell < srb [at] osc [dot] edu >
     */
    private void doCopy(String from, String to) throws IOException {

        final int BUFFER_SIZE = 1024;
        if ( ! to.equals(from) ) {
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ( (len = in.read(buffer)) > 0 ) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        }
    }

}  // class InternalStuff4 extends JComponent

