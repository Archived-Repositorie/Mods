package com.oresfall.wallwars;

import com.oresfall.wallwars.commands.Commands;
import com.oresfall.wallwars.events.EventPlayer;
import com.oresfall.wallwars.events.EventServer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to initialize
 */
public class Main implements ModInitializer {
	/**
	 * Namespace of mod
	 */
	public static final String modid = "wallwar";
	/**
	 * Logger for mod
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(modid);

	/**
	 * Event when mod gets initialized
	 */
	@Override
	public void onInitialize() {
		Commands.register();
		EventServer.register();
		EventPlayer.register();
		LOGGER.info("Mod has been loaded!");
	}
}

