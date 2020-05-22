package Mathematician.NPCSync.NPC;

import Mathematician.NPCSync.NPCSyncMain;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public class NPC {

    private PlayerSkin playerSkin;
    private EntityPlayer npc;
    private ItemStack boots;
    private ItemStack leggings;
    private ItemStack chestplate;
    private ItemStack helmet;

    public NPC (String nameNPC, Location location, ItemStack boots, ItemStack leggings, ItemStack chestplate,  ItemStack helmet){

        playerSkin = new PlayerSkin(nameNPC);

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), nameNPC);
        gameProfile.getProperties().put("textures", new Property("textures",playerSkin.getInfo()[0], playerSkin.getInfo()[1]));

        npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        setArmor(boots, leggings, chestplate, helmet);
    }

    public void showToPlayer(Player player){
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        Bukkit.getScheduler().scheduleSyncDelayedTask(NPCSyncMain.plugin, new Runnable() {
            @Override
            public void run() {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
            }
        }, 5);
    }

    public void cancelVisualization(Player  player){
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
    }

    public void cancelAllVisualizations(){
        for(Player player : Bukkit.getOnlinePlayers()){
            cancelVisualization(player);
        }
    }

    public void showToAllPlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            showToPlayer(player);
            displayArmorForPlayer(player);
        }
    }

    public void setLocation(Location location){
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        teleportNPCForAllPlayers();
    }

    public void teleportNPCForAllPlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(npc));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256/360)));
        }
    }

    public void displayAnimationForAllPlayers(Animation animation){
        for(Player player : Bukkit.getOnlinePlayers()){
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(npc, animation.getValue()));
        }
    }

    public void displayActionForAllPlayers(byte value){
        for(Player player : Bukkit.getOnlinePlayers()){
            DataWatcher dataWatcher = npc.getDataWatcher();
            dataWatcher.set(new DataWatcherObject<Byte>(0, DataWatcherRegistry.a), value);
            PacketPlayOutEntityMetadata actionPacket = new PacketPlayOutEntityMetadata(npc.getId(), dataWatcher, false);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionPacket);
        }
    }

    public void setArmor(ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet){
        this.boots = boots;
        this.leggings = leggings;
        this.chestplate = chestplate;
        this.helmet = helmet;
    }

    public void displayArmorForPlayer(Player player) {
        if(boots != null) {
            PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(boots));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(equipmentPacket);
            NPCSyncMain.plugin.getLogger().info(EnumItemSlot.FEET + " " + boots.getType().name());
        }
        if(leggings != null) {
            PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(leggings));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(equipmentPacket);
            NPCSyncMain.plugin.getLogger().info(EnumItemSlot.LEGS + " " + leggings.getType().name());
        }
        if(chestplate != null) {
            PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(chestplate));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(equipmentPacket);
            NPCSyncMain.plugin.getLogger().info(EnumItemSlot.CHEST + " " + chestplate.getType().name());
        }
        if(helmet != null) {
            PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(helmet));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(equipmentPacket);
            NPCSyncMain.plugin.getLogger().info(EnumItemSlot.HEAD + " " + helmet.getType().name());
        }
    }
}
