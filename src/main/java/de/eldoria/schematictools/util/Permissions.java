/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.util;

import com.sk89q.worldedit.command.tool.brush.Brush;
import de.eldoria.schematicbrush.util.WorldEditBrush;
import org.bukkit.entity.Player;

public class Permissions {
    private static final String BASE = "schematictools";

    private static String perm(String... perms) {
        return String.join(".", perms);
    }

    /**
     * Default permission to use a schematic tool. Can be overridden via {@link WorldEditBrush#setBrush(Player, Brush, String)}
     */
    public static final String USE = perm(BASE, "use");

    public static class Info {
        private static final String INFO = perm(BASE, "info");
        /**
         * Base command for seeing information about the current equipped tool
         */
        public static final String CURRENT = perm(INFO, "current");
        /**
         * Permission for seeing information about the all tools
         */

        public static final String ALL = perm(INFO, "all");

    }

    public static final String LIST = perm(BASE, "list");
    public static final String MANAGE = perm(BASE, "modify");

    public static final String BIND = perm(BASE, "bind");

}
