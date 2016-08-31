package com.gmail.trentech.simplechat.listeners;

import java.util.LinkedList;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.commands.channel.CMDChannel;
import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simplechat.data.Message;
import com.gmail.trentech.simplechat.data.Mute;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.SQLUtils;
import com.google.common.collect.Lists;

import ninja.leaping.configurate.ConfigurationNode;

public class EventListener {

	@Listener
	public void onClientConnectionEventJoin(ClientConnectionEvent.Join event) {
		Player player = event.getTargetEntity();

		CMDChannel.hash.put(player.getUniqueId(), "global");

		new Mute(player);

		SQLUtils.createPlayerTable(player);

		LinkedList<Message> messages = Message.all(player);

		for (Message message : messages) {
			if (!message.isRead()) {
				continue;
			}

			Builder builder = Text.builder().color(TextColors.GREEN).append(Text.of("You have unread messages! /mail"));
			builder.onClick(TextActions.runCommand("/mail"));
			builder.onHover(TextActions.showText(Text.of("Click to open mailbox")));

			player.sendMessage(builder.build());

			break;
		}
	}

	@Listener(order = Order.LAST)
	public void MessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player) {
		Optional<MessageChannel> optionalChannel = event.getChannel();

		if (!optionalChannel.isPresent()) {
			return;
		}
		MutableMessageChannel messageChannel = optionalChannel.get().asMutable();

		MessageFormatter formatter = event.getFormatter();

		String channel = CMDChannel.hash.get(player.getUniqueId());

		if (!channel.equalsIgnoreCase("global")) {
			Text prefix;

			if (Sponge.getPluginManager().isLoaded("com.gmail.trentech.simpletags")) {
				Optional<ChannelTag> optionalTag = ChannelTag.get(channel);

				if (optionalTag.isPresent()) {
					prefix = Text.join(optionalTag.get().getTag(), formatter.getHeader().toText());
					formatter.setHeader(TextTemplate.of(prefix));
				}
			} else {
				prefix = Text.join(Text.of(TextColors.GRAY, "[", channel, "]"), formatter.getHeader().toText());
				formatter.setHeader(TextTemplate.of(prefix));
			}

			for (MessageReceiver src : Lists.newArrayList(messageChannel.getMembers())) {
				if (src instanceof Player) {
					Player recipient = (Player) src;

					if (!recipient.hasPermission("simplechat.channel." + channel)) {
						messageChannel.removeMember(src);
					}
				}
			}
		}

		ConfigurationNode config = ConfigManager.get().getConfig();

		if (config.getNode("options", "world_chat").getBoolean()) {
			for (MessageReceiver src : Lists.newArrayList(messageChannel.getMembers())) {
				if (src instanceof Player) {
					Player recipient = (Player) src;

					if (!recipient.getWorld().equals(player.getWorld())) {
						messageChannel.removeMember(src);
					}
				}
			}
		}

		if (config.getNode("options", "ranged_chat", "enable").getBoolean()) {
			for (MessageReceiver src : Lists.newArrayList(messageChannel.getMembers())) {
				if (src instanceof Player) {
					Player recipient = (Player) src;

					int range = config.getNode("options", "ranged_chat", "range").getInt();

					double distance = player.getLocation().getPosition().distance(recipient.getLocation().getPosition());

					if (distance > range) {
						messageChannel.removeMember(src);
					}
				}
			}
		}

		for (MessageReceiver src : Lists.newArrayList(messageChannel.getMembers())) {
			if (src instanceof Player) {
				Player recipient = (Player) src;

				if (Mute.get(recipient).get().getPlayers().contains(player.getUniqueId().toString())) {
					messageChannel.removeMember(src);
				}
			}
		}

		event.setChannel(messageChannel);

		String messageOrig = TextSerializers.FORMATTING_CODE.serialize(event.getFormatter().getBody().toText());

		Text message = Main.instance().processText(messageOrig);

		formatter.setBody(TextTemplate.of(message));
	}
}
