package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Settings {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("settings")
                .then(ChangeWorld.register())
                .then(SetLobby.register())
                .then(SetSpawnStart.register());
    }
}
