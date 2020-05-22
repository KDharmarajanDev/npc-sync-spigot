package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPC.NPCAction;
import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerSprintEvent implements Listener {

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event){
        Player player = event.getPlayer();
        if(NPCSyncDataHandler.isBeingSynced(player)) {
            NPCAction[] actions = new NPCAction[2];
            actions[0] = player.isSneaking() ? NPCAction.CROUCHED : NPCAction.STILL;
            actions[1] = event.isSprinting() ? NPCAction.SPRINTING : NPCAction.STILL;
            NPCSyncDataHandler.createNPCActionMessage(player.getPlayerListName(), actions);
        }
    }

}
