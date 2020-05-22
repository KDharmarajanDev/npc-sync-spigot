package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import Mathematician.NPCSync.NPCSyncMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoiningServerEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(Bukkit.getOnlinePlayers().size() == 1){
            NPCSyncDataHandler.createNPCSyncDataRequestMessage();
        }
        NPCSyncDataHandler.trackPossiblePlayer(player.getPlayerListName());
        new BukkitRunnable(){
            @Override
            public void run(){
                NPCSyncDataHandler.showAllNPCsToPlayer(player);
            }
        }.runTaskLater(NPCSyncMain.plugin, 140);
    }

}
