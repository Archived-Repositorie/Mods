package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Command for getting info about all games
 * Usage: `/game admin gamesinfo`
 */
public class GamesInfo {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("gamesinfo")
                .executes(GamesInfo::run);
    }
    public static int run(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();

        target.sendMessage(Text.literal("Info about games")
                .formatted(Formatting.BLUE)
        );
        for(Game game : Database.getGames()) {
            if(game == null) continue;
            target.sendMessage(Text.of(String.format(
                    """
                    Game %s:
                        Players: %s
                        Player count: %s
                        World: %s
                        Spawn Coords: %s
                    """,
                    game,
                    Arrays.deepToString(game.getPlayersByName().toArray()),
                    game.getPlayers().size(),
                    game.getWorld().getRegistryKey().getValue(),
                    Arrays.toString(new double[]{game.getSPlace().x, game.getSPlace().y, game.getSPlace().z})
            )));
        }
        return 0;
    }
}
