package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-moshi
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
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbProperty;

import javax.validation.constraints.AssertTrue;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.JsonbJsonrpcConfiguration.getJsonb;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createReader;
import static jakarta.json.Json.createValue;
import static java.util.Objects.requireNonNull;

public class JsonbJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJsonbJsonrpcRequestMessage<JsonbJsonrpcRequestMessage> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_ID + "=" + id
               + "," + PROPERTY_NAME_PARAMS + "=" + params
               + "}";
    }

    // -------------------------------------------------------------------------------------------------------------- id
    @Override
    public boolean hasId() {
        return id != null && id != JsonValue.NULL;
    }

    @Override
    @AssertTrue
    public boolean isIdContextuallyValid() {
        if (!hasId()) {
            return true;
        }
        return id instanceof JsonNumber || id instanceof JsonString;
    }

    @Override
    public String getIdAsString() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof JsonString) {
            return ((JsonString) id).getString();
        }
        assert id instanceof JsonNumber;
        return id.toString();
    }

    @Override
    public void setIdAsString(final String id) {
        if (id == null) {
            this.id = null;
            return;
        }
        this.id = createValue(id);
    }

    @Override
    public BigInteger getIdAsNumber() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof JsonNumber) {
            return ((JsonNumber) id).bigIntegerValueExact();
        }
        try {
            return new BigInteger(getIdAsString());
        } catch (final NumberFormatException nfe) {
            // suppressed
        }
        throw new JsonrpcBindException("unable to bind id as number");
    }

    @Override
    public void setIdAsNumber(final BigInteger id) {
        if (id == null) {
            this.id = null;
            return;
        }
        this.id = createValue(id);
    }

    @Override
    public Long getIdAsLong() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof JsonNumber) {
            return ((JsonNumber) id).longValueExact();
        }
        return super.getIdAsLong();
    }

    @Override
    public void setIdAsLong(final Long id) {
        if (id == null) {
            this.id = null;
            return;
        }
        this.id = createValue(id);
    }

    @Override
    public Integer getIdAsInteger() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof JsonNumber) {
            return ((JsonNumber) id).intValueExact();
        }
        return super.getIdAsInteger();
    }

    @Override
    public void setIdAsInteger(final Integer id) {
        if (id == null) {
            this.id = null;
            assert !hasId();
            return;
        }
        this.id = createValue(id);
    }

    // ---------------------------------------------------------------------------------------------------------- params
    @Override
    public boolean hasParams() {
        return params != null && params != JsonValue.NULL;
    }

    @Override
    @AssertTrue
    public boolean isParamsContextuallyValid() {
        if (!hasParams()) {
            return true;
        }
        return true;
    }

    @Override
    public <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasParams()) {
            return null;
        }
        final Jsonb jsonb = getJsonb();
        if (params instanceof JsonArray) {
            return ((JsonArray) params).getValuesAs(v -> jsonb.fromJson(v.toString(), elementClass));
        }
        assert params instanceof JsonObject;
        return new ArrayList<>(Collections.singletonList(getParamsAsObject(elementClass)));
    }

    @Override
    public void setParamsAsArray(final List<?> params) {
        if (params == null) {
            this.params = null;
            assert !hasParams();
            return;
        }
        this.params = createArrayBuilder(params).build();
    }

    @Override
    public <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasParams()) {
            return null;
        }
        try {
            return getJsonb().fromJson(params.toString(), objectClass);
        } catch (final JsonbException je) {
            throw new JsonrpcBindException(je);
        }
    }

    @Override
    public void setParamsAsObject(final Object params) {
        if (params == null) {
            this.params = null;
            assert !hasParams();
            return;
        }
        final String string = getJsonb().toJson(params);
        try (JsonReader reader = createReader(new StringReader(string))) {
            this.params = reader.read();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public JsonValue getId() {
        return id;
    }

    public void setId(final JsonValue id) {
        this.id = id;
    }

    public JsonStructure getParams() {
        return params;
    }

    public void setParams(final JsonStructure params) {
        this.params = params;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(PROPERTY_NAME_ID)
    private JsonValue id;

    @JsonbProperty(PROPERTY_NAME_PARAMS)
    private JsonStructure params;
}
