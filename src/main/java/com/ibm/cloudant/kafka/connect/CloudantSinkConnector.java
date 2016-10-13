package com.ibm.cloudant.kafka.connect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.log4j.Logger;
import com.ibm.cloudant.kafka.common.InterfaceConst;
import com.ibm.cloudant.kafka.common.MessageKey;
import com.ibm.cloudant.kafka.common.utils.ResourceBundleUtil;

public class CloudantSinkConnector extends SinkConnector {

	private static Logger LOG = Logger.getLogger(CloudantSinkConnector.class);
	
	private String dbUrl;
	private String dbUser;
	private String dbPassword;
	private String kafkaTopic;
	
	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		try {
			kafkaTopic = props.get(InterfaceConst.TOPIC);
			dbUrl = props.get(InterfaceConst.URL);
			dbUser = props.get(InterfaceConst.USER_NAME);
			dbPassword = props.get(InterfaceConst.PASSWORD);
		
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectException(ResourceBundleUtil.get(MessageKey.CONFIGURATION_EXCEPTION));
		       
		} catch (ClassCastException e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectException(ResourceBundleUtil.get(MessageKey.CONFIGURATION_EXCEPTION));
		}
	}

	@Override
	public Class<? extends Task> taskClass() {
			return CloudantSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		ArrayList<Map<String, String>> configs = new ArrayList<Map<String, String>>();

		// Only one input partition makes sense.
		Map<String, String> config = new HashMap<String, String>();

		try {
			config.put(InterfaceConst.TOPIC, kafkaTopic);
			config.put(InterfaceConst.URL, dbUrl);
			config.put(InterfaceConst.USER_NAME, dbUser);
			config.put(InterfaceConst.PASSWORD, dbPassword);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectException(ResourceBundleUtil.get(MessageKey.CONFIGURATION_EXCEPTION));
		}

		configs.add(config);
		return configs;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public ConfigDef config() {
		// TODO Auto-generated method stub
		return null;
	}

}
