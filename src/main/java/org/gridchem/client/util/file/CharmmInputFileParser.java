package org.gridchem.client.util.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.gridchem.client.exceptions.CharmmInputFileParsingException;

/* Parse out the referenced data files required by main input file (*.inp) */
public class CharmmInputFileParser {

	public static List<String> parse(File inpFile)
			throws CharmmInputFileParsingException {
		List<String> dataFiles = new ArrayList<String>();

		try {
			FileInputStream fstream = new FileInputStream(inpFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = null;
			while ((line = br.readLine()) != null) {

				if (line.toLowerCase().contains("open read")) {
					String[] strArray = line.split(" ");
					// Assume file path supplied
					String filePath = strArray[strArray.length - 1]; 
					if (filePath.startsWith("./") || filePath.contains("/")
							|| filePath.contains("@")) {
						in.close();
						throw new CharmmInputFileParsingException(
								"(Invalid Charmm input file) Referenced data file comes with path information. Please modify input file");
					}
					dataFiles.add(filePath);
				}
			}

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataFiles;
	}

	public static void main(String[] args)
			throws CharmmInputFileParsingException {
		List<String> dataFiles = parse(new File(
				"C:/Users/fanye/gridchem/data/.application_data/templates/charmm_charmm_mpi/brbtestismuchlongerthan15bytes.inp"));

		for (String name : dataFiles) {
			System.out.println(name);
		}
	}
}
