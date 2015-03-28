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

// Unzip.java

package nanocad;

import java.io.* ;
import java.util.zip.* ;
import java.text.SimpleDateFormat ;

public class ZipExtractor {
    static final int BUFFER = 1024 ;
    public ZipExtractor ( String arg) {
	String fileSeparator = System.getProperty("file.separator");
    try {
	SimpleDateFormat sdf = new SimpleDateFormat( "mm:ss" ) ;
	BufferedOutputStream dest = null ;
	FileInputStream fis = new FileInputStream( arg ) ;
	ZipInputStream zis = new ZipInputStream( new BufferedInputStream( fis )) ;
	ZipEntry entry ;
	System.out.println( "ZipExtractor.java::Starting Time : " + sdf.format( new java.util.Date() ) ) ;
	while( ( entry = zis.getNextEntry() ) != null ) {
	   // System.out.println( "Extracting: " + entry ) ;
	    int count ;
	    byte data[] = new byte[ BUFFER ] ;
	    //prepare folders...
	    if( entry.isDirectory() ) {
                //System.out.println("A directory "+entry.getName()+ " will be created");
		( new File(newNanocad.applicationDataDir + newNanocad.fileSeparator + 
				entry.getName() ) ).mkdirs() ;
		continue ;
	    }

	    int iLastSlash = entry.getName().lastIndexOf("/");

            /// 2005/10/31 11:37sk
            //System.out.println (" The path for the new entry in Zip file is "+  entry.getName().substring( 0, iLastSlash +1));


            ( new File( newNanocad.applicationDataDir + newNanocad.fileSeparator + entry.getName().substring( 0, iLastSlash + 1) ) ).mkdirs() ;
	    ////( new File( entry.getName().substring( 0, iLastSlash + 1) ) ).mkdirs() ;

	//folders prepared

	    FileOutputStream fos = new FileOutputStream(newNanocad.applicationDataDir + newNanocad.fileSeparator + 
	    		entry.getName());
	    dest = new BufferedOutputStream(fos, BUFFER);
	    while ((count = zis.read(data, 0, BUFFER))!= -1) {
		dest.write(data, 0, count);
	    }
	    dest.flush();
	    dest.close();
	}
	zis.close();
	System.out.println( "ZipExtractor.java::Ending Time : " + sdf.format( new java.util.Date() ) ) ;
    }
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
}  
 



