/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration.elements;

import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.schematicbrush.storage.Storage;
import de.eldoria.schematicbrush.storage.brush.Brush;
import de.eldoria.schematicbrush.util.Escape;
import de.eldoria.schematictools.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Tool {
    private static final UUID GLOBAL = new UUID(0L, 0L);
    /**
     * The unique numeric id of the brush
     */
    private int id;
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

    public Tool() {}

    public Tool(UUID owner, String name, String brushName, int id, String permission, int usages) {
        this.owner = owner;
        this.name = name;
        this.brushName = brushName;
        this.id = id;
        this.permission = permission;
        this.usages = usages;
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
        var argument = Escape.argument(name);

        return MessageComposer.create()
                .text("<heading>%s", Escape.text(name))
                .newLine()
                .text("<name>Brush: <value>%s", Escape.text(brushName))
                .space()
                .text("<change><click:suggest_command:'%s \"%s\" brushName '>[Change]</click>", base, argument)
                .newLine()
                .text("<name>Brush Owner: <value>%s", Escape.text(ownerName()))
                .newLine()
                .text("<name>Permission: <value>%s", Escape.text(permission()))
                .space()
                .text("<change><click:suggest_command:'%s \"%s\" permission '>[Change]</click>", base, argument)
                .newLine()
                .text("<name>Usages: <value>%s", hasUsage() ? usages : "Unlimited")
                .space()
                .text("<change><click:suggest_command:'%s \"%s\" usages '>[Change]</click>", base, argument)
                .space()
                .text("<change><click:run_command:'%s \"%s\" usages -1'>[Unlimited]</click>", base, argument)
                .build();
    }

    public String asInfoComponent() {
        return MessageComposer.create()
                .text("<heading>%s", Escape.text(name))
                .newLine()
                .text("<name>Brush: <value>%s", Escape.text(brushName))
                .newLine()
                .text("<name>Brush Owner: <value>%s", Escape.text(ownerName()))
                .newLine()
                .text("<name>Permission: <value>%s", Escape.text(permission()))
                .newLine()
                .text("<name>Usages: <value>%s", hasUsage() ? usages : "Unlimited")
                .build();
    }

    public String asListComponent() {
        return MessageComposer.create()
                .text("<name><hover:show_text:'%s'>%s</hover>", Escape.argument(asInfoComponent()), Escape.text(name))
                .space()
                .text("<click:run_command:'/schematictools info \"%s\"'><change>[Info]</click>", Escape.argument(name))
                .build();
    }

    private String ownerName() {
        if (hasGlobalBrush()) return "global";
        return String.valueOf(Bukkit.getOfflinePlayer(owner).getName());
    }

    public boolean hasGlobalBrush() {
        return owner.equals(GLOBAL);
    }

    @Override
    public String toString() {
        return "Tool{owner=%s, name='%s', brushName='%s', id=%d}".formatted(owner, name, brushName, id);
    }
}
