/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration.elements;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolsTest {
    private static Tools withTools(int count) {
        var tools = new Tools();
        for (var i = 0; i < count; i++) {
            tools.add(tools.create("tool" + i, "brush").build());
        }
        return tools;
    }

    @Test
    void lastPageHoldsTheRest() {
        var tools = withTools(5);

        assertEquals(List.of("tool4"), tools.page(2, 2).stream().map(Tool::name).toList());
        assertEquals(3, tools.pages(2));
    }

    @Test
    void pageBeyondTheEndIsEmpty() {
        assertTrue(withTools(5).page(3, 2).isEmpty());
        assertTrue(withTools(0).page(1, 15).isEmpty());
    }

    @Test
    void negativePageIsTheFirstPage() {
        assertEquals(List.of("tool0", "tool1"), withTools(5).page(-1, 2).stream().map(Tool::name).toList());
    }
}
