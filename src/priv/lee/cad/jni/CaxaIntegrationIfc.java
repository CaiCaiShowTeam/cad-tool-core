package priv.lee.cad.jni;

public class CaxaIntegrationIfc {

	public CaxaIntegrationIfc() {

	}

	public native int connect();

	public native void disconnect();

	public native String getCurrentCadDetail();

	public native void getCurrentCadPath();

	public native String getCurrentCadTitle();

	public native String getCurrentTechnology();
}
