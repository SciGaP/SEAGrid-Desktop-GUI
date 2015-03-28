/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Mar 20, 2007
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

package org.gridchem.client.gui.panels.myccg.job.steering;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gridchem.service.beans.JobBean;

/**
 * Panel to turn checkpointing on and off for a given job.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class CheckpointPanel extends JPanel {

    private JobBean job;
    
    private JCheckBox checkBox;
    
    private boolean changed = false;
    
    public CheckpointPanel(JobBean job) {
        super();
        
        this.job = job;
        
        init();
    }
    
    private void init() {
        
        setLayout(new GridBagLayout());
//        setBorder(new TitledBorder(new EtchedBorder(),""));
       
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        
        add(new JLabel("<html><div align='justify'>" +
                "<p>This property detrmines whether or not the</p>" +
                "<p align='left'>file is checkpointable</p>" +
                "</div></html>"),c);
        
        checkBox = new JCheckBox("Checkpoint");
//        checkBox.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent arg0) {
//                
//            }
//            
//        })
        checkBox.setSelected(job.isCheckpointable());
        
        JPanel dataPanel = new JPanel(new GridLayout(1,1));
        dataPanel.setBorder(new RoundBorder(RoundBorder.LOWERED,10));
//        dataPanel.setBackground(Color.LIGHT_GRAY);
        dataPanel.add(checkBox, BorderLayout.LINE_START);
        
        c.gridy = 1;
        c.fill = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(dataPanel,c);
        
    }
    
    public boolean isChanged() {
        System.out.println(true == true);
        return job.isCheckpointable() == checkBox.isSelected();
    }
    
    public boolean isCheckpointable() {
        return checkBox.isSelected();
    }
    
    

    
    
    
    
    
    

    
    
    
    
    
    

    
    
    
    
    
    

    
    
    
    
    
    

    
    
    
    
    
    
    
}
