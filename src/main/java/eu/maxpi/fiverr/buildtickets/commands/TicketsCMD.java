package eu.maxpi.fiverr.buildtickets.commands;

import eu.maxpi.fiverr.buildtickets.utils.InventoryMaker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketsCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) return true;

        InventoryMaker.openTickets(p, 0);
        return true;
    }
}
