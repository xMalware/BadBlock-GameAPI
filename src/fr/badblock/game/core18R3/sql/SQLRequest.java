package fr.badblock.game.core18R3.sql;

import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class SQLRequest {

	private SQLRequestType 		requestType;
	public 	String 		   		request;
	private Callback<ResultSet> callback;

	public SQLRequest(SQLRequestType requestType, String request, Callback<ResultSet> callback) {
		this.setRequest(request);
		this.setRequestType(requestType);
		this.setCallback(callback);
	}

	public void done() {
		try {
			Statement statement = GameAPI.getAPI().getSqlDatabase().createStatement();
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
