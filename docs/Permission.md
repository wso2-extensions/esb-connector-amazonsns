
# Working with Permission in Amazon SNS

[[ Overview ]](#Overview) [[ Operation details ]](#Operation-details) [[ Sample configuration ]](#Sample-configuration)

### Overview
The following operations allow you to work with permissions. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with permissions, see [Sample configuration](#Sample-configuration).

### Operation details
This section provides details on each operation.

| Operation | Description |
| --- | --- |
| addPermission | Adds a statement to a topic's access control policy.|
| removePermission | Removes a statement from a topic's access control policy.|

### Add Permission
This operation adds a statement to a topic's access control policy, granting access for the specified AWS accounts to the specified actions.

#### addPermission
```xml
<amazonsns.addPermission>
    <topicArn>{$ctx:topicArn}</topicArn>
    <label>{$ctx:label}</label>
    <actionNameMember>{$ctx:actionNameMember}</actionNameMember>
    <accountIdMember>{$ctx:accountIdMember}</accountIdMember>
</amazonsns.addPermission>
```
#### Properties
* topicArn: Required - The ARN of the topic whose access control policy you wish to modify.
* actionNameMember: Required - The action you want to allow for the specified principal(s).
* accountIdMember: Required - The AWS account IDs of the users (principals) who will be given access to the specified actions.
* label: Required - A unique identifier for the new policy statement.
 
#### Sample requestSample request
Following is a sample REST/XML request that can be handled by the addPermission operation

#### Sample Request for addPermission
```xml
<addPermission>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <topicArn>arn:aws:sns:us-west-2:899940420354:Test_Topic_300</topicArn>
    <label>NewPermission</label>
    <actionNameMember>Publish</actionNameMember>
    <accountIdMember>XXX4922XX281XX98692XXXX</accountIdMember>
    <version></version>
</addPermission>
```
### Remove Permission
This operation removes a statement from a topic's access control policy.

#### removePermission
```xml
<amazonsns.removePermission>
    <topicArn>{$ctx:topicArn}</topicArn>
    <label>{$ctx:label}</label>
</amazonsns.removePermission>
```
#### Properties
* topicArn: Required - The ARN of the topic whose access control policy you wish to modify.
* label: Required - The unique label of the statement you want to remove.

#### Sample request
Following is a sample REST/XML request that can be handled by the removePermission operation

#### Sample Request for removePermission
```xml
<removePermission>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <topicArn>arn:aws:sns:us-west-2:899940420354:Test_Topic_300</topicArn>
    <label>NewPermission</label>
    <version></version>
</removePermission>
```
### Sample configuration
Following is a sample proxy service that illustrates how to connect to Amazon SNS with the init operation and use the addPermission operation. The sample request for this proxy can be found in addPermission sample request. You can use this sample as a template for using other operations in this category.

##### Sample Proxy
```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazonsns_addPermission"
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
         <property name="topicArn" expression="//topicArn/text()"/>
         <property name="label" expression="//label/text()"/>
         <property name="actionNameMember" expression="//actionNameMember/text()"/>
         <property name="accountIdMember" expression="//accountIdMember/text()"/>
          <amazonsns.init>
            <region>{$ctx:region}</region>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <version>{$ctx:version}</version>
         </amazonsns.init>
         <amazonsns.addPermission>
            <topicArn>{$ctx:topicArn}</topicArn>
            <label>{$ctx:label}</label>
            <actionNameMember>{$ctx:actionNameMember}</actionNameMember>
            <accountIdMember>{$ctx:accountIdMember}</accountIdMember>
        </amazonsns.addPermission>
         <respond/>
      </inSequence>
      <outSequence>
        <send/>
      </outSequence>
   </target>
   <description/>
</proxy>                                 
```
