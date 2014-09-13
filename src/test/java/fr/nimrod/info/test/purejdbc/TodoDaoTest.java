package fr.nimrod.info.test.purejdbc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.hsqldb.jdbcDriver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import fr.nimrod.info.test.annotations.Data;
import fr.nimrod.info.test.annotations.DataExpected;
import fr.nimrod.info.test.annotations.Datas;
import fr.nimrod.info.test.annotations.Schema;
import fr.nimrod.info.test.rule.NimrodDbRule;
import fr.nimrod.info.test.sample.purejdbc.model.Todo;
import fr.nimrod.info.test.sample.purejdbc.model.dao.PoolConnection;
import fr.nimrod.info.test.sample.purejdbc.model.dao.TodoDao;

public class TodoDaoTest {

	@Rule
	public NimrodDbRule dbUnitRule = new NimrodDbRule(TodoDaoTest.class, jdbcDriver.class, "jdbc:hsqldb:mem:database",
			"sa", "");
	
	private TodoDao instanceUnderTest;
	
	private PoolConnection poolMock;
	
	@Before
	public void before() throws SQLException{
		poolMock = mock(PoolConnection.class);
		when(poolMock.getConnection()).thenReturn(dbUnitRule.getDataSource().getConnection());
		instanceUnderTest = new TodoDao(){{
			setPool(poolMock);
		}};
	}
	
	@Test
	@Schema({"/jdbc/todo/todo.sql"})
	@DataExpected(file="/jdbc/todo/todo-expected.json", tableName="todo")
	public void createTodo() throws SQLException{
		Todo todo = new Todo();
		todo.setIdentifiant(1l);
		todo.setSummary("test");
		todo.setDescription("test corps");
		
		instanceUnderTest.persistTodo(todo);
	}
	
	@Test
	@Schema({"/jdbc/todo/todo.sql"})
	
	@Datas(
		@Data("/jdbc/todo/todo.json")
	)
	
	@DataExpected(file="/jdbc/todo/todo-expected.xml", tableName="todo")
	public void createTodoWithDataInject() throws SQLException{
		Todo todo = new Todo();
		todo.setIdentifiant(1l);
		todo.setSummary("test");
		todo.setDescription("test corps");
		
		instanceUnderTest.persistTodo(todo);
	}
	
	@Test
	@Schema({"/jdbc/todo/todo.sql"})
	@Datas(
		@Data("")
	)
	@DataExpected(file="/jdbc/todo/todo-expected.json", tableName="todo")
	public void createTodoWithDataEmptyInject() throws SQLException{
		Todo todo = new Todo();
		todo.setIdentifiant(1l);
		todo.setSummary("test");
		todo.setDescription("test corps");
		
		instanceUnderTest.persistTodo(todo);
	}
	
	@Test
	@Schema({"/jdbc/todo/todo.sql"})
	@Datas({
		@Data(""),
		@Data("/jdbc/todo/todo.json")
	})
	@DataExpected(file="/jdbc/todo/todo-expected.xml", tableName="todo")
	public void createTodoWithOneDataEmptyAndOneDataInject() throws SQLException{
		Todo todo = new Todo();
		todo.setIdentifiant(1l);
		todo.setSummary("test");
		todo.setDescription("test corps");
		
		instanceUnderTest.persistTodo(todo);
	}
	
	/*@Test(expected=DbUnitParameterizedException.class)
	@Schema({"/jdbc/todo/todo.sql"})
	@DataExpected(file="", tableName="todo")
	public void createTodoWithDataExpectedFileEmptyInject() throws SQLException{
		Todo todo = new Todo();
		todo.setIdentifiant(1l);
		todo.setSummary("test");
		todo.setDescription("test corps");
		
		instanceUnderTest.persistTodo(todo);
	}*/
}
