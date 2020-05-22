package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPC.NPCAction;
import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(NPCSyncDataHandler.isBeingSynced(player)) {
            NPCAction[] actions = new NPCAction[2];
            actions[0] = event.isSneaking() ? NPCAction.CROUCHED : NPCAction.STILL;
            actions[1] = player.isSprinting() ? NPCAction.SPRINTING : NPCAction.STILL;
            NPCSyncDataHandler.createNPCActionMessage(player.getPlayerListName(), actions);
        }
    }
}
