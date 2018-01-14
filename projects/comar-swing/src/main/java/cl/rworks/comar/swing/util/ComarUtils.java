/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.swing.util;

import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebInnerNotification;
import java.awt.Component;

/**
 *
 * @author rgonzalez
 */
public class ComarUtils {

    public static void showInfo(String msg) {
        WebInnerNotification not = new WebInnerNotification();
        not.setContent(msg);
        not.setDisplayTime(2000);
        not.setIcon(NotificationIcon.information.getIcon());
        NotificationManager.showInnerNotification(not);
    }

    public static void showWarn(String msg) {
        WebInnerNotification not = new WebInnerNotification();
        not.setContent(msg);
        not.setDisplayTime(2000);
        not.setIcon(NotificationIcon.warning.getIcon());
        NotificationManager.showInnerNotification(not);
    }

    public static void showError(String msg) {
        WebInnerNotification not = new WebInnerNotification();
        not.setContent(msg);
        not.setDisplayTime(2000);
        not.setIcon(NotificationIcon.error.getIcon());
        NotificationManager.showInnerNotification(not);
    }

    public static int showYesNo(Component owner, String msg, String title) {
        return WebOptionPane.showConfirmDialog(owner, msg, title, WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
    }

}
