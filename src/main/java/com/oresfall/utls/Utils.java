package com.oresfall.utls;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class Utils {
    public static ServerWorld getWorldByName(MinecraftServer server, String worldName) {
        ServerWorld world = null;
        for(ServerWorld serverWorld : server.getWorlds()) {
            System.out.println(serverWorld.getRegistryKey().getValue().toString());
            if(serverWorld.getRegistryKey().getValue().toString().contains(worldName)) {
                world = serverWorld;
            }
        }
        return world;
    }
}
