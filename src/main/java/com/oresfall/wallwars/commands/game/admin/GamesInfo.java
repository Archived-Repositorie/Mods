package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.db.Database;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class GamesInfo {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(Database.getGames().size() < 0 || Database.getGames() == null) {
            target.sendMessage(Text.empty().append("There is no games registered").formatted(Formatting.RED));
            return -1;
        }

        target.sendMessage(Text.of("Info about games"));
        for(Game game : Database.getGames()) {
            if(game == null) continue;
            target.sendMessage(Text.of(String.format(
                    """
                    Game %s:
                        Players: %s
                        Player count: %s
                        World: %s
                    """,
                    game,
                    Arrays.deepToString(game.getPlayersByName().toArray()),
                    game.getPlayers().size(),
                    game.getWorld().getRegistryKey().getValue()
            )));
        }
        Main.LOGGER.info(Arrays.deepToString(Database.getGames().toArray()));
        return 0;
    }
}
