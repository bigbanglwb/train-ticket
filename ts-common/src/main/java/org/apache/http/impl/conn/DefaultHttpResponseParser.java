//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.http.impl.conn;

import edu.fudan.common.util.logTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;

public class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse> {
    private final Log log;
    private final HttpResponseFactory responseFactory;
    private final CharArrayBuffer lineBuf;
//    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBHttpClientConnection.class);
    /** @deprecated */
    @Deprecated
    public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
        super(buffer, parser, params);
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(responseFactory, "Response factory");
        this.responseFactory = responseFactory;
        this.lineBuf = new CharArrayBuffer(128);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, MessageConstraints constraints) {
        super(buffer, lineParser, constraints);
        this.log = LogFactory.getLog(this.getClass());
        this.responseFactory = (HttpResponseFactory)(responseFactory != null ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
        this.lineBuf = new CharArrayBuffer(128);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer, MessageConstraints constraints) {
        this(buffer, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)constraints);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer) {
        this(buffer, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)MessageConstraints.DEFAULT);
    }

    protected HttpResponse parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException {
        int count = 0;
        ParserCursor cursor = null;

        while(true) {
            this.lineBuf.clear();
            int i = sessionBuffer.readLine(this.lineBuf);
            logTime.recvResponseime.add(System.nanoTime());
//            LOGGER.info("Get response && Deserialization start time [{}]",System.nanoTime());


            if (i == -1 && count == 0) {
                throw new NoHttpResponseException("The target server failed to respond");
            }

            cursor = new ParserCursor(0, this.lineBuf.length());
            if (this.lineParser.hasProtocolVersion(this.lineBuf, cursor)) {
                StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
                return this.responseFactory.newHttpResponse(statusline, (HttpContext)null);
            }

            if (i == -1 || this.reject(this.lineBuf, count)) {
                throw new ProtocolException("The server failed to respond with a valid HTTP response");
            }

            if (this.log.isDebugEnabled()) {
                this.log.debug("Garbage in response: " + this.lineBuf.toString());
            }

            ++count;
        }
    }

    protected boolean reject(CharArrayBuffer line, int count) {
        return false;
    }
}
