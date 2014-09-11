package fr.nimrod.info.test.sample.purejdbc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import fr.nimrod.info.test.sample.purejdbc.model.Todo;


public class TodoDao {

	private static final String ADD_TODO = "INSERT INTO TODO (id, summary, description) values (?, ?, ?)";
	
	@Getter
	@Setter
	private PoolConnection pool;

	public void persistTodo(Todo todo) throws SQLException{
		Connection connection = getPool().getConnection();
		
		@Cleanup PreparedStatement preparedStatement = connection.prepareStatement(ADD_TODO);
		preparedStatement.setLong(1, todo.getIdentifiant());
		preparedStatement.setString(2, todo.getSummary());
		preparedStatement.setString(3, todo.getDescription());
		preparedStatement.executeUpdate();
		
		connection.commit();
		
		getPool().releaseConnection(connection);
	}
	
	public void findTodo(Long identifiant){
		
	}
	
	public void getAllTodo(){
		
	}
	
	
	
}
