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
package com.ibm.cloudant.kafka.common;

public class MessageKey {
	public static final String CONFIGURATION_EXCEPTION =  "ConfigurationException";
	public static final String READ_CLOUDANT_STREAM_ERROR = "ReadCloudantStreamError";
	public static final String CLOUDANT_DATABASE_ERROR = "CloudantDatabaseError";
	public static final String STREAM_CLOSED_ERROR = "ClosedStreamError";
	public static final String STREAM_TERMINATE_ERROR = "TerminateStreamError";

	public static final String VALIDATION_AUTH_MUST_BE_SET = "ValidationAuthMustBeSet";
	public static final String VALIDATION_AUTH_BOTH_MUST_BE_SET = "ValidationAuthBothMustBeSet";
	public static final String VALIDATION_AUTH_AT_LEAST_ONE_MUST_BE_SET = "ValidationAuthAtLeastOneMustBeSet";
	public static final String VALIDATION_MUST_BE_ONE_OF = "ValidationMustBeOneOf";
	public static final String VALIDATION_NOT_A_URL = "ValidationNotAUrl";

	public static final String CLOUDANT_CONNECTION_URL_DOC = "CloudantConnectUrlDoc";
	public static final String CLOUDANT_CONNECTION_DB_DOC = "CloudantConnectDbDoc";
	public static final String CLOUDANT_CONNECTION_USR_DOC = "CloudantConnectUsrDoc";
	public static final String CLOUDANT_CONNECTION_PWD_DOC  ="CloudantConnectPwdDoc";
	public static final String CLOUDANT_CONNECTION_AUTH_TYPE_DOC = "CloudantConnectAuthTypeDoc";
	public static final String CLOUDANT_CONNECTION_BEARER_TOKEN_DOC = "CloudantConnectBearerTokenDoc";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_ID_DOC = "CloudantConnectIamProfileIdDoc";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_NAME_DOC = "CloudantConnectIamProfileNameDoc";
	public static final String CLOUDANT_CONNECTION_CR_TOKEN_FILENAME_DOC = "CloudantConnectCrTokenFilenameDoc";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_CRN_DOC = "CloudantConnectIamProfileCrnDoc";
	public static final String CLOUDANT_CONNECTION_APIKEY_DOC = "CloudantConnectApikeyDoc";
	public static final String CLOUDANT_CONNECTION_AUTH_URL_DOC = "CloudantConnectAuthUrlDoc";	
	public static final String CLOUDANT_CONNECTION_SCOPE_DOC = "CloudantConnectScopeDoc";
	public static final String CLOUDANT_CONNECTION_CLIENT_ID_DOC = "CloudantConnectClientIdDoc";
	public static final String CLOUDANT_CONNECTION_CLIENT_SECRET_DOC = "CloudantConnectClientSecretDoc";
	
	public static final String CLOUDANT_CONNECTION_URL_DISP = "CloudantConnectUrlDisp";
	public static final String CLOUDANT_CONNECTION_DB_DISP = "CloudantConnectDbDisp";
	public static final String CLOUDANT_CONNECTION_USR_DISP = "CloudantConnectUsrDisp";
	public static final String CLOUDANT_CONNECTION_PWD_DISP = "CloudantConnectPwdDisp";
	public static final String CLOUDANT_CONNECTION_AUTH_TYPE_DISP = "CloudantConnectAuthTypeDisp";
	public static final String CLOUDANT_CONNECTION_BEARER_TOKEN_DISP = "CloudantConnectBearerTokenDisp";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_ID_DISP = "CloudantConnectIamProfileIdDisp";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_NAME_DISP = "CloudantConnectIamProfileNameDisp";
	public static final String CLOUDANT_CONNECTION_CR_TOKEN_FILENAME_DISP = "CloudantConnectCrTokenFilenameDisp";
	public static final String CLOUDANT_CONNECTION_IAM_PROFILE_CRN_DISP = "CloudantConnectIamProfileCrnDisp";
	public static final String CLOUDANT_CONNECTION_APIKEY_DISP = "CloudantConnectApikeyDisp";
	public static final String CLOUDANT_CONNECTION_AUTH_URL_DISP = "CloudantConnectAuthUrlDisp";
	public static final String CLOUDANT_CONNECTION_SCOPE_DISP = "CloudantConnectScopeDisp";
	public static final String CLOUDANT_CONNECTION_CLIENT_ID_DISP = "CloudantConnectClientIdDisp";
	public static final String CLOUDANT_CONNECTION_CLIENT_SECRET_DISP = "CloudantConnectClientSecretDisp";

	public static final String CLOUDANT_LAST_SEQ_NUM_DOC = "CloudantLastSeqNumDoc";
	public static final String CLOUDANT_LAST_SEQ_NUM_DISP = "CloudantLastSeqNumDisp";
	
	public static final String CLOUDANT_LIMITATION = "CloudantLimitation";
	
	public static final String KAFKA_TOPIC_LIST_DOC = "KafkaTopicListDoc";
	public static final String KAFKA_TOPIC_LIST_DISP = "KafkaTopicListDisp";

	public static final String CLOUDANT_OMIT_DDOC_DISP = "CloudantOmitDDocDisp";
	public static final String CLOUDANT_OMIT_DDOC_DOC = "CloudantOmitDDocDoc";

	public static final String CLOUDANT_STRUCT_SCHEMA_DISP = "CloudantSchemaDisp";
	public static final String CLOUDANT_STRUCT_SCHEMA_DOC = "CloudantSchemaDoc";

	public static final String CLOUDANT_STRUCT_SCHEMA_FLATTEN_DISP = "CloudantSchemaFlattenDisp";
	public static final String CLOUDANT_STRUCT_SCHEMA_FLATTEN_DOC = "CloudantSchemaFlattenDoc";

	public static final String CLOUDANT_STRUCT_SCHEMA_JSON_ELEMENT = "CloudantSchemaUnknownJsonElement";
	public static final String CLOUDANT_STRUCT_SCHEMA_JSON_PRIMITIVE = "CloudantSchemaUnknownJsonPrimitive";
	public static final String CLOUDANT_STRUCT_SCHEMA_JSON_MIXED_ARRAY = "CloudantSchemaMixedArrays";

	public static final String GUID_SCHEMA = "GuidSchema";
}
