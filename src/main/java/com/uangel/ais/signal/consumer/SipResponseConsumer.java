package com.uangel.ais.signal.consumer;

import com.uangel.ais.signal.process.incoming.SipResponseModule;
import com.uangel.ais.util.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ResponseEvent;
import stack.java.uangel.sip.header.CSeqHeader;
import stack.java.uangel.sip.message.Response;

import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author dajin kim
 */
public class SipResponseConsumer extends SipResponseModule implements Runnable {
    static final Logger log = LoggerFactory.getLogger(SipResponseConsumer.class);
    private final BlockingQueue<ResponseEvent> queue;
    private boolean isQuit = false;

    public SipResponseConsumer(BlockingQueue<ResponseEvent> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        queueProcessing();
    }

    private void queueProcessing() {
        while (!isQuit) {
            try {
                // poll: 큐가 비었을 때 null 리턴, timeOut 설정 가능
                // take: 꺼낼 수 있는 원소가 있을 때까지 기다림
                ResponseEvent msg = queue.poll(10, TimeUnit.MILLISECONDS);
                if (msg == null) {
                    SleepUtil.trySleep(10);
                    continue;
                }

                log.debug("SipResponseConsumer queueSize[{}], Msg Phrase[{}]", queue.size(), msg.getResponse().getReasonPhrase());
                if (checkTidProcess(msg)) continue;

                responseMessageProcessing(msg);

            } catch (Exception e) {
                log.error("SipResponseConsumer.queueProcessing", e);
                if (e.getClass() == InterruptedException.class || e.getClass() == SocketException.class) {
                    isQuit = true;
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void responseMessageProcessing(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        String method = ((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod();
        int code = response.getStatusCode();

        // Inbound 만 고려
        if (code == Response.OK) {
            // Bye 200ok
            inOk(responseEvent, method);
        } else if (code >= Response.BAD_REQUEST) {
            // Bye Error
            inError(responseEvent, method);
        }
    }
}
