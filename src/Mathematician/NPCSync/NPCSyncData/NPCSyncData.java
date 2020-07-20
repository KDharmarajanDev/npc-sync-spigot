package Mathematician.NPCSync.NPCSyncData;

import Mathematician.NPCSync.NPC.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

public class NPCSyncData {

    private Vector offSet;
    private Location location;
    private String playerName;
    private NPC npc;

    public NPCSyncData(Location location, String playerName, Vector offSet){
        this.offSet = offSet;
        this.location = location;
        this.playerName = playerName;
        try {
            createNPC(new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public NPCSyncData(Location location, String  playerName) {
        this.offSet = new Vector(0,0,0);
        this.location = location;
        this.playerName = playerName;
        try {
            createNPC(new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public NPCSyncData(Location location, String  playerName, ItemStack boots, ItemStack leggings, ItemStack chestplate,  ItemStack helmet){
        this.offSet = new Vector(0,0,0);
        this.location = location;
        this.playerName = playerName;
        try {
            createNPC(boots, leggings, chestplate, helmet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNPC(ItemStack boots, ItemStack leggings, ItemStack chestplate,  ItemStack helmet) {
        npc = new NPC(playerName, getNPCLocation(), boots, leggings, chestplate, helmet);
        npc.setLocation(getNPCLocation());
        showToAllPlayers();
    }

    public void showToAllPlayers() {
        npc.showToAllPlayers();
    }

    public Location getNPCLocation(){
        return location.add(offSet);
    }

    public void setNewLocation(Location location) {
        this.location = location;
        npc.setLocation(getNPCLocation());
    }

    public String getPlayerName(){
        return playerName;
    }

    public NPC getNPC(){
        return npc;
    }
}
