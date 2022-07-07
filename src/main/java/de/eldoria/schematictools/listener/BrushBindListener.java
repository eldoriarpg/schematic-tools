/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.listener;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.extension.platform.Actor;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematicbrush.util.WorldEditBrush;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.util.SchematicTool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BrushBindListener implements Listener {
    private static final WorldEdit WORLD_EDIT = WorldEdit.getInstance();
    private final Plugin plugin;
    private final SchematicBrushReborn sbr;
    private final Configuration configuration;

    public BrushBindListener(Plugin plugin, SchematicBrushReborn sbr, Configuration configuration) {
        this.plugin = plugin;
        this.sbr = sbr;
        this.configuration = configuration;
    }

    @EventHandler
    public void onItemSwap(PlayerItemHeldEvent event) {
        var previousSlot = event.getPreviousSlot();
        var newSlot = event.getNewSlot();

        var player = event.getPlayer();

        var inventory = player.getInventory();
        var previousItem = inventory.getItem(previousSlot);
        var newItem = inventory.getItem(newSlot);

        unbindTool(player, previousItem);
        bindTool(player, newItem);
    }

    public void unbindTool(Player player, ItemStack stack) {
        var toolId = SchematicTool.getToolId(stack);
        if (toolId.isEmpty()) return;

        try {
            getLocalSession(player).setTool(BukkitAdapter.adapt(stack).getType(), null);
        } catch (InvalidToolBindException e) {
            throw new RuntimeException(e);
        }
    }

    public void bindTool(Player player, ItemStack stack) {
        var toolId = SchematicTool.getToolId(stack);
        if (toolId.isEmpty()) return;

        var used = SchematicTool.getUsed(stack);
        if (used.get() == 0) return;

        var tool = configuration.tools().byId(toolId.get());

        if (tool.isEmpty()) {
            plugin.getLogger().warning("Brush Tool with ID " + toolId.get() + " does not exist anymore.");
            return;
        }

        tool.get().getBrush(sbr.storageRegistry().activeStorage())
                .thenAccept(brush -> {
                    if (brush.isEmpty()) {
                        plugin.getLogger().warning("Tool " + tool.get() + " has an invalid brush.");
                        return;
                    }

                    var build = brush.get().snapshot().load(player, sbr.brushSettingsRegistry(), sbr.schematics()).build(plugin, player);
                    //TODO: We need a default permission
                    WorldEditBrush.setBrush(player, build, tool.get().permission());
                });
    }

    private static LocalSession getLocalSession(Player player) {
        Actor actor = BukkitAdapter.adapt(player);

        return WORLD_EDIT.getSessionManager().get(actor);
    }
}
