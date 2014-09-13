package fr.nimrod.info.test.exceptions;

public class DbUnitParameterizedException extends DbUnitTestException{

	private static final long serialVersionUID = 1L;

	public DbUnitParameterizedException(String message, Exception e) {
		super(message, e);
	}


}
