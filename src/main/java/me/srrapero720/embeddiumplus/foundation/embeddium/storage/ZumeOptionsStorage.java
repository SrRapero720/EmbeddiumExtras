package me.srrapero720.embeddiumplus.foundation.embeddium.storage;


import dev.nolij.zume.api.config.v1.ZumeConfig;
import dev.nolij.zume.api.config.v1.ZumeConfigAPI;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

public class ZumeOptionsStorage implements OptionStorage<ZumeConfig> {
    private final ZumeConfig cloneConfig = ZumeConfigAPI.getSnapshot();

    @Override
    public ZumeConfig getData() {
        return cloneConfig;
    }

    @Override
    public void save() {
        try {
            ZumeConfigAPI.replaceConfig(cloneConfig);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot store ZUME config");
        }
    }
}