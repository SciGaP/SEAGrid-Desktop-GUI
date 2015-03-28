
/**
 * FileTransferServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package org.gridchem.service.filetransfer;

    /**
     *  FileTransferServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class FileTransferServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public FileTransferServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public FileTransferServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for requestUploadFile method
            * override this method for handling normal response from requestUploadFile operation
            */
           public void receiveResultrequestUploadFile(
                    org.gridchem.service.filetransfer.FileTransferServiceStub.RequestUploadFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from requestUploadFile operation
           */
            public void receiveErrorrequestUploadFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for requestDownloadFile method
            * override this method for handling normal response from requestDownloadFile operation
            */
           public void receiveResultrequestDownloadFile(
                    org.gridchem.service.filetransfer.FileTransferServiceStub.RequestDownloadFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from requestDownloadFile operation
           */
            public void receiveErrorrequestDownloadFile(java.lang.Exception e) {
            }
                


    }
    