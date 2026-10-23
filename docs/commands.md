---
sidebar_position: 3
---

# Commands and permissions

## Commands

The base command is `/schematictools`. The short form is `/sbt`.
All commands can only be used by players, not from the console.

| Command | Permission | What it does |
|---|---|---|
| `/sbt create <name> <brush> [-p <permission>] [-u <usages>] [-o <owner>]` | `schematictools.modify` | Create a tool from a saved brush. |
| `/sbt bind <name>` | `schematictools.bind` | Bind a tool to the item in your main hand. |
| `/sbt info` | `schematictools.info.current` | Show the tool in your main hand. |
| `/sbt info <name>` | `schematictools.info.all` | Show any tool by name. |
| `/sbt list [page]` | `schematictools.list` | List all tools, 15 per page. |
| `/sbt modify <name> <field> <value>` | `schematictools.modify` | Change a tool. |
| `/sbt remove <name>` | `schematictools.modify` | Delete a tool. |

:::note
The base command itself needs `schematictools.use`. Give it to everyone who should run any `/sbt` command.
:::

### create

```text
/sbt create <name> <brush> [-p <permission>] [-u <usages>] [-o <owner>]
```

| Argument | Required | What it means |
|---|---|---|
| `name` | yes | Name of the new tool. Must not be taken yet. Use quotes for names with spaces. |
| `brush` | yes | Name of a saved brush of yours, or of the owner given with `-o`. |
| `-p <permission>` | no | Permission needed to use the tool. Default: `schematictools.use`. |
| `-u <usages>` | no | How often each item can paste. 1 or more. Default: no limit. |
| `-o <owner>` | no | Name of the player whose saved brush to use. The player must have played on the server before. |

### bind

```text
/sbt bind <name>
```

Hold the item in your main hand. Blocks cannot be used.
Binding an item that was bound before replaces the old tool and starts its usage count at 0.

### info

```text
/sbt info [name]
```

Without a name, it shows the tool in your main hand. With a name, it shows that tool.

The overview shows the brush, the brush owner, the permission and the usage limit.
With `schematictools.modify` it also shows `[Change]` buttons and an `[Unlimited]` button for the usage limit.
With `schematictools.info.all` it shows a `[Back]` button to the list.

### list

```text
/sbt list [page]
```

Shows all tools. Hover over a name to see its details, or click `[Info]`.
Use the arrows at the bottom to turn pages. If you type a page number, the first page is `0`.

### modify

```text
/sbt modify <name> <field> <value>
```

| Field | Value |
|---|---|
| `name` | New name for the tool. Must not be taken yet. |
| `brushName` | Name of another saved brush. Add `-o <owner>` to use a brush of another player. |
| `usages` | 1 or more, or `-1` for no limit. |
| `permission` | Permission needed to use the tool. |

The field names are not case sensitive.

### remove

```text
/sbt remove <name>
```

Deletes the tool. Items bound to it stop working.

### Update command

When a new version is found, admins get a chat message with buttons. The buttons run this command:

```text
/schematictools:eldo-update <download|enable-auto|later|never>
```

The console, operators and players with `schematictools.updates` may use it.
See [updates](./configuration.md#updates) for what each choice changes.

## Permissions

| Permission | Default | What it allows |
|---|---|---|
| `schematictools.use` | operators | Run `/sbt`. Also needed to use every tool that has no permission of its own. |
| `schematictools.bind` | operators | Bind tools to items. |
| `schematictools.list` | operators | List all tools. |
| `schematictools.info.current` | operators | See the tool in your hand. |
| `schematictools.info.all` | operators | See any tool by name. |
| `schematictools.modify` | operators | Create, change and remove tools. |
| `schematictools.updates` | operators | Hear about new versions on join and download them. |

To let normal players use tools, give them `schematictools.use`, or the permission you set with `-p` on the tool.

A tool only checks its own permission. Players do not need any Schematic Brush Reborn permission to paste with a tool.
For the core permissions see the [permission reference](/schematic-brush-reborn/reference/permissions).
