package com.srq.rpc.service;

import com.srq.rpc.annotation.RpcService;
import com.srq.rpc.api.service.HelloService;

/**
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 15:03
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hi(String name) {
        return "Hi! " + name;
    }
}
