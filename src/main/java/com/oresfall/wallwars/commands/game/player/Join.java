package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.playerclass.Player;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;
import static com.oresfall.wallwars.utls.Utils.errorMsg;

/**
 * Command for joining game
 * Usage: `/game player join {game}`
 */
public class Join {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("join")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .executes(Join::run)
                );
    }
    public static int run(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        Game game = GameArgumentType.getGame( context,"game");
        Player player = Database.getPlayer(target);


        if(player.getInGame()) {
            target.sendMessage(errorMsg("You are already in game!"));
            return -1;
        }

        if(!player.joinGame(game)) {
            target.sendMessage(errorMsg( "You can't join this game"));
            return -1;
        }

        target.sendMessage(defaultMsg("Joined the game!"));
        return 0;
    }
}
