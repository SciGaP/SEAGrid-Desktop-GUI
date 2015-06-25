package org.apache.airavata.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by dimuthuupeksha on 6/23/15.
 */
public class FileManager {
    public static boolean uploadFile(String localFile, String remoteFile){
        JSch jsch = new JSch();
        String privateKey = "~/.ssh/id_rsa";
        Session session = null;
        try {
            jsch.addIdentity(privateKey);
            session = jsch.getSession("airavata","gw111.iu.xsede.org",22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp channel = null;
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            File locaF = new File(localFile);
            File remoteF = new File(remoteFile);
            //If you want you can change the directory using the following line.
            channel.mkdir(remoteF.getParent());
            channel.cd(remoteF.getParent());
            channel.put(new FileInputStream(locaF), remoteF.getName());
            channel.disconnect();
            session.disconnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
