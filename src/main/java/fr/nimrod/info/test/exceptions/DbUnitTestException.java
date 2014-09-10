package fr.nimrod.info.test.exceptions;


public class DbUnitTestException extends Exception {

	public DbUnitTestException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = 1L;

}
