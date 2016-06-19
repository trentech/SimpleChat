package com.gmail.trentech.simplechat.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.sql.SqlService;

import com.gmail.trentech.simplechat.Main;

public abstract class SQLUtils {

    protected static SqlService sql;

    protected static DataSource getDataSource() throws SQLException {
	    if (sql == null) {
	        sql = Main.getGame().getServiceManager().provide(SqlService.class).get();
	    }
	    
        return sql.getDataSource("jdbc:h2:./config/simplechat/chat");
	}

	public static void createTable() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Players (Name TEXT, Players TEXT)");
			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void createPlayerTable(Player player) {
		String playerUuid = player.getUniqueId().toString();
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + playerUuid + "` (Name TEXT, Sender TEXT, Message TEXT, Read BOOLEAN)");
			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}