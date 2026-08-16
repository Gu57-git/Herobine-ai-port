package org.jakub1221.herobrineai.NPC.AI;

public class PathManager {
    private Path path = null;

    public void setPath(Path path) {
        this.path = path;
    }

    public void update() {
        if (path != null) {
            path.update();
        }
    }
}