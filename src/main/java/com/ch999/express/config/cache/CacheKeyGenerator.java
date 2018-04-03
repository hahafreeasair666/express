package com.ch999.express.config.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ClassUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;

public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String className = target.getClass().getSimpleName();
        String methodName = method.getName();
        String defaultKey = "express:" + className + ":" + methodName;
        if (params.length == 0) {
            return defaultKey;
        }
        StringBuilder key = new StringBuilder();
        key.append(defaultKey);
        for (Object param : params) {
            key.append(":");
            if (param == null) {
                key.append("");
            } else {
                Class<?> clazz = param.getClass();
                if (ClassUtils.isPrimitiveArray(clazz) || param instanceof String[]) {
                    //处理原始数组
                    int length = Array.getLength(param);
                    for (int i = 0; i < length; i++) {
                        key.append(Array.get(param, i));
                        if (i != length - 1) {
                            key.append(',');
                        }
                    }
                } else if (param instanceof Collection) {
                    //处理集合
                    Collection collection = (Collection) param;
                    StringBuilder sb = new StringBuilder();
                    for (Object obj : collection) {
                        if (ClassUtils.isPrimitiveOrWrapper(obj.getClass()) || obj instanceof String) {
                            //原始类型直接加入
                            sb.append(obj);
                        } else {
                            //其他则加hashcode
                            sb.append(obj.hashCode());
                        }
                        sb.append(",");
                    }
                    key.append(StringUtils.removeEnd(sb.toString(), ","));
                } else if (ClassUtils.isPrimitiveOrWrapper(clazz) || param instanceof String) {
                    //处理原始类型
                    key.append(param);
                } else {
                    //其他
                    key.append(param.hashCode());
                }
            }
        }

        return key.toString();
    }
}
