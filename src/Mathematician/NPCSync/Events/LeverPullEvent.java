package Mathematician.NPCSync.Events;

import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import Mathematician.NPCSync.NPCSyncMain;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

public class LeverPullEvent implements Listener {

    @EventHandler
    public void onLeverPull(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(NPCSyncDataHandler.isBeingSynced(player)){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                Block block = event.getClickedBlock();
                if(block.getType() == Material.LEVER){
                    Lever lever = (Lever) block.getState().getData();
                    NPCSyncDataHandler.createLeverFlipMessage(block.getLocation(), !lever.isPowered());
                }
            }
        }
    }
}
