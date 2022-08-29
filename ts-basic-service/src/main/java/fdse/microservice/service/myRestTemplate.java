package fdse.microservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;

@Component
public class myRestTemplate extends RestTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(myRestTemplate.class);

    @Nullable
    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
        logger.info("this is my restTemplate");
        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;

        Object var14;
        try {
            ClientHttpRequest request = this.createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            long time = System.nanoTime();
            LOGGER.info("send request used time[{}]", time);
            response = request.execute();
            LOGGER.info("send request used time[{}]", System.nanoTime() - time);
            this.handleResponse(url, method, response);
            var14 = responseExtractor != null ? responseExtractor.extractData(response) : null;
        } catch (IOException var12) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = query != null ? resource.substring(0, resource.indexOf(63)) : resource;
            throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + resource + "\": " + var12.getMessage(), var12);
        } finally {
            if (response != null) {
                response.close();
            }

        }

        return (T) var14;
    }
}
