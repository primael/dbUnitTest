package fr.nimrod.info.test.dataSet;

import lombok.SneakyThrows;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import fr.nimrod.info.test.dataSet.implementation.JsonDataSet;

public interface DataSetStrategy {

	@SneakyThrows
	static AbstractDataSet getImplementation(String file, Class<?> resourceBase) throws IllegalStateException {
		if(file.endsWith(".json")){
			return new JsonDataSet(resourceBase.getResourceAsStream(file));
		}
		if(file.endsWith(".xml")) {
			return new FlatXmlDataSetBuilder().build(resourceBase.getResourceAsStream(file));
		}
		throw new IllegalStateException("ged-test ne supporte que le format json et xml... Pour l'instant");
	}
}
