package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.playerclass.Player;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class GroupBase {
    private final Game game;
    private final ArrayList<Player> players = new ArrayList<>();

    GroupBase(Game game) {
        this.game = game;
    }

    public void removePlayers() {
        players.clear();
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Game getGame() {
        return game;
    }

    public void sendMessage(Text message) {
        players.forEach(player -> player.getPlayerEntity().sendMessage(message));
    }

    public void sendMessage(SignedMessage message, MessageType.Parameters params) {
        SentMessage sentMessage = SentMessage.of(message);

        players.forEach(player -> player.getPlayerEntity().sendChatMessage(sentMessage, false, params));
    }

    public void setPhaseToPlayers(int phase) {
        for(Player player : players) player.setPhase(phase);
    }
}
