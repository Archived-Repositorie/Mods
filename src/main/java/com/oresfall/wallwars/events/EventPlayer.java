package com.oresfall.wallwars.events;

import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.db.Database;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Class for player events
 */
public class EventPlayer {
    /**
     * Event when player joins
     * Look at {@link #register()} for more informations
     * @param serverPlayNetworkHandler
     * @param packetSender
     * @param server
     */
    private static void Join(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer server) {
        leaving(serverPlayNetworkHandler);
        Database.addPlayerToDefaultTeam(serverPlayNetworkHandler.getPlayer());
    }

    /**
     * Event when player disconnects
     * Look at {@link #register()} for more informations
     * @param serverPlayNetworkHandler
     * @param server
     */
    private static void Disconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer server) {
        leaving(serverPlayNetworkHandler);
    }

    /**
     * Registers player events
     */
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register(EventPlayer::Disconnect);
        ServerPlayConnectionEvents.JOIN.register(EventPlayer::Join);
    }

    /**
     * Used to leave players from game after disconnecting
     * @param serverPlayNetworkHandler player
     */
    private static void leaving(@NotNull ServerPlayNetworkHandler serverPlayNetworkHandler) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)player;

        for(Game game : Database.getGames()) {
            if(game == null) continue;
            game.leavePlayer(player);
            targetData.getPersistentData().putBoolean("JoinedGame",false);
        }
        Database.addPlayerToDefaultTeam(player);
    }
}
