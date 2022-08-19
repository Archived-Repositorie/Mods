package com.oresfall.wallwars.commands.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.oresfall.wallwars.Main;
import net.minecraft.server.command.ServerCommandSource;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class MapSuggestions implements SuggestionProvider<ServerCommandSource> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        File folder = new File(Main.schematics);
        File[] files = folder.listFiles((file, name) -> file.getName().endsWith(".schem"));
        if(files != null && files.length > 0) {
            for(File file : files) {
                builder.suggest(file.getName());
            }
        }
        return builder.buildFuture();
    }
}
