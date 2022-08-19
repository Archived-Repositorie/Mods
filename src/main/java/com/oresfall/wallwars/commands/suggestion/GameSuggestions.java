package com.oresfall.wallwars.commands.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GameSuggestions implements SuggestionProvider<ServerCommandSource> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ArrayList<Game> games = Database.getGames();
        if(games.size() > 0) {
            for(Game game : games) {
                builder.suggest(game.toString());
            }
        }
        return builder.buildFuture();
    }
}
