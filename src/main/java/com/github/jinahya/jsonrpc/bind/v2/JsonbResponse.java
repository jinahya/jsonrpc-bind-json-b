package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public abstract class JsonbResponse<ResultType, ErrorType extends ErrorObject<?>, IdType>
        extends ResponseObject<ResultType, ErrorType, IdType> {

}
