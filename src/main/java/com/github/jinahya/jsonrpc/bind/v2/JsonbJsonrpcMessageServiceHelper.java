package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import java.lang.reflect.Method;

import static com.github.jinahya.jsonrpc.bind.v2.JsonbJsonrpcConfiguration.getJsonb;

public final class JsonbJsonrpcMessageServiceHelper {

    @SuppressWarnings({"unchecked"})
    static <T extends JsonrpcMessage> T fromJson(final Object source, final Class<T> clazz) {
        assert source != null;
        assert clazz != null;
        final Class<?> sourceClass = source.getClass();
        for (final Method method : Jsonb.class.getMethods()) {
            if (!"fromJson".equals(method.getName())) {
                continue;
            }
            if (method.getParameterCount() != 2) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (!parameterTypes[0].isAssignableFrom(sourceClass)) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                return (T) method.invoke(getJsonb(), source, clazz);
            } catch (final ReflectiveOperationException roe) {
                final Throwable cause = roe.getCause();
                if (cause instanceof JsonbException) {
                    throw new JsonrpcBindException(cause);
                }
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException("unable to invoke fromJson with " + source + " and " + clazz);
    }

    static <T extends JsonrpcMessage> void toJson(final T value, final Object target) {
        assert target != null;
        assert value != null;
        final Class<?> targetClass = target.getClass();
        for (final Method method : Jsonb.class.getMethods()) {
            if (!"toJson".equals(method.getName())) {
                continue;
            }
            if (method.getParameterCount() != 2) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (!parameterTypes[1].isAssignableFrom(targetClass)) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                method.invoke(getJsonb(), value, target);
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException("unable to invoke toJson with " + value + " and " + target);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JsonbJsonrpcMessageServiceHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
