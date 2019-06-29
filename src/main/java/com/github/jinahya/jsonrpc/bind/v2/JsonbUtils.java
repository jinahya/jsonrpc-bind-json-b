package com.github.jinahya.jsonrpc.bind.v2;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;

final class JsonbUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public <T> T value(final Jsonb jsonb, final JsonObject object, final Class<? extends T> clazz) {
        if (jsonb == null) {
            throw new NullPointerException("jsonb is null");
        }
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return jsonb.fromJson(object.toString(), clazz);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JsonbUtils() {
        super();
    }
}
