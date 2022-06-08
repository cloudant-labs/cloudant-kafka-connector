/*
 * Copyright © 2022 IBM Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ibm.cloudant.kafka.connect;

import com.ibm.cloud.cloudant.internal.ServiceFactory;
import com.ibm.cloud.cloudant.v1.Cloudant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ibm.cloudant.kafka.common.utils.JavaCloudantUtil.VERSION;

public class CachedClientManager {

    // Cloudant clients, keyed by connector name
    static Map<String, Cloudant> clientCache = new ConcurrentHashMap<>();

    private CachedClientManager() {
        // no instantiation
    }

    public static Cloudant getInstance(Map<String, String> props) {
        String connectorName = props.get("name");
        return clientCache.computeIfAbsent(connectorName, p -> ServiceFactory.getInstance(props, VERSION));
    }

    public static void removeInstance(Map<String, String> props) {
        String connectorName = props.get("name");
        clientCache.remove(connectorName);
    }

}
