package com.oresfall.eventserver;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class EventServer {
    private static void Started(MinecraftServer server) {

    }
    private static void Starting(MinecraftServer server) {

    }
    private static void Stopping(MinecraftServer server) {
        server.getWorlds();
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(EventServer::Started);
        ServerLifecycleEvents.SERVER_STARTING.register(EventServer::Starting);
        ServerLifecycleEvents.SERVER_STOPPING.register(EventServer::Stopping);
    }
}
