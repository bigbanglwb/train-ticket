//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.http.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fdse.microservice.service.BasicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

abstract class AbstractBufferingClientHttpRequest extends AbstractClientHttpRequest {
    private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractBufferingClientHttpRequest.class);
    AbstractBufferingClientHttpRequest() {
    }

    protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
        return this.bufferedOutput;
    }

    protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
        byte[] bytes = this.bufferedOutput.toByteArray();
        if (headers.getContentLength() < 0L) {
            headers.setContentLength((long)bytes.length);
        }
        LOGGER.info("AbstractBufferingClientHttpRequest this is point 2 [{}]",System.nanoTime());
        ClientHttpResponse result = this.executeInternal(headers, bytes);
        LOGGER.info("AbstractBufferingClientHttpRequest this is point 7 [{}]",System.nanoTime());
        this.bufferedOutput = new ByteArrayOutputStream(0);
        return result;
    }

    protected abstract ClientHttpResponse executeInternal(HttpHeaders var1, byte[] var2) throws IOException;
}
