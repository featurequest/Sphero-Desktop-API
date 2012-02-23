#!/bin/bash

# Version settings
VERSION=`cat VERSION`

# Compile settings

ARG="nojavadoc"
if [ -n "$1" ]; then
	ARG="withjavadoc"
fi

echo "Compiling with ${ARG}"
ANT_CMD="ant ${ARG}"

# Zip settings
MOVE_TO="Sphero-Desktop-API-${VERSION}.zip"
MOVE_FILE="Sphero-Desktop-API.zip"
LATEST_FILE="latest.jar"
JAR_LOC="dist/Sphero-Desktop-API-${VERSION}.jar"

## EXECUTION ##
rm -f Sphero-Desktop-API*.zip && ${ANT_CMD} && mv ${MOVE_FILE} ${MOVE_TO} && git add ${MOVE_TO} && svn add ${MOVE_TO} && cp ${JAR_LOC} ${LATEST_FILE}
