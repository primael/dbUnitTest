package fr.nimrod.info.test.annotations;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class DataExpectedTest {

	@Test
	@DataExpected(file="test", tableName="test")
	public void attributeExistance() throws NoSuchMethodException, SecurityException {
		
		Method method = this.getClass().getMethod("attributeExistance", new Class[0]);
		
		DataExpected dataExpected = method.getAnnotation(DataExpected.class);
		
		Assert.assertNotNull("L'annotation n'a pas été trouvés", dataExpected);
		
		Assert.assertNotNull("La valeur de file de l'annotation n'est pas récupérés", dataExpected.file());
		
		Assert.assertNotNull("La valeur de tableName de l'annotation n'est pas récupérés", dataExpected.tableName());
	}
}
