package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDWorldChat implements CommandExecutor {

	public CMDWorldChat() {
		Help help = new Help("worldchat", "worldchat", " Toggle on and off world specific chat");
		help.setPermission("simplechat.cmd.chat.worldchat");
		help.setSyntax(" /chat worldchat <boolean>\n /c w <boolean>");
		help.setExample(" /chat worldchat true");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean bool = args.<Boolean> getOne("boolean").get();

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("options", "world_chat").setValue(bool);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "World specific chat to to ", bool));

		return CommandResult.success();
	}

}
