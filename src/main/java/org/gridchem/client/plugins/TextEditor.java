/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 23, 2006
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

package org.gridchem.client.plugins;

//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;
import org.gridchem.client.util.Env;


/**
 * TODO: I need a drawing how all these components relate to each other.
 */
public class TextEditor extends JPanel {
  private static final Logger logger = Logger.getLogger(TextEditor.class);  
    
  private String fileName, filePath;
  private JEditorPane window;
  private JScrollPane scrollWindow;
  private static int KEYCODE_N = 78;
  private static int KEYCODE_S = 83;
  private static int KEYCODE_E = 69;
  private static int KEYCODE_O = 79;
  
//  public TextEditor(GridCommandManager gcm, URI uri) {
//    this();     
//    window.setFont(new Font("COURIER",Font.PLAIN,12));
//    loadFile(uri.toString());
//  }
//
  public TextEditor(URI uri) {
      this();
      window.setFont(new Font("COURIER",Font.PLAIN,12));
      
      try {
          System.out.println("TE opened for " + uri.toURL());
          File data = new File(uri.getPath());
          if (data.exists()) {
              System.out.println("file " + data.toURL() + " exists.");
          }
          loadFile(data.toURL());
//          if (uri.getPath().indexOf(Env.getGridchemDataDir()) == -1) {
//              loadFile(new URL(Env.getGridchemDataDir() + File.separator  + uri.getPath()));
//          } else {
//              loadFile(uri.toURL());
//          }
      } catch (Exception e) {
          loadFile((URL)null);
      }
  }
  
  //For easy access from Desktop Icons, 10/7/04
  public TextEditor(String uri){
    this();     
    window.setFont(new Font("COURIER",Font.PLAIN,12));
    loadFile(uri);
    this.setPreferredSize(new Dimension(500,400));
  }
  
  public TextEditor() {
    this.setLayout(new BorderLayout());
    window = new JEditorPane(); 
    
    addHyperlinkListener(window);
    
    addKeyListener(window);
    scrollWindow = new JScrollPane(window,
              JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollWindow,BorderLayout.CENTER);
  }

  public void addHyperlinkListener(final JEditorPane editorPane) {
    editorPane.setEditable(false);
    editorPane.addHyperlinkListener(
      new HyperlinkListener() {
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
              try {
                editorPane.setPage(event.getURL());
              } catch(IOException ioe) {
                // Some warning to user
              }
            }
          }
      });
  }
  
  public void addKeyListener(javax.swing.JComponent compenent) {
    compenent.addKeyListener( new java.awt.event.KeyListener() {
          public void keyPressed(java.awt.event.KeyEvent event) {
            System.out.println("P: "+event.getKeyCode()+ " | "+event.isControlDown());
            if(event.getKeyCode() == KEYCODE_S && event.isControlDown()) {
              save();
            }else if(event.getKeyCode() == KEYCODE_O && event.isControlDown()) {
              open();
            }else if(event.getKeyCode() == KEYCODE_N && event.isControlDown()) {
                  newFile();
            }else if(event.getKeyCode() == KEYCODE_E && event.isControlDown()) {
              editable();
            }
            
          }
          public void keyTyped(java.awt.event.KeyEvent event) { }
          public void keyReleased(java.awt.event.KeyEvent event) {  }
        });
  }
  public void newFile() {
    window.setText("");
  }
  
  public void setContentType(String contentType) {
    window.setContentType(contentType);
  }
  
  public void setText(String content){
    window.setText(content);
  }
  
  public void setPage(URL url) {
    try {
        window.setPage(url);
    } catch (IOException ioException) {
        window.setText("<h1>Error: "+ioException+"</h1>");
    }
  }
  
  public void loadFile(String fileName) {
    try {
        if(fileName == null) {
            fileName = chooseFile(false);
            fileName = (fileName != null) ? (new File(fileName)).toURL().toString() : null;
        }
        this.fileName = fileName;
        System.out.println("Loading file: " + fileName);
        window.setPage(fileName);
        
        
    } catch (IOException e) {
        this.fileName = null;
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
  
  public void loadFile(URL fileURL) {
      try {
          if(fileURL == null) {
              fileURL = new URL(chooseFile(false));
          }
          this.fileName = fileURL.getPath();
          System.out.println("Loading URL: " + fileURL);
          window.setPage(fileURL);
          
      } catch (IOException e) {
          this.fileName = null;
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }
  
  public void editable() {      
    this.window.setEditable(!this.window.isEditable());
    if(this.window.isEditable()) { window.getCaret().setVisible(true); }
  }
  
  public void open() {
    loadFile((String)null);
  }
  
  public void save() {
    saveToFile(null);
  }
  
  public void saveToFile(String fileName) {
    if(fileName == null) {
      this.fileName = fileName = chooseFile(true);  
    }
    if(fileName != null) {
      try {         
        FileWriter fh = new FileWriter(fileName,false);

        fh.flush();
        window.write(fh);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        saveToFile(null);
        e.printStackTrace();
      }
    }else {
      // user canceled
    }
  }
  
  public String chooseFile(boolean isSave) {
    
    JFileChooser chooser = new JFileChooser(filePath);
    if(isSave) {
      chooser.showSaveDialog(this);
    }else {
      chooser.showOpenDialog(this);
    }
    if(chooser != null && chooser.getSelectedFile() != null) {
      filePath = chooser.getSelectedFile().getPath();
      return chooser.getSelectedFile().toString();
    }else {
      return null;
    }
    
  }
}
