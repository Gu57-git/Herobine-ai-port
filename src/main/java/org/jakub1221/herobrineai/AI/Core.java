package org.jakub1221.herobrineai.AI;

public abstract class Core {
    public enum CoreType {
        ANY, START, ATTACK, HAUNT, BOOK, BUILD_CAVE, BURN, BURY_PLAYER, CURSE,
        DESTROY_TORCHES, GRAVEYARD, HEADS, PYRAMID, RANDOM_EXPLOSION,
        RANDOM_POSITION, RANDOM_SOUND, SIGNS, SOUNDF, TEMPLE, TOTEM
    }
    public enum AppearType {
        APPEAR, DISAPPEAR
    }

    protected CoreType coreType;
    protected AppearType appearType;
    protected org.jakub1221.herobrineai.HerobrineAI PluginCore;

    public Core(CoreType type, AppearType appear, org.jakub1221.herobrineai.HerobrineAI plugin) {
        this.coreType = type;
        this.appearType = appear;
        this.PluginCore = plugin;
    }

    public CoreType getCoreType() { return coreType; }
    public AppearType getAppearType() { return appearType; }

    public CoreResult RunCore(Object[] data) {
        return CallCore(data);
    }

    public abstract CoreResult CallCore(Object[] data);
}