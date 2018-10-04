package priv.lee.cad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FTPUtils {

	private static FTPUtils utils;

	public static FTPUtils newInstance() {
		if (utils == null) {
			utils = new FTPUtils();
		}
		return utils;
	}

	private FTPClient ftpClient;
	private String host;
	private final String HOST = "ftp.host";
	private String passwd;
	private final String PASSWD = "ftp.passwd";
	private String path;
	private final String PATH = "ftp.path";
	private String port;
	private final String PORT = "ftp.port";
	private String user;
	private final String USER = "ftp.user";
	private final String UTF_8 = "UTF-8";

	private FTPUtils() {
		this.host = PropertiesUtils.readProperty(HOST);
		this.port = PropertiesUtils.readProperty(PORT);
		this.path = PropertiesUtils.readProperty(PATH);
		this.user = PropertiesUtils.readProperty(USER);
		this.passwd = PropertiesUtils.readProperty(PASSWD);
	}

	public boolean connect() {
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}

		boolean login = false;
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(host, Integer.parseInt(port));
			login = ftpClient.login(user, passwd);
			if (login) {
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ftpClient = null;
		}
		return login;
	}

	public boolean delete(File serverFile) {
		ClientAssert.notNull(serverFile, "FTP server file is required");
		try {
			ftpClient.changeWorkingDirectory(path);
			return ftpClient.deleteFile(new String(serverFile.getName().getBytes(UTF_8), UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void disconnect() {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean download(File serverFile, File localFile) {
		ClientAssert.notNull(serverFile, "FTP server file is required");
		ClientAssert.notNull(localFile, "Local file is required");

		boolean download = false;
		if (!connect()) {
			return download;
		}

		try {
			ftpClient.changeWorkingDirectory(path);
			ftpClient.enterLocalPassiveMode();
			FileOutputStream output = new FileOutputStream(localFile);
			download = ftpClient.retrieveFile(new String(serverFile.getName().getBytes(UTF_8), UTF_8), output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return download;
	}

	public boolean upload(File localFile) throws IOException {
		ClientAssert.notNull(localFile, "Local file is required");

		boolean upload = false;
		if (!connect()) {
			return upload;
		}

		FileInputStream input = null;
		try {
			input = new FileInputStream(localFile);
			ftpClient.changeWorkingDirectory(path);
			ftpClient.enterLocalPassiveMode();
			upload = ftpClient.storeFile(new String(localFile.getName().getBytes(UTF_8), UTF_8), input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return upload;
	}
}
