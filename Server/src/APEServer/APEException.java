package APEServer;

public class APEException extends Exception {
	private static final long serialVersionUID = 1L;
	private String text;

	public APEException(String message) {
		this.text = message;
	}

	@Override
	public String getMessage() {
		return this.text;
	}

}
