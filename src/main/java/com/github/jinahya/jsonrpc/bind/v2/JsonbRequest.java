package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jsonb
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

import javax.json.Json;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public abstract class JsonbRequest<ParamsType, IdType> extends RequestObject<ParamsType, IdType> {

    static <T extends JsonbRequest<U, V>, U, V> T jsonbRequestOf(
            final Class<? extends T> objectClass, final U params, final V id) {
        return RequestObject.of(objectClass, params, id);
    }

    static <T extends JsonbRequest<?, IdType>, IdType extends JsonValue> T a(final T request, final String id) {
        final JsonString value = Json.createValue(id);
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public <T extends JsonbRequest<U, ?>, U extends JsonValue> Optional<List<U>> getParamsAsArray(
            final T object, Class<U> clazz) {
        return ofNullable(object.getParams()).map(JsonValue::asJsonArray).map(v -> v.getValuesAs(clazz));
    }

    public <T extends JsonbRequest<U, ?>, U extends JsonValue, V> Optional<List<V>> getParamsAsArray(
            final T object, Class<U> clazz, final Function<? super U, ? extends V> mapper) {
        return getParamsAsArray(object, clazz).map(v -> v.stream().map(mapper).collect(toList()));
    }

    public <T extends JsonbRequest<U, ?>, U extends JsonValue, V> Optional<List<V>> getParamsAsArray(
            final T object, final Function<? super JsonValue, ? extends V> mapper) {
        return ofNullable(object.getParams())
                .map(v -> v.asJsonArray().getValuesAs(JsonValue.class).stream().map(mapper).collect(toList()));
//        return ofNullable(object.getParams())
//                .map(JsonValue::asJsonArray)
//                .map(v -> v.getValuesAs(JsonValue.class))
//                .map(v -> v.stream().map(mapper).collect(toList()));
    }
}
