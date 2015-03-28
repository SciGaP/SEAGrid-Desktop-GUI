package org.gridchem.client.gui.jobsubmission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.gridchem.client.GridChem;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JobScriptParser {
	
	private EditJobPanel editJobPanel;
	
	private File xmlFile;
	private List<JobBean> jobList;

	public JobScriptParser(EditJobPanel editJobPanel, String path) {
		this.editJobPanel = editJobPanel;
		xmlFile = new File(path);
		jobList = new ArrayList<JobBean> ();
	}
	
	public void parse() {
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			
			doc.getDocumentElement().normalize();
			
			Element configuration = (Element) doc.getElementsByTagName("configuration").item(0);
			
			String machine = configuration.getElementsByTagName("machine").item(0).getTextContent();
			String queue = configuration.getElementsByTagName("queue").item(0).getTextContent();
			String walltime = configuration.getElementsByTagName("walltime").item(0).getTextContent();
			String ncpus = configuration.getElementsByTagName("ncpus").item(0).getTextContent();
			String memory = configuration.getElementsByTagName("memory").item(0).getTextContent();
			String allocation = configuration.getElementsByTagName("allocation").item(0).getTextContent();
			
			NodeList jobList = doc.getElementsByTagName("job");
			
			for (int i = 0; i < jobList.getLength(); i ++) {
				Node job = jobList.item(i);
				
				if (job.getNodeType() == Node.ELEMENT_NODE) {
					Element eJob = (Element) job;
					
					JobBean jobBean = new JobBean();
					jobBean.setName(eJob.getElementsByTagName("name").item(0).getTextContent());
					jobBean.setExperimentName(editJobPanel.getResProj());
					jobBean.setAllocationName(allocation);
					jobBean.setSystemName(machine);
					jobBean.setSoftwareName(editJobPanel.getAppPackageName());
					jobBean.setModuleName(editJobPanel.getModuleName());
					jobBean.setUsedMemory(Long.parseLong(memory));
					jobBean.setProjectName(GridChem.project.getName());
					jobBean.setUserId(GridChem.user.getId());
					jobBean.setQueueName(queue);
					jobBean.setRequestedCpus(Long.parseLong(ncpus));
					
					String [] walltimeList = walltime.split(":");
					
					Calendar cal = Calendar.getInstance();
			        cal.clear();
			        
					if (walltimeList.length == 2) {
						cal.add(Calendar.HOUR, Integer.parseInt(walltimeList[0]));
						cal.add(Calendar.MINUTE, Integer.parseInt(walltimeList[1]));
					} else {
						cal.add(Calendar.MINUTE, Integer.parseInt(walltimeList[0]));
					}
					
					jobBean.setRequestedCpuTime(cal);
					
					Element eInputs = (Element) eJob.getElementsByTagName("inputs").item(0);
					NodeList inputs = eInputs.getElementsByTagName("input");
					
					for (int j = 0; j < inputs.getLength(); j++) {
						Element input = (Element) inputs.item(j);
						
						LogicalFileBean lFile = new LogicalFileBean();
			            lFile.setJobId(-1);
			            lFile.setLocalPath(input.getTextContent());
			            jobBean.getInputFiles().add(lFile);
					}
					
					this.jobList.add(jobBean);
					
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public List<JobBean> getJobList() {
		return this.jobList;
	}
}
