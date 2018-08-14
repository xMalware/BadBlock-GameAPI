package fr.badblock.game.core18R3.sql;

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
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
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
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		
		if(checkConnection());
		connection.close();
	}

	@Override
	public Statement createStatement() throws Exception {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		
		if(!checkConnection()) {
			openConnection();
		}

		return connection.createStatement();
	}

	@Override
	public PreparedStatement preparedStatement(String request) throws Exception {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		
		if(!checkConnection()) {
			openConnection();
		}

		return connection.prepareStatement(request);
	}

	@Override
	public void call(String request, SQLRequestType requestType) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		
		if (requestType.equals(SQLRequestType.QUERY)) throw new IllegalArgumentException("U can't done a query if you don't handle a callback!");
		call(request, requestType, null);
	}

	@Override
	public void call(String request, SQLRequestType requestType, Callback<ResultSet> callback) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		SQLThread availableThread = null;
		SQLThread firstThread = null;
		for (SQLThread sqlThread : sqlThread)
			if (sqlThread.isAvailable()) {
				availableThread = sqlThread;
				break;
			}else firstThread = sqlThread;
		if (availableThread != null) availableThread.call(new SQLRequest(this, requestType, request, callback));
		else if (firstThread != null) firstThread.call(new SQLRequest(this, requestType, request, callback));
	}
	
	public String mysql_real_escape_string(String str) {
		if (str == null) {
			return null;
		}

		if (str.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "").length() < 1) {
			return str;
		}

		String clean_string = str;
		clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
		clean_string = clean_string.replaceAll("\\n", "\\\\n");
		clean_string = clean_string.replaceAll("\\r", "\\\\r");
		clean_string = clean_string.replaceAll("\\t", "\\\\t");
		clean_string = clean_string.replaceAll("\\00", "\\\\0");
		clean_string = clean_string.replaceAll("'", "\\\\'");
		clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

		if (clean_string.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]", "").length() < 1) {
			return clean_string;
		}
		try {
			java.sql.Statement stmt = connection.createStatement();
			String qry = "SELECT QUOTE('" + clean_string + "')";

			stmt.executeQuery(qry);
			java.sql.ResultSet resultSet = stmt.getResultSet();
			resultSet.first();
			String r = resultSet.getString(1);
			return r.substring(1, r.length() - 1);
		} catch (Exception error) {
			error.printStackTrace();
			return str;
		}
	}
	
}
