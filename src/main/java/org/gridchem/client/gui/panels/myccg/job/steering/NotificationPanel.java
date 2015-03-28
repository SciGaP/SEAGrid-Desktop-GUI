/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Mar 19, 2007
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gridchem.client.util.Env;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.NotificationBean;
import org.gridchem.service.model.enumeration.JobStatusType;
import org.gridchem.service.model.enumeration.NotificationType;

/**
 * Panel to view and edit the notifications associated with a job.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class NotificationPanel extends JPanel implements ActionListener {
    
    private static JComboBox typeCombo;
    private static JComboBox statusCombo;
    
    protected boolean changedNotification = false;
    
    private JobBean job;
    protected ArrayList<NotificationBean> notifications;
    
    public NotificationPanel(JobBean job) {
        
        super();
        
        this.job = job;
        
        notifications = new ArrayList<NotificationBean>();
        
//        for (NotificationBean n: job.getNotifications()) {
//            NotificationBean nCopy = new NotificationBean(n.getType(),n.getStatus(),n.getMessage());
//            notifications.add(nCopy);
//        }
        
        init();
        
    }
    
    private void init() {
    
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        
        add(new JLabel("<html><div align='justify'>" + 
                "<p>This property determines the notifications     </p>" +
                "<p>received for this job. Past notifications</p>" + 
                "<p align='left'>will be ignored.</p></div></html>"),c);
        
        int index = 1;
        
        JPanel dataPanel = new JPanel(new GridLayout(notifications.size(),1));
        dataPanel.setBorder(new RoundBorder(RoundBorder.LOWERED,10));
//        dataPanel.setBackground(Color.LIGHT_GRAY);
        
        for (NotificationBean notif: notifications) {
            
            typeCombo = new JComboBox(NotificationType.values());
            typeCombo.setSelectedItem(notif.getType());
            typeCombo.setName("type" + index);
            typeCombo.addActionListener(this);
            
            statusCombo = new JComboBox(JobStatusType.values());
            statusCombo.setSelectedItem(notif.getStatus());
            statusCombo.setName("status" + index);
            statusCombo.addActionListener(this);
            
            JButton deleteButton = new JButton(
                    new ImageIcon(Env.getApplicationDataDir() + 
                            "/images/navigation/button-cancel.png"));
            deleteButton.setName(index + "");
            deleteButton.addActionListener(this);
            
            JPanel nItem = new JPanel();
            nItem.setName(index+"");
            nItem.add(typeCombo);
            nItem.add(statusCombo);
            nItem.add(deleteButton);
            
            dataPanel.add(nItem);
            index++;
        }
        
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = 1;
        add(dataPanel,c);
        
    }
    
    protected void removeNotification(int row) {
        
        changedNotification = true;
        
//        remove(row);
        
        notifications.remove(row);
        
        removeAll();
        
        init();
        
    }
    
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed");
        if (e.getSource() instanceof JButton ) {
            System.out.println("Delete button pressed");
            removeNotification(new Integer(((JButton)e.getSource())
                .getName()).intValue());
            
        } else if (e.getSource() instanceof JComboBox) {
            if (((JComboBox)e.getSource()).getName().startsWith("type")) {
                System.out.println("new message type selected");
                int index = new Integer(((JComboBox)e.getSource())
                        .getName().substring(4)).intValue() - 1;
                
                System.out.println("Selected notification " + index + 
                        " " + notifications.get(index).toString());
                
                notifications.get(index).setType(
                        ((NotificationType)((JComboBox)e.getSource()).getSelectedItem()));
                System.out.println("Updated notification " + index + 
                        " to " + notifications.get(index).toString());
            } else if (((JComboBox)e.getSource()).getName().startsWith("status")) {
                System.out.println("new status type selected");
                int index = new Integer(((JComboBox)e.getSource())
                        .getName().substring(6)).intValue() - 1;
                
                System.out.println("Selected notification " + index + 
                        " " + notifications.get(index).toString());
                
                notifications.get(index).setStatus(
                        ((JobStatusType)((JComboBox)e.getSource()).getSelectedItem()));
                
                System.out.println("Updated notification " + index + 
                        " to " + notifications.get(index).toString());
                
            }
        }
        changedNotification = true;
        
    }
    
    public HashSet<NotificationBean> getNotifications() {
        HashSet<NotificationBean> nots = new HashSet<NotificationBean>();
        nots.addAll(notifications);
        return nots;
    }
    
    public boolean isChanged() {
        boolean missing = true;
        // disabled notifications for migration.
//        if (changedNotification) {
//            for(NotificationBean n: notifications) {
//                System.out.println("Checking for: " + n.toString());
//                for(NotificationBean jn: job.getNotifications()) {
//                    if (jn.equals(n)) {
//                        System.out.println("Found matching notification: " + jn.toString());
//                        missing = false;
//                        break;
//                    } else {
//                        missing = true;
//                    }
//                }
//                
//                if (missing) {
//                    return true;
//                }
//            }
//        }
//        return changedNotification;
        return false;
    }
}
