//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.catalina.connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.servlet.ServletOutputStream;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.catalina.security.SecurityUtil;
import org.apache.catalina.util.SessionConfig;
import org.apache.coyote.ActionCode;
import org.apache.coyote.Constants;
import org.apache.coyote.ContinueResponseTiming;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.UEncoder;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.buf.UEncoder.SafeCharsSet;
import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.MediaTypeCache;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.security.Escape;

public class Response implements HttpServletResponse {
    private static final Log log = LogFactory.getLog(Response.class);
    protected static final StringManager sm = StringManager.getManager(Response.class);
    private static final MediaTypeCache MEDIA_TYPE_CACHE = new MediaTypeCache(100);
    private static final boolean ENFORCE_ENCODING_IN_GET_WRITER = Boolean.parseBoolean(System.getProperty("org.apache.catalina.connector.Response.ENFORCE_ENCODING_IN_GET_WRITER", "true"));
    /** @deprecated */
    @Deprecated
    protected SimpleDateFormat format;
    protected org.apache.coyote.Response coyoteResponse;
    protected final OutputBuffer outputBuffer;
    protected CoyoteOutputStream outputStream;
    protected CoyoteWriter writer;
    protected boolean appCommitted;
    protected boolean included;
    private boolean isCharacterEncodingSet;
    protected boolean usingOutputStream;
    protected boolean usingWriter;
    protected final UEncoder urlEncoder;
    protected final CharChunk redirectURLCC;
    private final List<Cookie> cookies;
    private HttpServletResponse applicationResponse;
    protected Request request;
    protected ResponseFacade facade;

    public Response() {
        this(8192);
    }

    public Response(int outputBufferSize) {
        this.format = null;
        this.appCommitted = false;
        this.included = false;
        this.isCharacterEncodingSet = false;
        this.usingOutputStream = false;
        this.usingWriter = false;
        this.urlEncoder = new UEncoder(SafeCharsSet.WITH_SLASH);
        this.redirectURLCC = new CharChunk();
        this.cookies = new ArrayList();
        this.applicationResponse = null;
        this.request = null;
        this.facade = null;
        this.outputBuffer = new OutputBuffer(outputBufferSize);
    }

    public void setCoyoteResponse(org.apache.coyote.Response coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
        this.outputBuffer.setResponse(coyoteResponse);
    }

    public org.apache.coyote.Response getCoyoteResponse() {
        return this.coyoteResponse;
    }

    public Context getContext() {
        return this.request.getContext();
    }

    public void recycle() {
        this.cookies.clear();
        this.outputBuffer.recycle();
        this.usingOutputStream = false;
        this.usingWriter = false;
        this.appCommitted = false;
        this.included = false;
        this.isCharacterEncodingSet = false;
        this.applicationResponse = null;
        if (this.getRequest().getDiscardFacades()) {
            if (this.facade != null) {
                this.facade.clear();
                this.facade = null;
            }

            if (this.outputStream != null) {
                this.outputStream.clear();
                this.outputStream = null;
            }

            if (this.writer != null) {
                this.writer.clear();
                this.writer = null;
            }
        } else if (this.writer != null) {
            this.writer.recycle();
        }

    }

    public List<Cookie> getCookies() {
        return this.cookies;
    }

    public long getContentWritten() {
        return this.outputBuffer.getContentWritten();
    }

    public long getBytesWritten(boolean flush) {
        if (flush) {
            try {
                this.outputBuffer.flush();
            } catch (IOException var3) {
            }
        }

        return this.getCoyoteResponse().getBytesWritten(flush);
    }

    public void setAppCommitted(boolean appCommitted) {
        this.appCommitted = appCommitted;
    }

    public boolean isAppCommitted() {
        return this.appCommitted || this.isCommitted() || this.isSuspended() || this.getContentLength() > 0 && this.getContentWritten() >= (long)this.getContentLength();
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        if (this.facade == null) {
            this.facade = new ResponseFacade(this);
        }

        if (this.applicationResponse == null) {
            this.applicationResponse = this.facade;
        }

        return this.applicationResponse;
    }

    public void setResponse(HttpServletResponse applicationResponse) {
        Object r;
        for(r = applicationResponse; r instanceof HttpServletResponseWrapper; r = ((HttpServletResponseWrapper)r).getResponse()) {
        }

        if (r != this.facade) {
            throw new IllegalArgumentException(sm.getString("response.illegalWrap"));
        } else {
            this.applicationResponse = applicationResponse;
        }
    }

    public void setSuspended(boolean suspended) {
        this.outputBuffer.setSuspended(suspended);
    }

    public boolean isSuspended() {
        return this.outputBuffer.isSuspended();
    }

    public boolean isClosed() {
        return this.outputBuffer.isClosed();
    }

    public boolean setError() {
        return this.getCoyoteResponse().setError();
    }

    public boolean isError() {
        return this.getCoyoteResponse().isError();
    }

    public boolean isErrorReportRequired() {
        return this.getCoyoteResponse().isErrorReportRequired();
    }

    public boolean setErrorReported() {
        return this.getCoyoteResponse().setErrorReported();
    }

    public void finishResponse() throws IOException {
        this.outputBuffer.close();
    }

    public int getContentLength() {
        return this.getCoyoteResponse().getContentLength();
    }

    public String getContentType() {
        return this.getCoyoteResponse().getContentType();
    }

    public PrintWriter getReporter() throws IOException {
        if (this.outputBuffer.isNew()) {
            this.outputBuffer.checkConverter();
            if (this.writer == null) {
                this.writer = new CoyoteWriter(this.outputBuffer);
            }

            return this.writer;
        } else {
            return null;
        }
    }

    public void flushBuffer() throws IOException {
        this.outputBuffer.flush();
    }

    public int getBufferSize() {
        return this.outputBuffer.getBufferSize();
    }

    public String getCharacterEncoding() {
        String charset = this.getCoyoteResponse().getCharacterEncoding();
        if (charset != null) {
            return charset;
        } else {
            Context context = this.getContext();
            String result = null;
            if (context != null) {
                result = context.getResponseCharacterEncoding();
            }

            if (result == null) {
                result = Constants.DEFAULT_BODY_CHARSET.name();
            }

            return result;
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this.usingWriter) {
            throw new IllegalStateException(sm.getString("coyoteResponse.getOutputStream.ise"));
        } else {
            this.usingOutputStream = true;
            if (this.outputStream == null) {
                this.outputStream = new CoyoteOutputStream(this.outputBuffer);
            }

            return this.outputStream;
        }
    }

    public Locale getLocale() {
        return this.getCoyoteResponse().getLocale();
    }

    public PrintWriter getWriter() throws IOException {
        if (this.usingOutputStream) {
            throw new IllegalStateException(sm.getString("coyoteResponse.getWriter.ise"));
        } else {
            if (ENFORCE_ENCODING_IN_GET_WRITER) {
                this.setCharacterEncoding(this.getCharacterEncoding());
            }

            this.usingWriter = true;
            this.outputBuffer.checkConverter();
            if (this.writer == null) {
                this.writer = new CoyoteWriter(this.outputBuffer);
            }

            return this.writer;
        }
    }

    public boolean isCommitted() {
        return this.getCoyoteResponse().isCommitted();
    }

    public void reset() {
        if (!this.included) {
            this.getCoyoteResponse().reset();
            this.outputBuffer.reset();
            this.usingOutputStream = false;
            this.usingWriter = false;
            this.isCharacterEncodingSet = false;
        }
    }

    public void resetBuffer() {
        this.resetBuffer(false);
    }

    public void resetBuffer(boolean resetWriterStreamFlags) {
        if (this.isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.resetBuffer.ise"));
        } else {
            this.outputBuffer.reset(resetWriterStreamFlags);
            if (resetWriterStreamFlags) {
                this.usingOutputStream = false;
                this.usingWriter = false;
                this.isCharacterEncodingSet = false;
            }

        }
    }

    public void setBufferSize(int size) {
        if (!this.isCommitted() && this.outputBuffer.isNew()) {
            this.outputBuffer.setBufferSize(size);
        } else {
            throw new IllegalStateException(sm.getString("coyoteResponse.setBufferSize.ise"));
        }
    }

    public void setContentLength(int length) {
        this.setContentLengthLong((long)length);
    }

    public void setContentLengthLong(long length) {
        if (!this.isCommitted()) {
            if (!this.included) {
                this.getCoyoteResponse().setContentLength(length);
            }
        }
    }

    public void setContentType(String type) {
        if (!this.isCommitted()) {
            if (!this.included) {
                if (type == null) {
                    this.getCoyoteResponse().setContentType((String)null);

                    try {
                        this.getCoyoteResponse().setCharacterEncoding((String)null);
                    } catch (UnsupportedEncodingException var4) {
                    }

                    this.isCharacterEncodingSet = false;
                } else {
                    String[] m = MEDIA_TYPE_CACHE.parse(type);
                    if (m == null) {
                        this.getCoyoteResponse().setContentTypeNoCharset(type);
                    } else {
                        this.getCoyoteResponse().setContentTypeNoCharset(m[0]);
                        if (m[1] != null && !this.usingWriter) {
                            try {
                                this.getCoyoteResponse().setCharacterEncoding(m[1]);
                            } catch (UnsupportedEncodingException var5) {
                                log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[]{m[1]}), var5);
                            }

                            this.isCharacterEncodingSet = true;
                        }

                    }
                }
            }
        }
    }

    public void setCharacterEncoding(String charset) {
        if (!this.isCommitted()) {
            if (!this.included) {
                if (!this.usingWriter) {
                    try {
                        this.getCoyoteResponse().setCharacterEncoding(charset);
                    } catch (UnsupportedEncodingException var3) {
                        log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[]{charset}), var3);
                        return;
                    }

                    if (charset == null) {
                        this.isCharacterEncodingSet = false;
                    } else {
                        this.isCharacterEncodingSet = true;
                    }

                }
            }
        }
    }

    public void setLocale(Locale locale) {
        if (!this.isCommitted()) {
            if (!this.included) {
                this.getCoyoteResponse().setLocale(locale);
                if (!this.usingWriter) {
                    if (!this.isCharacterEncodingSet) {
                        if (locale == null) {
                            try {
                                this.getCoyoteResponse().setCharacterEncoding((String)null);
                            } catch (UnsupportedEncodingException var6) {
                            }
                        } else {
                            Context context = this.getContext();
                            if (context != null) {
                                String charset = context.getCharset(locale);
                                if (charset != null) {
                                    try {
                                        this.getCoyoteResponse().setCharacterEncoding(charset);
                                    } catch (UnsupportedEncodingException var5) {
                                        log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[]{charset}), var5);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public String getHeader(String name) {
        return this.getCoyoteResponse().getMimeHeaders().getHeader(name);
    }

    public Collection<String> getHeaderNames() {
        MimeHeaders headers = this.getCoyoteResponse().getMimeHeaders();
        int n = headers.size();
        List<String> result = new ArrayList(n);

        for(int i = 0; i < n; ++i) {
            result.add(headers.getName(i).toString());
        }

        return result;
    }

    public Collection<String> getHeaders(String name) {
        Enumeration<String> enumeration = this.getCoyoteResponse().getMimeHeaders().values(name);
        Set<String> result = new LinkedHashSet();

        while(enumeration.hasMoreElements()) {
            result.add(enumeration.nextElement());
        }

        return result;
    }

    public String getMessage() {
        return this.getCoyoteResponse().getMessage();
    }

    public int getStatus() {
        return this.getCoyoteResponse().getStatus();
    }

    public void addCookie(Cookie cookie) {
        if (!this.included && !this.isCommitted()) {
            this.cookies.add(cookie);
            String header = this.generateCookieString(cookie);
            this.addHeader("Set-Cookie", header, this.getContext().getCookieProcessor().getCharset());
        }
    }

    public void addSessionCookieInternal(Cookie cookie) {
        if (!this.isCommitted()) {
            String name = cookie.getName();
            String headername = "Set-Cookie";
            String startsWith = name + "=";
            String header = this.generateCookieString(cookie);
            boolean set = false;
            MimeHeaders headers = this.getCoyoteResponse().getMimeHeaders();
            int n = headers.size();

            for(int i = 0; i < n; ++i) {
                if (headers.getName(i).toString().equals("Set-Cookie") && headers.getValue(i).toString().startsWith(startsWith)) {
                    headers.getValue(i).setString(header);
                    set = true;
                }
            }

            if (!set) {
                this.addHeader("Set-Cookie", header);
            }

        }
    }

    public String generateCookieString(Cookie cookie) {
        return SecurityUtil.isPackageProtectionEnabled() ? (String)AccessController.doPrivileged(new PrivilegedGenerateCookieString(this.getContext(), cookie, this.request.getRequest())) : this.getContext().getCookieProcessor().generateHeader(cookie, this.request.getRequest());
    }

    public void addDateHeader(String name, long value) {
        if (name != null && name.length() != 0) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    this.addHeader(name, FastHttpDateFormat.formatDate(value));
                }
            }
        }
    }

    public void addHeader(String name, String value) {
        this.addHeader(name, value, (Charset)null);
    }

    private void addHeader(String name, String value, Charset charset) {
        if (name != null && name.length() != 0 && value != null) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    char cc = name.charAt(0);
                    if (cc != 'C' && cc != 'c' || !this.checkSpecialHeader(name, value)) {
                        this.getCoyoteResponse().addHeader(name, value, charset);
                    }
                }
            }
        }
    }

    private boolean checkSpecialHeader(String name, String value) {
        if (name.equalsIgnoreCase("Content-Type")) {
            this.setContentType(value);
            return true;
        } else {
            return false;
        }
    }

    public void addIntHeader(String name, int value) {
        if (name != null && name.length() != 0) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    this.addHeader(name, "" + value);
                }
            }
        }
    }

    public boolean containsHeader(String name) {
        char cc = name.charAt(0);
        if (cc == 'C' || cc == 'c') {
            if (name.equalsIgnoreCase("Content-Type")) {
                return this.getCoyoteResponse().getContentType() != null;
            }

            if (name.equalsIgnoreCase("Content-Length")) {
                return this.getCoyoteResponse().getContentLengthLong() != -1L;
            }
        }

        return this.getCoyoteResponse().containsHeader(name);
    }

    public void setTrailerFields(Supplier<Map<String, String>> supplier) {
        this.getCoyoteResponse().setTrailerFields(supplier);
    }

    public Supplier<Map<String, String>> getTrailerFields() {
        return this.getCoyoteResponse().getTrailerFields();
    }

    public String encodeRedirectURL(String url) {
        return this.isEncodeable(this.toAbsolute(url)) ? this.toEncoded(url, this.request.getSessionInternal().getIdInternal()) : url;
    }

    /** @deprecated */
    @Deprecated
    public String encodeRedirectUrl(String url) {
        return this.encodeRedirectURL(url);
    }

    public String encodeURL(String url) {
        String absolute;
        try {
            absolute = this.toAbsolute(url);
        } catch (IllegalArgumentException var4) {
            return url;
        }

        if (this.isEncodeable(absolute)) {
            if (url.equalsIgnoreCase("")) {
                url = absolute;
            } else if (url.equals(absolute) && !this.hasPath(url)) {
                url = url + '/';
            }

            return this.toEncoded(url, this.request.getSessionInternal().getIdInternal());
        } else {
            return url;
        }
    }

    /** @deprecated */
    @Deprecated
    public String encodeUrl(String url) {
        return this.encodeURL(url);
    }

    /** @deprecated */
    @Deprecated
    public void sendAcknowledgement() throws IOException {
        this.sendAcknowledgement(ContinueResponseTiming.ALWAYS);
    }

    public void sendAcknowledgement(ContinueResponseTiming continueResponseTiming) throws IOException {
        if (!this.isCommitted()) {
            if (!this.included) {
                this.getCoyoteResponse().action(ActionCode.ACK, continueResponseTiming);
            }
        }
    }

    public void sendError(int status) throws IOException {
        this.sendError(status, (String)null);
    }

    public void sendError(int status, String message) throws IOException {
        if (this.isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.sendError.ise"));
        } else if (!this.included) {
            this.setError();
            this.getCoyoteResponse().setStatus(status);
            this.getCoyoteResponse().setMessage(message);
            this.resetBuffer();
            this.setSuspended(true);
        }
    }

    public void sendRedirect(String location) throws IOException {
        this.sendRedirect(location, 302);
    }

    public void sendRedirect(String location, int status) throws IOException {
        if (this.isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.sendRedirect.ise"));
        } else if (!this.included) {
            this.resetBuffer(true);

            try {
                String locationUri;
                if (this.getRequest().getCoyoteRequest().getSupportsRelativeRedirects() && this.getContext().getUseRelativeRedirects()) {
                    locationUri = location;
                } else {
                    locationUri = this.toAbsolute(location);
                }

                this.setStatus(status);
                this.setHeader("Location", locationUri);
                if (this.getContext().getSendRedirectBody()) {
                    PrintWriter writer = this.getWriter();
                    writer.print(sm.getString("coyoteResponse.sendRedirect.note", new Object[]{Escape.htmlElementContent(locationUri)}));
                    this.flushBuffer();
                }
            } catch (IllegalArgumentException var5) {
                log.warn(sm.getString("response.sendRedirectFail", new Object[]{location}), var5);
                this.setStatus(404);
            }

            this.setSuspended(true);
        }
    }

    public void setDateHeader(String name, long value) {
        if (name != null && name.length() != 0) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    this.setHeader(name, FastHttpDateFormat.formatDate(value));
                }
            }
        }
    }

    public void setHeader(String name, String value) {
        if (name != null && name.length() != 0 && value != null) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    char cc = name.charAt(0);
                    if (cc != 'C' && cc != 'c' || !this.checkSpecialHeader(name, value)) {
                        this.getCoyoteResponse().setHeader(name, value);
                    }
                }
            }
        }
    }

    public void setIntHeader(String name, int value) {
        if (name != null && name.length() != 0) {
            if (!this.isCommitted()) {
                if (!this.included) {
                    this.setHeader(name, "" + value);
                }
            }
        }
    }

    public void setStatus(int status) {
        this.setStatus(status, (String)null);
    }

    /** @deprecated */
    @Deprecated
    public void setStatus(int status, String message) {
        if (!this.isCommitted()) {
            if (!this.included) {
                this.getCoyoteResponse().setStatus(status);
                this.getCoyoteResponse().setMessage(message);
            }
        }
    }

    protected boolean isEncodeable(String location) {
        if (location == null) {
            return false;
        } else if (location.startsWith("#")) {
            return false;
        } else {
            Request hreq = this.request;
            Session session = hreq.getSessionInternal(false);
            if (session == null) {
                return false;
            } else if (hreq.isRequestedSessionIdFromCookie()) {
                return false;
            } else if (!hreq.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL)) {
                return false;
            } else if (SecurityUtil.isPackageProtectionEnabled()) {
                Boolean result = (Boolean)AccessController.doPrivileged(new PrivilegedDoIsEncodable(this.getContext(), hreq, session, location));
                return result;
            } else {
                return doIsEncodeable(this.getContext(), hreq, session, location);
            }
        }
    }

    private static boolean doIsEncodeable(Context context, Request hreq, Session session, String location) {
        URL url = null;

        try {
            url = new URL(location);
        } catch (MalformedURLException var10) {
            return false;
        }

        if (!hreq.getScheme().equalsIgnoreCase(url.getProtocol())) {
            return false;
        } else if (!hreq.getServerName().equalsIgnoreCase(url.getHost())) {
            return false;
        } else {
            int serverPort = hreq.getServerPort();
            if (serverPort == -1) {
                if ("https".equals(hreq.getScheme())) {
                    serverPort = 443;
                } else {
                    serverPort = 80;
                }
            }

            int urlPort = url.getPort();
            if (urlPort == -1) {
                if ("https".equals(url.getProtocol())) {
                    urlPort = 443;
                } else {
                    urlPort = 80;
                }
            }

            if (serverPort != urlPort) {
                return false;
            } else {
                String contextPath = context.getPath();
                if (contextPath != null) {
                    String file = url.getFile();
                    if (!file.startsWith(contextPath)) {
                        return false;
                    }

                    String tok = ";" + SessionConfig.getSessionUriParamName(context) + "=" + session.getIdInternal();
                    if (file.indexOf(tok, contextPath.length()) >= 0) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    protected String toAbsolute(String location) {
        if (location == null) {
            return location;
        } else {
            boolean leadingSlash = location.startsWith("/");
            String scheme;
            if (location.startsWith("//")) {
                this.redirectURLCC.recycle();
                scheme = this.request.getScheme();

                try {
                    this.redirectURLCC.append(scheme, 0, scheme.length());
                    this.redirectURLCC.append(':');
                    this.redirectURLCC.append(location, 0, location.length());
                    return this.redirectURLCC.toString();
                } catch (IOException var10) {
                    throw new IllegalArgumentException(location, var10);
                }
            } else if (!leadingSlash && UriUtil.hasScheme(location)) {
                return location;
            } else {
                this.redirectURLCC.recycle();
                scheme = this.request.getScheme();
                String name = this.request.getServerName();
                int port = this.request.getServerPort();

                try {
                    this.redirectURLCC.append(scheme, 0, scheme.length());
                    this.redirectURLCC.append("://", 0, 3);
                    this.redirectURLCC.append(name, 0, name.length());
                    String relativePath;
                    if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
                        this.redirectURLCC.append(':');
                        relativePath = port + "";
                        this.redirectURLCC.append(relativePath, 0, relativePath.length());
                    }

                    if (!leadingSlash) {
                        relativePath = this.request.getDecodedRequestURI();
                        int pos = relativePath.lastIndexOf(47);
                        CharChunk encodedURI = null;
                        if (SecurityUtil.isPackageProtectionEnabled()) {
                            try {
                                encodedURI = (CharChunk)AccessController.doPrivileged(new PrivilegedEncodeUrl(this.urlEncoder, relativePath, pos));
                            } catch (PrivilegedActionException var11) {
                                throw new IllegalArgumentException(location, var11.getException());
                            }
                        } else {
                            encodedURI = this.urlEncoder.encodeURL(relativePath, 0, pos);
                        }

                        this.redirectURLCC.append(encodedURI);
                        encodedURI.recycle();
                        this.redirectURLCC.append('/');
                    }

                    this.redirectURLCC.append(location, 0, location.length());
                    this.normalize(this.redirectURLCC);
                } catch (IOException var12) {
                    throw new IllegalArgumentException(location, var12);
                }

                return this.redirectURLCC.toString();
            }
        }
    }

    private void normalize(CharChunk cc) {
        int truncate = cc.indexOf('?');
        if (truncate == -1) {
            truncate = cc.indexOf('#');
        }

        char[] truncateCC = null;
        if (truncate > -1) {
            truncateCC = Arrays.copyOfRange(cc.getBuffer(), cc.getStart() + truncate, cc.getEnd());
            cc.setEnd(cc.getStart() + truncate);
        }

        if (cc.endsWith("/.") || cc.endsWith("/..")) {
            try {
                cc.append('/');
            } catch (IOException var12) {
                throw new IllegalArgumentException(cc.toString(), var12);
            }
        }

        char[] c = cc.getChars();
        int start = cc.getStart();
        int end = cc.getEnd();
        int index = 0;
        int startIndex = 0;

        int pos;
        for(pos = 0; pos < 3; ++pos) {
            startIndex = cc.indexOf('/', startIndex + 1);
        }

        index = startIndex;

        while(true) {
            index = cc.indexOf("/./", 0, 3, index);
            if (index < 0) {
                index = startIndex;

                while(true) {
                    index = cc.indexOf("/../", 0, 4, index);
                    if (index < 0) {
                        if (truncateCC != null) {
                            try {
                                cc.append(truncateCC, 0, truncateCC.length);
                            } catch (IOException var11) {
                                throw new IllegalArgumentException(var11);
                            }
                        }

                        return;
                    }

                    if (index == startIndex) {
                        throw new IllegalArgumentException();
                    }

                    int index2 = -1;

                    for(pos = start + index - 1; pos >= 0 && index2 < 0; --pos) {
                        if (c[pos] == '/') {
                            index2 = pos;
                        }
                    }

                    this.copyChars(c, start + index2, start + index + 3, end - start - index - 3);
                    end = end + index2 - index - 3;
                    cc.setEnd(end);
                    index = index2;
                }
            }

            this.copyChars(c, start + index, start + index + 2, end - start - index - 2);
            end -= 2;
            cc.setEnd(end);
        }
    }

    private void copyChars(char[] c, int dest, int src, int len) {
        System.arraycopy(c, src, c, dest, len);
    }

    private boolean hasPath(String uri) {
        int pos = uri.indexOf("://");
        if (pos < 0) {
            return false;
        } else {
            pos = uri.indexOf(47, pos + 3);
            return pos >= 0;
        }
    }

    protected String toEncoded(String url, String sessionId) {
        if (url != null && sessionId != null) {
            String path = url;
            String query = "";
            String anchor = "";
            int question = url.indexOf(63);
            if (question >= 0) {
                path = url.substring(0, question);
                query = url.substring(question);
            }

            int pound = path.indexOf(35);
            if (pound >= 0) {
                anchor = path.substring(pound);
                path = path.substring(0, pound);
            }

            StringBuilder sb = new StringBuilder(path);
            if (sb.length() > 0) {
                sb.append(';');
                sb.append(SessionConfig.getSessionUriParamName(this.request.getContext()));
                sb.append('=');
                sb.append(sessionId);
            }

            sb.append(anchor);
            sb.append(query);
            return sb.toString();
        } else {
            return url;
        }
    }

    private static class PrivilegedEncodeUrl implements PrivilegedExceptionAction<CharChunk> {
        private final UEncoder urlEncoder;
        private final String relativePath;
        private final int end;

        public PrivilegedEncodeUrl(UEncoder urlEncoder, String relativePath, int end) {
            this.urlEncoder = urlEncoder;
            this.relativePath = relativePath;
            this.end = end;
        }

        public CharChunk run() throws IOException {
            return this.urlEncoder.encodeURL(this.relativePath, 0, this.end);
        }
    }

    private static class PrivilegedDoIsEncodable implements PrivilegedAction<Boolean> {
        private final Context context;
        private final Request hreq;
        private final Session session;
        private final String location;

        public PrivilegedDoIsEncodable(Context context, Request hreq, Session session, String location) {
            this.context = context;
            this.hreq = hreq;
            this.session = session;
            this.location = location;
        }

        public Boolean run() {
            return Response.doIsEncodeable(this.context, this.hreq, this.session, this.location);
        }
    }

    private static class PrivilegedGenerateCookieString implements PrivilegedAction<String> {
        private final Context context;
        private final Cookie cookie;
        private final HttpServletRequest request;

        public PrivilegedGenerateCookieString(Context context, Cookie cookie, HttpServletRequest request) {
            this.context = context;
            this.cookie = cookie;
            this.request = request;
        }

        public String run() {
            return this.context.getCookieProcessor().generateHeader(this.cookie, this.request);
        }
    }
}
