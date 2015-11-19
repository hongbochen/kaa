/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
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
 */
package org.kaaproject.kaa.server.common.core.plugin.base;

import org.kaaproject.kaa.server.common.core.plugin.def.ContractDef;
import org.kaaproject.kaa.server.common.core.plugin.def.ContractType;

public class BaseContractDef implements ContractDef {

    private static final long serialVersionUID = 5898867461695874611L;

    private String name;
    private int version;
    private ContractType type;

    public BaseContractDef(String name, int version, ContractType type) {
        super();
        this.name = name;
        this.version = version;
        this.type = type;
    }

    @Override 
    public String getName() {
        return name;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public ContractType getType() {
        return type;
    }

}