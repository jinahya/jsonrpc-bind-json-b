package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jsonb
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
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

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public abstract class JsonbResponse<ResultType, ErrorType extends ErrorObject<?>, IdType>
        extends ResponseObject<ResultType, ErrorType, IdType> {

    static <T extends JsonbResponse<U, V, W>, U, V extends ErrorObject<?>, W> T jsonbResponseOf(
            final Class<? extends T> objectClass, final U result, final V error, final W id) {
        return ResponseObject.of(objectClass, result, error, id);
    }
}
