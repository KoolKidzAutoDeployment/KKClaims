package com.koolkidzmc.kkclaims.utils;

import com.koolkidzmc.kkclaims.KKClaims;

public class TaskManager {

    public class Async {
        public static void run(Runnable runnable) {
            KKClaims.getPlugin(KKClaims.class).getServer().getScheduler().runTaskAsynchronously(KKClaims.getPlugin(KKClaims.class), runnable);
        }

        public static void runTask(Runnable runnable, long interval) {
            KKClaims.getPlugin(KKClaims.class).getServer().getScheduler().runTaskTimerAsynchronously(KKClaims.getPlugin(KKClaims.class), runnable, 0L, interval);
        }

        public static void runLater(Runnable runnable, long delay) {
            KKClaims.getPlugin(KKClaims.class).getServer().getScheduler().runTaskLaterAsynchronously(KKClaims.getPlugin(KKClaims.class), runnable, delay);
        }
    }
}
