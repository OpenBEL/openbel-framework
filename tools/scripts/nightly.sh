#!/usr/bin/env bash
if [ ! -f "pom.xml" ]; then
    echo "${0} should be run from the top-level directory." >&2
    exit 1
fi

MVN_VERSION=$(mvn \
              org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate \
              -Dexpression=belframework-release.version)

BUILD_VERSION="${MVN_VERSION}-${bamboo.buildNumber}"

echo "MVN_VERSION: ${MVN_VERSION}"
echo "BUILD_VERSION: ${BUILD_VERSION}"

mvn -Dbelframework-release.version=${BUILD_VERSION} -Pdistribution clean package assembly:assembly
