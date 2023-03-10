package eu.maxpi.fiverr.buildtickets;

import eu.maxpi.fiverr.buildtickets.commands.TicketsCMD;
import eu.maxpi.fiverr.buildtickets.utils.PluginLoader;
import eu.maxpi.fiverr.buildtickets.utils.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class BuildTickets extends JavaPlugin {

    private static BuildTickets instance = null;
    public static BuildTickets getInstance() { return BuildTickets.instance; }
    private static void setInstance(BuildTickets instance) { BuildTickets.instance = instance; }

    public static HashMap<String, Ticket> tickets = new HashMap<>();
    public static HashMap<String, Double> points = new HashMap<>();

    @Override
    public void onEnable() {
        setInstance(this);

        PluginLoader.load();

        loadCommands();
        loadTasks();
        loadEvents();

        Bukkit.getLogger().info("BuildTickets by fiverr.com/macslolz was enabled successfully!");
    }

    private void loadCommands(){
        getCommand("tickets").setExecutor(new TicketsCMD());
    }

    private void loadTasks(){

    }

    private void loadEvents(){

    }

    @Override
    public void onDisable() {
        PluginLoader.save();

        Bukkit.getLogger().info("BuildTickets by fiverr.com/macslolz was disabled successfully!");
    }
}
