#!/usr/bin/env bash
VERSION=$(grep "^    <version>.*</version>" pom.xml -o \
    | grep ">.*<" -o | tr -d '<>')
echo -n "Ready to package ${VERSION}. Hit [ENTER] or [CTRL-C]."; read NULL
mvn -Pdistribution clean package assembly:assembly install

