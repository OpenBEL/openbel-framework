# generate the model from the schema
xjc -p org.openbel.framework.ws.model \
    -d ./src/main/java/ \
    -no-header \
    ./src/main/webapp/WEB-INF/xsd/belframework-web-api.xsd 

