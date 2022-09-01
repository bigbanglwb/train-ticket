//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.catalina.connector;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import fdse.microservice.controller.StationController;
import org.apache.tomcat.util.res.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoyoteOutputStream extends ServletOutputStream {
    protected static final StringManager sm = StringManager.getManager(CoyoteOutputStream.class);
    protected OutputBuffer ob;

    protected CoyoteOutputStream(OutputBuffer ob) {
        this.ob = ob;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(StationController.class);
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    void clear() {
        this.ob = null;
    }

    public void write(int i) throws IOException {
        boolean nonBlocking = this.checkNonBlockingWrite();
        this.ob.writeByte(i);
//        LOGGER.info("1Serialization end && send start time[{}]",System.nanoTime());
        if (nonBlocking) {
            this.checkRegisterForWrite();
        }

    }

    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        boolean nonBlocking = this.checkNonBlockingWrite();
        this.ob.write(b, off, len);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("2Serialization end && send start time[{}]",System.nanoTime());
        if (nonBlocking) {
            this.checkRegisterForWrite();
        }

    }

    public void write(ByteBuffer from) throws IOException {
        boolean nonBlocking = this.checkNonBlockingWrite();
        this.ob.write(from);
        if (nonBlocking) {
            this.checkRegisterForWrite();
        }

    }

    public void flush() throws IOException {
        boolean nonBlocking = this.checkNonBlockingWrite();
        this.ob.flush();
//        LOGGER.info("3Serialization end && send start time[{}]",System.nanoTime());
        if (nonBlocking) {
            this.checkRegisterForWrite();
        }

    }

    private boolean checkNonBlockingWrite() {
        boolean nonBlocking = !this.ob.isBlocking();
        if (nonBlocking && !this.ob.isReady()) {
            throw new IllegalStateException(sm.getString("coyoteOutputStream.nbNotready"));
        } else {
            return nonBlocking;
        }
    }

    private void checkRegisterForWrite() {
        this.ob.checkRegisterForWrite();
    }

    public void close() throws IOException {
        this.ob.close();
    }

    public boolean isReady() {
        return this.ob.isReady();
    }

    public void setWriteListener(WriteListener listener) {
        this.ob.setWriteListener(listener);
    }
}
