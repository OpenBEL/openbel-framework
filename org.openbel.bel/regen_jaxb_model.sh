#!/usr/bin/env bash
PROG="xjc"
if [ -z $(which ${PROG} 2>/dev/null) ]; then
    echo "no '${PROG}' in your PATH"
    exit 1
fi

xjc -extension -d src/main/java ../docs/xbel/xbel.xsd -b ../docs/xbel/bindings.xjb -p org.openbel.bel.xbel.model
