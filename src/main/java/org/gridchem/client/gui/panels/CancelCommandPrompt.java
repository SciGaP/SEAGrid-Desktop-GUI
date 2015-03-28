/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Sep 24, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.panels;

import org.gridchem.client.GridChem;
import org.gridchem.client.SwingWorker;

import com.asprise.util.ui.progress.ProgressDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Generic panel containing an indeterminate progress bar and 
 * providing the functionality to cancel a swing worker by 
 * invoking the "interrupt" method.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class CancelCommandPrompt  {
    
    private int increments;
    private ProgressDialog progressDialog;
    private SwingWorker worker;
    
    
    /**
     * When the worker needs to update the GUI we do so by queuing
     * a Runnable for the event dispatching thread with 
     * SwingUtilities.invokeLater().  In this case we're 
     * setting the progress bar to display incremental advances
     * to monitor file downloads.
     */
    public CancelCommandPrompt(final Component callingWindow, 
            final String title, final String message, 
            final int increments, final SwingWorker worker) {
        
        this.increments = increments;
        
        this.worker = worker;
        
        System.out.println("Invoked in event dispatch thread " + SwingUtilities.isEventDispatchThread());
        
        progressDialog = 
            new ProgressDialog(callingWindow,title);
        
        progressDialog.beginTask(message, increments, true);
             
//        
//        try {
        
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
    }
    
    public void finished() {
        Runnable doFinished = new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.finished();
                }
            }
        };
        
        SwingUtilities.invokeLater(doFinished);
    }
    
    /**
     * When the worker needs to update the GUI we do so by queuing
     * a Runnable for the event dispatching thread with 
     * SwingUtilities.invokeLater().  In this case we're just
     * changing the progress bars value.
     */
    public void updateStatus() {
        Runnable doSetProgressBarValue;
        if (this.increments != -1) {
            doSetProgressBarValue = new Runnable() {
                public void run() {
                    progressDialog.worked(1);
                }
            };
            SwingUtilities.invokeLater(doSetProgressBarValue);
        } 
    }
    
    /**
     * When the worker needs to update the GUI we do so by queuing
     * a Runnable for the event dispatching thread with 
     * SwingUtilities.invokeLater().  In this case we're just
     * changing the status field's value.
     */
    public void updateStatus(final String message) {
        Runnable doSetProgressBarValue = new Runnable() {
            public void run() {
                progressDialog.setTaskName(message);
            }
        };
        SwingUtilities.invokeLater(doSetProgressBarValue);
    }
    
    
    
    
    public void startSubTask(final String message, final int i) {
        increments = i;
        
        Runnable doSetProgressBarValue = new Runnable() {
            public void run() {
                progressDialog.beginSubTask(message, i);
            }
        };
        
        SwingUtilities.invokeLater(doSetProgressBarValue);
    }
    
    public void updateSubStatus(final String message) {
        Runnable doSetProgressBarValue = new Runnable() {
            public void run() {
                progressDialog.setSubTaskName(message);
            }
        };
        
        SwingUtilities.invokeLater(doSetProgressBarValue);
    }
    
    public void updateSubStatus() {
        Runnable doSetProgressBarValue = new Runnable() {
            public void run() {
                progressDialog.subWorked(1);
            }
        };
        
        SwingUtilities.invokeLater(doSetProgressBarValue);
    }
    
    public boolean isCancelled() {
//        return progressDialog.isCanceled();
        return false;
    }
   
}
