package priv.lee.cad.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import priv.lee.cad.bean.HandleResult;
import priv.lee.cad.constant.RMIMethod;
import wt.method.RemoteMethodServer;

public class ClientInstanceUtils {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BASIC = "Basic ";
	private static final String HOME = "user.home";
	public static boolean login = false;
	private static String RMI_SERVER_SERVICE = "rmi.server.service";
	private static RemoteMethodServer rms;
	private static final String SEPREATOR = ":";
	private static final String service = PropertiesUtils.readProperty(RMI_SERVER_SERVICE);
	private static final String TEMP_DIR = "temporary.directory";
	private static final String TEMP_FILE = "temporary.file";

	public static synchronized boolean connect(String server, String user, String passwd) {
		ClientAssert.hasText(server, "Remote server is required");
		ClientAssert.hasText(user, "User is required");
		ClientAssert.hasText(passwd, "Password is required");

		try {
			initRemoteMethodServer(server, user, passwd);

			HttpURLConnection conn = (HttpURLConnection) new URL(server).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.addRequestProperty(AUTHORIZATION, toAuthorization(user, passwd));
			conn.connect();
			return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static File getTemporaryDirectory() {
		String tempDir = PropertiesUtils.readProperty(TEMP_DIR);
		ClientAssert.hasText(tempDir, "Missing client instance temporary direction");

		File directory = new File(System.getProperty(HOME) + File.separator + tempDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}

	public static File getTemporaryFile() {
		return new File(getTemporaryDirectory(), getTemporaryFileName());
	}

	public static String getTemporaryFileName() {
		String name = PropertiesUtils.readProperty(TEMP_FILE);
		ClientAssert.hasText(name, "Missing client instance temporary file");
		return name;
	}

	public static String getWelcomeMessage() {
		return invoke(RMIMethod.GET_WELCOME_MESSAGE, null, null, String.class);
	}

	private static void initRemoteMethodServer(String server, String user, String passwd) throws MalformedURLException {
		ClientInstanceUtils.rms = RemoteMethodServer.getInstance(new URL(server));
		ClientInstanceUtils.rms.setUserName(user);
		ClientInstanceUtils.rms.setPassword(passwd);
	}

	@SuppressWarnings("unchecked")
	public static <T> T invoke(String method, Class<?>[] argTypes, Object[] argValues, Class<T> returnType) {
		ClientAssert.hasText(method, "RMI method is required");
		ClientAssert.notNull(returnType, "Return value type is required");
		ClientAssert.notNull(rms, "Remote method server had not initialized");

		try {
			HandleResult<T> result = (HandleResult<T>) rms.invoke(method, service, null,
					argTypes == null ? new Class<?>[] {} : argTypes, argValues == null ? new Object[] {} : argValues);

			ClientAssert.notNull(result, "Fatal error: HandleResult is null");
			ClientAssert.isTrue(result.getCode() == HandleResult.OK, result.getMessage());
			return result.getResult();
		} catch (RemoteException e) {
			ClientAssert.hasText(e.getLocalizedMessage(), e.getLocalizedMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			ClientAssert.hasText(e.getLocalizedMessage(), e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static String toAuthorization(String user, String passwd) {
		return BASIC + new String(Base64Utils.encode((user + SEPREATOR + passwd).getBytes()));
	}
}
