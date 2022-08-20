package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.commands.suggestion.GameSuggestions;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

public class ForceGenerateMap {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("forcegeneratemap")
                .then(
                        CommandManager.argument("game", StringArgumentType.word())
                                .executes(ForceGenerateMap::run).suggests(new GameSuggestions())
                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = Utils.getGameFromContext(context,"game");
        ServerPlayerEntity target = context.getSource().getPlayer();

        game.generateMap();

        target.sendMessage(defaultMsg(String.format("Generated map of %s", game)));
        return 0;
    }
}
