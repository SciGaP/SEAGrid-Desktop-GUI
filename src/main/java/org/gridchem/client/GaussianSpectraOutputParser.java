package org.gridchem.client ;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File ;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.Vector;
import java.util.*;


public class GaussianSpectraOutputParser{
 
	Vector<String> freq = new Vector() ;
	Vector<String> intensity = new Vector() ;
	String[] SPEC_TYPE = {"IR", "VCD", "RAMAN", "ROA"} ; 

	private String goutFileName = null;
	private String dataDirName = null;
	private boolean IRexist = false ;
	private boolean RAMANexist = false;
	private boolean VCDexist = false;
	private boolean ROAexist = false;
	

	public GaussianSpectraOutputParser(String dirName, String gfileName){
		goutFileName = gfileName;
		dataDirName = dirName;
		
		IRexist = false;
		VCDexist = false;
		RAMANexist = false ;
		ROAexist = false;
		
	}

	public boolean makePlotFiles(){
		System.out.println("GaussianSpectraOutputParser:51: "+dataDirName+File.separator+goutFileName);
		File file = new File(dataDirName + File.separator + goutFileName);  
		boolean iexist = false;

		// IR first
		try{
			iexist = GaussianSpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[0]);
 		
 		} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
		
		if(iexist){
			System.out.println("GaussianSpectraOutputParser:70: "+dataDirName+File.separator+"IR.plt");
			GaussianSpectraOutputParser.this.writePltFile(dataDirName + File.separator + "IR.plt");
		
			IRexist = true;	
		
			iexist = false;
		}
		
		// VCD if exists
		try{
			
			iexist = GaussianSpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[1]);
		
	 	} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }	
		
		if(iexist){
			GaussianSpectraOutputParser.this.writePltFile(dataDirName + File.separator + "VCD.plt");
		
			VCDexist = true;	
		
			iexist = false;
		}
		
		
		// Raman if exists
		try{
			iexist = GaussianSpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[2]);
		 
		 } catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
		
		
		if(iexist){
			GaussianSpectraOutputParser.this.writePltFile(dataDirName + File.separator + "RAMAN.plt");
		
			RAMANexist = true;	
		
			iexist = false;
		}
		
		
		// ROA if exists
		try{
			iexist = GaussianSpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[3]);
		 
		} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
        
		if(iexist){
			GaussianSpectraOutputParser.this.writePltFile(dataDirName + File.separator + "ROA.plt");
		
			ROAexist = true;	
		

		}

		if( (!IRexist) && (!VCDexist) && (!RAMANexist) && (!ROAexist) ){
			iexist = false;
		}else{
			iexist = true;
		}

		return iexist ;
	}


	public void writePltFile(String fileName){
		
		File ofile = new File(fileName);
		FileWriter fw = null;
		try{
			fw = new FileWriter(ofile);
		}catch (IOException e){
			System.out.println("Exception :"+ e);
		}
		
		
		
		BufferedWriter bw = new BufferedWriter(fw) ;
		PrintWriter pw = new PrintWriter (bw, true);
		
		pw.println("DataSet : pixel ");
		
		int ndata = GaussianSpectraOutputParser.this.freq.size();
		
		for(int i = 0; i < ndata; i++){
			pw.println(""+GaussianSpectraOutputParser.this.freq.get(i)+" , "
				+GaussianSpectraOutputParser.this.intensity.get(i) );
			
		}
			
	
		GaussianSpectraOutputParser.this.freq.clear();
		GaussianSpectraOutputParser.this.intensity.clear();		
		
	
	}


	protected void _parseLine(String line, String specType){
	
		String[] headWord = {"Dip.", "Rot.", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention
		String headFreq = "Frequencies";

		
		String key = null;
		
		if (specType.equals( GaussianSpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
			key = headWord[0];
		}else if (specType.equals( GaussianSpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
			key = headWord[1];
		}else if (specType.equals( GaussianSpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
			key = headWord[2];
		}else if (specType.equals( GaussianSpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
			key = headWord[3];
		}

		StringTokenizer lcLine = new StringTokenizer(line);

		if( lcLine.countTokens() != 0){
			String stfirst = lcLine.nextToken();

			
			if(stfirst.equals(headFreq) ){
			
				System.out.println("key:" + key + "  first token:"+stfirst);
				
				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if ( (!(tok.equals("--"))) && (!(tok.equals("\n"))) ){
						
						GaussianSpectraOutputParser.this.freq.addElement(tok);
						
					}
				}
			}else if(stfirst.equals(key) ){	
				System.out.println(key+"\n");
				System.out.println(line);

				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if ( ((!tok.equals("str."))) && (!(tok.equals("--"))) && (!(tok.equals("\n"))) ){
						
						GaussianSpectraOutputParser.this.intensity.addElement(tok);
						
						System.out.println(tok);
					}
				}
			}
	
		}
		
	}

    public synchronized boolean read(InputStream in, String specType) throws IOException {
        boolean iflag= false;
        
        try {
            // NOTE: I tried to use exclusively the jdk 1.1 Reader classes,
            // but they provide no support like DataInputStream, nor
            // support for URL accesses.  So I use the older classes
            // here in a strange mixture.
  
            BufferedReader din = new BufferedReader(new InputStreamReader(in));

            try {
                String line = din.readLine();

                while (line != null) {
                    _parseLine(line, specType);
                    line = din.readLine();
                }
            } finally {
                din.close();
            }
        } catch (IOException e) {
            String[] errorMsg = new String[2];
            errorMsg[0] = "Failure reading input data.";
            errorMsg[1] = e.getMessage();
            throw e;
        }
        
        if(GaussianSpectraOutputParser.this.intensity.size() == 0){
        	iflag = false;
        }else {
        	iflag = true;
        }
        
        return iflag;
        
    }


}






