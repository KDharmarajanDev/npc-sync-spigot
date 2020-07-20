package Mathematician.NPCSync;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectionUtil {

    private static String versionNumber;
    private Object[] playerInfoPacketEnumConstants;

    private Class enumSlotClass;
    private Class entityClass;
    private Class itemStackClass;
    private Class packetClass;
    private Class entityPlayerClass;
    private Class playerInteractManagerClass;
    private Class worldServerClass;
    private Class dataWatcherObjectClass;

    private Constructor<?> entityTeleportPacket;
    private Constructor<?> packetEquipmentConstructor;
    private Constructor<?> entityHeadRotatePacket;
    private Constructor<?> entityDestroyPacket;
    private Constructor<?> entityAnimationPacket;
    private Constructor<?> playerInfoPacket;
    private Constructor<?> entitySpawnPacket;
    private Constructor<?> playerInteractManagerConstructor;
    private Constructor<?> entityPlayerConstructor;
    private Constructor<?> entityMetaDataPacket;
    private Constructor<?> dataWatcherObjectConstructor;

    private Method getHandleMethod;
    private Method getMinecraftServerMethod;
    private Method getWorldServer;
    private Method setNPCLocationMethod;
    private Method getDataWatcher;
    private Method getEntityPlayerID;
    private Method dataWatcherSetMethod;

    private Field playerConnection;
    private Field entityPlayerYaw;

    private Object dataWatcherSerializer;

    public ReflectionUtil() {
        try {
            versionNumber = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            playerInfoPacketEnumConstants = Objects.requireNonNull(getNMSClass("PacketPlayOutPlayerInfo")).getEnumConstants();
            enumSlotClass = getNMSClass("EnumItemSlot");
            itemStackClass = getNMSClass("ItemStack");
            entityPlayerClass = getNMSClass("EntityPlayer");
            packetClass = getNMSClass("Packet");
            playerInteractManagerClass = getNMSClass("PlayerInteractManager");
            worldServerClass = getNMSClass("WorldServer");
            dataWatcherObjectClass = getNMSClass("DataWatcherObject");
            entityClass = getNMSClass("Entity");

            packetEquipmentConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutEntityEquipment")).getConstructor(int.class, enumSlotClass, itemStackClass);
            entityTeleportPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutEntityTeleport")).getConstructor(entityClass);
            entityHeadRotatePacket = Objects.requireNonNull(getNMSClass("PacketPlayOutEntityHeadRotation")).getConstructor(entityClass, byte.class);
            entityDestroyPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutEntityDestroy")).getConstructor(int[].class);
            entityAnimationPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutAnimation")).getConstructor(entityClass, int.class);
            playerInfoPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutPlayerInfo")).getConstructor(getNMSClass("PacketPlayOutPlayerInfo")
                    .getDeclaredField("EnumPlayerInfoAction").getClass(), entityPlayerClass);
            entitySpawnPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutNamedEntitySpawn")).getConstructor(entityClass);
            playerInteractManagerConstructor = playerInteractManagerClass.getConstructor(worldServerClass);
            entityPlayerConstructor = Objects.requireNonNull(getNMSClass("EntityPlayer")).getConstructor(getNMSClass("MinecraftServer"),
                    worldServerClass, GameProfile.class, playerInteractManagerClass);
            entityMetaDataPacket = Objects.requireNonNull(getNMSClass("PacketPlayOutEntityMetadata")).getConstructor(int.class, getNMSClass("DataWatcher"), boolean.class);
            dataWatcherObjectConstructor = Objects.requireNonNull(getNMSClass("DataWatcherObject")).getConstructor(int.class, getNMSClass("DataWatcherSerializer"));

            getHandleMethod = entityPlayerClass.getMethod("getHandle");
            getMinecraftServerMethod = getNMSClass("CraftServer").getMethod("getServer");
            getWorldServer = getNMSClass("CraftWorld").getMethod("getHandle");
            setNPCLocationMethod = entityPlayerClass.getMethod("setLocation", double.class, double.class, double.class,
                    float.class, float.class);
            getDataWatcher = entityPlayerClass.getMethod("getDataWatcher");
            getEntityPlayerID = entityPlayerClass.getMethod("getId");
            dataWatcherSetMethod = getNMSClass("DataWatcher").getMethod("set", dataWatcherObjectClass, byte.class);

            playerConnection = entityPlayerClass.getField("playerConnection");
            entityPlayerYaw = entityPlayerClass.getField("yaw");

            dataWatcherSerializer = Objects.requireNonNull(getNMSClass("DataWatcherRegistry")).getField("a");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + versionNumber + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object[] getPlayerInfoPacketEnumConstants(){
        return playerInfoPacketEnumConstants;
    }

    public Class getPacketClass(){
        return packetClass;
    }

    public Class getEnumSlotClass(){
        return enumSlotClass;
    }

    public Constructor<?> getPacketEquipmentConstructor(){
        return packetEquipmentConstructor;
    }

    public Constructor<?> getEntityTeleportPacket(){
        return entityTeleportPacket;
    }

    public Constructor<?> getEntityHeadRotatePacket(){
        return entityHeadRotatePacket;
    }

    public Constructor<?> getEntityDestroyPacket(){
        return entityDestroyPacket;
    }

    public Constructor<?> getEntityAnimationPacket(){
        return entityAnimationPacket;
    }

    public Constructor<?> getPlayerInfoPacket(){
        return playerInfoPacket;
    }

    public Constructor<?> getEntitySpawnPacket(){
        return entitySpawnPacket;
    }

    public Constructor<?> getPlayerInteractManagerConstructor(){
        return playerInteractManagerConstructor;
    }

    public Constructor<?> getEntityPlayerConstructor(){
        return entityPlayerConstructor;
    }

    public Constructor<?> getEntityMetaDataPacket(){
        return entityMetaDataPacket;
    }

    public Constructor<?> getDataWatcherObjectConstructor(){
        return dataWatcherObjectConstructor;
    }

    public Method getGetHandleMethod(){
        return getHandleMethod;
    }

    public Method getGetMinecraftServerMethod(){
        return getMinecraftServerMethod;
    }

    public Method getGetWorldServer(){
        return getWorldServer;
    }

    public Method getSetNPCLocationMethod(){
        return setNPCLocationMethod;
    }

    public Method getGetDataWatcher(){
        return getDataWatcher;
    }

    public Method getGetEntityPlayerID(){
        return getEntityPlayerID;
    }

    public Method getDataWatcherSetMethod(){
        return dataWatcherSetMethod;
    }

    public Field getPlayerConnection(){
        return playerConnection;
    }

    public Field getEntityPlayerYaw(){
        return entityPlayerYaw;
    }

    public Object getDataWatcherSerializer(){
        return dataWatcherSerializer;
    }
}
