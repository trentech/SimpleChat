package com.gmail.trentech.simplechat.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.simplechat.Main;

public class Mute {

	private final String name;
	private List<String> players = new ArrayList<>();

	public Mute(Player player) {
		this.name = player.getUniqueId().toString();
		save();
	}

	private Mute(String name, List<String> players) {
		this.name = name;
		this.players = players;
	}

	public String getName() {
		return name;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player.getUniqueId().toString());
		update();
	}

	public void removePlayer(Player player) {
		players.remove(player.getUniqueId().toString());
		update();
	}

	public static Optional<Mute> get(Player player) {
		Optional<Mute> optionalMute = Optional.empty();

		String name = player.getUniqueId().toString();
		try {
			String database = ConfigManager.get(Main.getPlugin()).getConfig().getNode("settings", "sql", "database").getString();

			SQLManager sqlManager = SQLManager.get(Main.getPlugin(), database);
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix("PLAYERS"));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("Name").equalsIgnoreCase(name)) {
					String dbList = result.getString("Players");

					if (dbList != null) {
						String[] receiverArray = dbList.split(";");
						optionalMute = Optional.of(new Mute(name, new ArrayList<String>(Arrays.asList(receiverArray))));
					} else {
						optionalMute = Optional.of(new Mute(name, new ArrayList<String>()));
					}
					break;
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return optionalMute;
	}

	private void save() {
		try {
			String database = ConfigManager.get(Main.getPlugin()).getConfig().getNode("settings", "sql", "database").getString();

			SQLManager sqlManager = SQLManager.get(Main.getPlugin(), database);
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("INSERT into " + sqlManager.getPrefix("PLAYERS") + " (Name, Players) VALUES (?, ?)");

			if (!this.players.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder();

				for (String string : this.players) {
					stringBuilder.append(string + ";");
				}

				statement.setString(2, stringBuilder.toString().substring(0, stringBuilder.length() - 1));
			} else {
				statement.setString(2, null);
			}

			statement.setString(1, this.name);

			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void update() {
		try {
			String database = ConfigManager.get(Main.getPlugin()).getConfig().getNode("settings", "sql", "database").getString();

			SQLManager sqlManager = SQLManager.get(Main.getPlugin(), database);
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("UPDATE " + sqlManager.getPrefix("PLAYERS") + " SET Players = ? WHERE Name = ?");

			if (!this.players.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder();

				for (String string : this.players) {
					stringBuilder.append(string + ";");
				}

				statement.setString(1, stringBuilder.toString().substring(0, stringBuilder.length() - 1));
			} else {
				statement.setString(1, null);
			}

			statement.setString(2, this.name);

			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
