package Mathematician.NPCSync;

import Mathematician.NPCSync.Commands.MainCommands;
import Mathematician.NPCSync.Events.*;
import Mathematician.NPCSync.NPCSyncData.NPCSyncDataHandler;
import Mathematician.NPCSync.NPCSyncData.NPCSyncDataReceiver;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class NPCSyncMain extends JavaPlugin {

    public static NPCSyncMain plugin;
    public static BukkitScheduler scheduler;
    private static String pluginInformationString = ChatColor.DARK_BLUE + "[" + ChatColor.GOLD + "NPC Sync" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.GOLD;

    @Override
    public void onEnable() {
        //Initial Information
        plugin = this;
        getLogger().info("Running NPC Sync Plugin!");

        //Events Registering
        this.getServer().getPluginManager().registerEvents(new PlayerPunchingHandling(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoiningServerEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSneakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSprintEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeavingServerEvent(), this);
        this.getServer().getPluginManager().registerEvents(new LeverPullEvent(), this);

        //Command Registering
        getCommand("npcsync").setExecutor(new MainCommands());

        scheduler = getServer().getScheduler();
        plugin = this;

        new BukkitRunnable(){
            @Override
            public void run(){
                NPCSyncDataHandler.updatePlayerSyncing();
            }
        }.runTaskTimer(this, 0, 1);

        checkIfBungee();
        if (!getServer().getPluginManager().isPluginEnabled(this)) {
            return;
        }
        getServer().getMessenger().registerIncomingPluginChannel(this, "npcsync:channel", new NPCSyncDataReceiver());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "npcsync:channel");
    }

    @Override
    public void onDisable(){

    }

    public static void sendPluginMessage(Player player, String message){
        player.sendMessage(pluginInformationString + message);
    }

    private void checkIfBungee() {
        // we check if the server is Spigot/Paper (because of the spigot.yml file)
        if (!getServer().getVersion().contains("Spigot") && !getServer().getVersion().contains("Paper")) {
            getLogger().severe("You probably run CraftBukkit... Please update atleast to spigot for this to work...");
            getLogger().severe("Plugin disabled!" );
            getServer().getPluginManager().disablePlugin( this );
            return;
        }
        if (getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean("settings.bungeecord")) {
            getLogger().severe("This server is not BungeeCord.");
            getLogger().severe("If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.");
            getLogger().severe("Plugin disabled!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
