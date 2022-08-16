/*
 * Copyright © 2016, 2022 IBM Corp. All rights reserved.
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
package com.ibm.cloud.cloudant.kafka.connect;

import com.ibm.cloud.cloudant.kafka.common.InterfaceConst;
import com.ibm.cloud.cloudant.kafka.common.MessageKey;
import com.ibm.cloud.cloudant.kafka.common.utils.JavaCloudantUtil;
import com.ibm.cloud.cloudant.kafka.common.utils.ResourceBundleUtil;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudantSourceConnector extends SourceConnector {

	private static Logger LOG = LoggerFactory.getLogger(CloudantSourceConnector.class);
	
	private Map<String, String> configProperties;

	@Override
	public ConfigDef config() {
		return CloudantSourceConnectorConfig.CONFIG_DEF;
	}

	@Override
	public void start(Map<String, String> props) {
	     configProperties = props;
	}

	@Override
	public void stop() {
		CachedClientManager.removeInstance(configProperties);
	}

	@Override
	public Class<? extends Task> taskClass() {
		return CloudantSourceTask.class;
	}
	
	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		
		try {
			if (maxTasks > 1) {
				LOG.warn(String.format("tasks.max requested was %d, but only 1 task supported", maxTasks));
			}
			List<Map<String, String>> taskConfigs = new ArrayList<Map<String, String>>(1);
			Map<String, String> taskProps = new HashMap<String, String>(configProperties);
			taskProps.put(InterfaceConst.TASK_NUMBER, String.valueOf(0));
			taskConfigs.add(taskProps);
			return taskConfigs;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectException(ResourceBundleUtil.get(MessageKey.CONFIGURATION_EXCEPTION), e);
		}
	}

	@Override
	public String version() {
		return JavaCloudantUtil.VERSION;
	}

	@Override
	public Config validate(Map<String, String> connectorConfigs) {
		CloudantConfigValidator validator = new CloudantConfigValidator(connectorConfigs, config());
		return validator.validate();
	}

}
