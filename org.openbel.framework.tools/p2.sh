#!/usr/bin/env bash

# Run phase two with remote debugging capabilities
#JVM_OPTIONS="-Xdebug -Xrunjdwp:transport=dt_socket,address=1044,server=y,suspend=n"
JVM_OPTIONS=""

mvn -o -q exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath $JVM_OPTIONS org.openbel.framework.tools.PhaseTwoApplication $*"
