package fr.nimrod.info.test.sample.purejdbc.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Stack;

import lombok.SneakyThrows;

public class PoolConnection {

	private static volatile PoolConnection instance = null;
	
	static {
		instance = new PoolConnection();
	}
	
	
	public final static PoolConnection getInstance(){
		return instance;
	}
	
	private PoolConnection(){
		
	}
		
	
	private final String driverName = "";
	
	private final String url = "";
	
	private final String login = "";
	
	private final String password = "";
	
	private Stack<Connection> connections = new Stack<Connection>();
	
	public Connection getConnection(){
		if(connections.isEmpty())
			createConnection();
		return connections.pop();
	}
	
	@SneakyThrows
	public void releaseConnection(Connection connection){
		if(connection.isValid(1)){
			connections.push(connection);
		} else {
			connection.close();
		}	
	}
	
	@SneakyThrows
	private Connection createConnection(){
		System.out.println(this);
		Class.forName(driverName).newInstance();
		return DriverManager.getConnection(url, login, password);
	}
}
