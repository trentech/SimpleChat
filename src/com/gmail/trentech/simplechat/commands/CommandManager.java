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
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("playerName"))), GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new CMDMessage())
		    .build();
	
	public CommandSpec cmdReply = CommandSpec.builder()
		    .permission("simplechat.cmd.message")	    
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new CMDReply())
		    .build();
	
	public CommandSpec cmdGlobal = CommandSpec.builder()
		    .permission("simplechat.cmd.global")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new CMDGlobal())
		    .build();
	
	public CommandSpec cmdSay = CommandSpec.builder()
		    .permission("simplechat.cmd.say")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new CMDSay())
		    .build();
	
	public CommandSpec cmdMute = CommandSpec.builder()
		    .permission("simplechat.cmd.mute")	    
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("player"))))
		    .executor(new CMDMute())
		    .build();
	
	
	private CommandSpec cmdBAdd = CommandSpec.builder()
		    .permission("simplechat.cmd.broadcast.add")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new com.gmail.trentech.simplechat.commands.broadcast.CMDAdd())
		    .build();
	
	private CommandSpec cmdBRemove = CommandSpec.builder()
		    .permission("simplechat.cmd.broadcast.remove")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("channel"))))
		    .executor(new com.gmail.trentech.simplechat.commands.broadcast.CMDRemove())
		    .build();
	
	private CommandSpec cmdBList = CommandSpec.builder()
		    .permission("simplechat.cmd.broadcast.list")
		    .executor(new com.gmail.trentech.simplechat.commands.broadcast.CMDList())
		    .build();
	
	public CommandSpec cmdBroadcast = CommandSpec.builder()
		    .permission("simplechat.cmd.broadcast")
		    .child(cmdBAdd, "add", "a")
		    .child(cmdBRemove, "remove", "r")
		    .child(cmdBList, "list", "l")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message"))))
		    .executor(new com.gmail.trentech.simplechat.commands.broadcast.CMDBroadcast())
		    .build();
	
	
	private CommandSpec cmdChCreate = CommandSpec.builder()
		    .permission("simplechat.cmd.channel.create")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
		    .executor(new com.gmail.trentech.simplechat.commands.channel.CMDCreate())
		    .build();
	
	private CommandSpec cmdChRemove = CommandSpec.builder()
		    .permission("simplechat.cmd.channel.remove")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
		    .executor(new com.gmail.trentech.simplechat.commands.channel.CMDRemove())
		    .build();
	
	private CommandSpec cmdChList = CommandSpec.builder()
		    .permission("simplechat.cmd.channel.list")
		    .executor(new com.gmail.trentech.simplechat.commands.channel.CMDList())
		    .build();
	
	public CommandSpec cmdChannel = CommandSpec.builder()
		    .permission("simplechat.cmd.channel")
		    .child(cmdChCreate, "create", "c")
		    .child(cmdChRemove, "remove", "r")
		    .child(cmdChList, "list", "l")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))), GenericArguments.optional(GenericArguments.string(Text.of("message"))))
		    .executor(new CMDChannel())
		    .build();


	private CommandSpec cmdBroadcast1 = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.broadcast")
		    .arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("boolean"))))
		    .executor(new com.gmail.trentech.simplechat.commands.chat.CMDBroadcast())
		    .build();

	private CommandSpec cmdRange = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.range")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("boolean"))), GenericArguments.optional(GenericArguments.string(Text.of("value"))))
		    .executor(new CMDRange())
		    .build();
	
	private CommandSpec cmdSnoop = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.snoop")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("boolean"))), GenericArguments.optional(GenericArguments.string(Text.of("value"))))
		    .executor(new CMDSnoop())
		    .build();
	
	private CommandSpec cmdWorldChat = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.worldchat")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("boolean"))))
		    .executor(new CMDWorldChat())
		    .build();

	public CommandSpec cmdChat = CommandSpec.builder()
		    .permission("simplechat.cmd.chat")
		    .child(cmdBroadcast1, "broadcast", "b")
		    .child(cmdRange, "range", "r")
		    .child(cmdSnoop, "snoop", "s")
		    .child(cmdWorldChat, "worldchat", "w")
		    .executor(new CMDChat())
		    .build();
}
