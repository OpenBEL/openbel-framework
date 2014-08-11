#!/usr/bin/env bash
if [ ! -f "pom.xml" ]; then
    echo "${0} should be run from the top-level directory." >&2
    exit 1
fi

VERSION=$(grep -F "<belframework-release.version>" pom.xml \
          | grep ">.*<" -o | tr -d '<>')
echo "VERSION: ${VERSION}"

if [ ! -z "${buildNumber}" ]; then
    echo "Applying build number."
    VERSION+="-build${buildNumber}"
    echo "VERSION: ${VERSION}"
fi

SHORT_REV=$(git rev-parse --short HEAD 2>/dev/null)
if [ ! -z "$SHORT_REV" ]; then
    echo "Applying git revision."
    VERSION+="-${SHORT_REV}"
    echo "VERSION: ${VERSION}"
fi

echo "Applying date."
VERSION+="-$(date +%Y-%m-%d)"
echo "VERSION: ${VERSION}"

set -x verbose
export MAVEN_OPTS="-XX:MaxPermSize=128M" 
mvn -Dbelframework-release.version=${VERSION} \
    -Pdistribution clean package assembly:assembly install

cd tests/functional || exit 1
mvn -Dbelframework-release.version=${VERSION} clean verify

