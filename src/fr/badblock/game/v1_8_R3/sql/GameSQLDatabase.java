package fr.badblock.game.v1_8_R3.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Queues;

import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSQLDatabase implements SQLDatabase {
	
	private final String hostname, port, username, password, database;
	private final int    threads = 16;
	
	protected Connection connection;
	protected Queue<String> toSave = Queues.newLinkedBlockingDeque();
	protected List<SQLThread> sqlThread = new ArrayList<>();
	
	protected Thread saving = new Thread() {
		@Override
		public void run(){
			synchronized (saving) {
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
						saving.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	{
		saving.start();
		for (int i = 0; i < threads; i++) 
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
