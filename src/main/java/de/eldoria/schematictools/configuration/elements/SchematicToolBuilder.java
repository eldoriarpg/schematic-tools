/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.configuration.elements;

import java.util.UUID;

public class SchematicToolBuilder {
    private static final UUID GLOBAL = new UUID(0L, 0L);
    private final UUID owner;
    private final String name;
    private final String brushName;
    private final int id;
    private String permission = null;
    private int usages = -1;

    public SchematicToolBuilder(UUID owner, String name, String brushName, int id) {
        this.owner = owner;
        this.name = name;
        this.brushName = brushName;
        this.id = id;
    }

    public SchematicToolBuilder(String name, String brushName, int id) {
        this.name = name;
        this.brushName = brushName;
        this.id = id;
        owner = GLOBAL;
    }

    public SchematicToolBuilder withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public SchematicToolBuilder withUsages(int usages) {
        this.usages = usages;
        return this;
    }

    public Tool build() {
        return new Tool(owner, name, brushName, id, permission, usages);
    }
}
