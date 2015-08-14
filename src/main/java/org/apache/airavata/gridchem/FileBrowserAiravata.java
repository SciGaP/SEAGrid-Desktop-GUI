package org.apache.airavata.gridchem;

import org.apache.commons.lang.Validate;
import org.apache.oodt.cas.filemgr.datatransfer.DataTransfer;
import org.apache.oodt.cas.filemgr.datatransfer.RemoteDataTransferFactory;
import org.apache.oodt.cas.filemgr.datatransfer.RemoteDataTransferer;
import org.apache.oodt.cas.filemgr.ingest.StdIngester;
import org.apache.oodt.cas.filemgr.metadata.CoreMetKeys;
import org.apache.oodt.cas.filemgr.structs.Product;
import org.apache.oodt.cas.filemgr.structs.exceptions.ConnectionException;
import org.apache.oodt.cas.filemgr.system.XmlRpcFileManagerClient;
import org.apache.oodt.cas.metadata.Metadata;
import org.apache.oodt.cas.metadata.SerializableMetadata;
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
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dimuthuupeksha on 6/14/15.
 */
public class FileBrowserAiravata {
    private static final String transferServiceFacClass = "org.apache.oodt.cas.filemgr.datatransfer.RemoteDataTransferFactory";
    private static final String FILE_SERVER = "http://gw111.iu.xsede.org";
    private static final int PORT = 9000;
    private XmlRpcFileManagerClient client;

    /**
     * Uploads files to file server
     * @param fileLocationDir parent directory of file
     * @param fileName file name with extension
     * @param productType default type is "GenericFile"
     */
    public void ingestFile(String fileLocationDir, String fileName, String productType){
        StdIngester ingester;
        ingester = new StdIngester(transferServiceFacClass);
        Metadata prodMet = new Metadata();
        prodMet.addMetadata("Filename", fileName);
        prodMet.addMetadata("ProductType", productType);
        prodMet.addMetadata("DataVersion", "1.0");

        try {
            prodMet.addMetadata(CoreMetKeys.FILE_LOCATION, fileLocationDir);

            ingester.ingest(new URL(getUrl()), new File(fileLocationDir+File.separator+fileName), prodMet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a file from remote repository
     * @param productId product id
     * @param productName product name. If product id is not known, give file name as product name
     * @param destination output directory
     */
    public void retrieveFile(String productId, String productName, File destination) {
        try {
            DataTransfer dt = new RemoteDataTransferFactory().createDataTransfer();
            XmlRpcFileManagerClient fmClient = getClient();
            dt.setFileManagerUrl(fmClient.getFileManagerUrl());
            Product product = null;
            if (productId != null) {
                product = fmClient.getProductById(productId);
            } else if (productName != null) {
                product = fmClient.getProductByName(productName);
            } else {
                throw new Exception("Must specify either productId or productName");
            }
            if (product != null) {
                product.setProductReferences(fmClient.getProductReferences(product));
                dt.retrieveProduct(product, destination);
            } else {
                throw new Exception("Product was not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getUrl() {
        return FILE_SERVER + ":" + PORT;
    }

    private XmlRpcFileManagerClient getClient()
            throws MalformedURLException, ConnectionException {
        Validate.notNull(getUrl(), "Must specify url");

        if (client != null) {
            return client;
        } else {
            return new XmlRpcFileManagerClient(new URL(getUrl()), false);
        }
    }

    public static void main (String args[]) {
        FileBrowserAiravata fb = new FileBrowserAiravata();
        //fb.ingestFile("/tmp", "blah.txt", "GenericFile");
        fb.retrieveFile(null, "blah.txt", new File("/tmp/out"));
    }

}
