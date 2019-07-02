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
import javax.json.bind.Jsonb;
import javax.validation.constraints.AssertTrue;
import java.util.stream.Stream;

public class JsonbServerRequest extends JsonbClientRequest<JsonValue, JsonValue> {

    // -----------------------------------------------------------------------------------------------------------------
    public static JsonbServerRequest of(final JsonObject jsonObject) {
        final JsonbServerRequest instance = new JsonbServerRequest();
        instance.setJsonrpc(jsonObject.getString(PROPERTY_NAME_JSONRPC));
        instance.setParams(jsonObject.get(PROPERTY_NAME_PARAMS));
        instance.setId(jsonObject.get(PROPERTY_NAME_ID));
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @AssertTrue
    private boolean isParamsEitherJsonArrayOrJsonObject() {
        final JsonValue params = getParams();
        return params == null || params instanceof JsonArray || params instanceof JsonObject;
    }

    @AssertTrue
    private boolean isIdEitherJsonStringOrJsonNumber() {
        final JsonValue id = getId();
        return id == null || id instanceof JsonString || id instanceof JsonNumber;
    }

    // ---------------------------------------------------------------------------------------------------------- params
    public <T> T getParamsAsNamed(final Jsonb jsonb, final Class<? extends T> clazz) {
        final JsonValue params = getParams();
        if (params == null) {
            return null;
        }
        // https://stackoverflow.com/a/55677645/330457
        return jsonb.fromJson(params.asJsonObject().toString(), clazz); // ClassCastException
    }

    public <T> Stream<T> getParamsAsPositional(final Jsonb jsonb, final Class<? extends T> clazz) {
        final JsonValue params = getParams();
        if (params == null) {
            return null;
        }
        return params.asJsonArray().stream().map(v -> jsonb.fromJson(v.toString(), clazz)); // ClassCastException
    }
}
