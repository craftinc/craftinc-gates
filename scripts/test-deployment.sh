#!/bin/bash

SCRIPT_DIR="$(dirname "$0")"

DEVELOPER=$(whoami)

if [ $DEVELOPER = "tobi" ]; then
	BUKKIT_DIR="$HOME/minecraft/testbuk"
	PLUGIN_DIR="$HOME/minecraft/testbuk/plugins"
	START_STOP_SCRIPT="$BUKKIT_DIR/../minecraft.sh"
else
	BUKKIT_DIR="$SCRIPT_DIR/../bukkit-testserver"
	PLUGIN_DIR="$SCRIPT_DIR/../bukkit-testserver/plugins"
	START_STOP_SCRIPT="$SCRIPT_DIR/minecraft.sh"
fi

# TODO: This is a bad solution! Maven should write necessary information into an extra file.
ARTIFACT_ID="$(grep -C5 '<groupId>de.craftinc' "$SCRIPT_DIR/../pom.xml" | grep '<name>' | sed 's/[ \t]*<name>//g' | sed 's/<\/name>[ \t]*//g')"

# TODO: This is a bad solution! Maven should write necessary information into an extra file.
VERSION="$(grep -C5 '<groupId>de.craftinc' "$SCRIPT_DIR/../pom.xml" | grep '<version>' | sed 's/[ \t]*<version>//g' | sed 's/<\/version>[ \t]*//g')"

mkdir -p "$PLUGIN_DIR"

cp "$SCRIPT_DIR/../target/$ARTIFACT_ID $VERSION".jar "$PLUGIN_DIR/"

echo -e "ddidderr\nmice_on_drugs\nMochaccino\nbeuth_el_max" > "$BUKKIT_DIR/ops.txt"

$START_STOP_SCRIPT reload_or_start
