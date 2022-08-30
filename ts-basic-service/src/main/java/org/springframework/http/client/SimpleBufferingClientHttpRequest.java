//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.http.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

final class SimpleBufferingClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final HttpURLConnection connection;
    private final boolean outputStreaming;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBufferingClientHttpRequest.class);

    SimpleBufferingClientHttpRequest(HttpURLConnection connection, boolean outputStreaming) {
        this.connection = connection;
        this.outputStreaming = outputStreaming;
    }

    public String getMethodValue() {
        return this.connection.getRequestMethod();
    }

    public URI getURI() {
        try {
            return this.connection.getURL().toURI();
        } catch (URISyntaxException var2) {
            throw new IllegalStateException("Could not get HttpURLConnection URI: " + var2.getMessage(), var2);
        }
    }

    protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        addHeaders(this.connection, headers);
        if (this.getMethod() == HttpMethod.DELETE && bufferedOutput.length == 0) {
            this.connection.setDoOutput(false);
        }

        if (this.connection.getDoOutput() && this.outputStreaming) {
            this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
        }

        this.connection.connect();
        if (this.connection.getDoOutput()) {
            LOGGER.info("this is point 2 [{}]",System.currentTimeMillis());
            FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
        } else {
            this.connection.getResponseCode();
        }
        LOGGER.info("this is point 7 [{}]",System.currentTimeMillis());
        return new SimpleClientHttpResponse(this.connection);
    }

    static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
        String method = connection.getRequestMethod();
        if ((method.equals("PUT") || method.equals("DELETE")) && !StringUtils.hasText(headers.getFirst("Accept"))) {
            headers.set("Accept", "*/*");
        }

        headers.forEach((headerName, headerValues) -> {
            if ("Cookie".equalsIgnoreCase(headerName)) {
                String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
                connection.setRequestProperty(headerName, headerValue);
            } else {
                Iterator var6 = headerValues.iterator();

                while(var6.hasNext()) {
                    String headerValuex = (String)var6.next();
                    String actualHeaderValue = headerValuex != null ? headerValuex : "";
                    connection.addRequestProperty(headerName, actualHeaderValue);
                }
            }

        });
    }
}
