package com.gmail.trentech.simplechat.commands.channel;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDTagChannel implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder().permission("simpletags.cmd.tag.channel").arguments(GenericArguments.string(Text.of("channel")), GenericArguments.optional(GenericArguments.string(Text.of("tag")))).executor(new CMDTagChannel()).build();

	public CMDTagChannel() {
		Help help = new Help("channel", "channel", " View and edit channel tags");
		help.setSyntax(" /tag channel <channel> <tag>\n /t g <channel> <tag>");
		help.setExample(" /tag channel private\n /tag channel private &e[private]\n /tag channel private reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = args.<String> getOne("channel").get();

		Optional<ChannelTag> optionalChannelTag = ChannelTag.get(name);

		if (!args.hasAny("tag")) {
			if (optionalChannelTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalChannelTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag channel <channel> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalChannelTag.isPresent()) {
				optionalChannelTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalChannelTag.isPresent()) {
			ChannelTag channelTag = optionalChannelTag.get();
			channelTag.setTag(tag);
		} else {
			ChannelTag.create(name, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
