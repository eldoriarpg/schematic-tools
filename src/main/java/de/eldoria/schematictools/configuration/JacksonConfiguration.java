/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import de.eldoria.eldoutilities.config.JacksonConfig;
import de.eldoria.schematictools.configuration.elements.ToolRemoval;
import de.eldoria.schematictools.configuration.elements.Tools;
import de.eldoria.schematictools.configuration.elements.UpdateSettings;
import dev.chojo.ocular.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class JacksonConfiguration extends JacksonConfig<ConfigFile> implements Configuration {
    public static final Key<Tools> TOOLS =
            Key.builder(Path.of("tools.yml"), Tools::new).name("Tools").build();

    public JacksonConfiguration(@NotNull Plugin plugin) {
        super(plugin, Key.builder(Path.of("config.yml"), ConfigFile::new).build());
    }

    @Override
    public Tools tools() {
        return secondary(TOOLS);
    }

    @Override
    public ToolRemoval toolRemoval() {
        return main().toolRemoval();
    }

    @Override
    public UpdateSettings updates() {
        return main().updates();
    }
}
