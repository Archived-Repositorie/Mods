package com.oresfall.wallwars.mixin;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.playerclass.Player;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Redirect(
            method = "handleDecoratedMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V")
    )
    private void broadcast(PlayerManager instance, SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        Player player = Database.getPlayer(sender.getUuid());
        player.getTeam().sendMessage(message, params);
    }
}
