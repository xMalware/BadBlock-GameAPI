package fr.badblock.game.v1_8_R3.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSQLDatabase implements SQLDatabase {
	private static final int THREADS = 16;
	private final String hostname, port, username, password, database;
	
	protected Connection 	  connection = null;
	protected List<SQLThread> sqlThread  = new ArrayList<>();
	
	{
		for (int i = 0; i < THREADS; i++) 
			sqlThread.add(new SQLThread(i));
			
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
	public void call(String request, SQLRequestType requestType) {
		if (requestType.equals(SQLRequestType.QUERY)) throw new IllegalArgumentException("U can't done a query if you don't handle a callback!");
		call(request, requestType, null);
	}

	@Override
	public void call(String request, SQLRequestType requestType, Callback<ResultSet> callback) {
		SQLThread availableThread = null;
		SQLThread firstThread = null;
		for (SQLThread sqlThread : sqlThread)
			if (sqlThread.isAvailable()) {
				availableThread = sqlThread;
				break;
			}else firstThread = sqlThread;
		if (availableThread != null) availableThread.call(new SQLRequest(requestType, request, callback));
		else if (firstThread != null) firstThread.call(new SQLRequest(requestType, request, callback));
	}
}
