# generate the client stubs from the WSDL
if [ -z $1 ]; then
    echo 'usage: build_client_stubs.sh [PORT]'
    exit 1
fi

wsimport -s src/main/java -p org.openbel.framework.ws.client -keep -Xnocompile http://localhost:$1/openbel-ws/belframework.wsdl
