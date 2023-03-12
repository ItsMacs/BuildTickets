package eu.maxpi.fiverr.buildtickets.utils;

import eu.maxpi.fiverr.buildtickets.BuildTickets;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static HashMap<String, Double> titles = new HashMap<>();
    public static void load(){
        BuildTickets.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(BuildTickets.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));
        config.getConfigurationSection("titles").getKeys(false).forEach(s -> titles.put(ColorTranslator.translate(s), config.getDouble("titles." + s)));

        YamlConfiguration storage = YamlConfiguration.loadConfiguration(new File(BuildTickets.getInstance().getDataFolder() + "/storage.yml"));
        if(storage.contains("points")) storage.getConfigurationSection("points").getKeys(false).forEach(s -> BuildTickets.points.put(s, storage.getDouble("points." + s)));
        if(storage.contains("tickets")) storage.getConfigurationSection("tickets").getKeys(false).forEach(s -> {
            Ticket t = new Ticket(s, storage.getString("tickets." + s + ".desc"), storage.getString("tickets." + s + ".assigned"), storage.getString("tickets." + s + ".assignee"), storage.getLong("tickets." + s + ".epoch"), storage.getLocation("tickets." + s + ".loc"), storage.getBoolean("tickets." + s + ".completed"));
            if(storage.getBoolean("tickets." + s + ".revision")){
                BuildTickets.revisionTickets.add(t);
                return;
            }

            BuildTickets.tickets.add(t);
        });
    }

    public static void save(){
        YamlConfiguration storage = new YamlConfiguration();

        BuildTickets.points.forEach((s, d) -> storage.set("points." + s, d));

        BuildTickets.tickets.forEach(t -> {
            storage.set("tickets." + t.id + ".assignee", t.assignee);
            storage.set("tickets." + t.id + ".assigned", t.assigned);
            storage.set("tickets." + t.id + ".epoch", t.creationEpoch);
            storage.set("tickets." + t.id + ".loc", t.coordinates);
            storage.set("tickets." + t.id + ".desc", t.description);
            storage.set("tickets." + t.id + ".revision", false);
            storage.set("tickets." + t.id + ".completed", t.completed);
        });

        BuildTickets.revisionTickets.forEach(t -> {
            storage.set("tickets." + t.id + ".assignee", t.assignee);
            storage.set("tickets." + t.id + ".assigned", t.assigned);
            storage.set("tickets." + t.id + ".epoch", t.creationEpoch);
            storage.set("tickets." + t.id + ".loc", t.coordinates);
            storage.set("tickets." + t.id + ".desc", t.description);
            storage.set("tickets." + t.id + ".revision", true);
            storage.set("tickets." + t.id + ".completed", t.completed);
        });

        try{
            storage.save(new File(BuildTickets.getInstance().getDataFolder() + "/storage.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
