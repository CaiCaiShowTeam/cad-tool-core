package priv.lee.cad.model;

public enum ResourceType {

	SHORTDESCRIPTION("shortDescription"), TEXT("text"), TITLE("title");

	String type;

	ResourceType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
