package com.gmail.trentech.simplechat.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.selector.Selector;

import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.SingleTag;

public class CMDSay implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("say").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		String message = args.<String> getOne("message").get();

		MutableMessageChannel channel = MessageChannel.TO_ALL.asMutable();
		
		if(message.startsWith("@")) {
			channel = MessageChannel.TO_NONE.asMutable();

			String selector = message.substring(0, message.indexOf(" "));
			
			for(Entity entity : Selector.parse(selector).resolve(src)) {

				if(entity instanceof Player) {
					channel.addMember((MessageReceiver) entity);
				}
			}
			
			if(channel.getMembers().size() == 0) {
				throw new CommandException(Text.of(TextColors.RED, "Selector returned no players"));
			}

			channel.addMember(Sponge.getServer().getConsole());
			
			message = message.replace(selector, "");
		}
		
		Text text = Main.instance().getText(message, src.hasPermission("simplechat.text.color"), src.hasPermission("simplechat.text.url"));
		
		if(Sponge.getPluginManager().isLoaded("simpletags")) {
			if(src instanceof Player) {
				Player player = (Player) src;
				
				Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);

				if (optionalPlayerTag.isPresent()) {
					text = Text.join(optionalPlayerTag.get().getTag(), Text.of(" "), text);
				} else {
					text = Text.join(PlayerTag.getDefault(player), Text.of(" "), text);
				}
			} else {
				Optional<SingleTag> optionalTag = SingleTag.get("simpletags", "console");
				
				if(optionalTag.isPresent()) {
					text = Text.join(optionalTag.get().getTag(), Text.of(" "), text);
				}
			}
		}

		channel.send(text);

		return CommandResult.success();
	}

}
