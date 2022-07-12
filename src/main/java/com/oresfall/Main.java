package com.oresfall;

import com.oresfall.commands.Commands;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements DedicatedServerModInitializer {
	public static final String modid = "wallwar";
	public static final Logger LOGGER = LoggerFactory.getLogger(modid);

	@Override
	public void onInitializeServer() {
		LOGGER.info("Mod has been loaded!");
		Commands.register();

	}
}

