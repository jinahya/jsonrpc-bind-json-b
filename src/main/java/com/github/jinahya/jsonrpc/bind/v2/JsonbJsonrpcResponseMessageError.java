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

import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.AssertTrue;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.JsonbJsonrpcConfiguration.getJsonb;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createReader;

public class JsonbJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJsonbJsonrpcResponseMessageError<JsonbJsonrpcResponseMessageError>,
                   JsonrpcResponseMessageError {

    @Override
    public String toString() {
        return super.toString() + "{" +
               PROPERTY_NAME_DATA + "=" + data
               + "}";
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Override
    public boolean hasData() {
        return data != null && data != JsonValue.NULL;
    }

    @Override
    @AssertTrue
    public boolean isDataContextuallyValid() {
        return super.isDataContextuallyValid();
    }

    @Override
    public <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasData()) {
            return null;
        }
        final Jsonb jsonb = getJsonb();
        if (data instanceof JsonArray) {
            return ((JsonArray) data).getValuesAs(v -> jsonb.fromJson(v.toString(), elementClass));
        }
        return new ArrayList<>(singletonList(getDataAsObject(elementClass)));
    }

    @Override
    public void setDataAsArray(final List<?> data) {
        if (data == null) {
            this.data = null;
            assert !hasData();
            return;
        }
        this.data = createArrayBuilder(data).build();
    }

    @Override
    public <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasData()) {
            return null;
        }
        return getJsonb().fromJson(data.toString(), objectClass);
    }

    @Override
    public void setDataAsObject(final Object data) {
        if (data == null) {
            this.data = null;
            assert !hasData();
            return;
        }
        final String string = getJsonb().toJson(data);
        try (JsonReader reader = createReader(new StringReader(string))) {
            this.data = reader.readValue();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(PROPERTY_NAME_DATA)
    private JsonValue data;
}
