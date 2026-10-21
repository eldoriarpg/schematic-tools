/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration.elements;

import de.eldoria.schematictools.configuration.builder.SchematicToolBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Tools {
    private List<Tool> tools = new ArrayList<>();
    private int currentId = 0;

    public Tools() {
    }

    public List<Tool> tools() {
        return tools;
    }

    public SchematicToolBuilder create(String name, String brushName) {
        return new SchematicToolBuilder(name, brushName, currentId++);
    }

    public SchematicToolBuilder create(UUID owner, String name, String brushName) {
        return new SchematicToolBuilder(owner, name, brushName, currentId++);
    }

    public void add(Tool build) {
        tools.add(build);
    }

    public Optional<Tool> byName(String name) {
        return tools.stream().filter(tool -> tool.name().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Tool> byId(int id) {
        return tools.stream().filter(tool -> tool.id() == id).findFirst();
    }

    public void remove(Tool tool) {
        tools.removeIf(t -> t.id() == tool.id());
    }

    public List<String> complete(String name) {
        if (name.isBlank()) return tools.stream()
                .map(Tool::name)
                .toList();
        return tools.stream()
                .map(Tool::name)
                .filter(s -> s.startsWith(name))
                .toList();
    }

    public List<Tool> page(int page, int pageSize) {
        return tools.subList(page * pageSize, Math.min(tools.size(), page * pageSize + pageSize));
    }

    public int pages(int pageSize) {
        return (int) Math.ceil(tools.size() / (double) pageSize);
    }
}
