# generate the model from the schema
xjc -p org.openbel.framework.ws.model \
    -d ./src/main/java/ \
    -no-header \
    ./src/main/resources/belframework-web-api.xsd 

# copy the scehma to the WEB-INF/xsd folder
cp ./src/main/resources/*.xsd ./src/main/webapp/WEB-INF/xsd

