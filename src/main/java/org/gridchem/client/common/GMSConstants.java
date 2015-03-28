/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 25, 2006
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

package org.gridchem.client.common;

/**
 * Client-side implementation of the resource properties exposed by the GMS.
 * 
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 *
 */
import javax.xml.namespace.QName;

public interface GMSConstants {
    public static final String GMS_NS="http://localhost:8080/examples/gms";
    public static final QName RP_SET = new QName(GMS_NS, "GMSResourceProperties");
    public static final QName MESSAGE_RP = new QName(GMS_NS, "StatusMessage");
    public static final QName LOGIN_RP = new QName(GMS_NS, "UserLogin");
    public static final QName USER_PREFERENCES_RP = new QName(GMS_NS, "UserPreferences");
    public static final QName USER_RESOURCES_RP = new QName(GMS_NS, "UserResources");
    public static final QName JOB_QUERY_RP = new QName(GMS_NS, "UserJob");
    public static final QName USER_FILES_RP = new QName(GMS_NS, "UserFiles");
    public static final QName USER_PROJECTS_RP = new QName(GMS_NS, "UserProjects");
    public static final QName JOB_SUBMIT_RP = new QName(GMS_NS, "Submit Job");
    public static final QName JOB_PREDICTION_RP = new QName(GMS_NS, "Predict Job Time");
    public static final QName JOB_KILL_RP = new QName(GMS_NS, "Kill Job");
    public static final QName FILE_QUERY_RP = new QName(GMS_NS, "UserFile");
    public static final QName FILE_RETRIEVE_RP = new QName(GMS_NS, "Retrieve File");
    public static final QName FILE_DELETE_RP = new QName(GMS_NS, "Delete Flie");
    public static final QName COMPUTE_RESOURCE_RP = new QName(GMS_NS, "Compute Resource");
    public static final QName STORAGE_RESOURCE_RP = new QName(GMS_NS, "Storage Resource");
    public static final QName NETWORK_RESOURCE_RP = new QName(GMS_NS, "Network Resource");
    public static final QName VIZ_RESOURCE_RP = new QName(GMS_NS, "Viz Resource");
    public static final QName SOFTWARE_RESOURCE_RP = new QName(GMS_NS, "Software Resource");
    public static final QName OTHER_RESOURCE_RP = new QName(GMS_NS, "Other Resource");
    public static final QName USER_USAGE_RP = new QName(GMS_NS, "UserUsageRecords");
}