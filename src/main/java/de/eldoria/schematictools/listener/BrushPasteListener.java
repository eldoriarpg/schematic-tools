/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.listener;

import de.eldoria.eldoutilities.messages.MessageChannel;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.messages.MessageType;
import de.eldoria.schematicbrush.event.PostPasteEvent;
import de.eldoria.schematicbrush.event.PrePasteEvent;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.util.SchematicTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BrushPasteListener implements Listener {
    private final Configuration configuration;
    private final MessageSender messageSender;

    public BrushPasteListener(Configuration configuration, MessageSender messageSender) {
        this.configuration = configuration;
        this.messageSender = messageSender;
    }

    @EventHandler
    public void onPrePaste(PrePasteEvent event) {
        var player = event.player();
        var currentTool = SchematicTool.getCurrentTool(player);
        if (currentTool.isEmpty()) return;

        var toolMeta = currentTool.get();

        var optionalTool = configuration.tools().byId(toolMeta.id());
        if (optionalTool.isEmpty()) return;

        var tool = optionalTool.get();
        if (!tool.hasUsage()) return;

        if (toolMeta.usages() >= tool.usages()) {
            event.setCancelled(true);
            messageSender.send(MessageChannel.ACTION_BAR, MessageType.ERROR,player, "The usages of your brush tool are exhausted.");
            toolMeta.updateUsage(tool);
            if (configuration.toolRemoval().isRemoveUsed()) {
                SchematicTool.getPlayerItem(player).setAmount(0);
            }
        }
    }

    @EventHandler
    public void onPostPaste(PostPasteEvent event) {
        var player = event.player();
        var optToolMeta = SchematicTool.getCurrentTool(player);
        if (optToolMeta.isEmpty()) return;

        var toolMeta = optToolMeta.get();
        toolMeta.incrementUsage();

        var optionalTool = configuration.tools().byId(toolMeta.id());
        if (optionalTool.isEmpty()) return;
        toolMeta.updateUsage(optionalTool.get());


        if (toolMeta.usages() >= optionalTool.get().usages()) {
            messageSender.send(MessageChannel.ACTION_BAR, MessageType.ERROR,player, "The usages of your brush tool are exhausted.");
            if (configuration.toolRemoval().isRemoveUsed()) {
                SchematicTool.getPlayerItem(player).setAmount(0);
            }
        }
    }
}
