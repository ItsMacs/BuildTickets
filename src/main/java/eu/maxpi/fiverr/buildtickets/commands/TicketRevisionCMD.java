package eu.maxpi.fiverr.buildtickets.commands;

import eu.maxpi.fiverr.buildtickets.utils.InventoryMaker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketRevisionCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) return true;
        if(!sender.hasPermission("buildtickets.revision"))

        InventoryMaker.openTicketRevisions(p, 0);
        return true;
    }
}
