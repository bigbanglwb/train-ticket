//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractClientHttpRequest implements ClientHttpRequest {
    private final HttpHeaders headers = new HttpHeaders();
    private boolean executed = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientHttpRequest.class);
    public AbstractClientHttpRequest() {
    }

    public final HttpHeaders getHeaders() {
        return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
    }

    public final OutputStream getBody() throws IOException {
        this.assertNotExecuted();
        return this.getBodyInternal(this.headers);
    }

    public final ClientHttpResponse execute() throws IOException {
        this.assertNotExecuted();
        ClientHttpResponse result = this.executeInternal(this.headers);
        LOGGER.info("this is point 10 [{}]",System.nanoTime());
        this.executed = true;
        return result;
    }

    protected void assertNotExecuted() {
        Assert.state(!this.executed, "ClientHttpRequest already executed");
    }

    protected abstract OutputStream getBodyInternal(HttpHeaders var1) throws IOException;

    protected abstract ClientHttpResponse executeInternal(HttpHeaders var1) throws IOException;
}
