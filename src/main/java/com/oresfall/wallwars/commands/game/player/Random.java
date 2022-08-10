package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
 * Command that teleport player to random game
 * Usage: `/game player random`
 */
public class Random {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("random")
                .executes(Random::run);
    }
    public static int run(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        Player player = Database.getPlayer(target.getUuid());
        assert player != null;

        if(player.getInGame()) {
            target.sendMessage(errorMsg("You are already in game!"));
            return -1;
        }

        Game game = Database.getGames().get(new java.util.Random().nextInt(Database.getGames().size()));


        if(!player.joinGame(game)) {
            target.sendMessage(errorMsg( "We didn't find any game available for you"));
            return -1;
        }

        target.sendMessage(defaultMsg("Joined the game!"));
        return 0;
    }
}
