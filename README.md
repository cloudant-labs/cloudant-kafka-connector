# Kafka Connect Cloudant

[![Build Status](https://travis-ci.org/cloudant-labs/kafka-connect-cloudant.svg?branch=master)](https://travis-ci.org/cloudant-labs/kafka-connect-cloudant)
[![Maven Central](https://img.shields.io/maven-central/v/com.cloudant/kafka-connect-cloudant.svg)](http://search.maven.org/#search|ga|1|g:"com.cloudant"%20AND%20a:"kafka-connect-cloudant")

Kafka Connect Cloudant Connector. This project includes source & sink connectors.

## Pre-release

**Note**: this README file is for a pre-release version of the
connector. This means it refers to configuration options and features
which are different to the currently released version. For information
about the currently released version, please see the [README
here](https://github.com/cloudant-labs/kafka-connect-cloudant/blob/0.100.2-kafka-1.0.0/README.md).

## Release Status

Experimental

## Table of Contents

* Configuration
* Usage

## Configuration

The Cloudant Kafka connector can be configured in standalone or distributed mode according to 
the [Kafka Connector documentation](http://docs.confluent.io/3.0.1/connect/userguide.html#configuring-connectors). At a minimum it is necessary to configure:

1. `bootstrap.servers`
2. If using a standalone worker `offset.storage.file.filename`.

### Converter configuration

The kafka distribution defaults are usually as follows:
```
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter.schemas.enable=true
value.converter.schemas.enable=true
```

#### Converter configuration: sink connector

For the sink connector, kafka keys are currently ignored; therefore the key converter settings are not relevant.

For the sink connector, we assume that the values in kafka are serialized JSON objects, and therefore `JsonConverter` is supported. If your values contain a schema (`{"schema": {...}, "payload": {...}}`), then set `value.converter.schemas.enable=true`, otherwise set `value.converter.schemas.enable=false`. Any other converter that converts the message values into `org.apache.kafka.connect.data.Struct` or `java.util.Map` types should also work. However, it must be noted that the subsequent serialization of `Map` or `Struct` values to JSON documents in the sink may not match expectations if a schema has not been provided.

### Authentication

In order to read from or write to Cloudant, some authentication properties need to be configured. These properties are common to both the source and sink connector.

A number of different authentication methods are supported. IAM authentication is the default and recommended method; see [locating your service credentials](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-locating-your-service-credentials) for details on how to find your IAM API key.

#### cloudant.auth.type

The authentication method (or type). This value is case insensitive.

The default value is `iam`.

Valid values are:

- `iam`
- `couchdb_session`
- `basic`
- `noAuth`
- `bearerToken`
- `container`
- `vpc`.

With the exception of `noAuth`, each of these authentication methods requires one or more additional properties to be set. These are listed below.

#### cloudant.apikey

For use with `iam` authentication.

#### cloudant.username, cloudant.password

For use with `couchdb_session` or `basic` authentication.

#### cloudant.bearer.token

For use with `bearerToken` authentication.

#### cloudant.iam.profile.id 

For use with `container` or `vpc` authentication.

#### cloudant.iam.profile.name

For use with `container` authentication.

#### cloudant.cr.token.filename

For use with `container` authentication.

#### cloudant.iam.profile.crn

For use with `vpc` authentication.

#### cloudant.auth.url, cloudant.scope, cloudant.client.id, cloudant.client.secret

For use with `iam`, `container`, or `vpc` authentication.

### Cloudant as source

In addition to those properties related to authentication, the Cloudant source connector supports the following properties:

Parameter | Value | Required | Default value | Description
---:|:---|:---|:---|:---
name|cloudant-source|YES|None|A unique name to identify the connector with.
connector.class|com.ibm.cloudant.kafka.connect.CloudantSourceConnector|YES|None|The connector class name.
topics|\<topic1\>,\<topic2\>,..|YES|None|A list of topics you want messages to be written to.
cloudant.url|https://\<uuid\>.cloudantnosqldb.appdomain.cloud|YES|None|The Cloudant server to read documents from.
cloudant.db|\<your-db\>|YES|None|The Cloudant database to read documents from.
cloudant.since|1-g1AAAAETeJzLYWBgYMlgTmGQT0lKzi9..|NO|0|The first change sequence to process from the Cloudant database above. 0 will apply all available document changes.
batch.size|400|NO|1000|The batch size used to bulk read from the Cloudant database.
cloudant.omit.design.docs|false|NO|false| Set to true to omit design documents from the messages produced.
cloudant.value.schema.struct|false|NO|false| _EXPERIMENTAL_ Set to true to generate a `org.apache.kafka.connect.data.Schema.Type.STRUCT` schema and send the Cloudant document payload as a `org.apache.kafka.connect.data.Struct` using the schema instead of the default of a string of the JSON document content when using the Cloudant source connector.
cloudant.value.schema.struct.flatten|false|NO|false| _EXPERIMENTAL_ Set to true to flatten nested arrays and objects from the Cloudant document during struct generation. Only used when cloudant.value.schema.struct is true and allows processing of JSON arrays with mixed element types when using that option.

#### Example

To read from a Cloudant database as source and write documents to a Kafka topic, here is a minimal `connect-cloudant-source.properties`, using the default IAM authentication:

```
name=cloudant-source
connector.class=com.ibm.cloudant.kafka.connect.CloudantSourceConnector
topics=mytopic
cloudant.url=https://some-uuid.cloudantnosqldb.appdomain.cloud
cloudant.db=my-db
cloudant.apikey=my-apikey
```

### Cloudant as sink

In addition to those properties related to authentication, the Cloudant sink connector supports the following properties:

Parameter | Value | Required | Default value | Description
---:|:---|:---|:---|:---
name|cloudant-sink|YES|None|A unique name to identify the connector with.
connector.class|com.ibm.cloudant.kafka.connect.CloudantSinkConnector|YES|None|The connector class name.
topics|\<topic1\>,\<topic2\>,..|YES|None|The list of topics you want to consume messages from.
cloudant.url|https://\<your-account\>.cloudant.com|YES|None|The Cloudant server to write documents to.
cloudant.db|\<your-db\>|YES|None|The Cloudant database to write documents to.
tasks.max|5|NO|1|The number of concurrent threads to use for parallel bulk insert into Cloudant.
batch.size|400|NO|1000|The maximum number of documents to commit with a single bulk insert.
replication|false|NO|false|Managed object schema in sink database <br>*true: duplicate objects from source <br>false: adjust objects from source (\_id = [\<topic-name\>\_\<partition\>\_\<offset>\_\<sourceCloudantObjectId\>], kc\_schema = Kafka value schema)*

#### Example

To consume messages from a Kafka topic and save as documents into a Cloudant database, here is a minimal `connect-cloudant-sink.properties`, using the default IAM authentication:

```
name=cloudant-sink
connector.class=com.ibm.cloudant.kafka.connect.CloudantSinkConnector
topics=mytopic
cloudant.url=https://some-uuid.cloudantnosqldb.appdomain.cloud
cloudant.db=my-db
cloudant.apikey=my-apikey
```

## Usage

The kafka-cloudant-connect jar is available to download from [Maven Central](http://search.maven.org/#search|ga|1|g:"com.cloudant"%20AND%20a:"kafka-connect-cloudant").

Kafka will use the $CLASSPATH to locate available connectors. Make sure to add the connector library to your $CLASSPATH first.

Connector execution in Kafka is available through scripts in the Kafka install path:

`$kafka_home/bin/connect-standalone.sh` or `$kafka_home/bin/connect-distributed.sh`

Use the appropriate configuration files for standalone or distributed execution with Cloudant as source, as sink, or both.

For example:
- standalone execution with Cloudant as source:

  ```
  $kafka_home/bin/connect-standalone.sh connect-standalone.properties connect-cloudant-source.properties
  ```

- standalone execution with Cloudant as sink:

  ```
  $kafka_home/bin/connect-standalone.sh connect-standalone.properties connect-cloudant-sink.properties
  ```

- standalone execution with multiple configurations, one using Cloudant as source and one using Cloudant as sink:

  ```
  $kafka_home/bin/connect-standalone.sh connect-standalone.properties connect-cloudant-source.properties connect-cloudant-sink.properties
  ```

Any number of connector configurations can be passed to the executing script.

INFO level logging is configured by default to the console. To change log levels or settings, work with

`$kafka_home/config/connect-log4j.properties`

and add log settings like

`log4j.logger.com.ibm.cloudant.kafka=DEBUG, stdout`
