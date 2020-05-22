package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public class PlayerPunchingHandling implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPunch(PlayerAnimationEvent event){
        Player player = event.getPlayer();
        if(NPCSyncDataHandler.isBeingSynced(player)) {
            if (event.getAnimationType() != null && event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
                NPCSyncDataHandler.createNPCSyncDataAnimateMessage(player.getPlayerListName());
            }
        }
    }
}
