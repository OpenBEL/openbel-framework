#!/usr/bin/env bash
if [ ! -d target/classes ]; then
    echo "Build core first."
    exit 1
fi

if [ ! -d ../BELFrameworkCommon/target/classes ]; then
    echo "Build common first."
    exit 1
fi

java -cp target/classes:../BELFrameworkCommon/target/classes \
    org.openbel.framework.common.cfg.SystemConfiguration
