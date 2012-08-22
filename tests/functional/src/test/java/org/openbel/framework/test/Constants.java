package org.openbel.framework.test;

public final class Constants {

    /**
     * {@value}
     */
    public static final String SMALL_CORPUS_KAM_NAME = "small_corpus";

    /**
     * Environment variable for tomcat http pprt: {@value}
     */
    public static final String TOMCAT_HTTP_PORT_ENV_VAR = "TOMCAT_HTTP_PORT";

    /**
     * Formatted {@link String string} for endpoint url: {@value}
     */
    public static final String ENDPOINT_FORMAT =
            "http://localhost:%d/openbel-ws/belframework.wsdl";

    private Constants() {}
}
