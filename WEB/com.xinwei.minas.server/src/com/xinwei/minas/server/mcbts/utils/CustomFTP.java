/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-25	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 基于SimpleFTP定制的FTP 增加了扫描列表的功能
 * 
 * 
 * @author tiance
 * 
 */

public class CustomFTP {
	private Log log = LogFactory.getLog(CustomFTP.class);
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;

	private static boolean DEBUG = false;

	/**
	 * Connects to the default port of an FTP server and logs in as
	 * anonymous/anonymous.
	 */
	public synchronized void connect(String host) throws IOException {
		connect(host, 21);
	}

	/**
	 * Connects to an FTP server and logs in as anonymous/anonymous.
	 */
	public synchronized void connect(String host, int port) throws IOException {
		connect(host, port, "anonymous", "anonymous");
	}

	/**
	 * Connects to an FTP server and logs in with the supplied username and
	 * password.
	 */
	public synchronized void connect(String host, int port, String user,
			String pass) throws IOException {
		if (socket != null) {
			throw new IOException(
					"SimpleFTP is already connected. Disconnect first.");
		}
		socket = new Socket(host, port);
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));

		String response = readLine();
		while (!response.startsWith("220 ")) {
			response = readLine();
		}

		if (!response.startsWith("220 ")) {
			throw new IOException(
					"SimpleFTP received an unknown response when connecting to the FTP server: "
							+ response);
		}

		sendLine("USER " + user);

		response = readLine();
		if (!response.startsWith("331 ")) {
			throw new IOException(
					"SimpleFTP received an unknown response after sending the user: "
							+ response);
		}

		sendLine("PASS " + pass);

		response = readLine();
		if (!response.startsWith("230 ")) {
			throw new IOException(
					"SimpleFTP was unable to log in with the supplied password: "
							+ response);
		}

		// Now logged in.
	}

	/**
	 * Disconnects from the FTP server.
	 */
	public synchronized void disconnect() throws IOException {
		try {
			sendLine("QUIT");
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
			}
			socket = null;
		}
	}

	/**
	 * Returns the working directory of the FTP server it is connected to.
	 */
	public synchronized String pwd() throws IOException {
		sendLine("PWD");
		String dir = null;
		String response = readLine();
		if (response.startsWith("257 ")) {
			int firstQuote = response.indexOf('\"');
			int secondQuote = response.indexOf('\"', firstQuote + 1);
			if (secondQuote > 0) {
				dir = response.substring(firstQuote + 1, secondQuote);
			}
		}
		return dir;
	}

	/**
	 * Changes the working directory (like cd). Returns true if successful.
	 */
	public synchronized boolean cwd(String dir) throws IOException {
		sendLine("CWD " + dir);
		String response = readLine();
		return (response.startsWith("250 "));
	}

	/**
	 * Sends a file to be stored on the FTP server. Returns true if the file
	 * transfer was successful. The file is sent in passive mode to avoid NAT or
	 * firewall problems at the client end.
	 */
	public synchronized boolean stor(File file) throws IOException {
		if (file.isDirectory()) {
			throw new IOException("SimpleFTP cannot upload a directory.");
		}

		String filename = file.getName();

		return stor(new FileInputStream(file), filename);
	}

	/**
	 * Sends a file to be stored on the FTP server. Returns true if the file
	 * transfer was successful. The file is sent in passive mode to avoid NAT or
	 * firewall problems at the client end.
	 */
	public synchronized boolean stor(InputStream inputStream, String filename)
			throws IOException {

		BufferedInputStream input = new BufferedInputStream(inputStream);

		sendLine("PASV");
		String response = readLine();
		if (!response.startsWith("227 ")) {
			throw new IOException("SimpleFTP could not request passive mode: "
					+ response);
		}

		String ip = null;
		int port = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);
		if (closing > 0) {
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken() + "." + tokenizer.nextToken();
				port = Integer.parseInt(tokenizer.nextToken()) * 256
						+ Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException(
						"SimpleFTP received bad data link information: "
								+ response);
			}
		}

		sendLine("STOR " + filename);

		Socket dataSocket = new Socket(ip, port);

		BufferedOutputStream output = null;
		try {
			response = readLine();
			if (!response.startsWith("150 ")) {
				throw new IOException(
						"SimpleFTP was not allowed to send the file: " + response);
			}		
			output = new BufferedOutputStream(dataSocket.getOutputStream());
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			output.flush();

			response = readLine();
			return response.startsWith("226 ");
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataSocket != null) {
				dataSocket.close();
			}
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
		}
	}

	public synchronized void delall(String dirname) throws Exception {
		// cd root
		// sendLine("CWD /");
		// String response = readLine();
		// if(!response.startsWith("250 ")){
		// throw new Exception("ftp client: failed to cwd root directory!");
		// }

		String response;
		String dirnames[] = dirname.split("/");
		for (int i = 0; i < dirnames.length; i++) {
			sendLine("CWD " + dirnames[i]);
			response = readLine();
			if (!response.startsWith("250 ")) {
				throw new Exception("ftp client: failed to cwd directory"
						+ dirnames[i] + "!");
			}
		}

		// lst all file name
		sendLine("PASV");
		response = readLine();
		if (!response.startsWith("227 ")) {
			throw new IOException("SimpleFTP could not request passive mode: "
					+ response);
		}

		String ip = null;
		int port = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);
		if (closing > 0) {
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken() + "." + tokenizer.nextToken();
				port = Integer.parseInt(tokenizer.nextToken()) * 256
						+ Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException(
						"SimpleFTP received bad data link information: "
								+ response);
			}
		}

		sendLine("NLST ");
		Socket dataSocket = new Socket(ip, port);
		BufferedInputStream input = null;
		try {
			response = readLine();
			if (response.startsWith("550 ")) {
				// no file need to delete
				// dataSocket.close();
				return;
			} else if (!response.startsWith("150 ")) {
				throw new IOException(
						"SimpleFTP was not allowed to send the file: " + response);
			}
			input = new BufferedInputStream(dataSocket.getInputStream());
			// start thread to read until 226 return
			ListResultReader reader = new ListResultReader(input, this);
			reader.start();

			response = readLine();
			if (!response.startsWith("226 ")) {
				throw new IOException("SimpleFTP delete files failed!");
			}
			reader.setFinished(true);
			reader.join();
			if (!reader.isFinished()) {
				throw new IOException("SimpleFTP delete files failed!");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataSocket != null) {
				dataSocket.close();
			}
			if (input != null) {
				input.close();
			}
		}
	}

	class ListResultReader extends Thread {
		public boolean finished = false;
		InputStream in;
		CustomFTP sftp;

		public ListResultReader(InputStream in, CustomFTP sftp) {
			this.in = in;
			this.sftp = sftp;
		}

		BufferedReader mreader;
		private long lastrecv = System.currentTimeMillis() + 10000000;

		public void run() {
			// read
			try {
				mreader = new BufferedReader(new InputStreamReader(in));
				while (!finished) {
					String sz = mreader.readLine();
					if (sz == null) {
						finished = true;
						return;
					}
					lastrecv = System.currentTimeMillis();
					sftp.sendLine("DELE " + sz);
					String response = sftp.readLine();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				finished = false;
			} finally {
				if (mreader != null) {
					try {
						mreader.close();
					} catch (IOException e) {
						log.error(e);
					}
				}
			}
		}

		/**
		 * @return Returns the deleted.
		 */
		public boolean isFinished() {
			return finished;
		}

		/**
		 * @param finished
		 *            The finished to set.
		 */
		public void setFinished(boolean finished) {
			while (true) {
				// 5秒之内没有收到数据，取消reader
				if (System.currentTimeMillis() - lastrecv > 5000) {
					this.finished = finished;
					try {
						mreader.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return;
				}
				Object mutex = new Object();
				synchronized (mutex) {
					try {
						mutex.wait(5000);
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
			}
		}
	}

	public synchronized void mkdir(String dirname) throws IOException,
			Exception {
		// cd root
		// sendLine("CWD /");
		// String response = readLine();
		// if(!response.startsWith("250 ")){
		// throw new Exception("ftp client: failed to cwd root directory!");
		// }

		String response;

		// split dirname to string array
		String dirnames[] = dirname.split("/");
		for (int i = 0; i < dirnames.length; i++) {
			sendLine("CWD " + dirnames[i]);
			response = readLine();
			if (!response.startsWith("250 ")) {
				sendLine("MKD " + dirnames[i]);
				response = readLine();
				if (!response.startsWith("257 ")) {
					throw new Exception("ftp client: failed to mkdir "
							+ dirnames[i] + "!");
				}
				sendLine("CWD " + dirnames[i]);
				response = readLine();
				if (!response.startsWith("250 ")) {
					throw new Exception("ftp client: failed to mkdir "
							+ dirnames[i] + "!");
				}
			}
		}
	}

	public synchronized void del(String filename) throws IOException, Exception {
		// cd root
		// sendLine("CWD /");
		// String response = readLine();
		// if(!response.startsWith("250 ")){
		// throw new Exception("ftp client: failed to cwd root directory!");
		// }

		sendLine("DELE " + filename);
		String response = readLine();
		if (!response.startsWith("250 ")) {
			throw new Exception("ftp client: failed to delete file-" + filename
					+ "\n" + response);
		}
	}

	/**
	 * Enter binary mode for sending binary files.
	 */
	public synchronized boolean bin() throws IOException {
		sendLine("TYPE I");
		String response = readLine();
		return (response.startsWith("200 "));
	}

	/**
	 * Enter ASCII mode for sending text files. This is usually the default
	 * mode. Make sure you use binary mode if you are sending images or other
	 * binary data, as ASCII mode is likely to corrupt them.
	 */
	public synchronized boolean ascii() throws IOException {
		sendLine("TYPE A");
		String response = readLine();
		return (response.startsWith("200 "));
	}

	/**
	 * Sends a raw command to the FTP server.
	 */
	private void sendLine(String line) throws IOException {
		if (socket == null) {
			throw new IOException("SimpleFTP is not connected.");
		}
		try {
			writer.write(line + "\r\n");
			writer.flush();
			if (DEBUG) {
				log.debug("> " + line);
			}
		} catch (IOException e) {
			socket = null;
			throw e;
		}
	}

	private String readLine() throws IOException {
		String line = reader.readLine();
		if (DEBUG) {
			log.debug("< " + line);
		}
		return line;
	}

	public List<String> listAll(String dirname) throws Exception {
		List<String> filenames = new ArrayList<String>();
		String response;
		String dirnames[] = dirname.split("/");
		for (int i = 0; i < dirnames.length; i++) {
			sendLine("CWD " + dirnames[i]);
			response = readLine();
			if (!response.startsWith("250 ")) {
				throw new Exception("ftp client: failed to cwd directory"
						+ dirnames[i] + "!");
			}
		}

		// lst all file name
		sendLine("PASV");
		response = readLine();
		if (!response.startsWith("227 ")) {
			throw new IOException("SimpleFTP could not request passive mode: "
					+ response);
		}

		String ip = null;
		int port = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);
		if (closing > 0) {
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken() + "." + tokenizer.nextToken();
				port = Integer.parseInt(tokenizer.nextToken()) * 256
						+ Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException(
						"SimpleFTP received bad data link information: "
								+ response);
			}
		}

		sendLine("NLST ");
		Socket dataSocket = new Socket(ip, port);	
		BufferedReader mreader = null;
		try {
			response = readLine();
			if (response.startsWith("550 ")) {
				// dataSocket.close();
				return null;
			} else if (!response.startsWith("150 ")) {
				throw new IOException(
						"SimpleFTP was not allowed to send the file: " + response);
			}

			BufferedInputStream input = new BufferedInputStream(
					dataSocket.getInputStream());
			mreader = new BufferedReader(new InputStreamReader(input));
			String sz;
			while ((sz = mreader.readLine()) != null) {
				filenames.add(sz);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dataSocket != null) {
				dataSocket.close();
			}
			if (mreader != null) {
				mreader.close();
			}
		}
		return filenames;
	}

}