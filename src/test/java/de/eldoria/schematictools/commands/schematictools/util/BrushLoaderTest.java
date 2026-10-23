/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BrushLoaderTest {
    @Test
    void globalBrushIsLookedUpWithoutItsPrefix() {
        assertEquals("forest", BrushLoader.storedName("g:forest"));
    }

    @Test
    void playerBrushKeepsItsName() {
        assertEquals("forest", BrushLoader.storedName("forest"));
    }

    @Test
    void onlyTheLeadingPrefixIsRemoved() {
        assertEquals("big:g:forest", BrushLoader.storedName("big:g:forest"));
    }

    @Test
    void ownerIsReadFromUuid() {
        var uuid = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");

        assertEquals(Optional.of(uuid), BrushLoader.parseUuid("069a79f4-44e9-4726-a5be-fca90e38aaf5"));
        assertEquals(Optional.of(uuid), BrushLoader.parseUuid("069A79F4-44E9-4726-A5BE-FCA90E38AAF5"));
    }

    @Test
    void namesAreNoUuid() {
        assertEquals(Optional.empty(), BrushLoader.parseUuid("Alex"));
        assertEquals(Optional.empty(), BrushLoader.parseUuid("069a79f444e94726a5befca90e38aaf5"));
    }
}
