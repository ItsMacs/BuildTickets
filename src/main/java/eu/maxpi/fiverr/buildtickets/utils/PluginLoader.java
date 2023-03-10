package eu.maxpi.fiverr.buildtickets.utils;

import eu.maxpi.fiverr.buildtickets.BuildTickets;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static void load(){
        BuildTickets.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(BuildTickets.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));


    }

    public static void save(){

    }

}
