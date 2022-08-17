package com.uangel.ais.signal.process.outgoing;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.service.ServiceDefine;
import com.uangel.ais.signal.module.SipSignal;
import lib.java.handler.sip.header.AllowList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.InvalidArgumentException;
import stack.java.uangel.sip.address.Address;
import stack.java.uangel.sip.address.SipURI;
import stack.java.uangel.sip.header.ContactHeader;
import stack.java.uangel.sip.header.ContentTypeHeader;
import stack.java.uangel.sip.header.MaxForwardsHeader;
import stack.java.uangel.sip.message.Message;
import stack.java.uangel.sip.message.Request;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dajin kim
 */
public class SipCreateHeader {
    static final Logger log = LoggerFactory.getLogger(SipCreateHeader.class);
    private static final SipSignal sipSignal = AppInstance.getInstance().getSipSignal();
    private static final AisConfig config = AppInstance.getInstance().getConfig();
    private static final String APPLICATION = "application";
    private static final String SDP = "sdp";

    public SipCreateHeader() {
        // nothing
    }

    /**
     * create SipURI
     *
     * @param toIp
     * @param toPort
     * @param toName
     * @return SipURI ( sip:toName@toIp:toPort )
     * @throws ParseException
     */
    public SipURI createSipURI(String toIp, int toPort, String toName) throws ParseException {
        SipURI sipURI;
        String host = toIp + ":" + toPort;

        if (toName != null) {
            sipURI = sipSignal.getAddressFactory().createSipURI(toName, host);
        } else {
            sipURI = (SipURI) sipSignal.getAddressFactory().createURI("sip:" + host);
        }
        log.debug("createSipURI sipURI : {}", sipURI);
        return sipURI;
    }

    /**
     * create sip ContactHeader
     *
     * @return ContactHeader
     * @throws ParseException
     */
    public ContactHeader createContactHeader() throws ParseException {
        // Contact 헤더는 Contact 주소를 파라미터로 받지 않고 config 에서 호출
        SipURI contactURI = createSipURI(config.getServerIp(), config.getServerPort(), null);
        Address contactAddress = sipSignal.getAddressFactory().createAddress(contactURI);

        ContactHeader contactHeader = sipSignal.getHeaderFactory().createContactHeader(contactAddress);
        log.debug("createContactHeader {}", contactHeader);
        return contactHeader;
    }

    /**
     * Create Content-Type header
     * */
    public ContentTypeHeader createContentType() throws ParseException {
        return sipSignal.getHeaderFactory().createContentTypeHeader(APPLICATION, SDP);
    }

    public void createSdpContentType(String sdp, Message message) {
        try {
            if (sdp != null && !sdp.isEmpty()) {
                byte[] contents = sdp.getBytes(StandardCharsets.UTF_8);
                message.setContent(contents, createContentType());
            }
        } catch (ParseException e) {
            log.error("SipCreateHeader.createSdpContentType.Exception ", e);
        }
    }

    /**
     * create sip MaxForwardHeader
     *
     * @return
     * @throws InvalidArgumentException
     */
    public MaxForwardsHeader createMaxForwardsHeader() throws InvalidArgumentException {
        MaxForwardsHeader maxForwardsHeader = sipSignal.getHeaderFactory().createMaxForwardsHeader(ServiceDefine.MAX_FORWARDS.getNum());
        log.debug("createMaxForwardsHeader {}", maxForwardsHeader);
        return maxForwardsHeader;
    }

    public AllowList createAllowHeader() throws ParseException {
        AllowList allows = new AllowList();
        List<String> allowList = new ArrayList<>();
        allowList.add(Request.INVITE);
        allowList.add(Request.ACK);
        allowList.add(Request.OPTIONS);
        allowList.add(Request.CANCEL);
        allowList.add(Request.BYE);
        allowList.add(Request.UPDATE);
        allowList.add(Request.INFO);
        allowList.add(Request.REFER);
        allowList.add(Request.NOTIFY);
        allowList.add(Request.MESSAGE);
        allowList.add(Request.PRACK);
        allows.setMethods(allowList);
        return allows;
    }
}
