
/*

Copyright (c) 2005, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu/

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
3. Neither the names of Center for Computational Sciences, University of Kentucky, 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.
*/


/**
 * Created on Apr 2, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */


package G03Input;

import java.util.Enumeration;

import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


public class InsertNode {
    
    public static void insertNode(String prefix,String nodeName){
        
    
        int startRow = 0;
           
        /* Expand the tree if it is not ...*/
        TreePath path2= G03MenuTree.tree.getNextMatch("G '03", startRow, Position.Bias.Forward);
        Enumeration el=G03MenuTree.root.children();
        TreeNode nod = (TreeNode)el.nextElement();
        TreePath path1 = path2.pathByAddingChild(nod);
        G03MenuTree.tree.scrollPathToVisible(path1);
        /* End of Expansion */  
             
        /*Add new Node now */
        System.out.println(prefix);
        TreePath path = G03MenuTree.tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
        MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
        
        // Insert new node as last child of node
        DefaultTreeModel model = (DefaultTreeModel)G03MenuTree.tree.getModel();
        model.insertNodeInto(newNode, node, node.getChildCount());
        
        /* Scroll down to show the node*/ 
        G03MenuTree.tree.scrollPathToVisible(new TreePath(newNode.getPath()));
    }
    public static boolean nodeExists(String str)
    {
        int startRow = 0;
	    TreePath path2= G03MenuTree.tree.getNextMatch("G '03", startRow, Position.Bias.Forward);
        Enumeration el=G03MenuTree.root.children();
        TreeNode nod = (TreeNode)el.nextElement();
        TreePath path1 = path2.pathByAddingChild(nod);
        G03MenuTree.tree.scrollPathToVisible(path1);
	    TreePath path3=G03MenuTree.tree.getNextMatch(str, startRow, Position.Bias.Forward);    
        if(path3==null)
        return false;
        else
        return true;
        }
    
    public static void deleteChildren(String prefix)
    {
     int startRow = 0;
      
     /* Expand the tree if it is not ...*/
     TreePath path2= G03MenuTree.tree.getNextMatch("G '03", startRow, Position.Bias.Forward);
     Enumeration el=G03MenuTree.root.children();
     TreeNode nod = (TreeNode)el.nextElement();
     TreePath path1 = path2.pathByAddingChild(nod);
     G03MenuTree.tree.scrollPathToVisible(path1);
    
     TreePath path = G03MenuTree.tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
     MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
     System.out.println("Node name:" +node +"Path"+ path);
     DefaultTreeModel model = (DefaultTreeModel)G03MenuTree.tree.getModel();
     
     System.out.println(node.getChildCount());
     int p=node.getChildCount();
     //Get all the children
     if(!(node.isLeaf()))
     for(int i=0;i<p;i++)
     {

         model.removeNodeFromParent((MutableTreeNode)node.getChildAt(0));
     }
     //model.removeNodeFromParent(node);
    
    }

    public static void deleteNode(String nodeName)
    {
     int startRow = 0;
      
     /* Expand the tree if it is not ...*/
     TreePath path2= G03MenuTree.tree.getNextMatch("G '03", startRow, Position.Bias.Forward);
     Enumeration el=G03MenuTree.root.children();
     TreeNode nod = (TreeNode)el.nextElement();
     TreePath path1 = path2.pathByAddingChild(nod);
     G03MenuTree.tree.scrollPathToVisible(path1);
    
     TreePath path = G03MenuTree.tree.getNextMatch(nodeName, startRow, Position.Bias.Forward);
     MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
     //System.out.println("Node name:" +node +"Path"+ path);
     DefaultTreeModel model = (DefaultTreeModel)G03MenuTree.tree.getModel();
     model.removeNodeFromParent(node);
     }
     //model.removeNodeFromParent(node);
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

