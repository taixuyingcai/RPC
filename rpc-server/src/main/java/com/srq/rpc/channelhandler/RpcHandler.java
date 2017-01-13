package com.srq.rpc.channelhandler;

import com.srq.rpc.RpcServer;
import com.srq.rpc.api.beans.RpcRequest;
import com.srq.rpc.api.beans.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 16:45
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private final Map<String, Object> serverMap; // 接口与实例的映射

    public RpcHandler(Map serverMap) {
        this.serverMap = serverMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("one client connected......");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        LOGGER.debug("receive message from client");
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        try {
            Object result = handle(rpcRequest);
            response.setResult(result);
        } catch (Exception e) {
            LOGGER.error("receiveMessage error ", e);
            response.setError(e);
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 执行client端的远程方法调用
     * @param request
     * @return
     * @throws Exception
     */
    private Object handle(RpcRequest request) throws Exception {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Object serviceBean = serverMap.get(className);
        Class cls = serviceBean.getClass();
        FastClass serviceFastClass = FastClass.create(cls);
        FastMethod method = serviceFastClass.getMethod(methodName, parameterTypes);

        return method.invoke(serviceBean, parameters);
    }
}
