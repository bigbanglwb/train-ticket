//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.AbstractClientHttpRequest;

import javax.swing.plaf.TableHeaderUI;

@Contract(
        threading = ThreadingBehavior.IMMUTABLE
)
public class HttpRequestExecutor {
    public static final int DEFAULT_WAIT_FOR_CONTINUE = 3000;
    private final int waitForContinue;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestExecutor.class);
    public HttpRequestExecutor(int waitForContinue) {
        this.waitForContinue = Args.positive(waitForContinue, "Wait for continue time");
    }

    public HttpRequestExecutor() {
        this(3000);
    }

    protected boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
        if ("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
            return false;
        } else {
            int status = response.getStatusLine().getStatusCode();
            return status >= 200 && status != 204 && status != 304 && status != 205;
        }
    }

    public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");

        try {
            LOGGER.info("HttpRequestExecutor send request");
            HttpResponse response = this.doSendRequest(request, conn, context);
            if (response == null) {
                LOGGER.info("HttpRequestExecutor recv response");
                response = this.doReceiveResponse(request, conn, context);
                LOGGER.info("HttpRequestExecutor recv response success");
            }

            return response;
        } catch (IOException var5) {
            closeConnection(conn);
            throw var5;
        } catch (HttpException var6) {
            closeConnection(conn);
            throw var6;
        } catch (RuntimeException var7) {
            closeConnection(conn);
            throw var7;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeConnection(HttpClientConnection conn) {
        try {
            conn.close();
        } catch (IOException var2) {
        }

    }

    public void preProcess(HttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.request", request);
        processor.process(request, context);
    }

    protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException, InterruptedException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        HttpResponse response = null;
        context.setAttribute("http.connection", conn);
        context.setAttribute("http.request_sent", Boolean.FALSE);
        LOGGER.info("stop before write");
        Thread.sleep(10000);
        conn.sendRequestHeader(request);
        if (request instanceof HttpEntityEnclosingRequest) {
            boolean sendentity = true;
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            if (((HttpEntityEnclosingRequest)request).expectContinue() && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
                conn.flush();
                if (conn.isResponseAvailable(this.waitForContinue)) {
                    response = conn.receiveResponseHeader();
                    if (this.canResponseHaveBody(request, response)) {
                        conn.receiveResponseEntity(response);
                    }

                    int status = response.getStatusLine().getStatusCode();
                    if (status < 200) {
                        if (status != 100) {
                            throw new ProtocolException("Unexpected response: " + response.getStatusLine());
                        }

                        response = null;
                    } else {
                        sendentity = false;
                    }
                }
            }

            if (sendentity) {
                conn.sendRequestEntity((HttpEntityEnclosingRequest)request);
            }
        }
        LOGGER.info("stop before flush");
        Thread.sleep(10000);
        conn.flush();
        context.setAttribute("http.request_sent", Boolean.TRUE);
        return response;
    }

    protected HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        HttpResponse response = null;
        int statusCode = 0;

        while(response == null || statusCode < 200) {
            response = conn.receiveResponseHeader();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 100) {
                throw new ProtocolException("Invalid response: " + response.getStatusLine());
            }

            if (this.canResponseHaveBody(request, response)) {
                conn.receiveResponseEntity(response);
            }
        }

        return response;
    }

    public void postProcess(HttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.response", response);
        processor.process(response, context);
    }
}
