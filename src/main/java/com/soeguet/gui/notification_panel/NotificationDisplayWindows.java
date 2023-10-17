package com.soeguet.gui.notification_panel;

import com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface;

import java.io.IOException;

public class NotificationDisplayWindows implements NotificationDisplayInterface {

    /**
     * This method displays a notification using a PowerShell script.
     *
     * @param sender the sender of the notification
     * @param message the content of the notification message
     */
    @Override
    public void displayNotification(final String sender, final String message) {

        String script = "[void] [System.Reflection.Assembly]::LoadWithPartialName('System.Windows.Forms');"
                + "$objNotifyIcon = New-Object System.Windows.Forms.NotifyIcon;"
                + "$objNotifyIcon.Icon = [System.Drawing.SystemIcons]::Information;"
                + "$objNotifyIcon.BalloonTipIcon = 'None';"
                + "$objNotifyIcon.BalloonTipText = '"+ message+"';"
                + "$objNotifyIcon.BalloonTipTitle = '"+sender+"';"
                + "$objNotifyIcon.Visible = $True;"
                + "$objNotifyIcon.ShowBalloonTip(7000);"
                + "Start-Sleep -Seconds 7;"
                + "$objNotifyIcon.Dispose();";

        String[] cmd = {"powershell", "-Command", script};

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}