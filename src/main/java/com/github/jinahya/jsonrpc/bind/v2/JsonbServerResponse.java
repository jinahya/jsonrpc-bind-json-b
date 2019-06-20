package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.json.JsonValue;

public abstract class JsonbServerResponse<ResultType, ErrorType extends ErrorObject<?>, IdType extends JsonValue>
        extends JsonbResponse<ResultType, ErrorType, IdType> {

}
