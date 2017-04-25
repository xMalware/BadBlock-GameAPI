package fr.badblock.game.core18R3.sql;

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
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		throw new IllegalAccessException("Cannot create statement with a fake database!");	
	}

	@Override
	public PreparedStatement preparedStatement(String request) throws Exception {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		throw new IllegalAccessException("Cannot create statement with a fake database!");	
	}

	@Override
	public void call(String request, SQLRequestType requestType) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
	}

	@Override
	public void call(String request, SQLRequestType requestType, Callback<ResultSet> callback) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
	}
}
