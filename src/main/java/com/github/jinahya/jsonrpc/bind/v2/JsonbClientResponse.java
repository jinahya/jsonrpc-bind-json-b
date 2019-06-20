package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public abstract class JsonbClientResponse<ResultType, ErrorType extends ErrorObject<?>, IdType>
        extends JsonbResponse<ResultType, ErrorType, IdType> {

}
