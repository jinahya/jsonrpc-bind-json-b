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

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import static java.util.Objects.requireNonNull;

/**
 * A configuration class for JSON-RPC 2.0.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JsonbJsonrpcConfiguration {

    private static Jsonb jsonb;

    /**
     * Returns current jsonb instance.
     *
     * @return current jsonb instance.
     */
    public static synchronized Jsonb getJsonb() {
        return jsonb;
    }

    /**
     * Replaces current jsonb instance with specified value.
     *
     * @param jsonb new jsonb instance.
     */
    static synchronized void setJsonb(final Jsonb jsonb) {
        JsonbJsonrpcConfiguration.jsonb = requireNonNull(jsonb, "jsonb is null");
    }

    /**
     * Replaces current jsonb instance with specified value.
     *
     * @param builder new jsonb instance.
     */
    public static synchronized void setJsonb(final JsonbBuilder builder) {
        setJsonb(builder.build());
    }

    static {
        setJsonb(JsonbBuilder.newBuilder());
    }

    private JsonbJsonrpcConfiguration() {
        throw new AssertionError("instantiation is not allowed");
    }
}
