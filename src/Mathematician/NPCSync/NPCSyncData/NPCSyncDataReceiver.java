package Mathematician.NPCSync.NPCSyncData;

import Mathematician.NPCSync.NPC.Animation;
import Mathematician.NPCSync.NPCSyncMain;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class NPCSyncDataReceiver implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(channel.equalsIgnoreCase("npcsync:channel")) {
            ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
            String subChannel = input.readUTF();
            String createData = input.readUTF();
            String[] elements = createData.split(",");
            if (elements.length > 0) {
                String playerName = elements[0];
                if (subChannel.equalsIgnoreCase("create") && elements.length >= 7) {
                    if(!NPCSyncDataHandler.containsPlayer(playerName)) {
                        World world = Bukkit.getServer().getWorld(elements[1]);
                        if (world == null) {
                            return;
                        }
                        Location location = new Location(world, Double.parseDouble(elements[2]), Double.parseDouble(elements[3]), Double.parseDouble(elements[4]),
                                Float.parseFloat(elements[5]), Float.parseFloat(elements[6]));
                        NPCSyncData npcSyncData;
                        if(elements.length >= 11){
                            npcSyncData = new NPCSyncData(location, playerName, new ItemStack(Material.matchMaterial(elements[7])), new ItemStack(Material.matchMaterial(elements[8])), new ItemStack(Material.matchMaterial(elements[9])), new ItemStack(Material.matchMaterial(elements[10])));
                        } else {
                            npcSyncData = new NPCSyncData(location, playerName);
                        }
                        NPCSyncDataHandler.addNPCSyncData(npcSyncData);
                    }
                } else if (subChannel.equalsIgnoreCase("animate") && elements.length >= 2) {
                    String animationType = elements[1];
                    if (animationType.equalsIgnoreCase("punch")) {
                        NPCSyncDataHandler.sendAnimationPacketToAllPlayers(playerName, Animation.SWING_ARM);
                    }
                } else if (subChannel.equalsIgnoreCase("move") && elements.length >= 7) {
                    World world = Bukkit.getServer().getWorld(elements[1]);
                    if (world == null) {
                        return;
                    }
                    Location location = new Location(world, Double.parseDouble(elements[2]), Double.parseDouble(elements[3]), Double.parseDouble(elements[4]),
                            Float.parseFloat(elements[5]), Float.parseFloat(elements[6]));
                    NPCSyncDataHandler.updateNPCSyncDataLocation(playerName, location);
                } else if (subChannel.equalsIgnoreCase("remove")) {
                    NPCSyncDataHandler.removeAllPlayerSyncData(playerName);
                } else if (subChannel.equalsIgnoreCase("request")){
                    if(elements.length == 1) {
                        NPCSyncMain.plugin.getLogger().info("Requested info!");
                        NPCSyncDataHandler.createAllNPCSyncDataCreateMessageForASpecificServer(elements[0]);
                    } else if (elements.length == 8){
                        NPCSyncMain.plugin.getLogger().info("Received info!");
                        World world = Bukkit.getServer().getWorld(elements[2]);
                        if (world == null) {
                            return;
                        }
                        Location location = new Location(world, Double.parseDouble(elements[3]), Double.parseDouble(elements[4]), Double.parseDouble(elements[5]),
                                Float.parseFloat(elements[6]), Float.parseFloat(elements[7]));
                        NPCSyncDataHandler.addNPCSyncData(new NPCSyncData(location, playerName));
                    }
                } else if(subChannel.equalsIgnoreCase("action") && elements.length >= 2){
                    byte action = Byte.parseByte(elements[1]);
                    NPCSyncDataHandler.sendActionPacketToAllPlayers(playerName, action);
                } else if(subChannel.equalsIgnoreCase("add") && elements.length == 1){
                    NPCSyncDataHandler.addPossiblePlayer(playerName);
                    NPCSyncDataHandler.trackPossiblePlayer(playerName);
                } else if(subChannel.equalsIgnoreCase("flip") && elements.length >= 5){
                    //Handles if a person flipped a lever
                    World world = Bukkit.getServer().getWorld(elements[0]);
                    Block possibleLever = world.getBlockAt(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]));
                    if(possibleLever.getType() == Material.LEVER){
                        BlockState state = possibleLever.getState();
                        Lever lever = (Lever) state.getData();
                        lever.setPowered(Boolean.parseBoolean(elements[4]));
                        state.setData(lever);
                        state.update(true);
                    }
                } else if(subChannel.equalsIgnoreCase("setArmor")  && elements.length >= 5){
                    NPCSyncData npcSyncData = NPCSyncDataHandler.getNPCSyncData(playerName);
                    npcSyncData.getNPC().setArmor(new ItemStack(Material.getMaterial(elements[1])), new ItemStack(Material.getMaterial(elements[2])), new ItemStack(Material.getMaterial(elements[3])), new ItemStack(Material.getMaterial(elements[4])));
                    NPCSyncDataHandler.refreshNPC(playerName);
                }
            }
        }
    }
}
