package com.oresfall.wallwars.events;

import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.db.Database;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventPlayer {
    private static void Join(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer server) {
        leaving(serverPlayNetworkHandler);

    }

    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register(EventPlayer::Disconnect);
        ServerPlayConnectionEvents.JOIN.register(EventPlayer::Join);
    }

    private static void Disconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer server) {
        leaving(serverPlayNetworkHandler);
    }

    private static void leaving(ServerPlayNetworkHandler serverPlayNetworkHandler) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)player;
        for(Game game : Database.getGames()) {
            if(game == null) continue;
            game.leavePlayer(player);
            targetData.getPersistentData().putBoolean("JoinedGame",false);
        }
    }
}
