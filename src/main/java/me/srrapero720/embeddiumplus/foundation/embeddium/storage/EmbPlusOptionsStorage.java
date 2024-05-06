package me.srrapero720.embeddiumplus.foundation.embeddium.storage;

import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.embeddiumplus.EmbyConfig;

public class EmbPlusOptionsStorage implements OptionStorage<Object> {
    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        EmbyConfig.SPECS.save();
    }
}
