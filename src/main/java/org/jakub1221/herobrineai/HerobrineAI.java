package org.jakub1221.herobrineai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.NPC.AI.Path;
import org.jakub1221.herobrineai.NPC.AI.PathManager;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.NPC.Protocol.ProtocolListener;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.entity.EntityManager;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;

public class HerobrineAI extends JavaPlugin implements Listener {

    private static HerobrineAI pluginCore;
    private AICore aicore;
    private ConfigDB configdb;
    private Support support;
    private EntityManager entMng;
    private PathManager pathMng;
    private NPCCore NPCman;
    public HumanNPC HerobrineNPC;
    public long HerobrineEntityID;
    public boolean isInitDone = false;
    private int pathUpdateINT = 0;

    public static String versionStr = "UNDEFINED";
    public static boolean isNPCDisabled = false;
    public static String bukkit_ver_string = "26.2";
    public static int HerobrineHP = 200;
    public static int HerobrineMaxHP = 200;
    public static final boolean isDebugging = false;
    public static boolean AvailableWorld = false;

    public static List<Material> AllowedBlocks = new ArrayList<>();
    public Map<Player, Long> PlayerApple = new HashMap<>();

    public static Logger log = Logger.getLogger("HerobrineAI");

    @Override
    public void onEnable() {
        PluginDescriptionFile pdf = this.getDescription();
        versionStr = pdf.getVersion();

        isInitDone = true;
        HerobrineAI.pluginCore = this;
        this.configdb = new ConfigDB(log);

        // Load config BEFORE NPCCore so HerobrineUUID/Name/Texture are read from config.yml
        configdb.Startup();
        configdb.Reload();

        this.NPCman = new NPCCore(this);

        com.comphenix.protocol.ProtocolLibrary.getProtocolManager()
            .addPacketListener(new ProtocolListener());

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        this.pathMng = new PathManager();
        this.aicore = new AICore();
        this.entMng = new EntityManager();

        Location nowloc = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
        nowloc.setYaw(1f);
        nowloc.setPitch(1f);
        HerobrineSpawn(nowloc);

        if (configdb.ItemInHand != null) {
            HerobrineNPC.setItemInHand(configdb.ItemInHand.getItemStack());
        }

        if (this.configdb.UseGraveyardWorld && Bukkit.getWorld("world_herobrineai_graveyard") == null) {
            log.info("[HerobrineAI] Creating Graveyard world...");
            WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
            wc.generateStructures(false);
            wc.type(org.bukkit.WorldType.FLAT);
            wc.createWorld();
            GraveyardWorld.Create();
        }
        log.info("[HerobrineAI] Plugin loaded! Version: " + versionStr);

        AllowedBlocks.add(Material.AIR);
        AllowedBlocks.add(Material.SNOW);
        AllowedBlocks.add(Material.SHORT_GRASS);
        AllowedBlocks.add(Material.RAIL);
        AllowedBlocks.add(Material.DEAD_BUSH);
        AllowedBlocks.add(Material.DANDELION);
        AllowedBlocks.add(Material.POPPY);
        AllowedBlocks.add(Material.STONE_PRESSURE_PLATE);
        AllowedBlocks.add(Material.OAK_PRESSURE_PLATE);
        AllowedBlocks.add(Material.VINE);
        AllowedBlocks.add(Material.TORCH);
        AllowedBlocks.add(Material.REDSTONE_WIRE);
        AllowedBlocks.add(Material.REDSTONE_TORCH);
        AllowedBlocks.add(Material.LEVER);
        AllowedBlocks.add(Material.STONE_BUTTON);
        AllowedBlocks.add(Material.LADDER);

        pathUpdateINT = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (Utils.getRandomGen().nextInt(4) == 2 
                && getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
                pathMng.setPath(new Path(Utils.getRandomGen().nextInt(15) - 7f, 
                    Utils.getRandomGen().nextInt(15) - 7f, this));
            }
        }, 200L, 200L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, 
            () -> pathMng.update(), 5L, 5L);

        this.getCommand("hb").setExecutor(new CmdExecutor(this));
        this.getCommand("hb-ai").setExecutor(new CmdExecutor(this));

        this.support = new Support();
        log.info("[HerobrineAI] ProtocolLib NPC system loaded.");
    }

    @Override
    public void onDisable() {
        if (isInitDone) {
            this.entMng.killAllMobs();
            Bukkit.getScheduler().cancelTask(pathUpdateINT);
            NPCman.DisableTask();
            // Don't call CancelTarget here - it schedules tasks which causes IllegalPluginAccessException
            // Just clean up directly
            aicore.Stop_BD();
            aicore.Stop_CG();
            aicore.Stop_MAIN();
            aicore.Stop_RC();
            aicore.Stop_RM();
            aicore.Stop_RP();
            aicore.Stop_RS();
            aicore.disableAll();
            log.info("[HerobrineAI] Plugin disabled!");
        }
    }

    public java.io.InputStream getInputStreamData(String src) {
        return HerobrineAI.class.getResourceAsStream(src);
    }

    public AICore getAICore() { return this.aicore; }
    public EntityManager getEntityManager() { return this.entMng; }
    public static HerobrineAI getPluginCore() { return pluginCore; }
    public NPCCore getNPCCore() { return this.NPCman; }

    public void HerobrineSpawn(Location loc) {
        log.info("[HerobrineAI] Spawning Herobrine at " + loc);
        HerobrineNPC = NPCman.spawnHumanNPC(ChatColor.WHITE + "Herobrine", loc);
        HerobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(this, true));
        HerobrineEntityID = HerobrineNPC.getBukkitEntity().getEntityId();
    }

    public void HerobrineRemove() {
        HerobrineEntityID = 0;
        HerobrineNPC = null;
        NPCman.removeAll();
    }

    public ConfigDB getConfigDB() { return this.configdb; }
    public String getVersionStr() { return versionStr; }
    public Support getSupport() { return this.support; }
    public PathManager getPathManager() { return this.pathMng; }

    public boolean canAttackPlayer(Player player, Player sender) {
        boolean opCheck = true, creativeCheck = true, ignoreCheck = true;

        if (!configdb.AttackOP && player.isOp()) opCheck = false;
        if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) creativeCheck = false;
        if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) ignoreCheck = false;

        if (opCheck && creativeCheck && ignoreCheck) return true;

        String msg = null;
        if (!opCheck) msg = "Player is an OP.";
        else if (!creativeCheck) msg = "Player is in creative mode.";
        else if (!ignoreCheck) msg = "Player has ignore permission.";

        if (sender == null) log.info("[HerobrineAI] " + msg);
        else sender.sendMessage(ChatColor.RED + "[HerobrineAI] " + msg);
        return false;
    }

    public boolean canAttackPlayerNoMSG(Player player) {
        if (!configdb.AttackOP && player.isOp()) return false;
        if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) return false;
        if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) return false;
        return true;
    }

    public String getAvailableWorldString() {
        return AvailableWorld ? "Yes" : "No";
    }
}