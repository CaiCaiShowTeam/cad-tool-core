package priv.lee.cad.model;

public interface ServerClientTemporary extends ClientTemporary {

	public String getServer();

	public String getUserName();

	public String getUserPasswd();

	public boolean isRememberMe();
}
