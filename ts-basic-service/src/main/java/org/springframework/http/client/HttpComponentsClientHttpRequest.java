//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

final class HttpComponentsClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final HttpClient httpClient;
    private final HttpUriRequest httpRequest;
    private final HttpContext httpContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpComponentsClientHttpRequest.class);

    HttpComponentsClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context) {
        this.httpClient = client;
        this.httpRequest = request;
        this.httpContext = context;
    }

    public String getMethodValue() {
        return this.httpRequest.getMethod();
    }

    public URI getURI() {
        return this.httpRequest.getURI();
    }

    HttpContext getHttpContext() {
        return this.httpContext;
    }

    protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        addHeaders(this.httpRequest, headers);
        if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
            HttpEntity requestEntity = new ByteArrayEntity(bufferedOutput);
            entityEnclosingRequest.setEntity(requestEntity);
        }

        HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
        LOGGER.info("this is point 7 [{}]",System.nanoTime());
        return new HttpComponentsClientHttpResponse(httpResponse);
    }

    static void addHeaders(HttpUriRequest httpRequest, HttpHeaders headers) {
        headers.forEach((headerName, headerValues) -> {
            if ("Cookie".equalsIgnoreCase(headerName)) {
                String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
                httpRequest.addHeader(headerName, headerValue);
            } else if (!"Content-Length".equalsIgnoreCase(headerName) && !"Transfer-Encoding".equalsIgnoreCase(headerName)) {
                Iterator var5 = headerValues.iterator();

                while(var5.hasNext()) {
                    String headerValuex = (String)var5.next();
                    httpRequest.addHeader(headerName, headerValuex);
                }
            }

        });
    }
}
