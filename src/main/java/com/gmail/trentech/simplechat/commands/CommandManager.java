package com.gmail.trentech.simplechat.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.simplechat.commands.channel.CMDChannel;
import com.gmail.trentech.simplechat.commands.chat.CMDChat;
import com.gmail.trentech.simplechat.commands.chat.CMDRange;
import com.gmail.trentech.simplechat.commands.chat.CMDSnoop;
import com.gmail.trentech.simplechat.commands.chat.CMDWorldChat;

public class CommandManager {

	public CommandSpec cmdMessage = CommandSpec.builder()
		    .permission("simplechat.cmd.message")	    
		    .arguments(GenericArguments.player(Text.of("player")), GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDMessage())
		    .build();

	public CommandSpec cmdReply = CommandSpec.builder()
		    .permission("simplechat.cmd.message")	    
		    .arguments(GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDReply())
		    .build();

	public CommandSpec cmdSay = CommandSpec.builder()
		    .permission("simplechat.cmd.say")
		    .arguments(GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDSay())
		    .build();
	
	public CommandSpec cmdMute = CommandSpec.builder()
		    .permission("simplechat.cmd.mute")	    
		    .arguments(GenericArguments.player(Text.of("player")))
		    .executor(new CMDMute())
		    .build();
	
	public CommandSpec cmdMail = CommandSpec.builder()
		    .permission("simplechat.cmd.mail")
		    .arguments(GenericArguments.optional(GenericArguments.user(Text.of("user"))), GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDMail())
		    .build();

	public CommandSpec cmdChannel = CommandSpec.builder()
		    .permission("simplechat.cmd.channel")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
		    .executor(new CMDChannel())
		    .build();

	private CommandSpec cmdRange = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.range")
		    .arguments(GenericArguments.bool(Text.of("boolean")), GenericArguments.optional(GenericArguments.integer(Text.of("value"))))
		    .executor(new CMDRange())
		    .build();
	
	private CommandSpec cmdSnoop = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.snoop")
		    .arguments(GenericArguments.bool(Text.of("boolean")))
		    .executor(new CMDSnoop())
		    .build();
	
	private CommandSpec cmdWorldChat = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.worldchat")
		    .arguments(GenericArguments.bool(Text.of("boolean")))
		    .executor(new CMDWorldChat())
		    .build();

	public CommandSpec cmdChat = CommandSpec.builder()
		    .permission("simplechat.cmd.chat")
		    .child(cmdRange, "range", "r")
		    .child(cmdSnoop, "snoop", "s")
		    .child(cmdWorldChat, "worldchat", "w")
		    .executor(new CMDChat())
		    .build();
}
