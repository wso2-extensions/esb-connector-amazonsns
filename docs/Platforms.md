
# Working with Platforms in Amazon SNS 

[[ Overview ]](#Overview) [[ Operation details ]](#operation-details) [[ Sample configuration ]](#sample-configuration)

### Overview
The following operations allow you to work with platforms. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with platforms, see [Sample configuration](#sample-configuration).

| Operation | Description |
| --- | --- |
| createPlatformApplication | Creates a platform application.|
| getPlatformApplicationAttributes | Retrieves the attributes of the platform application.|
| listPlatformApplications | Lists the platform applications. |
| deletePlatformApplication | Deletes a platform applications. |
| setPlatformApplicationAttributes | Sets the attributes of the platform. |

### Operation details
This section provides details on the operations.

### Creating Platform Application
This operation creates a platform application object for one of the supported push notification services, such as APNS and GCM, to which devices and mobile apps may register. You must specify PlatformPrincipal and PlatformCredential attributes when using the CreatePlatformApplication action. For GCM, PlatformCredential is "API key".

#### createPlatformApplication
```xml
<amazonsns.createPlatformApplication>
    <attributesEntryKey>{$ctx:attributesEntryKey}</attributesEntryKey>
    <attributesEntryValue>{$ctx:attributesEntryValue}</attributesEntryValue>
    <name>{$ctx:name}</name>
    <platform>{$ctx:platform}</platform>
</amazonsns.createPlatformApplication>
```
#### Properties

* attributesEntryKey: Required - For a list of attributes . (PlatformCredential).
* attributesEntryValue: Required - The value of the attribute you want to set (Server api key).
* name: Required - Application names must be made up of only uppercase and lowercase ASCII letters, numbers, underscores, hyphens, and periods, and must be between 1 and 256 characters long.
* platform: Required - The following platforms are supported: ADM (Amazon Device Messaging), APNS (Apple Push Notification Service), APNS_SANDBOX, and GCM (Google Cloud Messaging).

#### Sample request
Following is a sample REST/XML request that can be handled by the createPlatformApplication operation.

#### Sample Request for createPlatformApplication
```xml
<createPlatformApplication>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <attributesEntryKey>PlatformCredential</attributesEntryKey>
    <attributesEntryValue>AIzaSyAQl0n9p9U7GGoVGI-TVTwPy4xMniVa1Fk</attributesEntryValue>
    <name>GCMPushApp</name>
    <platform>GCM</platform>
</createPlatformApplication>
```
### Get Platform Applications Attributes
This operation retrieves the attributes of the platform application object for the supported push notification services, such as APNS and GCM.

#### getPlatformApplicationAttributes
```xml
<amazonsns.getPlatformApplicationAttributes>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
</amazonsns.getPlatformApplicationAttributes>
```
#### Properties

* platformApplicationArn: Required - PlatformApplicationArn for GetPlatformApplicationAttributesInput.

#### Sample request
Following is a sample REST/XML request that can be handled by the getPlatformApplicationAttributes operation.

#### Sample Request for getPlatformApplicationAttributes

```xml
<getPlatformApplicationAttributes>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <platformApplicationArn>arn:aws:sns:us-west-2:492228198692:app/GCM/AppOptional</platformApplicationArn>
</getPlatformApplicationAttributes>
```

#### List Platform Applications
This operation lists the platform application objects for the supported push notification services, such as APNS and GCM.

### listPlatformApplications
```xml
<amazonsns.listPlatformApplications>
    <nextToken>{$ctx:nextToken}</nextToken>
</amazonsns.listPlatformApplications>
```

#### Properties
* nextToken: Optional - NextToken string is used when calling ListPlatformApplications action to retrieve additional records that are available after the first page results.

#### Sample request
Following is a sample REST/XML request that can be handled by the listPlatformApplications operation.

#### Sample Request for listPlatformApplications
```xml
<listPlatformApplications>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <nextToken></nextToken>
</listPlatformApplications>
```

### Delete Platform Application
This operation deletes a platform application object for one of the supported push notification services, such as APNS and GCM.

#### deletePlatformApplication
```xml
<amazonsns.deletePlatformApplication>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
</amazonsns.deletePlatformApplication>
Properties
```
* platformApplicationArn: Required - PlatformApplicationArn of platform application object to delete.

#### Sample request
Following is a sample REST/XML request that can be handled by the deletePlatformApplication operation.

#### Sample Request for deletePlatformApplication
```xml
<deletePlatformApplication>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <platformApplicationArn>arn:aws:sns:us-west-2:492228198692:app/GCM/platformApp</platformApplicationArn>
</deletePlatformApplication>
```
#### Set Platform Application Attributes
This operation sets the attributes of the platform application object for the supported push notification services, such as APNS and GCM.

#### setPlatformApplicationAttributes
```xml
<amazonsns.setPlatformApplicationAttributes>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
    <attributesEntryKey>{$ctx:attributesEntryKey}</attributesEntryKey>
    <attributesEntryValue>{$ctx:attributesEntryValue}</attributesEntryValue>
</amazonsns.setPlatformApplicationAttributes>
```
#### Properties
* platformApplicationArn: Required - The ARN of the topic to modify.
* attributesEntryKey: Required - The name of the attribute Key you want to set.
* attributesEntryValue: Required  - The new value for the attribute.

Info

attributesEntryKey and attributesEntryValue  are the  map of the platform application attributes. Attributes in this map include the following:

    PlatformCredential - The credential received from the notification service. For APNS/APNS_SANDBOX, PlatformCredential is private key. For GCM, PlatformCredential is "API key".
    PlatformPrincipal - The principal received from the notification service. For APNS/APNS_SANDBOX, PlatformPrincipal is SSL certificate. For GCM, PlatformPrincipal is not applicable. For ADM, PlatformPrincipal is "client id".
    EventEndpointCreated - Topic ARN to which EndpointCreated event notifications should be sent.
    EventEndpointDeleted - Topic ARN to which EndpointDeleted event notifications should be sent.
    EventEndpointUpdated - Topic ARN to which EndpointUpdate event notifications should be sent.
    EventDeliveryFailure - Topic ARN to which DeliveryFailure event notifications should be sent upon Direct Publish delivery failure (permanent) to one of the application's endpoints.
    SuccessFeedbackRoleArn - IAM role ARN used to give Amazon SNS write access to use CloudWatch Logs on your behalf.
    FailureFeedbackRoleArn - IAM role ARN used to give Amazon SNS write access to use CloudWatch Logs on your behalf.
    SuccessFeedbackSampleRate - Sample rate percentage (0-100) of successfully delivered messages.
#### Sample request
Following is a sample REST/XML request that can be handled by the setPlatformApplicationAttributes operation.

#### Sample Request for setPlatformApplicationAttributes
```xml
<setPlatformApplicationAttributes>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <platformApplicationArn>arn:aws:sns:us-west-2:492228198692:app/GCM/AppOptional</platformApplicationArn>
    <attributesEntryKey>EventEndpointCreated</attributesEntryKey>
    <attributesEntryValue>arn:aws:sns:us-west-2:492228198692:Topic_A</attributesEntryValue>
 </setPlatformApplicationAttributes>
 ```
### Sample configuration
Following is a sample proxy service that illustrates how to connect to Amazon SNS with the init operation and use the deletePlatformApplication operation. The sample request for this proxy can be found in deletePlatformApplication sample request. You can use this sample as a template for using other operations in this category.

#### Sample Proxy
```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazonsns_deletePlatformApplication"
       transports="https,http"
       statistics="disable"
       trace="disable"
       startOnLoad="true">
   <target>
      <inSequence onError="faultHandlerSeq">
         <property name="region" expression="//region/text()"/>
         <property name="accessKeyId" expression="//accessKeyId/text()"/>
         <property name="secretAccessKey" expression="//secretAccessKey/text()"/>
         <property name="version" expression="//version/text()"/>
         <property name="platformApplicationArn" expression="//platformApplicationArn/text()"/>
         <amazonsns.init>
            <region>{$ctx:region}</region>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <version>{$ctx:version}</version>
         </amazonsns.init>
         <amazonsns.deletePlatformApplication>
            <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
        </amazonsns.deletePlatformApplication>
         <respond/>
      </inSequence>
      <outSequence>
        <send/>
      </outSequence>
   </target>
   <description/>
</proxy>
```
