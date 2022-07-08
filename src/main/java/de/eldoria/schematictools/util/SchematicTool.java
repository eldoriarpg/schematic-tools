/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.util;

import de.eldoria.eldoutilities.utils.DataContainerUtil;
import de.eldoria.schematictools.configuration.elements.Tool;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SchematicTool {

    private SchematicTool() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static final NamespacedKey TOOL_ID = new NamespacedKey("schematictools", "tool_id");
    public static final NamespacedKey USED = new NamespacedKey("schematictools", "used");
    public static final NamespacedKey UNIQUE = new NamespacedKey("schematictools", "unique");
    public static final NamespacedKey LORE_INDEX = new NamespacedKey("schematictools", "lore_index");

    private static ItemStack getPlayerItem(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public static Optional<ToolMeta> getCurrentTool(Player player) {
        return getTool(getPlayerItem(player));
    }

    public static Optional<Integer> getToolId(ItemStack stack) {
        return DataContainerUtil.get(stack, TOOL_ID, PersistentDataType.INTEGER);
    }

    public static Optional<Integer> getUsed(ItemStack stack) {
        return DataContainerUtil.get(stack, USED, PersistentDataType.INTEGER);
    }

    public static Optional<ToolMeta> getTool(ItemStack stack) {
        var toolId = getToolId(stack);
        if (toolId.isEmpty()) return Optional.empty();
        var used = getUsed(stack);
        return Optional.of(new ToolMeta(toolId.get(), used.get(), stack));
    }

    public static void makeUnique(ItemStack stack) {
        DataContainerUtil.setIfAbsent(stack, UNIQUE, PersistentDataType.STRING, String.valueOf(System.currentTimeMillis()));
    }

    public static void setToolId(ItemStack stack, int id) {
        DataContainerUtil.setIfAbsent(stack, TOOL_ID, PersistentDataType.INTEGER, id);
    }

    public static void initTool(ItemStack stack, Tool tool) {
        setToolId(stack, tool.id());
        makeUnique(stack);
        setUsed(stack, 0);
        updateUsage(stack, tool);
    }

    public static void setUsed(ItemStack stack, int value) {
        DataContainerUtil.setIfAbsent(stack, USED, PersistentDataType.INTEGER, value);
    }

    public static void incrementUsage(ItemStack stack) {
        DataContainerUtil.computeIfPresent(stack, USED, PersistentDataType.INTEGER, v -> v + 1);
    }

    public static void updateUsage(ItemStack stack, Tool tool) {
        if (!tool.hasUsage()) {
            var loreIndex = DataContainerUtil.get(stack, LORE_INDEX, PersistentDataType.INTEGER);
            loreIndex.ifPresent(index -> {
                var meta = stack.getItemMeta();
                var lore = meta.getLore();
                lore.remove(index.intValue());
                stack.setItemMeta(meta);
                DataContainerUtil.remove(stack, LORE_INDEX, PersistentDataType.INTEGER);
            });
            return;
        }

        var loreIndex = DataContainerUtil.get(stack, LORE_INDEX, PersistentDataType.INTEGER);
        var meta = stack.getItemMeta();
        List<String> lore;
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        if (loreIndex.isPresent()) {
            lore.set(loreIndex.get(), String.format("Used: %s/%s", 0, tool.usages()));
        } else {
            DataContainerUtil.putValue(stack, LORE_INDEX, PersistentDataType.INTEGER, lore.size());
            lore.add(String.format("Used: %s/%s", 0, tool.usages()));
        }
        stack.setItemMeta(meta);
    }

    public record ToolMeta(int id, int usages, ItemStack stack) {

        public void updateUsage(Tool tool) {
            SchematicTool.updateUsage(stack, tool);
        }

        public void incrementUsage() {
            SchematicTool.incrementUsage(stack);
        }
    }
}
