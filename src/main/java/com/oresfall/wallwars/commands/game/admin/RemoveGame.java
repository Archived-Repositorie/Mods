package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.db.Database;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


/**
 * Command for removing game
 * Usage: `/game admin removegame {game}`
 */
public class RemoveGame {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame( context,"game");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!Database.ifGameExist(game)) {
            target.sendMessage(Text.empty().append("Game doesn't exist!").formatted(Formatting.RED));
            return -1;
        }
        Database.removeGame(game);
        for(ServerPlayerEntity player : game.getPlayers()) {
            game.leavePlayer(player);
        }
        target.sendMessage(Text.of(String.format("Removed minigame %s", game)));
        return 0;
    }
}
