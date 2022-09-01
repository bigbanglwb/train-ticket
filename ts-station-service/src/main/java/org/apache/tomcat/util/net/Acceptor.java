//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.tomcat.util.net;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.http11.Http11Processor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jni.Error;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.res.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Acceptor<U> implements Runnable {
    private static final Log log = LogFactory.getLog(Acceptor.class);
    private static final StringManager sm = StringManager.getManager(Acceptor.class);
    private static final int INITIAL_ERROR_DELAY = 50;
    private static final int MAX_ERROR_DELAY = 1600;
    private final AbstractEndpoint<?, U> endpoint;
    private String threadName;
    private volatile boolean stopCalled = false;
    private final CountDownLatch stopLatch = new CountDownLatch(1);
    protected volatile AcceptorState state;

    public Acceptor(AbstractEndpoint<?, U> endpoint) {
        this.state = Acceptor.AcceptorState.NEW;
        this.endpoint = endpoint;
    }

    public final AcceptorState getState() {
        return this.state;
    }

    final void setThreadName(String threadName) {
        this.threadName = threadName;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(Acceptor.class);
    final String getThreadName() {
        return this.threadName;
    }

    public void run() {
        int errorDelay = 0;

        try {
            while(!this.stopCalled) {
                while(this.endpoint.isPaused() && !this.stopCalled) {
                    this.state = Acceptor.AcceptorState.PAUSED;

                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException var10) {
                    }
                }

                if (this.stopCalled) {
                    break;
                }

                this.state = Acceptor.AcceptorState.RUNNING;

                try {
                    this.endpoint.countUpOrAwaitConnection();
                    if (!this.endpoint.isPaused()) {
                        U socket = null;

                        try {
                            socket = this.endpoint.serverSocketAccept();
                            LOGGER.info("Socket connect finished time [{}]",System.nanoTime());
                        } catch (Exception var11) {
                            this.endpoint.countDownConnection();
                            if (!this.endpoint.isRunning()) {
                                break;
                            }

                            this.handleExceptionWithDelay(errorDelay);
                            throw var11;
                        }

                        errorDelay = 0;
                        if (!this.stopCalled && !this.endpoint.isPaused()) {
                            if (!this.endpoint.setSocketOptions(socket)) {
                                this.endpoint.closeSocket(socket);
                            }
                        } else {
                            this.endpoint.destroySocket(socket);
                        }
                    }
                } catch (Throwable var12) {
                    ExceptionUtils.handleThrowable(var12);
                    String msg = sm.getString("endpoint.accept.fail");
                    if (var12 instanceof Error) {
                        Error e = (Error)var12;
                        if (e.getError() == 233) {
                            log.warn(msg, var12);
                        } else {
                            log.error(msg, var12);
                        }
                    } else {
                        log.error(msg, var12);
                    }
                }
            }
        } finally {
            this.stopLatch.countDown();
        }

        this.state = Acceptor.AcceptorState.ENDED;
    }

    /** @deprecated */
    @Deprecated
    public void stop() {
        this.stop(10);
    }

    public void stop(int waitSeconds) {
        this.stopCalled = true;
        if (waitSeconds > 0) {
            try {
                if (!this.stopLatch.await((long)waitSeconds, TimeUnit.SECONDS)) {
                    log.warn(sm.getString("acceptor.stop.fail", new Object[]{this.getThreadName()}));
                }
            } catch (InterruptedException var3) {
                log.warn(sm.getString("acceptor.stop.interrupted", new Object[]{this.getThreadName()}), var3);
            }
        }

    }

    protected int handleExceptionWithDelay(int currentErrorDelay) {
        if (currentErrorDelay > 0) {
            try {
                Thread.sleep((long)currentErrorDelay);
            } catch (InterruptedException var3) {
            }
        }

        if (currentErrorDelay == 0) {
            return 50;
        } else {
            return currentErrorDelay < 1600 ? currentErrorDelay * 2 : 1600;
        }
    }

    public static enum AcceptorState {
        NEW,
        RUNNING,
        PAUSED,
        ENDED;

        private AcceptorState() {
        }
    }
}
