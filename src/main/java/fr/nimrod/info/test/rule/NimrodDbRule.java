package fr.nimrod.info.test.rule;

import lombok.SneakyThrows;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import fr.nimrod.info.test.statement.NimrodDbStatement;

public class NimrodDbRule implements MethodRule {

	private Class<?> resourceBase;
//	private IDatabaseConnection dbUnitConnection;
	private BasicDataSource dataSource;

	@SneakyThrows
	public NimrodDbRule(Class<?> resourceBase, Class<?> driver, String url, String user, String password) {

		this.dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driver.getCanonicalName());
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);

		this.resourceBase = resourceBase;
	}

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
		return new NimrodDbStatement(method, base, resourceBase, dataSource);
	}
	
//	public IDataSet jsonDataSet(String datasetResource) {
//		return new JsonDataSet(resourceBase.getResourceAsStream(datasetResource));
//	}

//	public ITable createQueryTable(String string, String string2) throws DataSetException, SQLException {
//		return dbUnitConnection.createQueryTable(string, string2);
//	}
}