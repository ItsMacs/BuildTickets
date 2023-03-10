package eu.maxpi.fiverr.buildtickets.utils;

import de.tr7zw.nbtapi.NBTItem;
import eu.maxpi.fiverr.buildtickets.BuildTickets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class InventoryMaker {

    public static ItemStack makeItem(Material m, String name){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack makeItem(Material m, String name, int amt){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item.setAmount(amt);
        return item;
    }

    public static ItemStack makeItem(ItemStack m, String name){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack makeItem(ItemStack m, String name, int amt){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item.setAmount(amt);
        return item;
    }

    public static ItemStack makeItem(ItemStack m, String name, String lore){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.split("\n")));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Makes an item
     * @param m
     * @param name
     * @param lore
     * @param nbt Expected format: KEY:VALUE (Only string values supported)
     * @return Desired item
     */
    public static ItemStack makeItem(ItemStack m, String name, String lore, String... nbt){
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.split("\n")));
        item.setItemMeta(meta);

        NBTItem i = new NBTItem(item);
        for(String s : nbt) i.setString(s.split(":")[0], s.split(":")[1]);
        return i.getItem();
    }

    public static void openTickets(Player p, int page){
        Inventory inv = Bukkit.createInventory(null, 54, PluginLoader.lang.get("ticket-list-gui"));

        ItemStack glass = makeItem(Material.BLACK_STAINED_GLASS_PANE, PluginLoader.lang.get("filler-item-name"));

        int index = 0;
        for(int i = 0; i < 54; i++){
            if(i < 9 || i >= 45){
                inv.setItem(i, glass);
                continue;
            }

            index = ((i - 9) + (page * 36));
            if(index >= BuildTickets.tickets.size()) continue;

            Ticket ticket = BuildTickets.tickets.get(index);
            inv.setItem(i, makeItem(new ItemStack(i % 2 == 0 ? Material.YELLOW_CONCRETE_POWDER : Material.GREEN_CONCRETE_POWDER), ticket.description, PluginLoader.lang.get("ticket-description").replace("%assignee%", ticket.assignee).replace("%date%", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(ticket.creationEpoch * 1000L))).replace("%assigned%", ticket.getAssigned(p)), "ticketaction:interact", "ticket:" + ticket.id));
        }

        if(p.hasPermission("buildtickets.create")){
            inv.setItem(49, makeItem(new ItemStack(Material.GREEN_DYE), PluginLoader.lang.get("create-ticket-name"), PluginLoader.lang.get("create-ticket-lore"), "ticketaction:create"));
        }

        if(page > 0){
            inv.setItem(45, makeItem(new ItemStack(Material.RED_DYE), PluginLoader.lang.get("back"), "", "ticketaction:prev", "page:" + page));
        }

        if(index < BuildTickets.tickets.size() - 1){
            inv.setItem(53, makeItem(new ItemStack(Material.LIME_DYE), PluginLoader.lang.get("forward"), "", "ticketaction:next", "page:" + page));
        }

        p.openInventory(inv);
    }

    public static void openBuilderTicket(Player p, Ticket t){
        Inventory inv = Bukkit.createInventory(null, 27, PluginLoader.lang.get("ticket-options-gui"));

        inv.setItem(10, makeItem(new ItemStack(Material.LIME_DYE), PluginLoader.lang.get("complete-ticket"), PluginLoader.lang.get("complete-ticket-lore"), "ticketaction:complete", "ticket:" + t.id));
        inv.setItem(16, makeItem(new ItemStack(Material.RED_DYE), PluginLoader.lang.get("unassign-ticket"), PluginLoader.lang.get("unassign-ticket-lore"), "ticketaction:unassign", "ticket:" + t.id));

        p.openInventory(inv);
    }

    public static void openTicketRevisions(Player p, int page){
        Inventory inv = Bukkit.createInventory(null, 54, PluginLoader.lang.get("ticket-revision-list-gui"));

        ItemStack glass = makeItem(Material.BLACK_STAINED_GLASS_PANE, PluginLoader.lang.get("filler-item-name"));

        int index = 0;
        for(int i = 0; i < 54; i++){
            if(i < 9 || i >= 45){
                inv.setItem(i, glass);
                continue;
            }

            index = ((i - 9) + (page * 36));
            if(index >= BuildTickets.revisionTickets.size()) continue;

            Ticket ticket = BuildTickets.revisionTickets.get(index);
            inv.setItem(i, makeItem(new ItemStack(i % 2 == 0 ? Material.YELLOW_CONCRETE_POWDER : Material.GREEN_CONCRETE_POWDER), ticket.description, PluginLoader.lang.get("ticket-description").replace("%assignee%", ticket.assignee).replace("%date%", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(ticket.creationEpoch * 1000L))).replace("%assigned%", ticket.getAssigned(p)), "ticketaction:interact_revision", "ticket:" + ticket.id));
        }

        if(p.hasPermission("buildtickets.create")){
            inv.setItem(49, makeItem(new ItemStack(Material.GREEN_DYE), PluginLoader.lang.get("create-ticket-name"), PluginLoader.lang.get("create-ticket-lore"), "ticketaction:create"));
        }

        if(page > 0){
            inv.setItem(45, makeItem(new ItemStack(Material.RED_DYE), PluginLoader.lang.get("back"), "", "ticketaction:prev_revision", "page:" + page));
        }

        if(index < BuildTickets.revisionTickets.size()){
            inv.setItem(53, makeItem(new ItemStack(Material.LIME_DYE), PluginLoader.lang.get("forward"), "", "ticketaction:next_revision", "page:" + page));
        }

        p.openInventory(inv);
    }

    public static void openRevisionTicket(Player p, Ticket t){
        Inventory inv = Bukkit.createInventory(null, 27, PluginLoader.lang.get("ticket-options-gui"));

        inv.setItem(10, makeItem(new ItemStack(Material.LIME_DYE), PluginLoader.lang.get("resolve-ticket"), PluginLoader.lang.get("resolve-ticket-lore"), "ticketaction:resolve", "ticket:" + t.id));
        inv.setItem(13, makeItem(new ItemStack(Material.ENDER_PEARL), PluginLoader.lang.get("tp-ticket"), PluginLoader.lang.get("tp-ticket-lore"), "ticketaction:tp", "ticket:" + t.id));
        inv.setItem(16, makeItem(new ItemStack(Material.RED_DYE), PluginLoader.lang.get("retry-ticket"), PluginLoader.lang.get("retry-ticket-lore"), "ticketaction:retry", "ticket:" + t.id));

        p.openInventory(inv);
    }

}
