#!/usr/bin/env bash
if [ ! -f "pom.xml" ]; then
    echo "${0} should be run from the top-level directory." >&2
    exit 1
fi

REL_VERSION=$(grep -F "<belframework-release.version>" pom.xml \
              | grep ">.*<" -o | tr -d '<>')
BUILD_VERSION="${REL_VERSION}-${buildNumber}"

echo "REL_VERSION: ${REL_VERSION}"
echo "BUILD_VERSION: ${BUILD_VERSION}"

set -x verbose
export MAVEN_OPTS="-XX:MaxPermSize=128M" 
mvn -Dbelframework-release.version=${BUILD_VERSION} \
    -Pdistribution clean package install assembly:assembly

cd tests/functional || exit 1
mvn -Dbelframework-release.version=${BUILD_VERSION} verify

