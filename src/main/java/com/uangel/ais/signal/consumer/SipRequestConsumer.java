package com.uangel.ais.signal.consumer;

import com.uangel.ais.signal.process.incoming.SipRequestModule;
import com.uangel.ais.util.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.message.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author dajin kim
 */
public class SipRequestConsumer extends SipRequestModule implements Runnable {
    static final Logger log = LoggerFactory.getLogger(SipRequestConsumer.class);
    private final BlockingQueue<RequestEvent> queue;
    private boolean isQuit = false;

    public SipRequestConsumer(BlockingQueue<RequestEvent> queue) {
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
                RequestEvent msg = queue.poll(10, TimeUnit.MILLISECONDS);
                if (msg == null) {
                    SleepUtil.trySleep(10);
                    continue;
                }

                log.debug("SipRequestConsumer queueSize[{}], Msg Request Method[{}]", queue.size(), msg.getRequest().getMethod());
                incomingProcessing(msg);

            } catch (Exception e) {
                log.error("SipRequestConsumer.queueProcessing", e);
                if (e.getClass() == InterruptedException.class) {
                    isQuit = true;
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void incomingProcessing(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        String method = request.getMethod();

        // Inbound 만 고려
        switch (method) {
            case Request.INVITE:
                inInvite(requestEvent);
                break;
            case Request.ACK:
                inAck(requestEvent);
                break;
            case Request.CANCEL:
                inCancel(requestEvent);
                break;
            case Request.BYE:
                inBye(requestEvent);
                break;
            case Request.OPTIONS:
                inOptions(requestEvent);
                break;
            case Request.UPDATE:
                inUpdate(requestEvent);
                break;
            default:
                log.warn("Receive Other Message [{}]", method);
                break;
        }
    }

}
