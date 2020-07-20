package Mathematician.NPCSync.NPC;

import Mathematician.NPCSync.NPCSyncMain;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class NPC {

    private PlayerSkin playerSkin;
    private Object npc;
    private ItemStack boots;
    private ItemStack leggings;
    private ItemStack chestplate;
    private ItemStack helmet;

    public NPC (String nameNPC, Location location, ItemStack boots, ItemStack leggings, ItemStack chestplate,  ItemStack helmet) {
        try {
            playerSkin = new PlayerSkin(nameNPC);

            Object nmsServer = NPCSyncMain.plugin.getReflectionUtil().getGetMinecraftServerMethod().invoke(Bukkit.getServer());

            Object nmsWorld = NPCSyncMain.plugin.getReflectionUtil().getGetWorldServer().invoke(location.getWorld());
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), nameNPC);
            gameProfile.getProperties().put("textures", new Property("textures", playerSkin.getInfo()[0], playerSkin.getInfo()[1]));

            npc = NPCSyncMain.plugin.getReflectionUtil().getEntityPlayerConstructor().newInstance(nmsServer, nmsWorld, gameProfile,
                    NPCSyncMain.plugin.getReflectionUtil().getPlayerInteractManagerConstructor().newInstance(nmsWorld));
            NPCSyncMain.plugin.getReflectionUtil().getSetNPCLocationMethod().invoke(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            setArmor(boots, leggings, chestplate, helmet);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showToPlayer(Player player) {
        try {
            Object entityPlayer = NPCSyncMain.plugin.getReflectionUtil().getGetHandleMethod().invoke(player);
            Object playerConnection = NPCSyncMain.plugin.getReflectionUtil().getPlayerConnection().get(entityPlayer);
            playerConnection.getClass().getMethod("sendPacket",
                    NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getPlayerInfoPacket()
                    .newInstance(NPCSyncMain.plugin.getReflectionUtil().getPlayerInfoPacketEnumConstants()[0], npc));
            playerConnection.getClass().getMethod("sendPacket",
                    NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getEntitySpawnPacket()
                    .newInstance(npc));
            Bukkit.getScheduler().scheduleSyncDelayedTask(NPCSyncMain.plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        playerConnection.getClass().getMethod("sendPacket",
                                NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getPlayerInfoPacket()
                                .newInstance(NPCSyncMain.plugin.getReflectionUtil().getPlayerInfoPacketEnumConstants()[4], npc));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }, 5);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancelVisualization(Player  player) {
        try {
            Object entityPlayer = NPCSyncMain.plugin.getReflectionUtil().getGetHandleMethod().invoke(player);
            Object playerConnection = NPCSyncMain.plugin.getReflectionUtil().getPlayerConnection().get(entityPlayer);
            playerConnection.getClass().getMethod("sendPacket",
                    NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getEntityDestroyPacket()
                    .newInstance(NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancelAllVisualizations() {
        for(Player player : Bukkit.getOnlinePlayers()){
            cancelVisualization(player);
        }
    }

    public void showToAllPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()){
            showToPlayer(player);
            displayArmorForPlayer(player);
        }
    }

    public void setLocation(Location location) {
        try {
            NPCSyncMain.plugin.getReflectionUtil().getSetNPCLocationMethod().invoke(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            teleportNPCForAllPlayers();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void teleportNPCForAllPlayers() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Object entityPlayer = NPCSyncMain.plugin.getReflectionUtil().getGetHandleMethod().invoke(player);
                Object playerConnection = NPCSyncMain.plugin.getReflectionUtil().getPlayerConnection().get(entityPlayer);
                playerConnection.getClass().getMethod("sendPacket",
                        NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getEntityTeleportPacket()
                        .newInstance(npc));
                playerConnection.getClass().getMethod("sendPacket",
                        NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getEntityHeadRotatePacket()
                        .newInstance(npc, (byte) ((Integer) NPCSyncMain.plugin.getReflectionUtil().getEntityPlayerYaw().get(npc) * 256 / 360)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayAnimationForAllPlayers(Animation animation) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Object entityPlayer = NPCSyncMain.plugin.getReflectionUtil().getGetHandleMethod().invoke(player);
                Object playerConnection = NPCSyncMain.plugin.getReflectionUtil().getPlayerConnection().get(entityPlayer);
                playerConnection.getClass().getMethod("sendPacket",
                        NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, NPCSyncMain.plugin.getReflectionUtil().getEntityAnimationPacket()
                        .newInstance(npc, animation.getValue()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayActionForAllPlayers(byte value) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Object dataWatcher = NPCSyncMain.plugin.getReflectionUtil().getGetDataWatcher().invoke(npc);
                NPCSyncMain.plugin.getReflectionUtil().getDataWatcherSetMethod().invoke(dataWatcher,
                        NPCSyncMain.plugin.getReflectionUtil().getDataWatcherObjectConstructor().newInstance(0,
                                NPCSyncMain.plugin.getReflectionUtil().getDataWatcherSerializer()), value);
                Object actionPacket = NPCSyncMain.plugin.getReflectionUtil().getEntityMetaDataPacket().newInstance(NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc), dataWatcher, false);
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                playerConnection.getClass().getMethod("sendPacket").invoke(actionPacket);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setArmor(ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet){
        this.boots = boots;
        this.leggings = leggings;
        this.chestplate = chestplate;
        this.helmet = helmet;
    }

    public void displayArmorForPlayer(Player player) {
        try {
            Object entityPlayer = NPCSyncMain.plugin.getReflectionUtil().getGetHandleMethod().invoke(player);
            Object playerConnection = NPCSyncMain.plugin.getReflectionUtil().getPlayerConnection().get(entityPlayer);
            if (boots != null) {
                Object equipmentPacket = NPCSyncMain.plugin.getReflectionUtil().getPacketEquipmentConstructor().newInstance(
                        NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc),
                        Enum.valueOf(NPCSyncMain.plugin.getReflectionUtil().getEnumSlotClass(), "FEET"), CraftItemStack.asNMSCopy(boots));
                playerConnection.getClass().getMethod("sendPacket", NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, equipmentPacket);
            }
            if (leggings != null) {
                Object equipmentPacket = NPCSyncMain.plugin.getReflectionUtil().getPacketEquipmentConstructor().newInstance(
                        NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc),
                        Enum.valueOf(NPCSyncMain.plugin.getReflectionUtil().getEnumSlotClass(), "LEGS"), CraftItemStack.asNMSCopy(leggings));
                playerConnection.getClass().getMethod("sendPacket", NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, equipmentPacket);
            }
            if (chestplate != null) {
                Object equipmentPacket = NPCSyncMain.plugin.getReflectionUtil().getPacketEquipmentConstructor().newInstance(
                        NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc),
                        Enum.valueOf(NPCSyncMain.plugin.getReflectionUtil().getEnumSlotClass(), "CHEST"), CraftItemStack.asNMSCopy(chestplate));
                playerConnection.getClass().getMethod("sendPacket", NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, equipmentPacket);
            }
            if (helmet != null) {
                Object equipmentPacket = NPCSyncMain.plugin.getReflectionUtil().getPacketEquipmentConstructor().newInstance(
                        NPCSyncMain.plugin.getReflectionUtil().getGetEntityPlayerID().invoke(npc),
                        Enum.valueOf(NPCSyncMain.plugin.getReflectionUtil().getEnumSlotClass(), "HEAD"), CraftItemStack.asNMSCopy(helmet));
                playerConnection.getClass().getMethod("sendPacket", NPCSyncMain.plugin.getReflectionUtil().getPacketClass()).invoke(playerConnection, equipmentPacket);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
