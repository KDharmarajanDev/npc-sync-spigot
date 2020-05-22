package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeavingServerEvent implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        NPCSyncDataHandler.removePlayer(player);
    }
}
