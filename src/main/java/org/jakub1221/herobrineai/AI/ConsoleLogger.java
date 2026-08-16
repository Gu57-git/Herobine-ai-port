package org.jakub1221.herobrineai.AI;

import java.util.logging.Logger;

public class ConsoleLogger {
    private Logger log = Logger.getLogger("HerobrineAI");

    public void info(String msg) {
        if (org.jakub1221.herobrineai.HerobrineAI.isDebugging) {
            log.info(msg);
        }
    }
}