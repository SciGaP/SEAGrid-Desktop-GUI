/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

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

/* JobList.java  based on JobList.java of John Lee,  by Rebecca Hartman-Baker
 This is the list of jobs, to be used for the job queue. */

package org.gridchem.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.gridchem.service.beans.JobBean;
import org.gridchem.service.karnak.JobPredictionService;

public class JobList implements java.util.List {
	static final JobList DUMB_LIST = new JobList();
	private ArrayList theList;
	private int huh;

	public JobList() {
		huh = 0;
		theList = new ArrayList(10);
	}

	public JobList(JobBean j) {
		huh = 0;
		theList = new ArrayList(10);
		theList.add(j);
	}

	public JobList(Collection al) {
		huh = 0;
		theList = new ArrayList(al);
		// theList = al;
	}

	// Some initial add and subtract methods
	// need to add more later, but it'll do for now

	public boolean add(JobBean j) {
		return theList.add(j);
	}

	public JobBean getFirstJob() {
		return (JobBean) theList.get(0);
	}

	// lixh_add
	public JobBean getLastJob() {
		return (JobBean) theList.get(size() - 1);
	}

	// lixh_add
	public JobBean getJob(int index) {
		return (JobBean) theList.get(index);
	}

	public void replaceJob(int index, JobBean job1) {
		theList.set(index, job1);
	}

	public JobBean removeFirstJob() {
		return (JobBean) theList.remove(0);
	}

	// lixh_add
	public JobBean removeJob(int index) {
		return (JobBean) theList.remove(index);
	}

	public int getJobIndex(JobBean j) {
		return theList.indexOf(j);
	}

	public ArrayList getList() {
		ArrayList L = new ArrayList();
		L = theList;
		return L;
	}

	public ArrayList<String> getJobNamesList() {
		ArrayList<String> s = new ArrayList<String>();
		ArrayList t = new ArrayList(this.getList());
		JobBean j;
		// JobList t = new JobList(this.getList());
		// Iterator i = t.iterator();
		// while (i.hasNext())
		while (!(t.isEmpty())) {
			j = (JobBean) t.remove(0);

			// s.add(j.getJobName()+ "  " + j.getApp() + "  " + j.getMachine());
			s.add(j.getExperimentName() + " " + j.getName() + " "
					+ j.getSoftwareName() + " " + j.getSystemName() + " " + JobPredictionService.predictNewStartTime(j));
		}
		return s;
	}

	public int size() {
		return theList.size();
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}

	public boolean contains(Object elem) {
		boolean b = theList.contains(elem);
		return b;
	}

	public Iterator iterator() {
		return theList.iterator();
	}

	public Object[] toArray() {
		return theList.toArray();
	}

	public Object[] toArray(Object[] a) {
		return theList.toArray(a);
	}

	/*************
	 * INSTRUMENTED METHODS *****************
	 */
	public boolean add(Object o) {
		return theList.add(o);
	}

	public void add(int index, Object o) {
		theList.add(index, o);
	}

	public boolean addAll(Collection c) {
		boolean b = theList.addAll(c);
		return b;
	}

	public boolean addAll(int index, Collection c) {
		boolean b = theList.addAll(index, c);
		return b;
	}

	public void clear() {
		theList.clear();
	}

	public Object remove(int index) {
		return theList.remove(index);
	}

	public boolean remove(Object o) { // never works!
		return false;
	}

	public boolean containsAll(Collection c) {
		boolean b = theList.containsAll(c);
		return b;
	}

	public boolean removeAll(Collection c) {
		return theList.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return theList.retainAll(c);
	}

	public Object get(int index) {
		JobBean jobOnDeck = (JobBean) theList.get(index);
		return (Object) jobOnDeck;
	}

	public Object set(int index, Object o) {
		return theList.set(index, o);
	}

	public int indexOf(Object o) {
		return theList.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return theList.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return theList.listIterator();
	}

	public ListIterator listIterator(int index) {
		return theList.listIterator(index);
	}

	public java.util.List subList(int from, int to) {
		return theList.subList(from, to);
	}

}
