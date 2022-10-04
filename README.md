# Cloudant Kafka Connector

[![Maven Central](https://img.shields.io/maven-central/v/com.cloudant/kafka-connect-cloudant.svg)](http://search.maven.org/#search|ga|1|g:"com.cloudant"%20AND%20a:"kafka-connect-cloudant")

This project includes [Apache Kafka](https://kafka.apache.org/) [Connect](https://kafka.apache.org/documentation.html#connect) source and sink connectors for IBM Cloudant.

These connectors can stream events:
- **from** Cloudant (source connector) to Kafka topic(s)
- **to** Cloudant (sink connector) from Kafka topic(s)

## Pre-release

**Note**: this README file is for a pre-release version of the
connector. This means it refers to configuration options and features
which are different to the currently released version. For information
about the currently released version, please see the [README
here](https://github.com/IBM/cloudant-kafka-connector/blob/0.100.2-kafka-1.0.0/README.md).

## Release Status

Experimental

## Usage

**Note**: The below instructions assume an installation of Kafka at `$KAFKA_HOME`.

### Quick Start

1. Download the jar from the [releases page](https://github.com/IBM/cloudant-kafka-connector/releases). The jar file contains the plugin and the non-Kafka dependencies needed to run.
2. Copy the jar to the `libs` directory _or_ the
[configured `plugin.path`](https://kafka.apache.org/documentation.html#connectconfigs_plugin.path) of your Kafka installation.
3. Edit the [source](docs/connect-cloudant-source-example.properties) or [sink](docs/connect-cloudant-sink-example.properties) example properties files and save this to the `config` directory of your Kafka installation.
4. Start Kafka.
5. Start the connector (see below).

Connector execution in Kafka is available through scripts in the Kafka install path:

`$KAFKA_HOME/bin/connect-standalone.sh` or `$KAFKA_HOME/bin/connect-distributed.sh`

Use the appropriate configuration files for standalone or distributed execution with Cloudant as source, as sink, or both.

For example:
- standalone execution with Cloudant as source:

  ```
  $KAFKA_HOME/bin/connect-standalone.sh \
  $KAFKA_HOME/config/connect-standalone.properties \
  $KAFKA_HOME/config/connect-cloudant-source.properties
  ```

- standalone execution with Cloudant as sink:

  ```
  $KAFKA_HOME/bin/connect-standalone.sh \
  $KAFKA_HOME/config/connect-standalone.properties \
  $KAFKA_HOME/config/connect-cloudant-sink.properties
  ```

- standalone execution with multiple configurations, one using Cloudant as source and one using Cloudant as sink:

  ```
  $KAFKA_HOME/bin/connect-standalone.sh \
  $KAFKA_HOME/config/connect-standalone.properties \
  $KAFKA_HOME/config/connect-cloudant-source.properties \
  $KAFKA_HOME/config/connect-cloudant-sink.properties
  ```

Any number of connector configurations can be passed to the executing script.


## Configuration

As outlined above, the Cloudant Kafka connector can be configured in standalone or distributed mode according to 
the [Kafka Connector documentation](https://kafka.apache.org/documentation.html#connect_configuring).

The `connect-standalone` or `connect-distributed` configuration files contain default values which are necessary for all connectors, such as:

1. `bootstrap.servers`
2. If using a standalone worker `offset.storage.file.filename`.
3. `offset.flush.interval.ms`

### Connector configuration

The `cloudant-source-example` and `cloudant-sink-example` properties files contain the minimum required to get started.
For a full reference explaining all the connector options, see [here (source)](docs/configuration-reference-source.md) and
[here (sink)](docs/configuration-reference-sink.md).

#### Authentication

In order to read from or write to Cloudant, some authentication properties need to be configured. These properties are common to both the source and sink connector, and are detailed in the configuration reference, linked above.

A number of different authentication methods are supported. IAM authentication is the default and recommended method; see [locating your service credentials](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-locating-your-service-credentials) for details on how to find your IAM API key.

### Converter configuration

Also present in the `connect-standalone` or `connect-distributed` configuration files are defaults for key and value conversion, which are as follows:
```
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter.schemas.enable=true
value.converter.schemas.enable=true
```

Depending on your needs, you may need to change these converter settings.
For instance, in the sample configuration files, value schemas are disabled on the assumption that users will read and write events which are "raw" JSON and do not have inline schemas.

#### Converter configuration: source connector

For the source connector:
* Keys are produced as `java.util.Map<String, String>` containing an `_id` entry with the original Cloudant document ID.
* Values are produced as a (schemaless) `java.util.Map<String, Object>`.
* These types are compatible with the default `org.apache.kafka.connect.json.JsonConverter` and should be compatible with any other converter that can accept a `Map`.
* The `schemas.enabled` may be safely used with a `key.converter` if desired.
* The source connector does not generate schemas for the record values by default. To use `schemas.enable` with the `value.converter` consider using a schema registry or the `MapToStruct` SMT detailed below.

#### Converter configuration: sink connector

For the sink connector:
1. Kafka keys are currently ignored; therefore the key converter settings are not relevant.
1. We assume that the values in kafka are serialized JSON objects, and therefore `JsonConverter` is supported. If your values contain a schema (`{"schema": {...}, "payload": {...}}`), then set `value.converter.schemas.enable=true`, otherwise set `value.converter.schemas.enable=false`. Any other converter that converts the message values into `org.apache.kafka.connect.data.Struct` or `java.util.Map` types should also work. However, it must be noted that the subsequent serialization of `Map` or `Struct` values to JSON documents in the sink may not match expectations if a schema has not been provided.
1. Inserting only a single revision of any `_id` is currently supported.  This means it cannot update or delete documents.
1. The `_rev` field in event values are preserved.  To remove `rev` during data flow, use the `ReplaceField` Single Message Transforms (SMT).
Example configuration:
    ```
    transforms=ReplaceField
    transforms.ReplaceField.type=org.apache.kafka.connect.transforms.ReplaceField$Value 
    transforms.ReplaceField.exclude=_rev
    ```
    See the [Kafka Connect transforms](https://kafka.apache.org/31/documentation.html#connect_transforms) documentation for more details.

**Note:** The ID of each document written to Cloudant by the sink connector can be configured as follows:

1. From the value of the `cloudant_doc_id` header on the event.  The value passed to this header must be a string and the `header.converter=org.apache.kafka.connect.storage.StringConverter` config is required.  This will overwrite the `_id` field if it already exists.
1. The value of the `_id` field in the JSON
1. If no other non-null or non-empty value is available the document will be created with a new UUID.

### SMTs

A number of SMTs (Single Message Transforms) have been provided as part of the library to customize fields or values of events during data flow.

See the [SMT reference](docs/smt-reference.md) for an overview of how to use these and Kafka built-in SMTs for common use cases.

# Logging

INFO level logging is configured by default to the console. To change log levels or settings, work with

`$KAFKA_HOME/config/connect-log4j.properties`

and add log settings like

`log4j.logger.com.ibm.cloud.cloudant.kafka=DEBUG, stdout`
