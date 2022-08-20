package com.oresfall.wallwars.events;

import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.db.Database;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Class for server events
 */
public class EventServer {
    /**
     * Event after staring server
     * @param server
     */
    private static void Started(MinecraftServer server) {
        Main.LOGGER.info("Loading configurations");
        Database.readConfigs(server);
        Database.setDefaultTeam(server,"default");
        Database.setSpectatorTeam(server,"spec");
        ServerTickEvents.START_SERVER_TICK.register(Database::onEveryTick);
        Main.LOGGER.info("Loaded");

    }
    /**
     * Event when server starts
     * @param server
     */
    private static void Starting(MinecraftServer server) {

    }
    /**
     * Event when server is stopping
     * @param server
     */
    private static void Stopping(MinecraftServer server) {
        Database.saveGames(server);
    }

    /**
     * Register events
     */
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(EventServer::Started);
        ServerLifecycleEvents.SERVER_STARTING.register(EventServer::Starting);
        ServerLifecycleEvents.SERVER_STOPPING.register(EventServer::Stopping);
    }
}
