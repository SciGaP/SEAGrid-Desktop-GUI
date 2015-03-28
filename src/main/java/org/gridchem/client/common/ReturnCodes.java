/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on May 2, 2005
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
 * @author doctordooley
 *
 * Insert Template description here.
 */
public class ReturnCodes {
    
    public static final int SUCCESS 							= 0;
    public static final int FAILURE 							= 1;
    public static final int SSH_DIRECTORY_TRANSFER_SUCCESS 		= 2;
    public static final int SSH_DIRECTORY_TRANSFER_FAILURE 		= 3;
    public static final int SSH_FILE_TRANSFER_SUCCESS 			= 4;
    public static final int SSH_FILE_TRANSFER_FAILURE 			= 5;
    public static final int SSH_ACKNOWLEDGEMENT_FAILURE 		= 6;
    public static final int SSH_CONNECTION_FAILURE 			= 7;
    public static final int SSH_SESSION_FAILURE 				= 8;
    public static final int SSH_AUTHENTICATION_FAILURE 			= 9;
    public static final int SSH_AUTHENTICATION_SUCCESS 			= 10;
    public static final int SSH_SESSION_SUCCESS 				= 11;
    public static final int SSH_CONNECTION_SUCCESS 			= 12;
    
    public static final String SSH_JOB_SUBMISSION_SUCCESS 		= new String("SSH_JOB_SUBMISSION_SUCCESS");
    public static final String SSH_JOB_SUBMISSION_FAILURE 		= new String("SSH_JOB_SUBMISSION_FAILURE");
    /*static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";
    static final int FILE_TRANSFER_FAILURE = "0";*/
}
