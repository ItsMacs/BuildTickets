package eu.maxpi.fiverr.buildtickets.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Ticket {

    public String id;

    public String assignee;
    public String description;
    public long creationEpoch;

    public String assigned;
    public Location coordinates;
    public boolean completed = false;

    public Ticket(String assignee, String description){
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.assignee = assignee;
        this.creationEpoch = ZonedDateTime.now().toEpochSecond();
    }

    public Ticket(String id, String desc, String assigned, String assignee, long creationEpoch, Location coords, boolean completed){
        this.description = desc;
        this.id = id;
        this.assignee = assignee;
        this.assigned = assigned;
        this.creationEpoch = creationEpoch;
        this.coordinates = coords;
        this.completed = completed;
    }

    public String getAssigned(Player p){
        if(assigned == null) return PluginLoader.lang.get("no-assigned");
        if(assigned.equalsIgnoreCase(p.getName())) return PluginLoader.lang.get("assigned-to-you");
        return assigned;
    }

}
