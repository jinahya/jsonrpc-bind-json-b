package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.JsonbServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JsonbUtils.JSONB;
import static com.github.jinahya.jsonrpc.bind.JsonpUtils.readJsonArrayFromResource;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@Slf4j
class BatchRequestTest {

    @Test
    void batch_01_request() throws IOException {
        final JsonArray jsonArray = readJsonArrayFromResource(getClass(), "batch_01_request.json");
        final List<JsonbServerRequest> requests = jsonArray.getValuesAs(k -> JsonbServerRequest.of((JsonObject) k));
        {
            final JsonbServerRequest request = requireValid(requests.get(0));
            final List<Integer> params = request.getParamsAsPositional(JSONB, Integer.class).collect(Collectors.toList());
            assertEquals("sum", request.getMethod());
            assertIterableEquals(asList(1, 2, 4), params);
            assertEquals("1", ((JsonString) request.getId()).getString());
        }
    }
}
