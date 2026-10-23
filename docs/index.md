---
sidebar_position: 1
---

# Schematic Tools

Schematic Tools is an add-on for [Schematic Brush Reborn](/schematic-brush-reborn).
It turns a saved brush into an item that players can use without setting up the brush themselves.

## Why use it

Setting up a schematic brush takes a few commands and some knowledge of the brush syntax.
With Schematic Tools, a staff member does that work once:

1. Build and save a brush with Schematic Brush Reborn.
2. Create a tool from the saved brush.
3. Bind the tool to an item and hand the item out.

Whoever holds the item can paste with the brush. They need no Schematic Brush Reborn commands for it.

Some ideas:

- Give builders a "tree" item that pastes random trees.
- Sell a decoration item in a shop that can paste ten times and then runs out.
- Hand out an item that only players with a certain rank can use.

## Features

- Bind a saved brush to any item that is not a block, including items with a custom name and lore.
- Limit who can use a tool with a permission of your choice.
- Limit how often each item can paste.
- Change a tool later. All items bound to it follow the change.
- Optionally remove items that are used up or whose tool or brush no longer exists.

## Requirements

| What | Version |
|---|---|
| Schematic Brush Reborn | 3 |
| FastAsyncWorldEdit | required |
| Paper | 1.21.11 or newer |
| Java | 25 |

See [Getting started](/schematic-brush-reborn/getting-started) to set up the core plugin.

## Installation

1. Make sure Schematic Brush Reborn 3 and FastAsyncWorldEdit are installed.
2. Download the Schematic Tools jar from [Lyna](https://lyna.eldoria.de/products/6) and put it into your server's `plugins` folder.
3. Restart the server.

The add-on creates its files in `plugins/SchematicTools/`.
There is nothing you have to set up before you can create your first tool.

### Updating from version 1

Version 2 needs Schematic Brush Reborn 3.
If your config was written by a version older than 1.1.0, update to 1.1.2 first and start the server once.
Otherwise version 2 stops with an error on startup.

## How it changes brushes

A tool does not copy the brush. It remembers the name and owner of the saved brush and loads it each time a player picks up the item.

- Change the saved brush, and every tool that uses it changes too.
- Delete the saved brush, and every tool that uses it stops working.

If you use [Schematic Brush Database](/schematic-brush-reborn/database), tools load their brush from the database like everything else.

## Next steps

- [Creating tools](./creating-tools.md): create a tool, bind it and hand it out.
- [Commands and permissions](./commands.md): every command and permission.
- [Configuration](./configuration.md): the config files.
