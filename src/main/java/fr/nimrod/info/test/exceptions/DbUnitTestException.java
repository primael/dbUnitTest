package fr.nimrod.info.test.exceptions;

import org.dbunit.DatabaseUnitException;


public class DbUnitTestException extends DatabaseUnitException {

	private static final long serialVersionUID = 1L;

	public DbUnitTestException(String message, Exception e) {
		super(message, e);
	}

}
