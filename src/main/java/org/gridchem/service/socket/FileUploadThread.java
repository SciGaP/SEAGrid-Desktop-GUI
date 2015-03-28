package org.gridchem.service.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.asprise.util.ui.progress.ProgressDialog;

public class FileUploadThread extends Thread{
	private String userName;
	private String experimentName;
	private String jobName;
	private String fileName;
	private String localFilePath;
	private String uploadPath;

	private Socket socket = null;
	private int port = 8821;

	public static int bufferSize = 2048;

	public FileUploadThread() {
		;
	}

	public FileUploadThread(int bufferSize) {
		FileDownloadThread.bufferSize = bufferSize;
	}

	public FileUploadThread(String userName, String experimentName,
			String jobName, String fileName, String localFilePath) {
		this.userName = userName;
		this.experimentName = experimentName;
		this.jobName = jobName;
		this.fileName = fileName;
		this.localFilePath = localFilePath;

		this.uploadPath = "/tmp/" + this.userName + "/" + this.experimentName
				+ "/" + this.jobName + "/" + this.fileName;
	}

	public String read(BufferedInputStream bis) {
		byte[] contents = new byte[bufferSize];

		int bytesRead = 0;
		String string = null;
		try {
			bytesRead = bis.read(contents);
			string = new String(contents, 0, bytesRead);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return string;
	}

	public void run() {
		synchronized (this) {
			try {

				ProgressDialog progressDialog = new ProgressDialog(null,
						"Upload Progress");
				progressDialog.millisToPopup = 0;
				progressDialog.millisToDecideToPopup = 50;
				progressDialog.displayTimeLeft = false;
				progressDialog.showAnimation = true;
				progressDialog.beginTask("Sending " + fileName, 100, true);

				Thread.sleep(500);
				progressDialog.worked(0);
				
				Thread.sleep(500);

				//socket = new Socket("gridchem-mw.ncsa.illinois.edu", port);
				socket = new Socket("gridchem.uits.iu.edu", port);				

				BufferedInputStream bufferedSocketInputStream = new BufferedInputStream(
						socket.getInputStream(), bufferSize);

				BufferedOutputStream bufferedSocketOutputStream = new BufferedOutputStream(
						socket.getOutputStream(), bufferSize);

				bufferedSocketOutputStream.write("correct token".getBytes());
				bufferedSocketOutputStream.flush();

				String msg = null;
				msg = read(bufferedSocketInputStream);
				System.out.println(msg);
				if (!msg.equals("authenticated")) {
					return;
				}
				System.out.println("Successfully authenticated");

				bufferedSocketOutputStream.write("upload".getBytes());
				bufferedSocketOutputStream.flush();

				msg = read(bufferedSocketInputStream);
				System.out.println(msg);

				bufferedSocketOutputStream.write(new String(uploadPath)
						.getBytes());
				bufferedSocketOutputStream.flush();

				msg = read(bufferedSocketInputStream);
				System.out.println(msg);

				msg = read(bufferedSocketInputStream);
				System.out.println(msg);

				if (!msg.equals("Please send data stream")) {
					return;
				}

				File localFile = new File(localFilePath);
				
				if (!localFile.exists()) {
					System.out.println("Local file doen't exist");
					return;
				}
				
				long size = localFile.length();
				

				long onePercentChunk = size / bufferSize / 50;

				byte[] buf = new byte[bufferSize];

				BufferedInputStream bufferedFileInputStream = new BufferedInputStream(
						new FileInputStream(this.localFilePath), bufferSize);

				// System.out.println("File length: " + len + "\n");
				System.out.println("Sending file: " + this.fileName + "\n");

				long flag = 0;
				while (true) {
					int read = 0;
					if (bufferedFileInputStream != null) {
						read = bufferedFileInputStream.read(buf);
					}

					if (read == -1) {
						break;
					}

					bufferedSocketOutputStream.write(buf, 0, read);
					bufferedSocketOutputStream.flush();

					flag++;
					if (flag == onePercentChunk) {
						progressDialog.worked(2);
						flag = 0;
					}
				}

				System.out.println("File has been successfully sent\n");

				bufferedFileInputStream.close();
				bufferedSocketInputStream.close();
				bufferedSocketOutputStream.close();

				progressDialog.finished();
				/*
				 * while ((msg = br.readLine()) != null)
				 * System.out.println(msg);
				 */
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Closing socket");
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				notify();
			}
		}
	}
}
