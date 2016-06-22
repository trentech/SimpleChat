package com.gmail.trentech.simplechat.commands.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDTagChannel implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder().permission("simpletags.cmd.tag.channel").arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag")))).executor(new CMDTagChannel()).build();

	public CMDTagChannel() {
		Help help = new Help("channel", "channel", " View and edit channel tags");
		help.setSyntax(" /tag channel <channel> <tag>\n /t g <channel> <tag>");
		help.setExample(" /tag channel private\n /tag channel private &e[private]\n /tag channel private reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag channel <channel> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get();

		Optional<ChannelTag> optionalChannelTag = ChannelTag.get(name);

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			if (optionalChannelTag.isPresent()) {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalChannelTag.get().getTag()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			list.add(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag channel <channel> <tag>"));

			if (src instanceof Player) {
				Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Channel")).build());

				pages.contents(list);

				pages.sendTo(src);
			} else {
				for (Text text : list) {
					src.sendMessage(text);
				}
			}

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
