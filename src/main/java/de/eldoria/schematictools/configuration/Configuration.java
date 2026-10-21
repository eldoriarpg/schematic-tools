/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import de.eldoria.schematictools.configuration.elements.ToolRemoval;
import de.eldoria.schematictools.configuration.elements.Tools;
import de.eldoria.schematictools.configuration.elements.UpdateSettings;

public interface Configuration {

    void save();

    Tools tools();

    ToolRemoval toolRemoval();

    UpdateSettings updates();
}
