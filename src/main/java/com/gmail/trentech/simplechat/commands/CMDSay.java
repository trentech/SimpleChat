package com.gmail.trentech.simplechat.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Help;
import com.gmail.trentech.simpletags.tags.ConsoleTag;

public class CMDSay implements CommandExecutor {

	public CMDSay(){
		Help help = new Help("say", "say", " Send a message to the server from the console");
		help.setSyntax(" /say <message>\n /s <message>");
		help.setExample(" /say Drop party coming soon");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("message")){
			src.sendMessage(Text.of(TextColors.YELLOW, "/say <message>"));
			return CommandResult.empty();
		}
		String message = args.<String>getOne("message").get();
		
		MessageChannel channel = MessageChannel.TO_ALL;

		channel.send(Text.of(ConsoleTag.get().get().getTag(), TextColors.WHITE, ": ", Main.processText(message)));	

		return CommandResult.success();
	}

}
