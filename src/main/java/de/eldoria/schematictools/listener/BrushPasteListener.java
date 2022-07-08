/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.listener;

import de.eldoria.eldoutilities.messages.MessageSender;
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
        var currentTool = SchematicTool.getCurrentTool(event.player());
        if (currentTool.isEmpty()) return;

        var toolMeta = currentTool.get();

        var optionalTool = configuration.tools().byId(toolMeta.id());
        if (optionalTool.isEmpty()) return;

        var tool = optionalTool.get();
        if (!tool.hasUsage()) return;

        if (tool.usages() >= toolMeta.usages()) {
            event.setCancelled(true);
            messageSender.sendMessage(event.player(), "Usaged of schematic tool are exceeded.");
        }
    }

    @EventHandler
    public void onPostPaste(PostPasteEvent event) {
        var currentTool = SchematicTool.getCurrentTool(event.player());
        if (currentTool.isEmpty()) return;

        currentTool.get().incrementUsage();
    }
}
