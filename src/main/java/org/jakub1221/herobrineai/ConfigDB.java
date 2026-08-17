package org.jakub1221.herobrineai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jakub1221.herobrineai.misc.CustomID;

public class ConfigDB {

    private Logger log;
    public YamlConfiguration config;
    public YamlConfiguration npc;
    public int ShowRate = 2;
    public boolean HitPlayer = true;
    public boolean SendMessages = true;
    public boolean Lighting = true;
    public boolean DestroyTorches = true;
    public int DestroyTorchesRadius = 5;
    public int ShowInterval = 144000;
    public boolean TotemExplodes = true;
    public boolean OnlyWalkingMode = false;
    public boolean BuildStuff = true;
    public boolean PlaceSigns = true;
    public boolean UseTotem = true;
    public boolean WriteBooks = true;
    public boolean Killable = false;
    public boolean UsePotionEffects = true;
    public int CaveChance = 40;
    public int BookChance = 5;
    public int SignChance = 5;
    public String DeathMessage = "You cannot kill me!";
    public List<String> useWorlds = new ArrayList<String>();;
    public List<String> useMessages = new ArrayList<String>();;
    public List<String> useSignMessages = new ArrayList<String>();;
    public List<String> useBookMessages = new ArrayList<String>();;
    public boolean BuildPyramids = true;
    public boolean UseGraveyardWorld = true;
    public boolean BuryPlayers = true;
    public boolean SpawnWolves = true;
    public boolean SpawnBats = true;
    public boolean UseWalkingMode = true;
    public int WalkingModeXRadius = 1000;
    public int WalkingModeZRadius = 1000;
    public int WalkingModeFromXRadius = 0;
    public int WalkingModeFromZRadius = 0;
    public boolean BuildTemples = true;
    public boolean UseArtifactBow = true;
    public boolean UseArtifactSword = true;
    public boolean UseArtifactApple = true;
    public boolean AttackCreative = true;
    public boolean AttackOP = true;
    public boolean SecuredArea_Build = true;
    public boolean SecuredArea_Attack = true;
    public boolean SecuredArea_Haunt = true;
    public boolean SecuredArea_Signs = true;
    public boolean SecuredArea_Books = true;
    public int HerobrineHP = 150;
    public int BuildInterval = 72000;
    public boolean UseHeads = true;
    public boolean UseCustomItems = false;
    public boolean UseAncientSword = true;
    public boolean UseNPC_Guardian = true;
    public boolean UseNPC_Warrior = true;
    public boolean UseNPC_Demon = true;
    public CustomID ItemInHand = null;
    public ArrayList<String> UseCustomItemsList = new ArrayList<String>();
    public boolean Explosions = true;
    public boolean Burn = true;
    public boolean Curse = true;
    public int maxBooks = 1;
    public int maxSigns = 1;
    public int maxHeads = 1;
    public boolean UseIgnorePermission = true;
    public String HerobrineUUID = "f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2";
    public String HerobrineName = "Herobrine";
    public String HerobrineTexture = "eyJ0aW1lc3RhbXAiOjE0MjE0ODczMzk3MTMsInByb2ZpbGVJZCI6ImY4NGM2YTc5MGE0ZTQ1ZTA4NzliY2Q0OWViZDRjNGUyIiwicHJvZmlsZU5hbWUiOiJIZXJvYnJpbmUiLCJpc1B1YmxpYyI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk4YjdjYTNjN2QzMTRhNjFhYmVkOGZjMThkNzk3ZmMzMGI2ZWZjODQ0NTQyNWM0ZTI1MDk5N2U1MmU2Y2IifX19";
    public String HerobrineSignature = "Edb1R3vm2NHUGyTPaOdXNQY9p5/Ez4xButUGY3tNKIJAzjJM5nQNrq54qyFhSZFVwIP6aM4Ivqmdb2AamXNeN0KgaaU/C514N+cUZNWdW5iiycPytfh7a6EsWXV4hCC9B2FoLkbXuxs/KAbKORtwNfFhQupAsmn9yP00e2c3ZQmS18LWwFg0vzFqvp4HvzJHqY/cTqUxdlSFDrQe/4rATe6Yx6v4zbZN2sHbSL+8AwlDDuP2Xr4SS6f8nABOxjSTlWMn6bToAYiymD+KUPoO0kQJ0Uw/pVXgWHYjQeM4BYf/FAxe8Bf1cP8S7VKueULkOxqIjXAp85uqKkU7dR/s4M4yHm6fhCOCLSMv6hi5ewTaFNYyhK+NXPftFqHcOxA1LbrjOe6NyphF/2FI79n90hagxJpWwNPz3/8I5rnGbYwBZPTsTnD8PszgQTNuWSuvZwGIXPIp9zb90xuU7g7VNWjzPVoOHfRNExEs7Dn9pG8CIA/m/a8koWW3pkbP/AMMWnwgHCr/peGdvF5fN+hJwVdpbfC9sJfzGwA7AgXG/6yqhl1U7YAp/aCVM9bZ94sav+kQghvN41jqOwy4F4i/swc7R4Fx2w5HFxVY3j7FChG7iuhqjUclm79YNhTG0lBQLiZbN5FmC9QgrNHRKlzgSZrXHWoG3YXFSqfn4J+Om9w=";

    public ConfigDB(Logger log) {
        this.log = log;
    }

    // Empty useWorlds list means "all worlds allowed" (otherwise the AI never acts by default)
    public boolean isWorldAllowed(String worldName) {
        return useWorlds == null || useWorlds.isEmpty() || useWorlds.contains(worldName);
    }

    public void Startup() {
        File f = new File("plugins/HerobrineAI");
        if (!f.exists()) f.mkdirs();
        File f2 = new File("plugins/HerobrineAI/config.yml");
        if (!f2.exists()) {
            try {
                f2.createNewFile();
                config = new YamlConfiguration();
                config.load(f2);
                config.set("config.ShowRate", 2);
                config.set("config.HitPlayer", true);
                config.set("config.SendMessages", true);
                config.set("config.Lighting", true);
                config.set("config.DestroyTorches", true);
                config.set("config.DestroyTorchesRadius", 5);
                config.set("config.ShowInterval", 144000);
                config.set("config.TotemExplodes", true);
                config.set("config.OnlyWalkingMode", false);
                config.set("config.BuildStuff", true);
                config.set("config.PlaceSigns", true);
                config.set("config.UseTotem", true);
                config.set("config.WriteBooks", true);
                config.set("config.Killable", false);
                config.set("config.UsePotionEffects", true);
                config.set("config.CaveChance", 40);
                config.set("config.BookChance", 5);
                config.set("config.SignChance", 5);
                config.set("config.DeathMessage", "You cannot kill me!");
                config.set("config.useWorlds", new ArrayList<String>());
                config.set("config.useMessages", new ArrayList<String>());
                config.set("config.useSignMessages", new ArrayList<String>());
                config.set("config.useBookMessages", new ArrayList<String>());
                config.set("config.BuildPyramids", true);
                config.set("config.UseGraveyardWorld", true);
                config.set("config.BuryPlayers", true);
                config.set("config.SpawnWolves", true);
                config.set("config.SpawnBats", true);
                config.set("config.UseWalkingMode", true);
                config.set("config.WalkingModeXRadius", 1000);
                config.set("config.WalkingModeZRadius", 1000);
                config.set("config.WalkingModeFromXRadius", 0);
                config.set("config.WalkingModeFromZRadius", 0);
                config.set("config.BuildTemples", true);
                config.set("config.UseArtifactBow", true);
                config.set("config.UseArtifactSword", true);
                config.set("config.UseArtifactApple", true);
                config.set("config.AttackCreative", true);
                config.set("config.AttackOP", true);
                config.set("config.SecuredArea_Build", true);
                config.set("config.SecuredArea_Attack", true);
                config.set("config.SecuredArea_Haunt", true);
                config.set("config.SecuredArea_Signs", true);
                config.set("config.SecuredArea_Books", true);
                config.set("config.HerobrineHP", 150);
                config.set("config.BuildInterval", 72000);
                config.set("config.UseHeads", true);
                config.set("config.UseCustomItems", false);
                config.set("config.UseAncientSword", true);
                config.set("config.UseNPC_Guardian", true);
                config.set("config.UseNPC_Warrior", true);
                config.set("config.UseNPC_Demon", true);
                config.set("config.ItemInHand", "GOLDEN_SWORD");
                config.set("config.UseCustomItemsList", new ArrayList<String>());
                config.set("config.Explosions", true);
                config.set("config.Burn", true);
                config.set("config.Curse", true);
                config.set("config.maxBooks", 1);
                config.set("config.maxSigns", 1);
                config.set("config.maxHeads", 1);
                config.set("config.UseIgnorePermission", true);
                config.set("config.HerobrineUUID", "f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2");
                config.set("config.HerobrineName", "Herobrine");
                config.save(f2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File f3 = new File("plugins/HerobrineAI/npc.yml");
        if (!f3.exists()) {
            try {
                f3.createNewFile();
                npc = new YamlConfiguration();
                npc.load(f3);
                npc.set("npc.Guardian.Speed", 0.3);
                npc.set("npc.Guardian.HP", 100);
                npc.set("npc.Guardian.SpawnChance", 5);
                npc.set("npc.Warrior.Speed", 0.3);
                npc.set("npc.Warrior.HP", 100);
                npc.set("npc.Warrior.SpawnChance", 5);
                npc.set("npc.Demon.Speed", 0.3);
                npc.set("npc.Demon.HP", 100);
                npc.set("npc.Demon.SpawnChance", 5);
                npc.save(f3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Reload() {
        File f = new File("plugins/HerobrineAI/config.yml");
        File f2 = new File("plugins/HerobrineAI/npc.yml");
        config = new YamlConfiguration();
        npc = new YamlConfiguration();
        try {
            config.load(f);
            npc.load(f2);
            ShowRate = config.getInt("config.ShowRate");
            HitPlayer = config.getBoolean("config.HitPlayer");
            SendMessages = config.getBoolean("config.SendMessages");
            Lighting = config.getBoolean("config.Lighting");
            DestroyTorches = config.getBoolean("config.DestroyTorches");
            DestroyTorchesRadius = config.getInt("config.DestroyTorchesRadius");
            ShowInterval = config.getInt("config.ShowInterval");
            TotemExplodes = config.getBoolean("config.TotemExplodes");
            OnlyWalkingMode = config.getBoolean("config.OnlyWalkingMode");
            BuildStuff = config.getBoolean("config.BuildStuff");
            PlaceSigns = config.getBoolean("config.PlaceSigns");
            UseTotem = config.getBoolean("config.UseTotem");
            WriteBooks = config.getBoolean("config.WriteBooks");
            Killable = config.getBoolean("config.Killable");
            UsePotionEffects = config.getBoolean("config.UsePotionEffects");
            CaveChance = config.getInt("config.CaveChance");
            BookChance = config.getInt("config.BookChance");
            SignChance = config.getInt("config.SignChance");
            DeathMessage = config.getString("config.DeathMessage");
            useWorlds = config.getStringList("config.useWorlds");
            useMessages = config.getStringList("config.useMessages");
            useSignMessages = config.getStringList("config.useSignMessages");
            useBookMessages = config.getStringList("config.useBookMessages");
            BuildPyramids = config.getBoolean("config.BuildPyramids");
            UseGraveyardWorld = config.getBoolean("config.UseGraveyardWorld");
            BuryPlayers = config.getBoolean("config.BuryPlayers");
            SpawnWolves = config.getBoolean("config.SpawnWolves");
            SpawnBats = config.getBoolean("config.SpawnBats");
            UseWalkingMode = config.getBoolean("config.UseWalkingMode");
            WalkingModeXRadius = config.getInt("config.WalkingModeXRadius");
            WalkingModeZRadius = config.getInt("config.WalkingModeZRadius");
            WalkingModeFromXRadius = config.getInt("config.WalkingModeFromXRadius");
            WalkingModeFromZRadius = config.getInt("config.WalkingModeFromZRadius");
            BuildTemples = config.getBoolean("config.BuildTemples");
            UseArtifactBow = config.getBoolean("config.UseArtifactBow");
            UseArtifactSword = config.getBoolean("config.UseArtifactSword");
            UseArtifactApple = config.getBoolean("config.UseArtifactApple");
            AttackCreative = config.getBoolean("config.AttackCreative");
            AttackOP = config.getBoolean("config.AttackOP");
            SecuredArea_Build = config.getBoolean("config.SecuredArea_Build");
            SecuredArea_Attack = config.getBoolean("config.SecuredArea_Attack");
            SecuredArea_Haunt = config.getBoolean("config.SecuredArea_Haunt");
            SecuredArea_Signs = config.getBoolean("config.SecuredArea_Signs");
            SecuredArea_Books = config.getBoolean("config.SecuredArea_Books");
            HerobrineHP = config.getInt("config.HerobrineHP");
            HerobrineAI.HerobrineHP = HerobrineHP;
            HerobrineAI.HerobrineMaxHP = HerobrineHP;
            BuildInterval = config.getInt("config.BuildInterval");
            UseHeads = config.getBoolean("config.UseHeads");
            UseCustomItems = config.getBoolean("config.UseCustomItems");
            UseAncientSword = config.getBoolean("config.UseAncientSword");
            UseNPC_Guardian = config.getBoolean("config.UseNPC_Guardian");
            UseNPC_Warrior = config.getBoolean("config.UseNPC_Warrior");
            UseNPC_Demon = config.getBoolean("config.UseNPC_Demon");
            UseCustomItemsList = (ArrayList<String>) config.getStringList("config.UseCustomItemsList");
            Explosions = config.getBoolean("config.Explosions");
            Burn = config.getBoolean("config.Burn");
            Curse = config.getBoolean("config.Curse");
            maxBooks = config.getInt("config.maxBooks");
            maxSigns = config.getInt("config.maxSigns");
            maxHeads = config.getInt("config.maxHeads");
            UseIgnorePermission = config.getBoolean("config.UseIgnorePermission");
            HerobrineUUID = config.getString("config.HerobrineUUID");
            HerobrineName = config.getString("config.HerobrineName");
            String itemStr = config.getString("config.ItemInHand");
            if (itemStr != null) {
                try {
                    ItemInHand = new CustomID(itemStr);
                } catch (Exception e) {
                    ItemInHand = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}