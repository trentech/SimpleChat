package com.gmail.trentech.simplechat.init;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.simplechat.commands.CMDMail;
import com.gmail.trentech.simplechat.commands.CMDMessage;
import com.gmail.trentech.simplechat.commands.CMDMute;
import com.gmail.trentech.simplechat.commands.CMDReply;
import com.gmail.trentech.simplechat.commands.CMDSay;
import com.gmail.trentech.simplechat.commands.channel.CMDChannel;
import com.gmail.trentech.simplechat.commands.chat.CMDChat;
import com.gmail.trentech.simplechat.commands.chat.CMDRange;
import com.gmail.trentech.simplechat.commands.chat.CMDSnoop;
import com.gmail.trentech.simplechat.commands.chat.CMDWorldChat;

public class Commands {

	private CommandElement element = GenericArguments.flags().flag("help").setAcceptsArbitraryLongFlags(true).buildWith(GenericArguments.none());
	
	public CommandSpec cmdMessage = CommandSpec.builder()
		    .permission("simplechat.cmd.message")	    
		    .arguments(element, GenericArguments.player(Text.of("player")), GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDMessage())
		    .build();

	public CommandSpec cmdReply = CommandSpec.builder()
		    .permission("simplechat.cmd.message")	    
		    .arguments(element, GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDReply())
		    .build();

	public CommandSpec cmdSay = CommandSpec.builder()
		    .permission("simplechat.cmd.say")
		    .arguments(element, GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDSay())
		    .build();
	
	public CommandSpec cmdMute = CommandSpec.builder()
		    .permission("simplechat.cmd.mute")	    
		    .arguments(element, GenericArguments.player(Text.of("player")))
		    .executor(new CMDMute())
		    .build();
	
	public CommandSpec cmdMail = CommandSpec.builder()
		    .permission("simplechat.cmd.mail")
		    .arguments(element, GenericArguments.optional(GenericArguments.user(Text.of("user"))), GenericArguments.remainingJoinedStrings(Text.of("message")))
		    .executor(new CMDMail())
		    .build();

	public CommandSpec cmdChannel = CommandSpec.builder()
		    .permission("simplechat.cmd.channel")
		    .arguments(element, GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
		    .executor(new CMDChannel())
		    .build();

	private CommandSpec cmdRange = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.range")
		    .arguments(element, GenericArguments.bool(Text.of("true|false")), GenericArguments.optional(GenericArguments.integer(Text.of("value"))))
		    .executor(new CMDRange())
		    .build();
	
	private CommandSpec cmdSnoop = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.snoop")
		    .arguments(element, GenericArguments.bool(Text.of("true|false")))
		    .executor(new CMDSnoop())
		    .build();
	
	private CommandSpec cmdWorldChat = CommandSpec.builder()
		    .permission("simplechat.cmd.chat.worldchat")
		    .arguments(element, GenericArguments.bool(Text.of("true|false")))
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
