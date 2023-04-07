/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.schematictools.configuration.elements.ToolRemoval;
import de.eldoria.schematictools.configuration.elements.Tools;
import org.bukkit.plugin.Plugin;

public interface Configuration {

    void save();
    Tools tools();
    ToolRemoval toolRemoval();
}
