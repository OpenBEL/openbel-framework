# generate the client stubs from the WSDL
if [ -z $1 ]; then
    echo 'usage: build_client_stubs.sh [SERVER:PORT]'
    exit 1
fi

mkdir temp
wsimport -s temp -p org.openbel.framework.ws.model -keep -Xnocompile http://$1/openbel-ws/belframework.wsdl
cp temp/org/openbel/framework/ws/model/WebAPI.java src/main/java/org/openbel/framework/ws/model/
cp temp/org/openbel/framework/ws/model/WebAPIService.java src/main/java/org/openbel/framework/ws/model/
rm -r temp
