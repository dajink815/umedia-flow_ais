package com.uangel.ais.signal.util;

import lib.java.handler.sip.header.SIPHeaderNames;
import stack.java.uangel.sip.header.ContactHeader;
import stack.java.uangel.sip.header.Header;
import stack.java.uangel.sip.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * @author dajin kim
 */
public class SipHeaderParser {

    private SipHeaderParser() {
        // nothing
    }

    /**
     * @fn getTargetUri
     * @brief Contact 헤더 URI 파싱 함수
     * @param message: 파싱 대상 Request/Response 메시지
     * @return Peer 로 전송할 Request 메시지 생성시 사용할 Target URI
     * */
    public static String getTargetUri(Message message) {
        String contactStr = null;
        // Contact: sip:contactTest@100.100.100.33:6062;+g.3gpp.icsi-ref="urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel";video
        ContactHeader contactHeader = (ContactHeader) message.getHeader(SIPHeaderNames.CONTACT);
        if (contactHeader != null) {
            // createURI() 에서 사용될 때 URI 형태여야 하므로 name-addr 및 params 는 제외하고 저장한다.
            // => getURI().toString() 호출 결과, params 는 포함되어 있음(ex. ';transport=UDP')
            contactStr = contactHeader.getAddress().getURI().toString();
        }
        return contactStr;
    }

    public static <T> List<T> createSipHeaderListType(Message message, String headerField, Class<T> clazz) {
        List<T> valList = new ArrayList<>();

        for (ListIterator<?> li = message.getHeaderNames(); li.hasNext(); ) {
            String headerName = String.valueOf(li.next());
            for (ListIterator<?> l = message.getHeaders(headerName); l.hasNext(); ) {
                Header header = (Header) l.next();
                if (headerName.equalsIgnoreCase(headerField)) {
                    valList.add(clazz.cast(header));
                }
            }
        }

        return valList;
    }
}
