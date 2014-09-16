package fr.nimrod.info.test.annotations;

import java.lang.reflect.Method;



import org.junit.Assert;
import org.junit.Test;

import fr.nimrod.info.test.annotations.Data.Phase;

public class DataTest {

	@Test
	@Datas({
		@Data(phase=Phase.AFTER, value="test.xml")
	})
	public void attributeExistance() throws NoSuchMethodException, SecurityException {
		
		Method method = this.getClass().getMethod("attributeExistance", new Class[0]);
		
		Datas datas = method.getAnnotation(Datas.class);
		
		Assert.assertNotNull("L'annotation n'a pas été trouvés", datas);
		
		Assert.assertNotNull("La valeur de l'annotation n'est pas récupérés", datas.value());
		
		Assert.assertEquals("Le nombre de sous annotaion ramene n'est pas egal à 1", 1, datas.value().length);
	}
}
