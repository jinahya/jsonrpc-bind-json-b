package com.github.jinahya.jsonrpc.bind.v2;

import javax.json.JsonStructure;
import javax.json.JsonValue;

public abstract class JsonbServerRequest<ParamsType extends JsonStructure, IdType extends JsonValue>
        extends JsonbRequest<ParamsType, IdType> {

}
