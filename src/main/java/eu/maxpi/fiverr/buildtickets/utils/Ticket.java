package eu.maxpi.fiverr.buildtickets.utils;

import org.bukkit.Location;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Ticket {

    public String id;

    public String assignee;
    public String description;
    public long creationEpoch;

    public String assigned;
    public Location coordinates;

    public Ticket(String assignee, String description){
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.assignee = assignee;
        this.creationEpoch = ZonedDateTime.now().toEpochSecond();
    }

}
