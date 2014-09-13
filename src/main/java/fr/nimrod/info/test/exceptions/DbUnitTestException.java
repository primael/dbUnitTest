package fr.nimrod.info.test.exceptions;

import org.dbunit.DatabaseUnitException;


public class DbUnitTestException extends DatabaseUnitException {

	public DbUnitTestException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = 1L;

}
