package com.gmail.trentech.simplechat.listeners;

import org.spongepowered.api.event.Listener;

import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simpletags.events.ChangeTagEvent;
import com.gmail.trentech.simpletags.tags.Tag;

public class TagListener {

	@Listener
	public void onChangeTagEventUpdate(ChangeTagEvent.Update event) {
		Tag tag = event.getTag();

		if (tag instanceof ChannelTag) {
			ChannelTag.cache.put(tag.getName(), (ChannelTag) tag);
		}
	}

	@Listener
	public void onChangeTagEventDelete(ChangeTagEvent.Delete event) {
		Tag tag = event.getTag();

		if (tag instanceof ChannelTag) {
			ChannelTag.cache.remove(tag.getName());
		}
	}
}
