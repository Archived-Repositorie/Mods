package com.oresfall;

import com.oresfall.commands.Commands;
import com.oresfall.eventserver.EventServer;
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
	}
}

