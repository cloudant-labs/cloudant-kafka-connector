package com.ibm.cloudant.kafka.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.ibm.cloudant.kafka.common.CloudantConst;

public class JavaCloudantUtil {

	private static Logger LOG = Logger.getLogger(JavaCloudantUtil.class.toString());
	
	public static JSONArray batchWrite(String url, String userName, String password, JSONArray data) throws JSONException  {
		LOG.debug(data.toString());
		// wrap result to JSONArray
		JSONArray result = new JSONArray();
		
		try {
			// get database object
			Database cantDB = getDBInst(url, userName, password);
			List<Object> entryObj = new ArrayList<Object>();
			for(int i=0; i < data.length();i++){
				entryObj.add(data.get(i));
			}

			//perform bulk insert for array of documents
			List<Response> resList = cantDB.bulk(entryObj);
		
			for(int j=0; j < resList.size();j++){
				Response res = resList.get(j);
	    	 
				JSONObject jsonResult = new JSONObject();
				// construct response which is similar to doPost()
				// {"rev":"380-270e81b096fe9ed54dc42a14b47467b9","id":"kafka@database","ok":true}
				jsonResult.put(CloudantConst.RESPONSE_ID,res.getId());
				jsonResult.put(CloudantConst.RESPONSE_REV,res.getRev());
				jsonResult.put(CloudantConst.RESPONSE_ERROR,res.getError());
				jsonResult.put(CloudantConst.RESPONSE_REASON,res.getReason());
				if (res.getError() != null) {
					jsonResult.put(CloudantConst.RESPONSE_OK,false);
				} else {
					jsonResult.put(CloudantConst.RESPONSE_OK,true);
				}
				jsonResult.put(CloudantConst.RESPONSE_CODE,res.getStatusCode());
	    	 
				result.put(jsonResult);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	    return result;
	}

	public static Database getDBInst(String url, String username, String password) {
		
		CloudantClient cantClient = null;
		Database cantDB = null; 
		
		// Create a new CloudantClient instance for account endpoint account.cloudant.com
		// url: https://account.cloudant.com/dbname
		// length of "://" is 3
		if (url != null) {
			String urlWithoutProtocal = url.substring(url.indexOf("://") +3);
			
			if (urlWithoutProtocal != null) {
				String account = urlWithoutProtocal.substring(0,urlWithoutProtocal.indexOf("."));
				if (account != null) {
					cantClient = ClientBuilder.account(account)
											  .username(username)
											  .password(password)
											  .build();
		
					String cantDBName = url.substring(url.lastIndexOf("/")+1);
					
					if (cantDBName != null) {
						// create a database instance (don't create it if database doesn't exist)
						cantDB = cantClient.database(cantDBName, false);
					}
				}
			}
		}
		return cantDB;
	}
}
