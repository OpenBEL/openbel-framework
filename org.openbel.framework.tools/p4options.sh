#!/usr/bin/env bash
if [ ! -d target/classes ]; then
    echo "Build tools first."
    exit 1
fi

if [ ! -d ../BELFrameworkCore/target/classes ]; then
    echo "Build core first."
    exit 1
fi

if [ ! -d ../BELFrameworkCommon/target/classes ]; then
    echo "Build common first."
    exit 1
fi

java -cp target/classes:../BELFrameworkCore/target/classes:../BELFrameworkCommon/target/classes \
    org.openbel.framework.tools.PhaseFourOptions
