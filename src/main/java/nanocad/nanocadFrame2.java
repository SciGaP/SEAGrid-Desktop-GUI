/*
 * Indentation is four.
 * This file does NOT use tabs.
 * Set your development tools appropriately.
 */

/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software") to deal with the Software without
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

/*
 * This class needs work: 
 * document its purpose - started
 * eliminate the public data members
 * eliminate or at least explain why code is commented out - started
 * Scott Brozell Sep 20, 2006
 */

/**
   Apparently, this class houses all GUI elements related to the
   Nanocad molecular editor.  In particular,
   the actual Nanocad window is an instance of newNanocad,
   a progress dialog box is an instance of JDialog (disabled on sep 27, 2006),
   the loading of startup files is spawned off to an instance of NanocadTask
   which affects the contents and lifetime of the progress dialog box,
   etc.

   @version $Id: nanocadFrame2.java,v 1.4 2007/11/16 20:11:31 srb Exp $
   @see newNanocad
   @see NanocadTask
*/


package nanocad;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import org.gridchem.client.Invariants;
import org.gridchem.client.NanocadTask;
import org.gridchem.client.Trace;
import org.gridchem.client.gui.panels.*;
import org.gridchem.client.gui.panels.myccg.job.JobPanel;


public class nanocadFrame2 extends JFrame implements ActionListener
{
        
    public boolean handleEvent( Event e)
    {
        if(e.id == Event.WINDOW_DESTROY)
        {
            dispose();
            System.exit(0);
            return true;
        }
        else
            return false;
    }

    public newNanocad nano;
    
    //lixh_5/2/05 
    public static JProgressBar progressBar;
    public static JLabel progressLabel;
    public static NanocadTask nanocadTask;
    public static JDialog progressDialog;
    private javax.swing.Timer timer;
    private Container progresscp;
    
    public nanocadFrame2()
    {
        super("Nanocad"+" - "+JobPanel.jobName);
        Trace.entry();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
/*        addWindowListener( new java.awt.event.WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
*/        
        

//        JMenu fileMenu = new JMenu("File");
//        JMenuItem exitMenuItem = new JMenuItem("Close");
//        fileMenu.add(exitMenuItem);
//        exitMenuItem.addActionListener (this);

        JMenuBar mb = new JMenuBar();
//        mb.add(fileMenu);

//        frame.setJMenuBar(mb);
        setJMenuBar(mb);
        
        
//         lixh_5/2/05
        nanocadTask = new NanocadTask(null);
        // Disabled the progress bar because it was unnecessary as there is no 
        // longer unzipping of files before opening Nanocad; kkotwani, sep 27 2006.
        //progressLabel = new JLabel("Progress: ");
        //progressBar = new JProgressBar(0, nanocadTask.getLengthOfTask());
        //progressBar.setStringPainted(true);
        JPanel progressPane = new JPanel(); 
        //progressDialog = new JDialog(this,"Loading...");
        progressPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        //progresscp = progressDialog.getContentPane();
        //progresscp.add("Center", progressPane);
        //progressDialog.setSize(300,95);
        //progressPane.add(progressLabel);
        //progressPane.add(progressBar);
        //progressBar.setIndeterminate(true);
        //progressDialog.setVisible(true);
        timer = getNanocadTimer();
        nanocadTask.go();
        timer.start();        
        
        nano = new newNanocad();
    
//        frame.getContentPane().add(nano, BorderLayout.CENTER);
        getContentPane().add(nano, BorderLayout.CENTER);
//        frame.setSize(850,850);
        setSize(750,600);
//        frame.setVisible(true);

//        WaltzPanel test = new WaltzPanel();
//        add(test, BorderLayout.CENTER);
        nano.init();
        nano.start();
//        frame.setVisible(true);
        setVisible(true);
//        frame.repaint();
//        repaint();

    Trace.exit();
 
    }

    public void actionPerformed(ActionEvent evt)
    {

        if (evt.getID() == java.awt.Event.WINDOW_DESTROY)
            System.exit(0);
        if (evt.getSource() instanceof MenuItem)
        {
            String menuLabel = ((MenuItem)evt.getSource()).getLabel();

            if(menuLabel.equals("Close"))
            {
//                dispose();
//                System.exit(0);
//                frame.setVisible(false);
                setVisible(false);
            } 
        }
    } 
    
    private javax.swing.Timer getNanocadTimer() {
        return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //progressBar.setValue(0);
                Trace.note("nanocadTask.current = " + nanocadTask.getCurrent() );
                if (nanocadTask.done()) {
                    timer.stop();
                    System.err.println("nanocadTask is done");
                    Toolkit.getDefaultToolkit().beep();
                    //progressLabel.setText("Progress: "); // lixh_add_2_9
                    //progressDialog.setVisible(false);
                }
            }
        });
    }
}


