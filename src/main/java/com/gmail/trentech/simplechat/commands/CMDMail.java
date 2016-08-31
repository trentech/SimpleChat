package com.gmail.trentech.simplechat.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.data.Message;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDMail implements CommandExecutor {

	public CMDMail() {
		Help help = new Help("mail", "mail", " Sender messages to offline players");
		help.setSyntax(" /mail <player> <message>\n /ml <player> <message>");
		help.setExample(" /mail\n /mail Notch I destroyed your house...sorry");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Players only..for now!"));
			return CommandResult.empty();
		}
		Player player = (Player) src;

		if (!args.hasAny("user")) {
			PaginationList.Builder pageBuilder = PaginationList.builder();

			pageBuilder.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Mail")).build());

			LinkedList<Message> messages = Message.all(player);
			List<Text> pages = new ArrayList<>();

			if (messages.isEmpty()) {
				pages.add(Text.of(TextColors.YELLOW, " No messages :)"));
			} else {

				for (Message msg : messages) {
					Text from = Text.of(TextColors.GOLD, "[", msg.getFrom(), "]");
					Text message = msg.getMessage();

					Builder builder = Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Click to delete message")));
					builder.onClick(TextActions.executeCallback(processDelete(msg.getUuid()))).append(Text.of("[Delete]"));

					pages.add(Text.of(builder.build(), from, TextColors.WHITE, ": ", message));

					msg.setRead(true);
				}
			}
			Builder builder = Text.builder().onHover(TextActions.showText(Text.of("Click to delete all messages")));

			builder.onClick(TextActions.executeCallback(processDeleteAll())).append(Text.of(TextColors.RED, "[Empty]"));

			pages.add(Text.of(TextColors.YELLOW, "\n/mail <player> <message>                                       ", builder.build()));

			pageBuilder.contents(pages);

			pageBuilder.sendTo(player);

			return CommandResult.success();
		}
		User user = args.<User> getOne("user").get();

		String msg = args.<String> getOne("message").get();

		String playerUuid = user.getUniqueId().toString();

		new Message(playerUuid, player.getName(), msg);

		Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(playerUuid);
		
		if (optionalPlayer.isPresent()) {
			Builder builder = Text.builder().color(TextColors.GREEN).append(Text.of("You have unread messages! /mail"));
			builder.onClick(TextActions.runCommand("/mail"));
			builder.onHover(TextActions.showText(Text.of("Click to open mailbox")));

			optionalPlayer.get().sendMessage(builder.build());
		}

		player.sendMessage(Text.of(TextColors.GREEN, "Message sent"));

		return CommandResult.success();
	}

	private static Consumer<CommandSource> processDelete(String uuid) {
		return (CommandSource src) -> {
			Player player = (Player) src;

			Optional<Message> optionalMessage = Message.get(player, uuid);

			if (optionalMessage.isPresent()) {
				optionalMessage.get().delete();
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Message deleted"));
			}
		};
	}

	private static Consumer<CommandSource> processDeleteAll() {
		return (CommandSource src) -> {
			Player player = (Player) src;
			LinkedList<Message> messages = Message.all(player);
			for (Message message : messages) {
				message.delete();
			}

			src.sendMessage(Text.of(TextColors.DARK_GREEN, "All messages deleted"));
		};
	}
}
