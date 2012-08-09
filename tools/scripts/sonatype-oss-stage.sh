#!/usr/bin/env bash
#
# sonatype-oss-stage.sh
# deploys bel framework modules to sonatype's staging repository 
#   input: VERSION GPG_PASSPHRASE

if [ $# != 2 ]; then
    echo "usage: sonatype-oss-stage.sh VERSION GPG_PASSPHRASE"
    exit 1
fi

VERSION=$1
PASSPHRASE=$2

# ensure perl is available
PERL_CMD=$(hash perl > /dev/null 2>&1)
if [ $? != 0 ]; then
    echo "OMG, you don't have perl?!"
    exit 1
fi

# ensure maven is available
MVN_CMD=$(hash mvn > /dev/null 2>&1)
if [ $? != 0 ]; then
    echo "OMG, you don't have maven?!"
    exit 1
fi

# ensure gpg is available
GPG_CMD=$(hash gpg > /dev/null 2>&1)
if [ $? != 0 ]; then
    echo "The 'gpg' command cannot be found."
    exit 1
fi

DIR=$(dirname $0)
PARENT_POM=$DIR/../../pom.xml
M2_REPO=$HOME/.m2/repository
GROUP=org/openbel

if [ ! -d $M2_REPO ]; then
    echo "The local maven repository doesn't exist at $HOME/.m2/repository.  Where my artifacts at?"
    exit 1
fi

if [ ! -f $PARENT_POM ]; then
    echo "The parent pom cannot be found at $DIR/../../pom.xml"
    exit 1
fi

# find modules, excluding webservice server (thanks maven fo the help)
MODULES=$(cat $PARENT_POM | perl -wlne 'print $1 if /<module>([a-z.]+)<\/module>/' | grep -v ^org.openbel.framework.ws$)
for m in $MODULES; do
    POM_ARTIFACT=$m-$VERSION.pom
    M2_LOCATION=$M2_REPO/$GROUP/$m/$VERSION/$POM_ARTIFACT
    if [ ! -f $M2_LOCATION ]; then
        echo "Your pom for $m does not exist at $M2_LOCATION"
        exit 1
    fi
    cp $M2_LOCATION ./$POM_ARTIFACT
    
    JAR_ARTIFACT=$m-$VERSION.jar
    M2_LOCATION=$M2_REPO/$GROUP/$m/$VERSION/$JAR_ARTIFACT
    if [ ! -f $M2_LOCATION ]; then
        echo "Your jar for $m does not exist at $M2_LOCATION"
        exit 1
    fi
    cp $M2_LOCATION ./$JAR_ARTIFACT

    SOURCE_ARTIFACT=$m-$VERSION-sources.jar
    M2_LOCATION=$M2_REPO/$GROUP/$m/$VERSION/$SOURCE_ARTIFACT
    if [ ! -f $M2_LOCATION ]; then
        echo "Your source jar for $m does not exist at $M2_LOCATION"
        exit 1
    fi
    cp $M2_LOCATION ./$SOURCE_ARTIFACT

    JAVADOC_ARTIFACT=$m-$VERSION-javadoc.jar
    M2_LOCATION=$M2_REPO/$GROUP/$m/$VERSION/$JAVADOC_ARTIFACT
    if [ ! -f $M2_LOCATION ]; then
        echo "Your javadoc jar for $m does not exist at $M2_LOCATION"
        exit 1
    fi
    cp $M2_LOCATION ./$JAVADOC_ARTIFACT

    # deploy pom, jar, javadoc, sources
    echo "Deploying module $m, along with pom/javadocs/sources"
    mvn gpg:sign-and-deploy-file -Dgpg.passphrase=$PASSPHRASE -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=staging -DpomFile=$POM_ARTIFACT -Dfile=$JAR_ARTIFACT -Dpackaging=jar
    mvn gpg:sign-and-deploy-file -Dgpg.passphrase=$PASSPHRASE -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=staging -DpomFile=$POM_ARTIFACT -Dfile=$JAVADOC_ARTIFACT -Dclassifier=javadoc -Dpackaging=jar
    mvn gpg:sign-and-deploy-file -Dgpg.passphrase=$PASSPHRASE -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=staging -DpomFile=$POM_ARTIFACT -Dfile=$SOURCE_ARTIFACT -Dclassifier=sources -Dpackaging=jar
done
