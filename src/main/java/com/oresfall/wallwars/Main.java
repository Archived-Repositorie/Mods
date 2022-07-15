package com.oresfall.wallwars;

import com.oresfall.wallwars.commands.Commands;
import com.oresfall.wallwars.events.EventPlayer;
import com.oresfall.wallwars.events.EventServer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String modid = "wallwar";
	public static final Logger LOGGER = LoggerFactory.getLogger(modid);

	@Override
	public void onInitialize() {
		LOGGER.info("Mod has been loaded!");
		Commands.register();
		EventServer.register();
		EventPlayer.register();
	}
}

