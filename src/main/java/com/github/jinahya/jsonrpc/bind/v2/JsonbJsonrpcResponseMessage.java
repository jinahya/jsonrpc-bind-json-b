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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.validation.constraints.AssertTrue;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.JsonbJsonrpcConfiguration.getJsonb;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createReader;
import static javax.json.Json.createValue;

public class JsonbJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJsonbJsonrpcResponseMessage<JsonbJsonrpcResponseMessage> {

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_RESULT + "=" + result
               + "," + PROPERTY_NAME_ID + "=" + id
               + "," + PROPERTY_NAME_ERROR + "=" + error
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

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    @AssertTrue
    public boolean isResultAndErrorExclusive() {
        return super.isResultAndErrorExclusive();
    }

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    public boolean hasResult() {
        return result != null && result != JsonValue.NULL;
    }

    @Override
    @AssertTrue
    public boolean isResultContextuallyValid() {
        return super.isResultContextuallyValid();
    }

    @Override
    public <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasResult()) {
            return null;
        }
        final Jsonb jsonb = getJsonb();
        if (result instanceof JsonArray) {
            return ((JsonArray) result).getValuesAs(v -> jsonb.fromJson(v.toString(), elementClass));
        }
        return new ArrayList<>(singletonList(getResultAsObject(elementClass)));
    }

    @Override
    public void setResultAsArray(final List<?> result) {
        if (result == null) {
            this.result = null;
            assert !hasResult();
            return;
        }
        this.result = createArrayBuilder(result).build();
    }

    @Override
    public <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        return getJsonb().fromJson(result.toString(), objectClass);
    }

    @Override
    public void setResultAsObject(final Object result) {
        if (result == null) {
            this.result = null;
            assert !hasResult();
            return;
        }
        final String string = getJsonb().toJson(result);
        try (JsonReader reader = createReader(new StringReader(string))) {
            this.result = reader.readValue();
        }
        if (hasResult()) {
            error = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean hasError() {
        return error != null && error != JsonValue.NULL;
    }

    @Override
    public JsonrpcResponseMessageError getErrorAs() {
        if (!hasError()) {
            return null;
        }
        final String string = error.toString();
        return getJsonb().fromJson(string, JsonbJsonrpcResponseMessageError.class);
    }

    @Override
    public void setErrorAs(final JsonrpcResponseMessageError error) {
        if (error == null) {
            this.error = null;
            assert !hasError();
            return;
        }
        final String string = getJsonb().toJson(error);
        try (final JsonReader reader = Json.createReader(new StringReader(string))) {
            this.error = reader.readObject();
        }
        if (hasError()) {
            result = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public JsonValue getId() {
        return id;
    }

    public void setId(final JsonValue id) {
        this.id = id;
    }

    public JsonValue getResult() {
        return result;
    }

    public void setResult(final JsonValue result) {
        this.result = result;
    }

    public JsonObject getError() {
        return error;
    }

    public void setError(final JsonObject error) {
        this.error = error;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JsonValue id;

    private JsonValue result;

    private JsonObject error;
}
