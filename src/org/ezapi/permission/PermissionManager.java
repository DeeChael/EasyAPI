package org.ezapi.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class PermissionManager {

    private final Player player;

    private final PermissionAttachment attachment;

    private boolean dropped = false;

    public PermissionManager(Player player, Plugin plugin) {
        this.player = player;
        this.attachment = player.addAttachment(plugin);
    }

    public void addPermission(String permission) {
        if (isDropped()) return;
        attachment.setPermission(permission, true);
    }

    public void addPermission(Permission permission) {
        if (isDropped()) return;
        addPermission(permission.getName());
    }

    public void removePermission(String permission) {
        if (isDropped()) return;
        attachment.unsetPermission(permission);
    }

    public void removePermission(Permission permission) {
        if (isDropped()) return;
        removePermission(permission.getName());
    }

    public boolean hasPermission(String permission) {
        if (isDropped()) return false;
        return player.hasPermission(permission);
    }

    public void removeAll() {
        if (isDropped()) return;
        for (String permission : attachment.getPermissions().keySet()) {
            removePermission(permission);
        }
    }

    public Player getPlayer() {
        if (isDropped()) return null;
        return player;
    }

    public PermissionAttachment getAttachment() {
        if (isDropped()) return null;
        return attachment;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void drop() {
        if (!dropped) {
            player.removeAttachment(attachment);
            this.dropped = true;
        }
    }

}
