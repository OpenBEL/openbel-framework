#!/usr/bin/env bash
mvn -o -q exec:java -Dexec.mainClass="org.openbel.framework.tools.Backtrack" -Dexec.args="$*"
