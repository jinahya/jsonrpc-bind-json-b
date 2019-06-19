package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.json.JsonValue;

public class JsonbResponse<ResultType, ErrorType extends ErrorObject<?>, IdType extends JsonValue>
        extends ResponseObject<ResultType, ErrorType, IdType> {

}
