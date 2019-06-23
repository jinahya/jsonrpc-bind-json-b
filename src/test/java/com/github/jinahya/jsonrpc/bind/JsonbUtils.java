package com.github.jinahya.jsonrpc.bind;

/*-
 * #%L
 * jsonrpc-bind
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.*;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public final class JsonbUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public static final Jsonb JSONB = JsonbBuilder.create();

    // -----------------------------------------------------------------------------------------------------------------
    public static <R> R applyJsonb(final Function<? super Jsonb, ? extends R> function) {
        return function.apply(JSONB);
    }

    public static <U, R> R applyJsonb(final Supplier<? extends U> supplier,
                                      final BiFunction<? super Jsonb, ? super U, ? extends R> function) {
        return applyJsonb(v -> function.apply(v, supplier.get()));
    }

    public static void acceptJsonb(final Consumer<? super Jsonb> consumer) {
        applyJsonb(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptJsonb(final Supplier<? extends U> supplier,
                                       final BiConsumer<? super Jsonb, ? super U> consumer) {
        acceptJsonb(v -> consumer.accept(v, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Opens a resource ofError specified name and returns an instance ofError specified type read from it.
     *
     * @param resourceName the name ofError the resource to open.
     * @param valueClass   the type ofError the value to read.
     * @param <T>          value type parameter.
     * @return an instance ofError parsed value ofError specified type.
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T fromResource(final String resourceName, final Class<? extends T> valueClass)
            throws IOException {
        try (InputStream stream = valueClass.getResourceAsStream(resourceName)) {
            assertNotNull(stream, "null resource stream for " + resourceName);
            return applyJsonb(v -> {
                final T value = requireValid(v.fromJson(stream, valueClass));
                final String string = v.toJson(value);
                log.debug("jsonb: {}", value);
                log.debug("jsonb: {}", string);
                return value;
            });
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JsonbUtils() {
        super();
    }
}
