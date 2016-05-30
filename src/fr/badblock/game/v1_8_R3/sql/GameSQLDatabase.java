package fr.badblock.game.v1_8_R3.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;

import com.google.common.collect.Queues;

import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSQLDatabase implements SQLDatabase {
	private final String hostname, port, username, password, database;
	protected Connection connection;
	
	protected Queue<String> toSave = Queues.newLinkedBlockingDeque();
	
	protected Thread saving = new Thread(){
		@Override
		public void run(){
			while(true){
				String update = null;
				
				while((update = toSave.poll()) != null){
					try {
						update(update, true);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e){}
			}
		}
	};
	
	{
		saving.start();
	}
	
	public Connection openConnection() throws SQLException, ClassNotFoundException {
		if (checkConnection()) {
			return this.connection;
		}
		
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection("jdbc:mysql://" + 
				this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.username, this.password);
		
		return this.connection;
	}
	
	public boolean checkConnection() throws SQLException {
		return (this.connection != null) && (!this.connection.isClosed());
	}
	
	public void closeConnection() throws SQLException {
		if(checkConnection());
			connection.close();
	}
	
	@Override
	public Statement createStatement() throws Exception {
		if(!checkConnection()) {
			openConnection();
		}
		
		return connection.createStatement();
	}
	
	@Override
	public PreparedStatement preparedStatement(String request) throws Exception {
		if(!checkConnection()) {
			openConnection();
		}
		
		return connection.prepareStatement(request);
	}
	
	@Override
	public ResultSet query(String request) throws Exception {
		if(!checkConnection()) {
			openConnection();
		}
		
		Statement statement = createStatement();
		statement.closeOnCompletion();
		
		ResultSet result = statement.executeQuery(request);

		return result;
	}

	@Override
	public void queryAsynchronously(String request, Callback<ResultSet> callback) {
		new Thread(){
			@Override
			public void run(){
				ResultSet result = null;
				Throwable error  = null;
				
				try {
					result = query(request);
				} catch (Throwable t) {
					error  = t;
				}
				
				callback.done(result, error);
			}
		}.start();
	}

	@Override
	public void update(String request, boolean synchronously) throws Exception {
		if(synchronously){
			if(!checkConnection()) {
				openConnection();
			}
			
			Statement statement = createStatement();
			statement.executeUpdate(request);
			
			statement.close();
		} else {
			toSave.add(request);
		}
	}

}
