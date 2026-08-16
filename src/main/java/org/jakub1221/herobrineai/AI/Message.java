package org.jakub1221.herobrineai.AI;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;

public class Message {
    public static void SendMessage(Player player) {
        if (HerobrineAI.getPluginCore().getConfigDB().SendMessages) {
            if (!HerobrineAI.getPluginCore().getConfigDB().useMessages.isEmpty()) {
                int index = Utils.getRandomGen().nextInt(HerobrineAI.getPluginCore().getConfigDB().useMessages.size());
                player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().useMessages.get(index));
            }
        }
    }
}