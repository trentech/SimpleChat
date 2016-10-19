package com.gmail.trentech.simplechat.commands.chat;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.helpme.Help;

public class CMDChat implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Help.executeList(src, Help.get("chat").get().getChildren());
			
			return CommandResult.success();
		}
		
		List<Text> list = new ArrayList<>();

		if (src.hasPermission("simplechat.cmd.chat.range")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplechat:chat range")).append(Text.of(" /chat range")).build());
		}
		if (src.hasPermission("simplechat.cmd.chat.snoop")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplechat:chat snoop")).append(Text.of(" /chat snoop")).build());
		}
		if (src.hasPermission("simplechat.cmd.chat.worldchat")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplechat:chat worldchat")).append(Text.of(" /chat worldchat")).build());
		}

		if (src instanceof Player) {
			Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

}
