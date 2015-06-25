package org.apache.airavata.gridchem;

import org.gridchem.client.GridChem;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SessionException;
import org.gridchem.client.gui.filebrowser.commands.FileCommand;
import org.gridchem.client.gui.filebrowser.commands.MKDIRCommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.optsComponent;
import org.gridchem.service.socket.FileUploadThread;

import javax.swing.*;

/**
 * Created by dimuthuupeksha on 6/14/15.
 */
public class FileBrowserAiravata implements StatusListener {

    public void uploadFileToGridChem(String userName,String experimentName,String jobName, String fileName, String localFilePath) throws InterruptedException {
        FileUploadThread fThread = new FileUploadThread(userName,experimentName,jobName,fileName,localFilePath);
        fThread.start();

        synchronized (fThread){
            fThread.wait();
        }
    }

    public void makeDirectory(final String newPath){
        final FileCommand mkdirCommand = new MKDIRCommand(this);

        try {
            SwingWorker worker = new SwingWorker() {
                public Object construct() {

                    mkdirCommand.getArguments().put("path",newPath);

                    try {
                        mkdirCommand.execute();
                    } catch (SessionException e) {
                        e.printStackTrace();
                    } catch (GMSException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }
                public void finished() {
                    //dbgui.stopWaiting();
                }
            };

            worker.start();

        } catch (Exception except) {
            except.printStackTrace();
        }
    }


    @Override
    public void statusChanged(StatusEvent event) {

    }


}
