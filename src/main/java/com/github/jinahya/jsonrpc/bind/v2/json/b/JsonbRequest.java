package com.github.jinahya.jsonrpc.bind.v2.json.b;

import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import javax.json.JsonStructure;
import javax.json.JsonValue;

public class JsonbRequest<ParamsType extends JsonStructure, IdType extends JsonValue>
        extends RequestObject<ParamsType, IdType> {

}
