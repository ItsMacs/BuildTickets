package eu.maxpi.fiverr.buildtickets.utils;

import eu.maxpi.fiverr.buildtickets.BuildTickets;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "buildtickets";
    }

    @Override
    public String getAuthor() {
        return "Macslolz";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params){
            case "points" -> {
                return String.valueOf(BuildTickets.points.getOrDefault(player.getName(), 0D));
            }

            case "title" -> {
                return BuildTickets.getTitle(player.getName());
            }

            default -> {
                return null;
            }
        }
    }
}
