package com.srq.rpc;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-10
 * Time: 11:05
 */
public class RpcBootstrap {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RpcBootstrap.class);

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-config.xml");
        LOGGER.debug("server stating");
    }
}
