package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.oresfall.wallwars.commands.game.admin.settings.Settings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Admin {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("admin")
                .requires(src -> src.hasPermissionLevel(3))
                .then(CreateGame.register())
                .then(RemoveGame.register())
                .then(GamesInfo.register())
                .then(ForceGenerateMap.register())
                .then(Settings.register());
    }
}
