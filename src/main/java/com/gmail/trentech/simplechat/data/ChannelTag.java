package com.gmail.trentech.simplechat.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.gmail.trentech.simpletags.tags.Tag;

public class ChannelTag extends Tag {

	public static ConcurrentHashMap<String, ChannelTag> cache = new ConcurrentHashMap<>();

	ChannelTag(String name, String tag) {
		super(name, ChannelTag.class, tag);
	}

	ChannelTag(Tag tag) {
		super(tag);
	}

	public static Optional<ChannelTag> get(String channel) {
		if (cache.containsKey(channel)) {
			return Optional.of(cache.get(channel));
		}

		return Optional.empty();
	}

	public static Optional<ChannelTag> create(String channel, String tag) {
		if (cache.containsKey(channel)) {
			return Optional.empty();
		}

		return Optional.of(new ChannelTag(channel, tag));
	}

	public static List<ChannelTag> getAll() {
		List<ChannelTag> list = new ArrayList<>();

		for (Entry<String, ChannelTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}

		return list;
	}

	public static void init() {
		ConcurrentHashMap<String, ChannelTag> hash = new ConcurrentHashMap<>();

		for (Tag tag : getAll(ChannelTag.class)) {
			hash.put(tag.getName(), new ChannelTag(tag));
		}

		cache = hash;
	}
}
