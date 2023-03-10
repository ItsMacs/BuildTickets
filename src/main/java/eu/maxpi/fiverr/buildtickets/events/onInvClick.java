package eu.maxpi.fiverr.buildtickets.events;

import de.tr7zw.nbtapi.NBTItem;
import eu.maxpi.fiverr.buildtickets.BuildTickets;
import eu.maxpi.fiverr.buildtickets.utils.InventoryMaker;
import eu.maxpi.fiverr.buildtickets.utils.PluginLoader;
import eu.maxpi.fiverr.buildtickets.utils.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class onInvClick implements Listener {

    @EventHandler
    public void generalTicketGUI(InventoryClickEvent event){
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType() == Material.AIR) return;

        NBTItem i = new NBTItem(event.getCurrentItem());
        if(!i.hasKey("ticketaction")) return;

        event.setCancelled(true);

        switch (i.getString("ticketaction")){
            case "create" -> {
                new AnvilGUI.Builder()
                        .text(" ")
                        .title("Enter build prompt")
                        .onComplete(c -> {
                            String s = c.getText().startsWith(" ") ? c.getText().substring(1) : c.getText();

                            return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                                BuildTickets.tickets.add(new Ticket(c.getPlayer().getName(), s));
                                c.getPlayer().sendMessage(PluginLoader.lang.get("ticket-created"));
                                Bukkit.getOnlinePlayers().stream().filter(p -> p != c.getPlayer()).forEach(p -> p.sendMessage(PluginLoader.lang.get("ticket-created-broadcast").replace("%player%", c.getPlayer().getName())));
                            }));
                        })
                        .itemOutput(new ItemStack(Material.PAPER))
                        .plugin(BuildTickets.getInstance())
                        .open((Player) event.getWhoClicked());
            }

            case "next" -> {
                InventoryMaker.openTickets((Player)event.getWhoClicked(), Integer.parseInt(i.getString("page")) + 1);
            }

            case "prev" -> {
                InventoryMaker.openTickets((Player)event.getWhoClicked(), Integer.parseInt(i.getString("page")) - 1);
            }

            case "next_revision" -> {
                InventoryMaker.openTicketRevisions((Player)event.getWhoClicked(), Integer.parseInt(i.getString("page")) + 1);
            }

            case "prev_revision" -> {
                InventoryMaker.openTicketRevisions((Player)event.getWhoClicked(), Integer.parseInt(i.getString("page")) - 1);
            }

            case "interact" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                if(t.assigned == null){
                    event.getWhoClicked().sendMessage(PluginLoader.lang.get("ticket-assigned"));
                    t.assigned = event.getWhoClicked().getName();
                    event.getWhoClicked().closeInventory();
                    return;
                }

                if(!t.assigned.equalsIgnoreCase(event.getWhoClicked().getName())) return;

                InventoryMaker.openBuilderTicket((Player)event.getWhoClicked(), t);
            }

            case "interact_revision" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                if(t.assigned == null){
                    return;
                }


                InventoryMaker.openRevisionTicket((Player)event.getWhoClicked(), t);
            }

            case "complete" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                t.coordinates = event.getWhoClicked().getLocation();
                event.getWhoClicked().closeInventory();

                BuildTickets.tickets.remove(t);
                BuildTickets.revisionTickets.add(t);
            }

            case "unassign" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                t.assigned = null;
                InventoryMaker.openTickets((Player)event.getWhoClicked(), 0);
            }

            case "resolve" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                BuildTickets.revisionTickets.remove(t);

                new AnvilGUI.Builder()
                        .onComplete((c) -> {
                            double d;

                            try{
                                d = Double.parseDouble(c.getText().startsWith(" ") ? c.getText().substring(1) : c.getText());
                            }catch (Exception e){
                                return List.of(AnvilGUI.ResponseAction.replaceInputText(" "));
                            }

                            BuildTickets.points.put(t.assigned, BuildTickets.points.getOrDefault(t.assigned, 0D) + d);
                            return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> InventoryMaker.openTicketRevisions(c.getPlayer(), 0)));
                        })
                        .title("Amt. of points")
                        .text(" ")
                        .itemOutput(new ItemStack(Material.LIME_DYE))
                        .itemLeft(new ItemStack(Material.PAPER))
                        .plugin(BuildTickets.getInstance())
                        .open((Player)event.getWhoClicked());
            }

            case "tp" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                event.getWhoClicked().teleport(t.coordinates);
                event.getWhoClicked().closeInventory();
            }

            case "retry" -> {
                Ticket t = BuildTickets.getTicket(i.getString("ticket"));
                if(t == null) return;

                t.assigned = null;
                t.coordinates = null;
                InventoryMaker.openTicketRevisions((Player)event.getWhoClicked(), 0);
            }
        }
    }

}
