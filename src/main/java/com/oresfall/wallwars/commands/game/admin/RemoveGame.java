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


/**
 * Command for removing game
 * Usage: `/game admin removegame {game}`
 */
public class RemoveGame {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("removegame")
                .then(
                        CommandManager.argument("game", StringArgumentType.word())
                                .executes(RemoveGame::run).suggests(new GameSuggestions())
                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = Utils.getGameFromContext(context,"game");
        ServerPlayerEntity target = context.getSource().getPlayer();

        game.removeGame();

        target.sendMessage(defaultMsg(String.format("Removed game %s", game)));
        return 0;
    }
}
