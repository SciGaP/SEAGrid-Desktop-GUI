/* A panel for controlling the format of a plotter.

 Copyright (c) 1998-2005 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

 PT_COPYRIGHT_VERSION_2
 COPYRIGHTENDKEY
 */
package org.gridchem.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.lang.Math;
import java.lang.Number ;

import javax.swing.JPanel;

import org.gridchem.client.ComponentDialog;
import org.gridchem.client.Query;
import org.gridchem.client.QueryListener;

import org.gridchem.client.PlotPoint;

//////////////////////////////////////////////////////////////////////////
//// Modified version of PlotFormatter for SpectraViewer (Feb.2007) JK


/**

 PlotFormatter is a panel that controls the format of a plotter object
 passed to the constructor.

 @see Plot
 @see PlotBox
 @author Edward A. Lee
 @version $Id: SpectraPlotFormatter.java,v 1.2 2007/11/16 20:11:40 srb Exp $
 @since Ptolemy II 1.0
 @Pt.ProposedRating Yellow (eal)
 @Pt.AcceptedRating Red (cxh)
 */
public class SpectraPlotFormatter extends JPanel {
    /** Construct a plot formatter for the specified plot object.
     */
    public SpectraPlotFormatter(SpectraPlotBox plot) {
    	
        super();
        _plot = plot;

        setLayout(new BorderLayout());
        _wideQuery = new Query();
        add(_wideQuery, BorderLayout.WEST);
        _narrowQuery = new Query();
        add(_narrowQuery, BorderLayout.EAST);

        // Populate the wide query.
//        _wideQuery.setTextWidth(20);
//        _originalTitle = plot.getTitle();
//        _wideQuery.addLine("title", "Title", _originalTitle);
//        _originalXLabel = plot.getXLabel();
//        _wideQuery.addLine("xlabel", "X Label", _originalXLabel);
//        _originalYLabel = plot.getYLabel();
//        _wideQuery.addLine("ylabel", "Y Label", _originalYLabel);
//        _originalXRange = plot.getXRange();
//        _wideQuery.addLine("xrange", "X Range", "" + _originalXRange[0] + ", "
//                + _originalXRange[1]);

        if (plot instanceof SpectraPlot) {
 
            if (plot instanceof SpectraPlot) {
                String currentSpectraType = ((SpectraPlot) plot).getSpectraType();
                _wideQuery.addRadioButtons("spectra_type", "Spectra", ((SpectraPlot) plot).spectraTypes , currentSpectraType);
            
            
                if(currentSpectraType == ((SpectraPlot) plot).spectraTypes[0]){
                	_originalSpectratype = 0;
                }else  if(currentSpectraType == ((SpectraPlot) plot).spectraTypes[1]){
                	_originalSpectratype = 1;
                }else  if(currentSpectraType == ((SpectraPlot) plot).spectraTypes[2]){
                	_originalSpectratype = 2;
                }else  if(currentSpectraType == ((SpectraPlot) plot).spectraTypes[3]){
                	_originalSpectratype = 3;
                }
            }
        }
        
        
        
          _originalXRange = plot.getXRange();
          _wideQuery.addLine("xmin", "Freq. min", "" + _originalXRange[0]);
          _wideQuery.addLine("xmax", "Freq. max", "" + _originalXRange[1]);
          
          String[] isLineshape ={"Yes", "No"};
          boolean curr_isLineshape = ((SpectraPlot) _plot).getLineshapeState();
          if ( curr_isLineshape == false){
        	  _wideQuery.addRadioButtons("spectraline", "Spectral Lineshape", isLineshape, isLineshape[1]);
          }else {
        	  _wideQuery.addRadioButtons("spectraline", "Spectral Lineshape", isLineshape, isLineshape[0]);
          }
         
          
          String[] linefunc = {"Gaussian","Lorentzian"};
          int curr_linefunc = ((SpectraPlot) _plot).getSpectralinetype();
          if (curr_linefunc == 0){
        	  _wideQuery.addRadioButtons("linefunction", "Lineshape Function",linefunc, linefunc[0]);
          }else if (curr_linefunc == 1){
              _wideQuery.addRadioButtons("linefunction", "Lineshape Function",linefunc, linefunc[1]);
          }
          
          double curr_lw = ((SpectraPlot) _plot).getSpectralinewidth();
          _wideQuery.addLine("linewidth", "Linewidth (FWMH) (1/cm) ", ((Double) curr_lw).toString() );
          
          
          
 //               _originalYRange = plot.getYRange();
 //       _wideQuery.addLine("yrange", "Y Range", "" + _originalYRange[0] + ", "
 //               + _originalYRange[1]);

 //       String[] marks = { "none", "points", "dots", "various", "pixels" };
        _originalMarks = "none";

 //       if (plot instanceof SpectraPlot) {
 //           _originalMarks = ((SpectraPlot) plot).getMarksStyle();
 //           _wideQuery.addRadioButtons("marks", "Marks", marks, _originalMarks);
 //       }

        
 /*       _originalXTicks = plot.getXTicks();
        _originalXTicksSpec = "";

        if (_originalXTicks != null) {
            StringBuffer buffer = new StringBuffer();
            Vector positions = _originalXTicks[0];
            Vector labels = _originalXTicks[1];

            for (int i = 0; i < labels.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }

                buffer.append(labels.elementAt(i).toString());
                buffer.append(" ");
                buffer.append(positions.elementAt(i).toString());
            }

            _originalXTicksSpec = buffer.toString();
        }

        _wideQuery.addLine("xticks", "X Ticks", _originalXTicksSpec);

        _originalYTicks = plot.getYTicks();
        _originalYTicksSpec = "";

        if (_originalYTicks != null) {
            StringBuffer buffer = new StringBuffer();
            Vector positions = _originalYTicks[0];
            Vector labels = _originalYTicks[1];

            for (int i = 0; i < labels.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }

                buffer.append(labels.elementAt(i).toString());
                buffer.append(" ");
                buffer.append(positions.elementAt(i).toString());
            }

            _originalYTicksSpec = buffer.toString();
        }

        _wideQuery.addLine("yticks", "Y Ticks", _originalYTicksSpec);
*/
        _originalGrid = plot.getGrid();
        _narrowQuery.addCheckBox("grid", "Grid", _originalGrid);
        _originalStems = false;
        _originalConnected = null;

/*        if (plot instanceof SpectraPlot) {
            _originalStems = ((SpectraPlot) plot).getImpulses();
            _narrowQuery.addCheckBox("stems", "Stems", _originalStems);
            _saveConnected();
            _narrowQuery.addCheckBox("connected", "Connect", ((SpectraPlot) plot)
                    .getConnected());
        }
*/
        _originalColor = plot.getColor();
        _narrowQuery.addCheckBox("color", "Use Color", _originalColor);

        
        
        
        // FIXME: setXLog() and setYLog() cause problems with
        // dropped data if they are toggled after data is read in.
        // This is because the log axis facility modifies the datasets
        // in addPlotPoint() in Plot.java.  When this is fixed
        // we can add the XLog and YLog facility to the Format menu
        //
        // _originalXLog = plot.getXLog();
        //_narrowQuery.addCheckBox("xlog", "X Log", _originalXLog);
        //if (_originalXTicks != null) {
        //    _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        //}
        // _originalYLog = plot.getYLog();
        //_narrowQuery.addCheckBox("ylog", "Y Log", _originalYLog);
        //if (_originalYTicks != null) {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        //}
        // Attach listeners.
        _wideQuery.addQueryListener(new QueryListener() {
            public void changed(String name) {
            	
            	String x_min = "" + _plot.getXRange()[0];
            	String x_max = "" + _plot.getXRange()[1];
            	
                if (name.equals("title")) {
                    _plot.setTitle(_wideQuery.getStringValue("title"));
                } else if (name.equals("xlabel")) {
                    _plot.setXLabel(_wideQuery.getStringValue("xlabel"));
                } else if (name.equals("ylabel")) {
                    _plot.setYLabel(_wideQuery.getStringValue("ylabel"));
                } else if (name.equals("xrange")) {
                    _plot.read("XRange: "+ _wideQuery.getStringValue("xrange"));
                } else if (name.equals("xticks")) {
                    String spec = _wideQuery.getStringValue("xticks").trim();
                    _plot.read("XTicks: " + spec);

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // if (spec.equals("")) {
                    //    _narrowQuery.setEnabled("xlog", true);
                    // } else {
                    //    _narrowQuery.setBoolean("xlog", false);
                    //    _narrowQuery.setEnabled("xlog", false);
                    // }
                } else if (name.equals("yticks")) {
                    String spec = _wideQuery.getStringValue("yticks").trim();
                    _plot.read("YTicks: " + spec);

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // if (spec.equals("")) {
                    //    _narrowQuery.setEnabled("ylog", true);
                    // } else {
                    //    _narrowQuery.setBoolean("ylog", false);
                    //    _narrowQuery.setEnabled("ylog", false);
                    // }
                } else if (name.equals("yrange")) {
                    _plot.read("YRange: "
                                    + _wideQuery.getStringValue("yrange"));
                } else if (name.equals("marks")) {
                    ((SpectraPlot) _plot).setMarksStyle(_wideQuery.getStringValue("marks"));
                } else if (name.equals("xmin")) {
                	x_min = _wideQuery.getStringValue("xmin");
                } else if (name.equals("xmax")) {
                	x_max = _wideQuery.getStringValue("xmax");
                } else if (name.equals("spectra_type")) {
                	
                	((SpectraPlot) _plot).setSpectraType(_wideQuery.getStringValue("spectra_type"));
                	
                } else if (name.equals("spectraline")) {
                	String ched_value = _wideQuery.getStringValue("spectraline") ;
                	if (ched_value == "Yes"){
                		((SpectraPlot) _plot).setLineshapeState(true);
                	}else{
                		((SpectraPlot) _plot).setLineshapeState(false);
                	}
                } else if (name.equals("linefunction")) {
                		String ched_value2 = _wideQuery.getStringValue("linefunction");
                		if (ched_value2 == "Gaussian"){
                			((SpectraPlot) _plot).setSpectralinetype(0);
                		}else if (ched_value2 == "Lorentzian"){
                			((SpectraPlot) _plot).setSpectralinetype(1);
                		}
           
                } else if (name.equals("linewidth")) {
                	double ched_value3 = (new Double(_wideQuery.getStringValue("linewidth"))).doubleValue();
                	
                	((SpectraPlot) _plot).setSpectralinewidth(ched_value3);
                	
                	
                }
                
                
                _plot.read("XRange: " + x_min + "," + x_max);
                
                _plot.repaint();
            }
        });

       _narrowQuery.addQueryListener(new QueryListener() {
            public void changed(String name) {
                if (name.equals("grid")) {
                    _plot.setGrid(_narrowQuery.getBooleanValue("grid"));
                } else if (name.equals("stems")) {
                    ((SpectraPlot) _plot).setImpulses(_narrowQuery
                            .getBooleanValue("stems"));
                    _plot.repaint();
                } else if (name.equals("color")) {
                    _plot.setColor(_narrowQuery.getBooleanValue("color"));

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // } else if (name.equals("xlog")) {
                    //    _plot.setXLog(_narrowQuery.getBooleanValue("xlog"));
                    // } else if (name.equals("ylog")) {
                    //    _plot.setYLog(_narrowQuery.getBooleanValue("ylog"));
                } else if (name.equals("connected")) {
                    _setConnected(_narrowQuery.getBooleanValue("connected"));
                }

                _plot.repaint();
            }
        });
        
    }

    ///////////////////////////////////////////////////////////////////////
    /////           Spectralineshpe (JK) //////////////
    
    
    /*

    (NOTE) FWHM = 2sqrt(2*ln2)*sigma  with Gaussian exp(-x*x/(2*sigma*sigma))
           FWHM = gamma with Lorentzian 0.5*gamma/(x*x + (0.5*gamma)*(0.5*gamma)) 
 """
  */
 
 	
 	private double Gaussian(double x, double xpos, double lw){
 		
 		double sig = lw/2*Math.sqrt(2*Math.log(2));
 		
 		double y = Math.exp(-(x-xpos)*(x-xpos)/2./sig/sig);
 		
 		return y; 
 		
 		
 	}
 	
 	private double Lorentzian(double x, double xpos, double lw){
 		
 		double gamma = lw;
  		
 		double y = 0.5*gamma/((x-xpos)*(x-xpos)+0.5*gamma*gamma);
 		
 		return y;
 		
 		
 	}
 	

 	public void drawSpectraLineshape(int itype){  // itype = 0 gaussian , itype =1 lorentzian  at the moment
 		
 		boolean first = true; 

 		int dataset = 1 ; // always 
 		int maindataset = 0 ; // alwyas
 		int numPoints = 5000 ;  // total number of the points for line shape (assume 1 to 5000 cm^-1 as a default
 		double lineScale = 1;  // scaled with the highest mode intensity
 		
 	    Vector pts = (Vector) ((SpectraPlot) _plot)._points.elementAt(maindataset);
        
        double[] tmpXrange = new double[2];
  /*      tmpXrange = _plot.getXRange();
        double xmin = tmpXrange[0];
        double xmax = tmpXrange[1] ;
    */
        double xmin = 0;
        double xmax = 4500;
        
        
        Vector<Integer> modesIntheRange = new Vector<Integer> ();
        
        
        for (int pointnum = 0; pointnum < pts.size(); pointnum++) {
     	    PlotPoint pt = (PlotPoint) pts.elementAt(pointnum);
     	    double x = pt.x;
     	    double y = pt.y;
     	    
     	    if ( (x > xmin) && (x < xmax )) {
     	    	modesIntheRange.addElement((Integer) pointnum);
     	    }
        }
        
        double xInterval=0.0 ; 
 	    if (xmax-xmin < 200){
 	    	numPoints = ((int) xmax - (int) xmin)*10 + 100 ; // 0.1 cm^-1 interval
 	    	xInterval = 0.1;
 	    }else{
 	    	numPoints = (int) xmax - (int) xmin + 100 ;   // 1 cm^-1 interval
 	    	xInterval = 1.0;
 	    }
        
        double[] yVal;
        double[] xVal;
        yVal = new double[numPoints];
        xVal = new double[numPoints];
        
        double tmpXval = xmin-50;
        
        int ipix = 0;
        while((tmpXval < xmax+50) && (ipix < numPoints)){
        	yVal[ipix] = 0.0;
    		tmpXval =  xmin-50 + xInterval*ipix ;   // x value corresponding each pixel of x axis
    		xVal[ipix] = tmpXval;
    		
        	for (int imode = 0 ; imode < modesIntheRange.size() ; imode++){
        		int ind = (modesIntheRange.elementAt(imode)).intValue();
        		PlotPoint pt = (PlotPoint) pts.elementAt(ind);
        		double xmode = pt.x ;
        		double ypick = pt.y ;
        		
        		if (itype == 0){	
        			yVal[ipix] = yVal[ipix]+ Gaussian(xVal[ipix], xmode, ((SpectraPlot) _plot).getSpectralinewidth() )*pt.y*lineScale ; 
        		}else if(itype == 1){
        			
        			yVal[ipix] = yVal[ipix]+ 5*Lorentzian(xVal[ipix], xmode, ((SpectraPlot) _plot).getSpectralinewidth() )*pt.y*lineScale ;  			
        		
        		// 5*scaling was used
        		
        		}
        		
        	}

    		ipix = ipix + 1;
    		
        	
        }
     
        	
 		((SpectraPlot) _plot).setImpulses(false, dataset);
 		
        for (ipix = 0 ; ipix <= numPoints;  ipix++) {
        
            ((SpectraPlot) _plot).addPoint(dataset, xVal[ipix] , yVal[ipix], !first);

            first = false;

        }
 		
 		
 	}

    
    
    
    
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Apply currently specified values to the associated plot.
     *  This requests a repaint of the plot.
     */
     
    
    // (JK) not sure it is safe
   public int getSpectraTypesIndex(String type){ 
	  
       int res = 0;
	   if (type == ((SpectraPlot) _plot).spectraTypes[0]){
		   res = 0;
	   }else if(type == ((SpectraPlot) _plot).spectraTypes[1]){
		   res =  1;
	   }else if(type == ((SpectraPlot) _plot).spectraTypes[2]){
		   res =  2;
	   }else if(type == ((SpectraPlot) _plot).spectraTypes[3]){
		   res = 3;
	   }
	   return res ;
   }
        
        
    public void apply() {
        // Apply current values.
//        _plot.setTitle(_wideQuery.getStringValue("title"));
//        _plot.setXLabel(_wideQuery.getStringValue("xlabel"));
//        _plot.setYLabel(_wideQuery.getStringValue("ylabel"));
//        _plot.read("XRange: " + _wideQuery.getStringValue("xrange"));
        
    	
    	_plot.read("XRange: " + _wideQuery.getStringValue("xmin") + "," + _wideQuery.getStringValue("xmax"));
        
        String dirName = ((SpectraPlot) _plot).dirName;
        
        
//        _plot.read("YRange: " + _wideQuery.getStringValue("yrange"));
//        _plot.setGrid(_narrowQuery.getBooleanValue("grid"));
//        _plot.setColor(_narrowQuery.getBooleanValue("color"));

        // FIXME: log axis format temporarily disable, see above.
        // _plot.setXLog(_narrowQuery.getBooleanValue("xlog"));
        // _plot.setYLog(_narrowQuery.getBooleanValue("ylog"));
        
        String changedSpectraType = ((SpectraPlot) _plot).getSpectraType();
        
        if (_originalSpectratype !=  getSpectraTypesIndex(changedSpectraType)) {

            File file = new File(dirName+ File.separator+ changedSpectraType+".plt");  
 
            try {
                    
                    ((SpectraPlot) _plot).clear(true);
                    
                    _plot.setTitle("Vibrational Spectra ("+changedSpectraType+")");
                    _plot.setXLabel("wavenumber (1/cm)");
                    _plot.setYLabel("Intensity");
 
                    ((SpectraPlot) _plot).setConnected(false,0);      // should be located before reading dataset
                    ((SpectraPlot) _plot).setImpulses(true);
                               
                    _plot.setXRange(0, 4500);
                    ((SpectraPlot) _plot).read(new FileInputStream(file));

                    _originalSpectratype = getSpectraTypesIndex(changedSpectraType) ;
                               
           } catch (FileNotFoundException ex) {

                      System.err.println("File not found: " + file + " : " + ex);
                               
                      ((SpectraPlot) _plot).setSpectraType("IR");
                      _plot.setXRange(0, 4500) ;

           } catch (IOException ex) {

                      System.err.println("Error reading input: " + file + " : " + ex);
                               
                      ((SpectraPlot) _plot).setSpectraType("IR");
                      _plot.setXRange(0, 4500) ;
                               
           }
           
           if(_originalSpectratype !=  getSpectraTypesIndex(changedSpectraType)) {  
        	   // in case of file io error, return to the previous one
        	   
               ((SpectraPlot) _plot).clear(true);
               String prevSpectraType = ((SpectraPlot) _plot).spectraTypes[_originalSpectratype];
               ((SpectraPlot) _plot).setSpectraType(prevSpectraType);
               
               
               _plot.setTitle("Vibrational Spectra "+"("+prevSpectraType+")");
               _plot.setXLabel("wavenumber (1/cm)");
               _plot.setYLabel("Intensity");

               ((SpectraPlot) _plot).setConnected(false,0);      // should be located before reading dataset
               ((SpectraPlot) _plot).setImpulses(true);
               _plot.setXRange(0, 4500) ;           
   
               try{
            	   ((SpectraPlot) _plot).read(new FileInputStream(dirName+File.separator+prevSpectraType+".plt"));
               
               } catch (FileNotFoundException ex) {

                   System.err.println("File not found: " + file + " : " + ex);
                            

               } catch (IOException ex) {

                   System.err.println("Error reading input: " + file + " : " + ex);
                            
               }
            	
           }
           
        }
 
   
        if ( ((SpectraPlot) _plot).getLineshapeState() == true){
        	// avoid the case when the line shape function is not fit into Xrange
        		double[] tmpXrange = new double[2]; 
        		
    /*    		tmpXrange = _plot.getXRange();
        		_plot.setXRange(tmpXrange[0]-50, tmpXrange[1]+50);
      */  		
        	
        		if( ((SpectraPlot) _plot).getSpectralinetype() == 0){  // means gaussian
     

                	((SpectraPlot) _plot).clear(1);  // just remove the previous one if any
                	
        			drawSpectraLineshape(0);
        			
        		}else if(((SpectraPlot) _plot).getSpectralinetype() == 1){   // means lorentzian

                	((SpectraPlot) _plot).clear(1);
        			drawSpectraLineshape(1);
        		}
        			
        		
        }else {
        	
        	((SpectraPlot) _plot).clear(1);
        	
        }

        
        
        
        
   /*     if (_plot instanceof SpectraPlot) {
            SpectraPlot cplot = (SpectraPlot) _plot;
            cplot.setMarksStyle(_wideQuery.getStringValue("marks"));
            cplot.setImpulses(_narrowQuery.getBooleanValue("stems"));
            _setConnected(_narrowQuery.getBooleanValue("connected"));
        }

     */   
        
        
        
        // FIXME: log axis format temporarily disable, see above.
        // String spec = _wideQuery.getStringValue("xticks").trim();
        // _plot.read("XTicks: " + spec);
        // if (spec.equals("")) {
        //    _narrowQuery.setEnabled("xlog", true);
        // } else {
        //    _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        // }
        // spec = _wideQuery.getStringValue("yticks").trim();
        // _plot.read("YTicks: " + spec);
        // if (spec.equals("")) {
        //    _narrowQuery.setEnabled("ylog", true);
        // } else {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        // }
        
 /*       double[] tmpYrange = new double[2];
        tmpYrange = _plot.getYRange();
        String ptType = null ;
        ptType = ((SpectraPlot) _plot).getSpectraType();
        if ( (ptType == "IR")|| (ptType == "RAMAN")){
        	_plot.setYRange(0, tmpYrange[1]);
        }
   */     
        _plot.repaint();
    }

    /** Open a format control window as a top-level, modal dialog.
     */
    public void openModal() {
        String[] buttons = { "Apply", "Cancel" };

        // NOTE: If the plot is in a top-level container that is a Frame
        // (as opposed to an applet), then tell the dialog that the Frame
        // owns the dialog.
        Container toplevel = _plot.getTopLevelAncestor();
        Frame frame = null;

        if (toplevel instanceof Frame) {
            frame = (Frame) toplevel;
        }

        ComponentDialog dialog = new ComponentDialog(frame, "Set plot format",
                this, buttons);

        if (dialog.buttonPressed().equals("Apply")) {
            apply();
        } else {
            restore();
        }
    }

    /** Restore the original configuration of the plot, and request a
     *  a redraw.
     */
    public void restore() {
        // Restore _original values.
        _plot.setTitle(_originalTitle);
        _plot.setXLabel(_originalXLabel);
        _plot.setYLabel(_originalYLabel);
        _plot.setXRange(_originalXRange[0], _originalXRange[1]);
        _plot.setYRange(_originalYRange[0], _originalYRange[1]);
        _plot.setGrid(_originalGrid);
        _plot.setColor(_originalColor);

        // FIXME: log axis format temporarily disable, see above.
        // _plot.setXLog(_originalXLog);
        // _plot.setYLog(_originalYLog);
        if (_plot instanceof SpectraPlot) {
            SpectraPlot cplot = (SpectraPlot) _plot;
            cplot.setMarksStyle(_originalMarks);
            cplot.setImpulses(_originalStems);
            _restoreConnected();
        }

        // FIXME: log axis format temporarily disabled, see above.
        // _plot.read("XTicks: " + _originalXTicksSpec);
        // if (_originalXTicksSpec.equals("")) {
        //    _narrowQuery.setEnabled("xlog", true);
        // } else {
        //   _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        // }
        // _plot.read("YTicks: " + _originalYTicksSpec);
        // if (_originalYTicksSpec.equals("")) {
        //    _narrowQuery.setEnabled("ylog", true);
        // } else {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        // }
        _plot.repaint();
    }
    

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** @serial The plot object controlled by this formatter. */
    protected final SpectraPlotBox _plot;

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////
    // Save the current connected state of all the point currently in the
    // plot.  NOTE: This method reaches into the protected members of
    // the Plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _saveConnected() {
        Vector points = ((SpectraPlot) _plot)._points;
        _originalConnected = new boolean[points.size()][];
        _originalPoints = new PlotPoint[points.size()][];

        for (int dataset = 0; dataset < points.size(); dataset++) {
            Vector pts = (Vector) points.elementAt(dataset);
            _originalConnected[dataset] = new boolean[pts.size()];
            _originalPoints[dataset] = new PlotPoint[pts.size()];

            for (int i = 0; i < pts.size(); i++) {
                PlotPoint pt = (PlotPoint) pts.elementAt(i);
                _originalConnected[dataset][i] = pt.connected;
                _originalPoints[dataset][i] = pt;
            }
        }
    }

    // Set the current connected state of all the point in the
    // plot.  NOTE: This method reaches into the protected members of
    // the Plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _setConnected(boolean value) {
        Vector points = ((SpectraPlot) _plot)._points;

        // Make sure the default matches.
        ((SpectraPlot) _plot).setConnected(value);

        boolean[][] result = new boolean[points.size()][];

        for (int dataset = 0; dataset < points.size(); dataset++) {
            Vector pts = (Vector) points.elementAt(dataset);
            result[dataset] = new boolean[pts.size()];

            boolean first = true;

            for (int i = 0; i < pts.size(); i++) {
                PlotPoint pt = (PlotPoint) pts.elementAt(i);
                pt.connected = value && !first;
                first = false;
            }
        }
    }

    // Restore the connected state of all the point that were in the
    // plot when their connected state was saved.
    // NOTE: This method reaches into the protected members of
    // the plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _restoreConnected() {
        for (int dataset = 0; dataset < _originalPoints.length; dataset++) {
            for (int i = 0; i < _originalPoints[dataset].length; i++) {
                PlotPoint pt = _originalPoints[dataset][i];
                pt.connected = _originalConnected[dataset][i];
            }
        }
    }


    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // Query widgets.
    private Query _wideQuery;

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // Query widgets.
    private Query _narrowQuery;

    private int _originalSpectratype ; //(JK)
    
    // Original configuration of the plot.
    private String _originalTitle;

    // Original configuration of the plot.
    private String _originalXLabel;

    // Original configuration of the plot.
    private String _originalYLabel;

    // Original configuration of the plot.
    private String _originalMarks;

    // Original configuration of the plot.
    private String _originalXTicksSpec;

    // Original configuration of the plot.
    private String _originalYTicksSpec;

    private double[] _originalXRange;

    private double[] _originalYRange;

    private Vector[] _originalXTicks;

    private Vector[] _originalYTicks;

    private boolean _originalGrid;

    private boolean _originalStems;

    private boolean _originalColor;

    private boolean[][] _originalConnected;

    private PlotPoint[][] _originalPoints;
}
