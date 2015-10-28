package org.apache.airavata.gridchem.file;

import java.io.*;

import org.apache.airavata.AiravataConfig;
import org.apache.airavata.services.impl.SampleFileResourceClient;
import org.apache.commons.lang.Validate;
import org.apache.oodt.cas.filemgr.datatransfer.DataTransfer;
import org.apache.oodt.cas.filemgr.datatransfer.RemoteDataTransferFactory;
import org.apache.oodt.cas.filemgr.ingest.StdIngester;
import org.apache.oodt.cas.filemgr.metadata.CoreMetKeys;
import org.apache.oodt.cas.filemgr.structs.Product;
import org.apache.oodt.cas.filemgr.structs.exceptions.ConnectionException;
import org.apache.oodt.cas.filemgr.system.XmlRpcFileManagerClient;
import org.apache.oodt.cas.metadata.Metadata;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FileBrowserAiravata {
    private static final String transferServiceFacClass = "org.apache.oodt.cas.filemgr.datatransfer.RemoteDataTransferFactory";
    private static final String FILE_SERVER_URL = AiravataConfig.getProperty("file_server_url");
    private XmlRpcFileManagerClient client;
    private static  SampleFileResourceClient sampleFileResourceClient = new SampleFileResourceClient();

    public FileBrowserAiravata(){

    }

    /**
     * Uploads files to file server
     * @param fileLocationDir parent directory of file
     * @param srcName file name with extension
     * @param destName product name of the destination file. If this is null, srcName is used
     * @param productType default type is "GenericFile"
     */
    public String ingestFile(String fileLocationDir, String srcName, String destPath, String destName, String productType) throws FileHandlerException {
//        StdIngester ingester;
//        ingester = new StdIngester(transferServiceFacClass);
//        Metadata prodMet = new Metadata();
//        prodMet.addMetadata("Filename", srcName);
//        prodMet.addMetadata("ProductName", destName);
//        prodMet.addMetadata("ProductType", productType);
//        prodMet.addMetadata("DataVersion", "1.0");
//
//        try {
//            prodMet.addMetadata(CoreMetKeys.FILE_LOCATION, fileLocationDir);
//            return ingester.ingest(new URL(getUrl()), new File(fileLocationDir+File.separator+srcName), prodMet);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new FileHandlerException(e.getMessage());
//        }
        sampleFileResourceClient.uploadFile(fileLocationDir+File.separator+srcName, destPath,
                destName);
        return "";
    }

    /**
     * Downloads a file from remote repository using file name or file id.
     * @param productId product id
     * @param productName product name. If product id is not known, give file name as product name
     * @param destination output directory
     */
    public void retrieveFile(String productId, String productName, File destination) throws FileHandlerException {
        try {
            DataTransfer dt = new RemoteDataTransferFactory().createDataTransfer();
            XmlRpcFileManagerClient fmClient = getClient();
            dt.setFileManagerUrl(fmClient.getFileManagerUrl());
            Product product;
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
            throw new FileHandlerException(e.getMessage());
        }
    }

    /**
     * Download a file from remote repository using file path
     * @param remoteFilePath
     * @throws IOException
     */
    public void retrieveFile(String remoteFilePath) throws IOException {
        String[] bits = AiravataConfig.getProperty("http_file_repo_location").split("/");
        String temp = bits[bits.length-1];
        final String remoteHttpPath = AiravataConfig.getProperty("http_file_repo_location") + remoteFilePath.split(temp)[1];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileDownloadDialog(remoteHttpPath).setVisible(true);
            }
        });
    }

    /**
     * Download a list of files from remote repository using file path
     * @param remoteFilePaths
     * @param outputPath
     * @throws IOException
     */
    public void retrieveFiles(List<String> remoteFilePaths, String outputPath){
        File outputDir = new File(outputPath);
        if(!outputDir.exists()){
            outputDir.mkdirs();
        }
        for(String remoteFilePath : remoteFilePaths) {
            String[] bits = AiravataConfig.getProperty("http_file_repo_location").split("/");
            String temp = bits[bits.length - 1];
            final String remoteHttpPath = AiravataConfig.getProperty("http_file_repo_location") + remoteFilePath.split(temp)[1];
            DownloadTask task = new DownloadTask(remoteHttpPath, outputPath);
            task.execute();
        }

        DesktopUtil.openAndWarn(new File(outputPath), null);
    }


    /**
     * Checks weather a product is available in the repository
     * @param productName product name
     * @return true if product is available. False otherwise
     * @throws FileHandlerException
     */
    public boolean hasProduct (String productName)  throws FileHandlerException {
        try {
//            return getClient().hasProduct(productName);
            return sampleFileResourceClient.isDirectoryExists(productName);
        } catch (Exception e) {
            throw new FileHandlerException(e.getMessage());
        }
    }

    private String getUrl() {
        return FILE_SERVER_URL;
    }

    private XmlRpcFileManagerClient getClient()
            throws MalformedURLException, ConnectionException {
        Validate.notNull(getUrl(), "Must specify url");

        if (client != null) {
            return client;
        } else {
            client = new XmlRpcFileManagerClient(new URL(getUrl()), false);
            return client;
        }
    }
}
