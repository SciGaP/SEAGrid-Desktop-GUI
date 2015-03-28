/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 16, 2007
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

package org.gridchem.client.gui.panels.myccg.resource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gridchem.client.util.Env;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.SiteBean;

/**
 * Informative panel containing information about the Site associated with
 * a given ComputeBean object.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class SiteInfoPanel extends JPanel {

    protected Box box;
    
    JLabel nameLabel;
    JLabel acronymLabel;
    JLabel imageLabel;
    
    private SiteBean site = null;
    
    public SiteInfoPanel() {
        
        super();
        
        this.site = null;
        
        load();
        
    }
    
    public SiteInfoPanel(ComputeBean hpc) {
        
        super();
        
        this.site = hpc.getSite();
        
        load(site);
        
    }
    
    private void load(SiteBean site) {
        
        removeAll();
        
        setLayout(new GridLayout(2,1));
        
        box = Box.createVerticalBox();
        nameLabel = new JLabel();
        imageLabel = new JLabel();
        // include the logo of the site with it's acronmym below it
        imageLabel.setIcon(new ImageIcon(Env.getImagesDir() + 
                    "/logos/" + site.getAcronym() + ".jpg"));
        
        acronymLabel = new JLabel(site.getAcronym());
        box.add(imageLabel);
        box.add(acronymLabel);
        
        // add the full site name
        
        
        String htmlText = "<html><p>" + site.getName() + "</p><p>" + 
            site.getDescription() + "</p></html>";
        
        nameLabel.setText(htmlText);
        
        // setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(box);
        add(nameLabel);
        
        revalidate();
        repaint();
    }
    
    private void load() {
        
        removeAll();
        
        setLayout(new GridLayout(2,1));
        
        box = Box.createVerticalBox();
        nameLabel = new JLabel();
        imageLabel = new JLabel();
        // include the logo of the site with it's acronmym below it
        imageLabel.setIcon(new ImageIcon(Env.getImagesDir() + "/logos/CCG.jpg"));
        
        imageLabel.setSize(new Dimension(88,56));
        acronymLabel = new JLabel("CCG");
        box.removeAll();
        box.add(imageLabel);
        box.add(acronymLabel);
        
        // add the full site name
        String htmlText = "<html><p>Computational Chemistry Grid </p>" + 
            "<p>The Computational Chemistry Grid is a virtual organization</p>" +
            "<p>that provides access to high performance computing resources</p>" +
            "<p>for computational chemistry with distributed support and services,</p>" +
            "<p>intuitive interfaces and measurable quality of service.</p></html>";
        
        nameLabel.setText(htmlText);
        
        add(box);
        add(nameLabel);
        setBackground(Color.WHITE);
        
        revalidate();
        repaint();
        
    }
    
    public SiteBean getSite() {
        
        return site;
        
    }
    
    public void setSite(SiteBean siteBean) {
        
        this.site = siteBean;
        
        load(site);
        
    }
    
    public void clearSite() {
        
        this.site = null;
        
        load();
        
    }
    
    
}
