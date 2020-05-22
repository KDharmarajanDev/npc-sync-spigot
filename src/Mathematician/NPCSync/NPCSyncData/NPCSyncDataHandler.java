package Mathematician.NPCSync.NPCSyncData;

import Mathematician.NPCSync.NPC.Animation;
import Mathematician.NPCSync.NPC.NPCAction;
import Mathematician.NPCSync.NPCSyncMain;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class NPCSyncDataHandler {

    private static ArrayList<String> possiblePlayers = new ArrayList<>();
    private static ArrayList<NPCSyncData> npcSyncData = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();

    public static void addNPCSyncData(NPCSyncData npcSyncDatum){
        npcSyncData.add(npcSyncDatum);
    }

    public static ArrayList<Player> getPlayers() {return players;}

    public static void updateNPCSyncDataLocation(String playerName, Location location){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                npcSyncData.setNewLocation(location);
                break;
            }
        }
    }

    public static void addPlayer(Player player){
        players.add(player);
        PlayerInventory inventory = player.getInventory();
        ItemStack[] armorPieces = inventory.getArmorContents();
        createNPCSyncDataCreateMessageArmor(player, armorPieces[0], armorPieces[1], armorPieces[2], armorPieces[3]);
    }

    public static boolean removePlayer(Player player){
        return players.remove(player);
    }

    public static void updatePlayerSyncing(){
        for(Player player : players){
            if(player != null) {
                createNPCSyncDataMoveMessage(player);
            }
        }
    }

    public static void removeNPCSyncData(NPCSyncData npcSyncDatum){
        npcSyncDatum.getNPC().cancelAllVisualizations();
        npcSyncData.remove(npcSyncDatum);
    }

    public static boolean removeNPCSyncData (String playerName){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                removeNPCSyncData(npcSyncData);
                return true;
            }
        }
        return false;
    }

    public static boolean removeAllPlayerSyncData(String playerName){
        return removeNPCSyncData(playerName) && players.removeIf(player -> player.getPlayerListName().equalsIgnoreCase(playerName));
    }

    public static boolean containsPlayer(String playerName){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                return true;
            }
        }
        return false;
    }

    public static void sendAnimationPacketToAllPlayers(String playerName, Animation animation){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                npcSyncData.getNPC().displayAnimationForAllPlayers(animation);
                break;
            }
        }
    }

    public static void sendActionPacketToAllPlayers(String playerName, byte value){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                npcSyncData.getNPC().displayActionForAllPlayers(value);
                break;
            }
        }
    }

    public static void hideAllNPCsToAllPlayers(){
        for(NPCSyncData data : npcSyncData){
            data.getNPC().cancelAllVisualizations();
        }
    }

    public static void refreshNPC(String playerName){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                npcSyncData.getNPC().cancelAllVisualizations();
                npcSyncData.showToAllPlayers();
                break;
            }
        }
    }

    public static void showAllNPCsToPlayer(Player player){
        for(NPCSyncData npcSyncData : npcSyncData){
            npcSyncData.getNPC().showToPlayer(player);
            npcSyncData.getNPC().displayArmorForPlayer(player);
        }
    }

    public static void createNPCSyncDataCreateMessage(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Location playerLocation = player.getLocation();
        out.writeUTF("create");
        out.writeUTF(player.getPlayerListName() + "," + playerLocation.getWorld().getName() + "," + playerLocation.getX() +
                "," + playerLocation.getY() + "," + playerLocation.getZ() + "," + playerLocation.getYaw() + "," + playerLocation.getPitch());
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCSyncDataCreateMessageArmor(Player player, ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Location playerLocation = player.getLocation();
        out.writeUTF("create");
        if(boots == null){
            boots = new ItemStack(Material.AIR);
        }
        if(leggings == null){
            leggings = new ItemStack(Material.AIR);
        }
        if(chestplate == null){
            chestplate = new ItemStack(Material.AIR);
        }
        if(helmet == null){
            helmet = new ItemStack(Material.AIR);
        }
        out.writeUTF(player.getPlayerListName() + "," + playerLocation.getWorld().getName() + "," + playerLocation.getX() +
                "," + playerLocation.getY() + "," + playerLocation.getZ() + "," + playerLocation.getYaw() + "," + playerLocation.getPitch()
                + "," + boots.getType().name() + "," + leggings.getType().name() + "," + chestplate.getType().name() + "," + helmet.getType().name());
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createAllNPCSyncDataCreateMessageForASpecificServer(String serverName){
        players.forEach(player -> {
            createNPCSyncDataCreateMessageForASpecificServer(player, serverName);
        });
    }

    public static void createNPCSyncDataCreateMessageForASpecificServer(Player player, String serverName){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Location playerLocation = player.getLocation();
        out.writeUTF("request");
        out.writeUTF(serverName + "," + player.getPlayerListName() + "," + playerLocation.getWorld().getName() + "," + playerLocation.getX() +
                "," + playerLocation.getY() + "," + playerLocation.getZ() + "," + playerLocation.getYaw() + "," + playerLocation.getPitch());
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCSyncDataMoveMessage(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Location playerLocation = player.getLocation();
        out.writeUTF("move");
        out.writeUTF(player.getPlayerListName() + "," + playerLocation.getWorld().getName() + "," + playerLocation.getX() +
                "," + playerLocation.getY() + "," + playerLocation.getZ() + "," + playerLocation.getYaw() + "," + playerLocation.getPitch());
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCSyncDataRemoveMessage(String  playerName){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("remove");
        out.writeUTF(playerName);
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCSyncDataAnimateMessage(String  playerName){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("animate");
        out.writeUTF(playerName + ",PUNCH");
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCSyncDataRequestMessage(){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("request");
        out.writeUTF("");
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createNPCActionMessage(String playerName, NPCAction[] actions){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("action");
        out.writeUTF(playerName + "," + NPCAction.getOverallValue(actions));
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createLeverFlipMessage(Location location, boolean ifPowered){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("flip");
        out.writeUTF(location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + ifPowered);
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static void createArmorUpdateMessage(String playerName, ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("setArmor");
        out.writeUTF(playerName + "," + boots.getType().name() + "," + leggings.getType().name() + "," +
                chestplate.getType().name() + "," + helmet.getType().name());
        NPCSyncMain.plugin.getServer().sendPluginMessage(NPCSyncMain.plugin, "npcsync:channel", out.toByteArray());
    }

    public static boolean isBeingSynced(Player player){
        return containsPlayer(player.getPlayerListName()) || players.contains(player);
    }

    public static void addPossiblePlayer(String playerName){
        possiblePlayers.add(playerName);
    }

    public static boolean trackPossiblePlayer(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null){
            if(possiblePlayers.remove(playerName)){
                players.add(player);
                removeNPCSyncData(playerName);
                return true;
            }
        }
        return false;
    }

    public static NPCSyncData getNPCSyncData(String playerName){
        for(NPCSyncData npcSyncData : npcSyncData){
            if(npcSyncData.getPlayerName().equalsIgnoreCase(playerName)){
                return npcSyncData;
            }
        }
        return null;
    }
}
