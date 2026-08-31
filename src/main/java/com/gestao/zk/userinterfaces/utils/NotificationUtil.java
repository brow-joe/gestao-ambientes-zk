package com.gestao.zk.userinterfaces.utils;

import lombok.experimental.UtilityClass;
import org.zkoss.zk.ui.util.Clients;

@UtilityClass
public final class NotificationUtil {
    private static final String POSITION = "bottom_center";
    private static final Boolean CLOSABLE = false;

    private static final String INFO = "info";
    private static final String WARNING = "warning";
    private static final String ERROR = "error";

    private static final Integer DURATION_3500 = 3500;
    private static final Integer DURATION_4000 = 4000;

    public static void success(String message) {
        showNotification(message, INFO, DURATION_3500);
    }

    public static void warning(String message) {
        showNotification(message, WARNING, DURATION_3500);
    }

    public static void error(String message) {
        showNotification(message, ERROR, DURATION_4000);
    }

    private static void showNotification(String message, String level, Integer duration) {
        Clients.showNotification(message, level, null, POSITION, duration, CLOSABLE);
    }
}
