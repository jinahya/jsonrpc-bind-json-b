package com.github.jinahya.jsonrpc.bind.v2;

import javax.json.Json;
import javax.json.JsonString;

import static java.util.Optional.ofNullable;

public class JsonpUtils2 {

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends JsonrpcObject<? extends JsonString>> String getIdAsString(final T object) {
        return ofNullable(object.getId()).map(JsonString::getString).orElse(null);
    }

    static <T extends JsonrpcObject<? super JsonString>> T setIdAsString(final T object, final String id) {
        object.setId(ofNullable(id).map(Json::createValue).orElse(null));
        return object;
    }
}
