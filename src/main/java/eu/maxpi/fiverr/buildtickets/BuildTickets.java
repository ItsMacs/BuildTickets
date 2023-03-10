package eu.maxpi.fiverr.buildtickets;

import eu.maxpi.fiverr.buildtickets.commands.TicketRevisionCMD;
import eu.maxpi.fiverr.buildtickets.commands.TicketsCMD;
import eu.maxpi.fiverr.buildtickets.events.onInvClick;
import eu.maxpi.fiverr.buildtickets.utils.PlaceholderAPI;
import eu.maxpi.fiverr.buildtickets.utils.PluginLoader;
import eu.maxpi.fiverr.buildtickets.utils.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class BuildTickets extends JavaPlugin {

    private static BuildTickets instance = null;
    public static BuildTickets getInstance() { return BuildTickets.instance; }
    private static void setInstance(BuildTickets instance) { BuildTickets.instance = instance; }

    public static List<Ticket> tickets = new ArrayList<>();
    public static List<Ticket> revisionTickets = new ArrayList<>();

    public static HashMap<String, Double> points = new HashMap<>();


    @Override
    public void onEnable() {
        setInstance(this);

        new PlaceholderAPI().register();

        PluginLoader.load();

        loadCommands();
        loadTasks();
        loadEvents();

        Bukkit.getLogger().info("BuildTickets by fiverr.com/macslolz was enabled successfully!");
    }

    private void loadCommands(){
        Objects.requireNonNull(getCommand("tickets")).setExecutor(new TicketsCMD());
        Objects.requireNonNull(getCommand("ticketrevision")).setExecutor(new TicketRevisionCMD());
    }

    private void loadTasks(){

    }

    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new onInvClick(), this);
    }

    public static Ticket getTicket(String id){
        Ticket t1 = BuildTickets.tickets.stream().filter(t -> t.id.equalsIgnoreCase(id)).findFirst().orElse(null);
        if(t1 == null) t1 = BuildTickets.revisionTickets.stream().filter(t -> t.id.equalsIgnoreCase(id)).findFirst().orElse(null);

        return t1;
    }

    public static String getTitle(String player){
        double points = BuildTickets.points.getOrDefault(player, 0D);

        String[] res = new String[]{null};
        PluginLoader.titles.keySet().forEach(s -> {
            if(res[0] == null) {
                res[0] = s;
                return;
            }

            if(points > PluginLoader.titles.get(s)) return;
            if(PluginLoader.titles.get(s) < PluginLoader.titles.get(res[0])) return;

            res[0] = s;
        });

        return res[0];
    }

    @Override
    public void onDisable() {
        PluginLoader.save();

        Bukkit.getLogger().info("BuildTickets by fiverr.com/macslolz was disabled successfully!");
    }
}
