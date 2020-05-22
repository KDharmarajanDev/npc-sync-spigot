package Mathematician.NPCSync.Commands;

import Mathematician.NPCSync.NPCSyncData.NPCSyncData;
import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import Mathematician.NPCSync.NPCSyncMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class MainCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("npcsync")) {
            if(sender instanceof Player && sender.hasPermission("NPCSync.npcsync")) {
                Player player = (Player) sender;
                if(args.length > 0){
                    if(args[0].equalsIgnoreCase("start")){
                        if(args.length == 1){
                            if(!NPCSyncDataHandler.isBeingSynced(player)) {
                                NPCSyncDataHandler.addPlayer(player);
                                NPCSyncMain.sendPluginMessage(player, "Successfully created syncing NPC!");
                            } else {
                                NPCSyncMain.sendPluginMessage(player, "You are already being synced!");
                            }
                            return true;
                        } else if(args.length == 2){
                            Player targetPlayer = Bukkit.getPlayer(args[1]);
                            if(targetPlayer != null && !NPCSyncDataHandler.isBeingSynced(player)){
                                if(!NPCSyncDataHandler.isBeingSynced(targetPlayer)) {
                                    NPCSyncDataHandler.addPlayer(targetPlayer);
                                    NPCSyncMain.sendPluginMessage(player, "Successfully created syncing NPC!");
                                } else {
                                    NPCSyncMain.sendPluginMessage(player, "You are already being synced!");
                                }
                            } else {
                                NPCSyncMain.sendPluginMessage(player, "The player specified does not exist!");
                            }
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("cancel")){
                        if(args.length == 1) {
                            NPCSyncDataHandler.removeNPCSyncData(player.getPlayerListName());
                            NPCSyncDataHandler.createNPCSyncDataRemoveMessage(player.getPlayerListName());
                            NPCSyncMain.sendPluginMessage(player, "Successfully removed syncing NPC!");
                            return true;
                        } else if(args.length == 2){
                            Player targetPlayer = Bukkit.getPlayer(args[1]);
                            if(targetPlayer != null){
                                if(!NPCSyncDataHandler.removeNPCSyncData(targetPlayer.getPlayerListName()) && !NPCSyncDataHandler.removePlayer(targetPlayer)){
                                    NPCSyncMain.sendPluginMessage(player, "That player is not currently being synced!");
                                } else {
                                    NPCSyncDataHandler.createNPCSyncDataRemoveMessage(targetPlayer.getPlayerListName());
                                    NPCSyncMain.sendPluginMessage(player, "Successfully removed syncing NPC!");
                                }
                            } else {
                                NPCSyncMain.sendPluginMessage(player, "The player specified does not exist!");
                            }
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("list")){
                        String output = "";
                        ArrayList<Player> players = NPCSyncDataHandler.getPlayers();
                        for(int i = 0; i < players.size(); i++){
                            output += players.get(i).getPlayerListName();
                            if(i < players.size() - 1){
                                output += ", ";
                            }
                        }
                        NPCSyncMain.sendPluginMessage(player, output);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
