package com.srq.rpc.channelhandler;

import com.srq.rpc.api.util.SerializationUtil;
import com.srq.rpc.constant.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * 编码
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 16:41
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 编码信息 RPC前缀 数据长度 数据
     * @param ctx
     * @param o
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf out) throws Exception {
        if (genericClass.isInstance(o)) {
            byte[] data = SerializationUtil.serializer(o);
            out.writeCharSequence(Constant.RPC_MSG_PRE, CharsetUtil.UTF_8);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}












