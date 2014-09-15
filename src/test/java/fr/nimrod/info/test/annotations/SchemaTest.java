package fr.nimrod.info.test.annotations;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class SchemaTest {

	@Test
	@Schema({"test"})
	public void attributeExistance() throws NoSuchMethodException, SecurityException {
		
		Method method = this.getClass().getMethod("attributeExistance", new Class[0]);
		
		Schema schema = method.getAnnotation(Schema.class);
		
		Assert.assertNotNull("L'annotation n'a pas �t� trouv�s", schema);
		
		Assert.assertNotNull("La valeur de l'annotation n'est pas r�cup�r�s", schema.value());
	}
}
