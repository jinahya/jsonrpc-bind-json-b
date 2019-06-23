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

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class JsonbServerRequest<ParamsType extends JsonValue, IdType extends JsonValue>
        extends JsonbClientRequest<ParamsType, IdType> {

    public static class Unknown extends JsonbServerRequest<JsonValue, JsonValue> {

        public static Unknown of(final JsonObject jsonObject) {
            final Unknown instance = new Unknown();
            try {
                instance.setJsonrpc(jsonObject.getString(PROPERTY_NAME_JSONRPC));
            } catch (NullPointerException | ClassCastException e) {
                // (omitted|invalid) $.jsonrpc
                instance.setJsonrpc(null);
            }
            try {
                instance.setMethod(jsonObject.getString(PROPERTY_NAME_METHOD));
            } catch (NullPointerException | ClassCastException e) {
                instance.setMethod(null);
            }
            try {
                instance.setParams(jsonObject.get(PROPERTY_NAME_PARAMS));
            } catch (NullPointerException | ClassCastException e) {
                instance.setParams(null);
            }
            try {
                instance.setId(jsonObject.get(PROPERTY_NAME_ID));
            } catch (NullPointerException | ClassCastException e) {
                instance.setId(null);
            }
            return instance;
        }

        public <T extends JsonValue, U> List<U> getParamsAsArray(final Class<? extends T> valueClass,
                                                                 final Function<? super T, ? extends U> valueMapper) {
            return getParams().asJsonArray().getValuesAs(valueClass).stream().map(valueMapper).collect(toList());
        }

        public <U> List<U> getParamsAsArray(final Function<? super JsonValue, ? extends U> valueMapper) {
            return getParamsAsArray(JsonValue.class, valueMapper);
        }
    }

    @AssertTrue
    private boolean isParamsEitherJsonArrayOrJsonObject() {
        final ParamsType params = getParams();
        return params == null || params instanceof JsonArray || params instanceof JsonObject;
    }

    @AssertTrue
    private boolean isIdEitherJsonStringOrJsonNumber() {
        final IdType id = getId();
        return id == null || id instanceof JsonString || id instanceof JsonNumber;
    }
}
