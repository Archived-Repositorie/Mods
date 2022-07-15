package com.oresfall.wallwars.utls;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class Utils {
    public static ServerWorld getWorldByName(MinecraftServer server, String worldName) {
        ServerWorld world = null;
        for(ServerWorld serverWorld : server.getWorlds()) {
            if(serverWorld.getRegistryKey().getValue().toString().contains(worldName)) {
                world = serverWorld;
            }
        }
        return world;
    }
}
