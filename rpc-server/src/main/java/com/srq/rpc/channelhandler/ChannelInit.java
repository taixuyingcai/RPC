package com.srq.rpc.channelhandler;

import com.srq.rpc.api.beans.RpcRequest;
import com.srq.rpc.api.beans.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;


/**
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 18:58
 */
public class ChannelInit extends ChannelInitializer<SocketChannel> {
    private Map<String, Object> serviceMap;

    public ChannelInit(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast(new RpcDecoder(RpcRequest.class))
                .addLast(new RpcEncoder(RpcResponse.class))
                .addLast(new RpcHandler(serviceMap));
    }
}
