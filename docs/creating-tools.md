---
sidebar_position: 2
---

# Creating tools

This page shows the full way from a brush to an item in a player's hand.
All commands start with `/schematictools`, or the short form `/sbt`.

## 1. Save a brush

Build your brush with Schematic Brush Reborn and save it under a name.
See [Presets](/schematic-brush-reborn/presets) for saving brushes and the [command reference](/schematic-brush-reborn/reference/commands) for the commands.

You can save it as one of your own brushes or as a global brush.

## 2. Create the tool

```text
/sbt create <name> <brush> [-p <permission>] [-u <usages>] [-o <owner>]
```

- `name`: the name of the new tool. It must not be taken. Put it in quotes if it has spaces.
- `brush`: the name of your saved brush. For a global brush, put `g:` in front of the name, for example `g:forest`.
- `-p <permission>`: the permission a player needs to use the tool. Without it, the tool uses `schematictools.use`.
- `-u <usages>`: how often each item can paste. Must be 1 or more. Without it, the items can paste without limit.
- `-o <owner>`: take the brush from another player's saved brushes. Enter the name of a player who has played on the server before, or the player's UUID.

Examples:

```text
/sbt create trees forest
/sbt create global-trees g:forest
/sbt create "small house" house -u 5
/sbt create vip-trees forest -p myserver.tools.trees
/sbt create shop-fountain fountain -o Alex -u 10
```

If the brush does not exist, the tool is not created and you see "No brush found with this name."

You need the permission `schematictools.modify`.

## 3. Bind the tool to an item

Hold the item you want to use in your main hand and run:

```text
/sbt bind <name>
```

The item can be anything that is not a block, for example a stick, a blaze rod or a named custom item.
It keeps its name and lore.

Binding does this to the item:

- It links the item to the tool.
- It marks the item as unique, so it does not stack with other items.
- If the tool has a usage limit, it adds a lore line `Used: 0/<usages>`.

You can bind as many items to the same tool as you like. Each item counts its own usages.
Binding an item again starts its count at 0 again.

You need the permission `schematictools.bind`.

## 4. Hand it out

Give the item to a player in any way you like: a kit, a shop, a crate or just `/give`.

## Using a tool

A player selects the item in the hotbar. The action bar shows "Activated schematic tool".
Now the item works like a schematic brush: right-clicking pastes a schematic.
When the player selects another slot or drops the item, the brush is switched off again.

:::tip
After you bind a tool, switch to another hotbar slot and back. The tool only switches on when the item is selected.
:::

The tool stays off when:

- The player does not have the tool's permission. Nothing happens and no message is shown.
- The item is used up.
- The tool was removed, or its brush was deleted.

For the last two cases you can choose to remove the item from the player. See [tool removal](./configuration.md#toolremoval).

## Usage limits

On a tool with a usage limit, every paste counts one use on the item.
The lore line shows the count, for example `Used: 3/10`.

Once the count reaches the limit, the item cannot paste any more and the player sees "The usages of your brush tool are exhausted."

The limit belongs to the tool, the count belongs to the item.
If you raise the limit of a tool later, used up items can paste again.

## Changing a tool

Run `/sbt info <name>` to see a tool. With the permission `schematictools.modify` the overview has `[Change]` buttons next to each value.
They fill in a `/sbt modify` command for you.

You can also type the command yourself:

```text
/sbt modify <name> <field> <value>
```

| Field | Value |
|---|---|
| `name` | A new name that is not taken yet. |
| `brushName` | Another saved brush. Use `g:<name>` for a global brush, or add `-o <owner>` to take it from another player. |
| `usages` | A number of 1 or more, or `-1` for no limit. |
| `permission` | The permission a player needs to use the tool. |

Changes apply to all items that are bound to the tool. Players see a new brush the next time they select the item.

## Removing a tool

```text
/sbt remove <name>
```

Items bound to a removed tool stop working. They stay in the inventories unless `removeInvalidTools` is switched on in the [configuration](./configuration.md#toolremoval).

See [Commands and permissions](./commands.md) for all commands at a glance.
