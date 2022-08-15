package com.oresfall.wallwars.events;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.playerclass.Player;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

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
        Player player = Database.getPlayer(serverPlayNetworkHandler.getPlayer().getUuid());
        if(player == null) {
            player = new Player(serverPlayNetworkHandler.getPlayer());
            Database.addPlayer(player);
            Database.getDefaultTeam().addPlayer(player);
        } else {
            player.setPlayerEntity(serverPlayNetworkHandler.getPlayer());
            Database.getDefaultTeam().addPlayer(player);
            player.leaveGame();
        }

    }

    /**
     * Event when player disconnects
     * Look at {@link #register()} for more informations
     */
    private static void Disconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer server) {
        Player player = Database.getPlayer(serverPlayNetworkHandler.getPlayer().getUuid());
        player.leaveGame();
    }

    /**
     * Registers player events
     */
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register(EventPlayer::Disconnect);
        ServerPlayConnectionEvents.JOIN.register(EventPlayer::Join);
    }
}
