package org.gridchem.service.karnak;

import java.util.Calendar;

import org.gridchem.service.beans.JobBean;

public class JobPredictionService {
	
	private static Karnak karnak = new Karnak();

	public static String predictNewStartTime(JobBean job) {
		String result = "N/A";
		
		Calendar cal = job.getRequestedCpuTime();
		
		int hours = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24 + cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        
        int seconds = hours * 3600 + minutes * 60;
        
        String hostname = findHPCName(job.getSystemName());
		
        try {
        	result = karnak.queuePrediction(job.getRequestedCpus().intValue(), seconds, hostname, job.getQueueName()).predictedStartTime.toString();
        } catch (KarnakException e) {
        	System.err.println("Failed to predict job start time");
        }
		
		return result;
	}
	
	public static String predictQueuedStartTime(JobBean job) {
		String result = "N/A";
		
		String hostname = findHPCName(job.getSystemName());
		
		try {
        	result = karnak.queuedPrediction(hostname, job.getLocalId()).predictedStartTime.toString();
        } catch (KarnakException e) {
        	System.err.println("Failed to predict job start time");
        }
		
		return result;
	}
	
	private static String findHPCName(String gcName) {
		String knName = "N/A";
		
		if (gcName.equals("Trestles")) {
			knName = "trestles.sdsc.xsede.org";
		} else if (gcName.equals("Blacklight")) {
			knName = "blacklight.psc.xsede.org";
		} else if (gcName.equals("Gordon")) {
			knName = "gordon.sdsc.xsede.org";
		} else if (gcName.equals("Stampede")) {
			knName = "stampede.tacc.xsede.org";
		} else if (gcName.equals("Kraken")) {
			knName = "kraken.nics.xsede.org";
		} else if (gcName.equals("Ranger")) {
			;;
		}
		
		return knName;
	}
}
