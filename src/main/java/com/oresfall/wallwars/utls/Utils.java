package com.oresfall.wallwars.utls;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Utilities to make life easier
 */
public class Utils {
    /**
     * Function to get world just by name
     *
     * @param server Server instance from where it will get worlds.
     * @param worldName Name of world (namespace:type)
     * @return Returns instance of ServerWords
     */
    @Nullable
    public static ServerWorld getWorldByName(@NotNull MinecraftServer server, String worldName) {
        for(ServerWorld serverWorld : server.getWorlds()) {
            if(serverWorld.getRegistryKey().getValue().toString().contains(worldName)) {
                return serverWorld;
            }
        }
        return null;
    }
}
