package com.oresfall.wallwars.events;

import com.oresfall.wallwars.db.Database;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class EventServer {
    private static void Started(MinecraftServer server) {
        Database.readGames(server);
        System.out.println(Arrays.deepToString(Database.getGamesByName().toArray()));
    }
    private static void Starting(MinecraftServer server) {

    }
    private static void Stopping(MinecraftServer server) {
        Database.saveGames();
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(EventServer::Started);
        ServerLifecycleEvents.SERVER_STARTING.register(EventServer::Starting);
        ServerLifecycleEvents.SERVER_STOPPING.register(EventServer::Stopping);

    }
}
