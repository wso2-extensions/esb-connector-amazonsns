
# Working with Topics in Amazon SNS
[[Overview ]](#overview) [[ Operation details ]](#operation-details) [[ Sample configuration ]](#sample-configuration)
### Overview
The following operations allow you to work with topics. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with topics, see Sample configuration.

| Operation | Description |
| --- | --- |
| createTopic | Creates an endpoint.|
| deleteTopic| Deletes a topic.
| listTopics | Lists topics. |
| publish | Sends a message to all of a topic's subscribed endpoints. |
| getTopicAttributes | Returns all of the properties of a topic. |
| setTopicAttributes | Allows a topic owner to set an attribute of the topic to a new value. |

#### Operation details
This section provides details on the operations.

#### Creating a topic
This operation creates a topic to which notifications can be published. Users can create at most 3000 topics. For more information, see http://aws.amazon.com/sns. This action is idempotent, so if the requester already owns a topic with the specified name, that topic's ARN is returned without creating a new topic. 

### createTopic
```xml
<amazonsns.createTopic>
    <name>{$ctx:name}</name>
</amazonsns.createTopic>
```
#### Properties
* name: Required - The name of the topic you want to create. Constraints: Topic names must be made up of only uppercase and lowercase ASCII letters, numbers, underscores, and hyphens, and must be between 1 and 256 characters long. Defines the number of records to be returned (Default is 25).
#### Sample request
Following is a sample REST/XML request that can be handled by the createTopic operation.

#### Sample Request for createTopic
```xml
<createTopic>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJDWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7Lidf1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <name>Test_Topic_200</name>
</createTopic>
```

### Deleting a topic
This operation deletes a topic and all its subscriptions. Deleting a topic might prevent some messages previously sent to the topic from being delivered to subscribers. This action is idempotent, so deleting a topic that does not exist does not result in an error. 

#### deleteTopic
```xml
<amazonsns.deleteTopic>
    <topicArn>{$ctx:topicArn}</topicArn>
</amazonsns.deleteTopic> 
```

#### Properties

* topicArn: Required - The ARN of the topic to be deleted. 

#### Sample request
Following is a sample REST/XML request that can be handled by the deleteTopic operation.

#### Sample Request for deleteTopic
```xml
<deleteTopic>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHSDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7Li1P3gfhJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <topicArn>arn:aws:sns:us-west-2:899940420354:TopicName2</topicArn>
</deleteTopic>
```
### Listing topics
This operation returns a list of the requester's topics. Each call returns a limited list of topics, up to 100. If there are more topics, a NextToken is also returned. Use the nextToken property in a new listTopics call to get further results. 

#### listTopics
```xml
<amazonsns.listTopics>
    <nextToken>{$ctx:nextToken}</nextToken>
</amazonsns.listTopics> 
```
#### Properties
* nextToken: Optional - Token returned by the previous listTopics request.
##### Sample request
Following is a sample REST/XML request that can be handled by the listTopics operation.

#### Sample Request for listTopics
```xml
<listTopics>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
   <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <nextToken></nextToken>
</listTopics> 
```
### Publishing messages
This operation sends a message to all of a topic's subscribed endpoints. When a message ID is returned, the message has been saved and Amazon SNS will attempt to deliver it to the topic's subscribers shortly. The format of the outgoing message to each subscribed endpoint depends on the notification protocol selected.

To use the publish action for sending a message to a mobile endpoint, such as an app on a Kindle device or mobile phone, you must specify the EndpointArn with the targetArn property. The EndpointArn is returned when making a CreatePlatformEndpoint call in the Amazon SNS API. 

#### publish
```xml
<amazonsns.publish>
    <message>{$ctx:message}</message>
    <subject>{$ctx:subject}</subject>
    <messageStructure>{$ctx:messageStructure}</messageStructure>
    <topicArn>{$ctx:topicArn}</topicArn>
    <targetArn>{$ctx:targetArn}</targetArn>
</amazonsns.publish> 
```
#### Properties
* message: Required - The message you want to send to the topic. If you want to send the same message to all transport protocols, include the text of the message as a String value. If you want to send different messages for each transport protocol, set the value of the messageStructure parameter to json and use a JSON object for the message parameter. See the Examples section in http://docs.aws.amazon.com/sns/latest/api/API_Publish.html for the format of the JSON object. Constraints: Messages must be UTF-8 encoded strings at most 256 KB in size (262144 bytes, not 262144 characters).


##### JSON-specific constraints
```xml
* Keys in the JSON object that correspond to supported transport protocols must have simple JSON string values.
* The values will be parsed (unescaped) before they are used in outgoing messages.
* Outbound notifications are JSON encoded (meaning that the characters will be re-escaped for sending).
* Values have a minimum length of 0 (the empty string, "", is allowed).
* Values have a maximum length bounded by the overall message size (so including multiple protocols may limit message sizes).
* Non-string values will cause the key to be ignored.
* Keys that do not correspond to supported transport protocols are ignored.
* Duplicate keys are not allowed.
* Failure to parse or validate any key or value in the message will cause the publish call to return an error (no partial delivery).
```

* subject: Optional - To be used as the "Subject" line when the message is delivered to email endpoints. This field will also be included, if present, in the standard JSON messages delivered to other endpoints. Constraints: Subjects must be ASCII text that begins with a letter, number, or punctuation mark; must not include line breaks or control characters; and must be less than 100 characters long.

* messageStructure: Optional - Set messageStructure to json if you want to send a different message for each protocol. For example, using one publish action, you can send a short message to your SMS subscribers and a longer message to your email subscribers. If you set messageStructure to json, the value of the Message parameter must:
       * be a syntactically valid JSON object; and
       * contain at least a top-level JSON key of "default" with a value that is a string.
You can define other top-level keys that define the message you want to send to a specific transport protocol (e.g., "http"). For information about sending different messages for each protocol using the AWS Management Console, go to [Create Different Messages for Each Protocol](https://docs.aws.amazon.com/sns/latest/dg/sns-getting-started.html#sns-message-formatting-by-protocol) in the Amazon Simple Notification Service Getting Started Guide. Valid value: json

* topicArn: Optional - The topic you want to publish to.
* targetArn: Optional - Either TopicArn or EndpointArn, but not both.

##### Sample request
Following is a sample REST/JSON request that can be handled by the publish operation.

```json
Sample Request for publish
{
    "region":"us-west-2",
    "accessKeyId":"AKIAJXHDKJWR2ZVPEBTQ",
    "secretAccessKey":"N9VT2P3MaL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6",
    "version":"",
    "subject":"subject",
    "message":{"default":"mess","email":"message"},
    "targetArn":"",
    "topicArn":"arn:aws:sns:us-west-2:899940420354:Test_Topic_300",
    "messageStructure":"json"  
}
```

### Get Topic Attributes
This operation returns all of the properties of a topic. Topic properties returned might differ based on the authorization of the user.

#### getTopicAttributes
```xml
<amazonsns.getTopicAttributes>
    <topicArn>{$ctx:topicArn}</topicArn>
</amazonsns.getTopicAttributes>
```
#### Properties
* topicArn: Required - The ARN of the topic whose properties you want to get.

#### Sample request
Following is a sample REST/XML request that can be handled by the getTopicAttributes operation.

#### Sample Request for getTopicAttributes
```xml
<getTopicAttributes>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <topicArn>arn:aws:sns:us-west-2:899940420354:Test_Topic_300</topicArn>
    <version></version>
</getTopicAttributes>
```

### Set Topic Attributes
This operation allows a topic owner to set an attribute of the topic to a new value.

#### setTopicAttributes
```xml
<amazonsns.setTopicAttributes>
    <topicArn>{$ctx:topicArn}</topicArn>
    <attributeName>{$ctx:attributeName}</attributeName>
    <attributeValue>{$ctx:attributeValue}</attributeValue>
</amazonsns.setTopicAttributes>
```
#### Properties
* topicArn: Required - The ARN of the topic to modify.
* attributeName: Required - The name of the attribute you want to set. Only a subset of the topic's attributes are mutable. (Valid values: DeliveryPolicy | RawMessageDelivery)
* attributeValue: Optional - The new value for the attribute in JSON format.
#### Sample request
Following is a sample REST/XML request that can be handled by the setTopicAttributes operation.

#### Sample Request for setTopicAttributes
```xml
<setTopicAttributes>
    <region>us-west-2</region>
    <accessKeyId>AKIAJXHDKJWR2ZVPEdBTQ</accessKeyId>
    <secretAccessKey>N9VTSD2P3MaL7Li1P3hgJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <version></version>
   <topicArn>arn:aws:sns:us-west-2:899940420354:Test_Topic_300</topicArn>
    <attributeName>DeliveryPolicy</attributeName>
    <attributeValue></attributeValue>
 </setTopicAttributes>
 ```

### Sample configuration
Following is a sample proxy service that illustrates how to connect to Amazon SNS with the init operation and use the createTopic operation. The sample request for this proxy can be found in createTopic sample request. You can use this sample as a template for using other operations in this category.
```xml
Sample Proxy
<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazonsns_createTopic"
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
         <property name="name" expression="//name/text()"/>
         <amazonsns.init>
            <region>{$ctx:region}</region>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <version>{$ctx:version}</version>
         </amazonsns.init>
         <amazonsns.createTopic>
            <name>{$ctx:name}</name>
         </amazonsns.createTopic>
         <respond/>
      </inSequence>
      <outSequence>
        <send/>
      </outSequence>
   </target>
   <description/>
</proxy>
```
