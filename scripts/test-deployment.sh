#!/bin/bash

SCRIPT_DIR=$(dirname "$0")

BUKKIT_DIR="$SCRIPT_DIR/../bukkit-testserver"
PLUGIN_DIR="$SCRIPT_DIR/../bukkit-testserver/plugins"

# TODO: This is a bad solution! Maven should write necessary information into an extra file.
ARTIFACT_ID="$(grep -C3 '<groupId>de.craftinc' "$SCRIPT_DIR/../pom.xml" | grep '<artifactId>' | sed 's/[ \t]*<artifactId>//g' | sed 's/<\/artifactId>[ \t]*//g')"

# TODO: This is a bad solution! Maven should write necessary information into an extra file.
VERSION="$(grep -C3 '<groupId>de.craftinc' "$SCRIPT_DIR/../pom.xml" | grep '<version>' | sed 's/[ \t]*<version>//g' | sed 's/<\/version>[ \t]*//g')"


mkdir -p "$PLUGIN_DIR"

cp "$SCRIPT_DIR/../target/$ARTIFACT_ID-$VERSION".jar "$PLUGIN_DIR/$ARTIFACT_ID".jar

echo -e "ddidderr\nmice_on_drugs\nMochaccino\nbeuth_el_max" > "$BUKKIT_DIR/ops.txt"

"$SCRIPT_DIR/minecraft.sh" reload_or_start
