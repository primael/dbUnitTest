package fr.nimrod.info.test.statement;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.google.common.io.Resources;

import fr.nimrod.info.test.annotations.Data;
import fr.nimrod.info.test.annotations.DataExpected;
import fr.nimrod.info.test.annotations.Schema;
import fr.nimrod.info.test.dataset.DataSetStrategy;

@Log4j2
@AllArgsConstructor
public class NimrodDbStatement extends Statement {

	private FrameworkMethod method;
	private Statement base;
	private Class<?> resourceBase;
	private BasicDataSource dataSource;

	@Override
	public void evaluate() throws Throwable {

		try {

			// test before
			Schema ddl = method.getAnnotation(Schema.class);
			if (ddl != null) {
				perform(ddl);
			}

			Data data = method.getAnnotation(Data.class);
			if (data != null) {
				perform(data);
			}

			// execute test
			base.evaluate();

		} finally {
			// ending test
			DataExpected expected = method.getAnnotation(DataExpected.class);
			if (expected != null) {
				verify(expected);
			}
		}
	}

	/**
	 * Methode permettant de jouer un ou des scripts sql passé en parametre.
	 * 
	 * @param ddl
	 *            l'annotation
	 * @throws IOException
	 * @throws SQLException
	 */
	private void perform(Schema ddl) throws IOException, SQLException {
		String[] values = ddl.value();
		for (String value : values) {
			log.debug("Decouverte d'une demande de script d'execution : " + value);
			String sql = Resources.toString(resourceBase.getResource(value), Charset.defaultCharset());

			@Cleanup Connection connection = dataSource.getConnection();

			@Cleanup java.sql.Statement statement = connection.createStatement();

			statement.executeQuery(sql);
			log.debug("Fin de l'execution du script");
		}
	}

	/**
	 * Méthode permettant d'injecter des données dans le schéma
	 * 
	 * @param data
	 * @throws DataSetException
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	private void perform(Data data) throws SQLException, DatabaseUnitException {
		log.debug("Decouverte d'une demande d'ajout de données");
		String[] dataSetFiles = data.value();
		List<IDataSet> dataSets = new ArrayList<IDataSet>(dataSetFiles.length);
		for (String dataSetFile : dataSetFiles) {
			log.debug("Ajout des données : " + dataSetFile);
			IDataSet ds = DataSetStrategy.getImplementation(dataSetFile, resourceBase);
			dataSets.add(ds);
		}
		CompositeDataSet dataSet = new CompositeDataSet(dataSets.toArray(new IDataSet[dataSets.size()]));
		DatabaseDataSourceConnection databaseDataSourceConnection = new DatabaseDataSourceConnection(dataSource);
		IDataSet fkDataSet = new FilteredDataSet(new DatabaseSequenceFilter(databaseDataSourceConnection), dataSet);
		DatabaseOperation.INSERT.execute(databaseDataSourceConnection, fkDataSet);
		log.debug("Ajout des données effectuées.");
	}

	private void verify(DataExpected dataExpected) throws SQLException, DatabaseUnitException {
		boolean flagFail = false;
		DatabaseDataSourceConnection databaseDataSourceConnection = new DatabaseDataSourceConnection(dataSource);
		SortedTable sortedTableActual = new SortedTable(databaseDataSourceConnection.createQueryTable(
				dataExpected.tableName(), "select * from " + dataExpected.tableName()));
		sortedTableActual.setUseComparable(true);
		SortedTable sortedTableExpected = new SortedTable(DataSetStrategy.getImplementation(dataExpected.file(),
				resourceBase).getTable(dataExpected.tableName()));
		sortedTableExpected.setUseComparable(true);

		ITable tableActual = sortedTableActual;
		ITable tableExpected = sortedTableExpected;
		if (dataExpected.ignoredColumn().length > 0) {
			tableActual = DefaultColumnFilter.excludedColumnsTable(sortedTableActual, dataExpected.ignoredColumn());
			tableExpected = DefaultColumnFilter.excludedColumnsTable(sortedTableExpected, dataExpected.ignoredColumn());
		}

		DiffCollectingFailureHandler diffCollectingHandler = new DiffCollectingFailureHandler();

		Assertion.assertEquals(tableExpected, tableActual, diffCollectingHandler);

		@SuppressWarnings("unchecked")
		List<Difference> diffList = diffCollectingHandler.getDiffList();
		for (Difference difference : diffList) {
			flagFail = true;
			StringBuilder message = new StringBuilder();
			message.append("Difference trouvé sur l'enregistrement n°" + (difference.getRowIndex() + 1));
			message.append(" sur la colonne " + difference.getColumnName());
			message.append(" valeur attendue " + difference.getExpectedValue());
			message.append(" valeur trouvée " + difference.getActualValue());
			log.error(message.toString());
		}

		if (flagFail) {
			Assert.fail();
		}
	}
}
