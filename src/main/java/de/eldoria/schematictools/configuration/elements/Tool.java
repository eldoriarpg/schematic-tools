/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.configuration.elements;

import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import de.eldoria.schematicbrush.storage.Storage;
import de.eldoria.schematicbrush.storage.brush.Brush;
import de.eldoria.schematicbrush.util.Colors;
import de.eldoria.schematictools.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SerializableAs("stTool")
public class Tool implements ConfigurationSerializable {
    private static final UUID GLOBAL = new UUID(0L, 0L);
    /**
     * The unique numeric id of the brush
     */
    private final int id;
    /**
     * The owner of the underlying brush
     */
    private UUID owner;
    /**
     * The name of this brush tool
     */
    private String name;
    /**
     * The name of the underlying brush
     */
    private String brushName;
    @Nullable
    private String permission;
    private int usages;

    public Tool(UUID owner, String name, String brushName, int id, String permission, int usages) {
        this.owner = owner;
        this.name = name;
        this.brushName = brushName;
        this.id = id;
        this.permission = permission;
        this.usages = usages;
    }

    public Tool(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        name = map.getValue("name");
        owner = UUID.fromString(map.getValue("owner"));
        brushName = map.getValue("brushName");
        id = map.getValue("id");
        permission = map.getValue("permission");
        usages = map.getValue("usages");
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("name", name)
                .add("owner", owner.toString())
                .add("brushName", brushName)
                .add("id", id)
                .add("permission", permission)
                .add("usages", usages)
                .build();
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public void brush(UUID owner, Brush brush) {
        this.owner = owner;
        brushName = brush.name();
    }

    public int id() {
        return id;
    }

    @NotNull
    public String permission() {
        return permission == null ? Permissions.USE : permission;
    }

    public void permission(String permission) {
        this.permission = permission;
    }

    public int usages() {
        return usages;
    }

    public boolean hasUsage() {
        return usages != -1;
    }

    public void usages(int usages) {
        this.usages = usages;
    }

    public boolean hasPermission(Player player) {
        if (permission == null) return true;
        return player.hasPermission(permission);
    }

    public CompletableFuture<Optional<Brush>> getBrush(Storage storage) {
        if (owner.equals(GLOBAL)) {
            return storage.brushes().globalContainer().get(brushName);
        }
        return storage.brushes().playerContainer(owner).get(brushName);
    }

    public String asModifyComponent() {
        var base = "/schematictools modify";

        return MessageComposer.create()
                .text("<%s>%s", Colors.HEADING, name)
                .newLine()
                .text("<%s>Brush: <%s>%s", Colors.NAME, Colors.VALUE, brushName)
                .space()
                .text("<%s><click:suggest_command:'%s \"%s\" brushName '>[Change]</click>", Colors.CHANGE, base, name)
                .newLine()
                .text("<%s>Brush Owner: <%s>%s", Colors.NAME, Colors.VALUE, hasGlobalBrush() ? "global" : Bukkit.getOfflinePlayer(owner).getName())
                .newLine()
                .text("<%s>Permission: <%s>%s", Colors.NAME, Colors.VALUE, permission())
                .space()
                .text("<%s><click:suggest_command:'%s \"%s\" permission '>[Change]</click>", Colors.CHANGE, base, name)
                .newLine()
                .text("<%s>Usages: <%s>%s", Colors.NAME, Colors.VALUE, hasUsage() ? usages : "Unlimited")
                .space()
                .text("<%s><click:suggest_command:'%s \"%s\" usages '>[Change]</click>", Colors.CHANGE, base, name)
                .space()
                .text("<%s><click:run_command:'%s \"%s\" usages -1'>[Unlimited]</click>", Colors.CHANGE, base, name)
                .build();
    }

    public String asInfoComponent() {
        return MessageComposer.create()
                .text("<%s>%s", Colors.HEADING, name)
                .newLine()
                .text("<%s>Brush: <%s>%s", Colors.NAME, Colors.VALUE, brushName)
                .newLine()
                .text("<%s>Brush Owner: <%s>%s", Colors.NAME, Colors.VALUE, hasGlobalBrush() ? "global" : Bukkit.getOfflinePlayer(owner).getName())
                .newLine()
                .text("<%s>Permission: <%s>%s", Colors.NAME, Colors.VALUE, permission())
                .newLine()
                .text("<%s>Usages: <%s>%s", Colors.NAME, Colors.VALUE, hasUsage() ? usages : "Unlimited")
                .build();
    }

    public String asListComponent() {
        return MessageComposer.create().text("<%s><hover:show_text:'%s'>%s", Colors.NAME, asInfoComponent(), name)
                .space()
                .text("<click:run_command:'/schematictools info \"%s\"'><%s>[Info]</click>", name, Colors.CHANGE)
                .build();
    }

    public boolean hasGlobalBrush() {
        return owner.equals(GLOBAL);
    }

    @Override
    public String toString() {
        return "Tool{owner=%s, name='%s', brushName='%s', id=%d}".formatted(owner, name, brushName, id);
    }
}
