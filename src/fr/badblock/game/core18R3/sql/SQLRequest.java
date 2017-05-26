package fr.badblock.game.core18R3.sql;

import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class SQLRequest {

	private SQLRequestType 		requestType;
	public 	String 		   		request;
	private Callback<ResultSet> callback;
	public SQLDatabase 			sqlDatabase;

	public SQLRequest(SQLDatabase sqlDatabase, SQLRequestType requestType, String request, Callback<ResultSet> callback) {
		this.setSqlDatabase(sqlDatabase);
		this.setRequest(request);
		this.setRequestType(requestType);
		this.setCallback(callback);
	}

	public void done(SQLDatabase sqlDatabase) {
		try {
			Statement statement = sqlDatabase.createStatement();
			if (requestType.equals(SQLRequestType.QUERY)) {
				ResultSet resultSet = statement.executeQuery(this.getRequest());
				this.getCallback().done(resultSet, null);
				if (!resultSet.isClosed())
					resultSet.close();
			}else statement.executeUpdate(this.getRequest());
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
