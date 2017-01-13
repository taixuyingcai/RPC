package com.srq.rpc.api.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用protostuff进行序列化和反序列化
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 17:18
 */
public final class SerializationUtil {
    private SerializationUtil() {}

    private static Map<Class<?>, Schema<?>> cachedMap = new HashMap<Class<?>, Schema<?>>();

    private static Objenesis objenesis = new ObjenesisStd();

    private static <T> Schema<T> getSchema(Class<T> cls ) {
        Schema<T> schema = (Schema<T>) cachedMap.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedMap.put(cls, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serializer(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserializer(byte[] data, Class<T> cls) {
        try {
            T message = (T) objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }
}
