package fr.badblock.game.v1_8_R3.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeSQLDatabase implements SQLDatabase {
	@Override
	public Statement createStatement() throws Exception {
		throw new IllegalAccessException("Cannot create statement with a fake database!");	
	}

	@Override
	public PreparedStatement preparedStatement(String request) throws Exception {
		throw new IllegalAccessException("Cannot create statement with a fake database!");	
	}

	@Override
	public void call(String request, SQLRequestType requestType) {

	}

	@Override
	public void call(String request, SQLRequestType requestType, Callback<ResultSet> callback) {

	}
}
