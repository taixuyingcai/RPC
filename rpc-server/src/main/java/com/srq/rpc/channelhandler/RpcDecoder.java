package com.srq.rpc.channelhandler;

import com.srq.rpc.api.util.SerializationUtil;
import com.srq.rpc.constant.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 16:42
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcDecoder.class);

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        CharSequence pref = in.readCharSequence(3, CharsetUtil.UTF_8);
        if (!pref.equals(Constant.RPC_MSG_PRE)) {
            LOGGER.warn("receive error message.");
            return;
        }
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[dataLength];
        in.readBytes(bytes);

        Object object = SerializationUtil.deserializer(bytes, genericClass);
        list.add(object);
    }
}
