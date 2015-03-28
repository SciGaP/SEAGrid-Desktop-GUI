package org.gridchem.client ;
/*
 * This package is the spectra output file parser  (march 30 2007)
 * 1. Gaussian 03 (98) 
 * 2. GAMESS US
 * 3. NWChem
 * 4. Molpro
 * 5. QChem
 * 
 * And, it makes the temporary files for plot and vib. animation
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File ;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Vector;
import java.util.*;


public class SpectraOutputParser{
	
	String[] PACKAGE_NAME = {"Gaussian", "GAMESS_US", "NWChem", "Molpro", "QChem", "ADF"};
	
	String[] SPEC_TYPE = {"IR", "VCD", "RAMAN", "ROA"} ; 	
	
	
 
	Vector<String> freq = new Vector() ;
	Vector<String> intensity = new Vector() ;


	private String outFileName = null;
	private String dataDirName = null;
	
	private boolean isGaussian = false;
	private boolean isGAMESS_US = false;
	private boolean isNWChem = false;
	private boolean isMolpro = false;
	private boolean isQChem = false;
	private boolean isADF = false;	

	private boolean IRexist = false ;
	private boolean RAMANexist = false;
	private boolean VCDexist = false;
	private boolean ROAexist = false;
	

	public SpectraOutputParser(String dirName, String fileName){
		outFileName = fileName;
		dataDirName = dirName;
		
		IRexist = false;
		VCDexist = false;
		RAMANexist = false ;
		ROAexist = false;
	
		String applicationName = checkOutputFileType(dataDirName + File.separator + outFileName) ;
		
		System.out.println(applicationName); // debug
		
		if (applicationName.equals(PACKAGE_NAME[0])){
			isGaussian = true;
		}	
		if (applicationName.equals(PACKAGE_NAME[1])){
			isGAMESS_US = true;
		}
				
		if (applicationName.equals(PACKAGE_NAME[2])){
			isNWChem = true;
		}
		
		if (applicationName.equals(PACKAGE_NAME[3])){
			isMolpro = true;
		}		
		if (applicationName.equals(PACKAGE_NAME[4])){
			isQChem = true;
		}
		
		if (applicationName.equals(PACKAGE_NAME[5])){
			isADF = true;
		}

		if (applicationName.equals("NOT KNOWN")){

			System.out.println("Output file is not recognized");
		}
			
		
		
		
	}
	
	public String checkOutputFileType(String ofileName){
		String outputTypeName = null;
		
		String signatureForGaussian = "Gaussian System";
		String signatureForGAMESS_US = "GAMESS";
		String signatureForNWChem = "NWChem";
		String signatureForMolpro = "MOLPRO";
		String signatureForQChem = "Q-Chem" ;

		String signatureForADF = "ADF";		
		
		File ofile = new File(ofileName);
		FileReader fr = null;
		BufferedReader br = null;
		
		try{
			
			fr = new FileReader(ofile);
			
		}catch(IOException ex){
			
			System.out.println("Error for reading : "+ ex);
		}
		
		br = new BufferedReader(fr);
		
		
        try {
            String line = null;
            
            while ((line = br.readLine()) != null) {
            	
            	System.out.println(line);   // debug
            	
            	if (line.indexOf(signatureForGaussian) != -1){
            		
            		return PACKAGE_NAME[0];
            		
            		
            	}else if(line.indexOf(signatureForGAMESS_US) != -1){
            		
            		return PACKAGE_NAME[1];
            		
            	}else if(line.indexOf(signatureForNWChem) != -1){
            		
            		return PACKAGE_NAME[2] ;
            		
                }else if(line.indexOf(signatureForMolpro) != -1){
            		
            		return PACKAGE_NAME[3] ;
            		
            	}else if(line.indexOf(signatureForQChem) != -1){
            		
            		return PACKAGE_NAME[4] ;
            		
            	}else if(line.indexOf(signatureForADF) != -1) {
            		
            		return PACKAGE_NAME[5] ;
            		
            	}

            }

        }catch(IOException ex){
        	
        	System.out.println("reading error "+ ex);
        	
        	
        	
        } finally {
        	try{
        		br.close();
        	}catch(IOException ex){
        		
        		System.out.println("closing error with "+ ex);
        	}
        }
		
		
        return "NOT KNOWN"; 
		
	}
	
	
	
	
	
	

	public boolean makePlotFiles(){

		
		File file = new File(dataDirName + File.separator + outFileName);  
		boolean iexist = false;

		// IR first
		try{
			
			iexist = SpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[0]);

 		
 		} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
		
		if(iexist){
	
			SpectraOutputParser.this.writePltFile(dataDirName + File.separator + "IR.plt");
		
			IRexist = true;	
		
			iexist = false;
		}
		
		// VCD if exists
		try{
			
			iexist = SpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[1]);
		
	 	} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }	
		
		if(iexist){
			SpectraOutputParser.this.writePltFile(dataDirName + File.separator + "VCD.plt");
		
			VCDexist = true;	
		
			iexist = false;
		}
		
		
		// Raman if exists
		try{
			iexist = SpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[2]);
		 
		 } catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
		
		
		if(iexist){
			SpectraOutputParser.this.writePltFile(dataDirName + File.separator + "RAMAN.plt");
		
			RAMANexist = true;	
		
			iexist = false;
		}
		
		
		// ROA if exists
		try{
			iexist = SpectraOutputParser.this.read(new FileInputStream(file), SPEC_TYPE[3]);
		 
		} catch (FileNotFoundException ex) {

            System.err.println("File not found: " + file + " : " + ex);

        } catch (IOException ex) {

            System.err.println("Error reading input: " + file + " : " + ex);

        }
        
		if(iexist){
			SpectraOutputParser.this.writePltFile(dataDirName + File.separator + "ROA.plt");
		
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
		
		int ndata = SpectraOutputParser.this.freq.size();
		
		for(int i = 0; i < ndata; i++){
			pw.println(""+SpectraOutputParser.this.freq.get(i)+" , "
				+SpectraOutputParser.this.intensity.get(i) );
			
		}
			
	
		SpectraOutputParser.this.freq.clear();
		SpectraOutputParser.this.intensity.clear();		
		
	
	}


	protected void _parseLineForGaussian(String line, String specType){
	
		String[] headWord = {"IR", "Rot.", "Raman", "Not yet"};  // should be confirmed by gaussian output convention
		String headFreq = "Frequencies";

		
		String key = null;
		
		if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
			key = headWord[0];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
			key = headWord[1];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
			key = headWord[2];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
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
						
						SpectraOutputParser.this.freq.addElement(tok);
						
					}
				}
			}else if(stfirst.equals(key) ){	
				System.out.println(key+"\n");
				System.out.println(line);

				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if ( ((!tok.equals("str."))) && ((!tok.equals("Inten"))) && ((!tok.equals("Activ"))) && (!(tok.equals("--"))) && (!(tok.equals("\n"))) ){
						
						SpectraOutputParser.this.intensity.addElement(tok);
						
						System.out.println(tok);
					}
				}
			}
	
		}
		
	}

	
	protected void _parseLineForGAMESS_US(String line, String specType){
		
		String[] headWord = {"IR", "Not yet", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention
		String headFreq = "FREQUENCY:";

		
		String key = null;
		
		if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
			key = headWord[0];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
			key = headWord[1];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
			key = headWord[2];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
			key = headWord[3];
		}

		StringTokenizer lcLine = new StringTokenizer(line);

		if( lcLine.countTokens() != 0){
			String stfirst = lcLine.nextToken();

			
			if(stfirst.equals(headFreq) ){
			
				System.out.println("key:" + key + "  first token:"+stfirst);
				
				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if ( (!(tok.equals("\n"))) ){
						
						SpectraOutputParser.this.freq.addElement(tok);
						
					}
				}
			}else if(stfirst.equals(key) ){	
				System.out.println(key+"\n");
				System.out.println(line);

				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if ( ((!tok.equals("INTENSITY:"))) &&  (!(tok.equals("\n"))) ){
						
						SpectraOutputParser.this.intensity.addElement(tok);
						
						System.out.println(tok);
					}
				}
			}
	
		}
		
	}

	
protected void _parseLineForMolpro(String line, String specType){
/* assumed the following output (The first character "*" in the line is not shown in an output :)
 * 
 * ex) H2O
 *                         1  A1           2 A1        3  B2
 * Wavenumbers [cm-1]     1660.85        3799.90      3916.40
 * Intensities [km/mol]     73.63           1.60        20.42
 * Intensities [relative]  200.00           2.18        27.74
 * 
 * 
 */
		String[] headWord = {"Intensities", "Not yet", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention
		String headFreq = "Wavenumbers";

		
		String key = null;
		
		if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
			key = headWord[0];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
			key = headWord[1];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
			key = headWord[2];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
			key = headWord[3];
		}

		StringTokenizer lcLine = new StringTokenizer(line);

		if( lcLine.countTokens() != 0){
			String stfirst = lcLine.nextToken();

			
			if(stfirst.equals(headFreq) ){
			
				System.out.println("key:" + key + "  first token:"+stfirst);  // debug
				
				
				
				while (lcLine.hasMoreTokens() ){
					String tok = lcLine.nextToken() ;
					if (!(tok.equals("[cm-1]")) ){
						
						SpectraOutputParser.this.freq.addElement(tok);
						
					}
				}
				
			}else if(stfirst.equals(key) ){	
				System.out.println(key+"\n");        //debug
				System.out.println(line);             //debug

				String unitString = lcLine.nextToken();
				if (unitString.equals("[relative]")) {
				
					while (lcLine.hasMoreTokens() ){
						String tok = lcLine.nextToken() ;
						
							SpectraOutputParser.this.intensity.addElement(tok);
						
							System.out.println(tok);    //debug
					}
				}
			}
	
		}
		
	}
	protected void _parseLineForQChem(String line, String specType){
/*	
	String[] headWord = {"Not Yet", "Not yet", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention
	String headFreq = "Frequencies";

	
	String key = null;
	
	if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
		key = headWord[0];
	}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
		key = headWord[1];
	}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
		key = headWord[2];
	}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
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
					
					SpectraOutputParser.this.freq.addElement(tok);
					
				}
			}
		}else if(stfirst.equals(key) ){	
			System.out.println(key+"\n");
			System.out.println(line);

			while (lcLine.hasMoreTokens() ){
				String tok = lcLine.nextToken() ;
				if ( ((!tok.equals("str."))) && (!(tok.equals("--"))) && (!(tok.equals("\n"))) ){
					
					SpectraOutputParser.this.intensity.addElement(tok);
					
					System.out.println(tok);
				}
			}
		}

	}
	*/
	}

	
	public void parseNWChem(BufferedReader din, String specType){
		
		String[] keyWord = {"Projected Infra Red", "Not yet", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention

		
		String firstToken = "Normal";
		String secondToken = "Mode";
		String thirdToken = "-";
	
		
		
		String key = null;
		
		if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
			key = keyWord[0];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
			key = keyWord[1];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
			key = keyWord[2];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
			key = keyWord[3];
		}

		String line = null;
		boolean firstlineFlag = false;
		boolean secondlineFlag = false;
		boolean thirdlineFlag = false;
		Integer imode = new Integer(1);
		
		String stFreq = null;
		String tmp = null;
		String unit1Int = null;
		String unit2Int = null;
		String unit3Int = null;
		String unit4Int = null;
		
		try{
			while ((line = din.readLine()) != null) {
		

				
				StringTokenizer lcLine = new StringTokenizer(line);
		
				if( lcLine.countTokens() != 0){
					String stfirst = lcLine.nextToken();

					if( (firstlineFlag)&&(secondlineFlag)&&(thirdlineFlag) ){
			
						if(stfirst.equals(imode.toString()) ){
						
							stFreq = lcLine.nextToken() ;
							SpectraOutputParser.this.freq.addElement(stFreq);
							tmp = lcLine.nextToken();   // should be "||"
						
							unit1Int = lcLine.nextToken();  // atomic unit
							unit2Int = lcLine.nextToken(); // (debye/ans)**2
							unit3Int = lcLine.nextToken(); // KM/mol
							unit4Int = lcLine.nextToken();  // arbitrary unit
						
							SpectraOutputParser.this.intensity.addElement(unit4Int);  // just simply
					
					
							imode = imode + 1;
							
							
						}else{
							// expects the modes are should be shown together
						
							firstlineFlag = false; secondlineFlag = false; thirdlineFlag = false;
						
						}
					
					}else{

						if((firstlineFlag == true)&&(secondlineFlag == true)){
							if(line.indexOf(thirdToken) != -1){
								thirdlineFlag = true;
							
							}
							
						}
						
						if(stfirst.equals(secondToken)){
							
							if (firstlineFlag){
								secondlineFlag = true;
							}
						}
						
				
						
						if(stfirst.equals(firstToken) ){
							
							
							if(line.indexOf(key) != -1){
								firstlineFlag = true;
							}
						}
			
			
			
					}
				}		
	
			}
		}catch(IOException ex){
			
			System.out.println("Error while parsing NWChem output with "+ ex);
		}
		
		
	}
	
	public void parseADF(BufferedReader din, String specType){
	/*  The following format is assumed in output file. 
	 * And, make sure this is after the string "List of All Frequencies:"
	 * 
	 * 
		               Frequency       Dipole Strength        Absorption Intensity (degeneracy not counted)
		                 cm-1           1e-40 esu2 cm2          km/mole
		              ----------          ----------          ----------
		              940.621418          696.120279          164.125937
		             1578.684510           57.138739           22.610183
		             1578.684510           57.138739           22.610183
		             3424.305870            1.116794            0.958570
		             3558.099078           12.549913           11.192750
		             3558.099078           12.549913           11.192750
	* 
	*/		
		String[] keyWord = {"Abosorption Intensity", "Not yet", "Not yet", "Not yet"};  // should be confirmed by gaussian output convention
				
		String firstToken = "Frequency";
		String secondToken = "cm-1";
		String thirdToken = "-";
				
				
		String key = null;
				
		if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[0]) ){ // means IR
					key = keyWord[0];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[1]) ) { // means VCD
					key = keyWord[1];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[2]) ){ // means RAMAN
					key = keyWord[2];
		}else if (specType.equals( SpectraOutputParser.this.SPEC_TYPE[3]) ){ // means ROA
					key = keyWord[3];
		}

		String line = null;
		boolean firstlineFlag = false;
		boolean secondlineFlag = false;
		boolean thirdlineFlag = false;
		boolean isNextFreqLine = false;
		Integer imode = new Integer(1);
				
		boolean isFreqListStart = false ;  // ADF has one more freq. info section
				
				
		String stFreq = null;

		String unit1Int = null;
		String unit2Int = null;
				
		try{
			while ((line = din.readLine()) != null) {
						
				if(line.indexOf("List of All Frequencies:") != -1){
					isFreqListStart = true ;
				}
						
				if(isFreqListStart){
						
					StringTokenizer lcLine = new StringTokenizer(line);
				
						
					if( lcLine.countTokens() != 0){
						
						String stFirst = lcLine.nextToken();

						if(isNextFreqLine == true){
							
							stFreq = stFirst ;
							SpectraOutputParser.this.freq.addElement(stFreq);
								
							unit1Int = lcLine.nextToken();  // Dipole Strength column
							unit2Int = lcLine.nextToken(); // Absorption Intensity column
								
							SpectraOutputParser.this.intensity.addElement(unit2Int);  // just simply
							
							imode = imode + 1;
							
						}else{
		                        
							if((firstlineFlag == true)&&(secondlineFlag == true)){
								if(line.indexOf(thirdToken) != -1){
									thirdlineFlag = true;
							
									isNextFreqLine = true; 
								}
									
							}
								
							if(stFirst.equals(secondToken)){
								secondlineFlag = true;
							}
								
							if(stFirst.equals(firstToken) ){
								firstlineFlag = true;
							}
					
						}
							
					}else {
							
						if(isNextFreqLine == true){
							isNextFreqLine = false;   // assume that a blank line means the end
							firstlineFlag = false;
							secondlineFlag = false;
							thirdlineFlag = false;
							
						}
					}
			
				}
			}
				
		}catch(IOException ex){
					
				System.out.println("Error while parsing ADF output with "+ ex);
		}
				
		System.out.println("the number of modes found with "+specType + ": "+ imode); 
	}
			
	
    public synchronized boolean read(InputStream in, String specType) throws IOException {
        boolean iflag= false;
        
        try {

            BufferedReader din = new BufferedReader(new InputStreamReader(in));

            if(isNWChem){
            	if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[0])){
                    parseNWChem(din, specType);
            	}else{
            		iflag = false;
            	}
            
            }else if (isADF){
            	//currently IR only
            	if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[0])){
            		parseADF(din, specType);
            	}else{
            		iflag = false;
            	}
            
            }else{
            
            	try {
            		String line = null;

                
                		while ((line = din.readLine()) != null) {
                			if(isGaussian){
                				if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[0])){
                					_parseLineForGaussian(line, specType);
                				}else if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[2])){
                					 _parseLineForGaussian(line, specType);
                				}else{
                					iflag = false;
                				}
                			
                			}else if(isGAMESS_US){
                				if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[0])){
                					_parseLineForGAMESS_US(line, specType);
                				}else {
                					iflag = false;
                				}
                				
                			}else if(isMolpro){
                				if(specType.equals(SpectraOutputParser.this.SPEC_TYPE[0])){
                					_parseLineForMolpro(line, specType);
                				}else {
                					iflag = false;
                				}	                				
                		
                			}else if(isQChem){
                				iflag = false;
                				// _parseLineForQChem(line, specType);
                			}else{
                				iflag = false;
                				System.out.println("Not matched to any pakcage can be parsed");
                			}
                		} 
                	
                } finally {
                			din.close();
                }

            
            
            
            }
            
            
        } catch (IOException e) {
            	String[] errorMsg = new String[2];
            	errorMsg[0] = "Failure reading input data.";
            	errorMsg[1] = e.getMessage();
            	throw e;
        }
        
        if(SpectraOutputParser.this.intensity.size() == 0){
        	iflag = false;
        }else {
        	iflag = true;
        }
        
        return iflag;
        
    }






    public void readNormalMode(Vector<Double>[] normal, String oFileName){
// will be used later for calling nanocad with normal mode information	
	
    	if((!isGaussian)&&(!isGAMESS_US)&&(!isNWChem)&&(!isMolpro)&&(!isQChem)&&(!isADF)){
		
    		System.out.println("Some thing wrong!  No output file");
    	}
	
    	if(isGaussian){
		
		
		
    	}
	
    	if(isGAMESS_US){
		
		
		
    	}
	
    	if(isNWChem){
		
		
		
    	}	
	
    	if(isMolpro){
		
		
		
    	}
	
    	if(isQChem){
		
		
		
    	}
	
    	if(isADF){
		
		
		
    	}
	
	}

}
