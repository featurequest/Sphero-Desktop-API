#!/bin/bash

# Version settings
VERSION=`cat VERSION`

# Compile settings 
ANT_CMD="ant package-for-store"

# Zip settings
MOVE_TO="Sphero-Desktop-API-${VERSION}.zip"
MOVE_FILE="Sphero-Desktop-API.zip"

## EXECUTION ##
rm -f Sphero-Desktop-API*.zip && ${ANT_CMD} && mv ${MOVE_FILE} ${MOVE_TO} && git add ${MOVE_TO} && svn add ${MOVE_TO}
