package com.gmail.trentech.simplechat.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.simplechat.Main;

public class Message {

	private String playerUuid;

	private String uuid = UUID.randomUUID().toString();
	private String from;
	private String message;

	private boolean read = false;

	public Message(String playerUuid, String from, String message) {
		this.playerUuid = playerUuid;
		this.from = from;
		this.message = message;
		save();
	}

	private Message(String playerUuid, String uuid, String from, String message, boolean read) {
		this.playerUuid = playerUuid;
		this.uuid = uuid;
		this.from = from;
		this.message = message;
		this.read = read;
	}

	public String getUuid() {
		return uuid;
	}

	public String getFrom() {
		return from;
	}

	public Text getMessage() {
		return Main.instance().getText(message, true, true);
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
		update();
	}

	private void save() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();
			
			PreparedStatement statement = connection.prepareStatement("INSERT into " + sqlManager.getPrefix(this.playerUuid.toString()) + "  (Name, Sender, Message, Read) VALUES (?, ?, ?, ?)");

			statement.setString(1, this.uuid);
			statement.setString(2, this.from);
			statement.setString(3, this.message);
			statement.setBoolean(4, this.read);

			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void update() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("UPDATE " + sqlManager.getPrefix(this.playerUuid.toString()) + " SET Read = ? WHERE Name = ?");

			statement.setBoolean(1, this.read);
			statement.setString(2, this.uuid);

			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("DELETE from " + sqlManager.getPrefix(this.playerUuid.toString()) + " WHERE Name = ?");

			statement.setString(1, this.uuid);
			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Optional<Message> get(Player player, String uuid) {
		String playerUuid = player.getUniqueId().toString();

		Optional<Message> optionalMessage = Optional.empty();

		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix(playerUuid));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("Name").equalsIgnoreCase(uuid)) {
					String from = result.getString("Sender");
					String message = result.getString("Message");
					boolean read = result.getBoolean("Read");

					optionalMessage = Optional.of(new Message(playerUuid, uuid, from, message, read));

					break;
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return optionalMessage;
	}

	public static LinkedList<Message> all(Player player) {
		String playerUuid = player.getUniqueId().toString();

		LinkedList<Message> list = new LinkedList<>();

		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix(playerUuid));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String uuid = result.getString("Name");
				String from = result.getString("Sender");
				String message = result.getString("Message");
				boolean read = result.getBoolean("Read");

				list.add(new Message(playerUuid, uuid, from, message, read));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
