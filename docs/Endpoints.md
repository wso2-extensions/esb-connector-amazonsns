# Working with Endpoints in Amazon SNS

[[  Overview ]](#Overview) [[ Operation details ]](#operation-details) [[  Sample configuration  ]](#Sample configuration)

### Overview 

The following operations allow you to work with endpoints. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with endpoints, see Sample configuration.

| Operation | Description |
| --- | --- |
| createEndpoint | Creates an endpoint.|
| deleteEndpoint| Deletes an endpoint. |
| getEndpointAttributes | Retrieves the endpoint attributes. |
| getSubscriptionAttributes| Returns all of the properties of a subscription. |
| listEndpoint| Lists endpoints. |
| listSubscriptions | Lists subscriptions. |
| setEndpointAttributes | Sets the attributes for an endpoint. |
| setSubscriptionAttributes | Allows a subscription owner to set an attribute of the topic to a new value.|
| subscribe | Subscribes to an endpoint. |
| unsubscribe | unsubscribe	Deletes a subscription.|


### Operation details

This section provides details on each operation.

#### Creating an endpoint

This operation creates an endpoint for a device and mobile app on one of the supported push notification services such as GCM and APNS. The endpoint Amazon Resource Name (ARN) that is returned when using createEndpoint can then be used by the publish action to send a message to a mobile app or by the subscribe action for subscription to a topic. The action is idempotent, so if the requester already owns an endpoint with the same device token and attributes, that endpoint's ARN is returned without creating a new endpoint. For more information, see [Using Amazon SNS Mobile Push Notifications](https://docs.aws.amazon.com/sns/latest/dg/sns-mobile-application-as-subscriber.html).

When using this operation with Baidu, two attributes must be provided: ChannelId and UserId. The token field must also contain the ChannelId. For more information, see [Creating an Amazon SNS Endpoint for Baidu](https://docs.aws.amazon.com/sns/latest/dg/SNSMobilePushBaiduEndpoint.html). 

#### createEndpoint
```xml
<amazonsns.createEndpoint>
    <customUserData>{$ctx:customUserData}</customUserData>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
    <token>{$ctx:token}</token>
</amazonsns.createEndpoint>
```
###### Properties
* customUserData: Optional - Arbitrary user data to associate with the endpoint. Amazon SNS does not use this data. The data must be in UTF-8 format and less than 2KB.
* platformApplicationArn: Required - PlatformApplicationArn returned from CreatePlatformApplication is used to create an endpoint.
* token: Required - Unique identifier created by the notification service for an app on a device. The specific name for the token will vary depending on the notification service being used. For example, when using APNS as the notification service, you need the device token. Alternatively, when using GCM or ADM, the device token equivalent is called the registration ID.

###### Sample request

Following is a sample REST/XML request that can be handled by the createEndPoint operation.

##### Sample Request for createEndpoint
```xml
<createEndpoint>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJWR2ZfgVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7hgLi1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <platformApplicationArn>arn:aws:sns:us-west-2:899940420354:app/GCM/gcmpushapp3</platformApplicationArn>
   <token>APA91bEUMaBnP1u_b41KjlZgh72RdUw1tzPGoGtbyOAvS9SyddtavDncQlNHGcaC_Dl3_6vh7KYQAvzhzOjhv-DN7DLpbCaaO9LecZw3gKJh9CsyIkUAdzVbIS4Y4ndAT72ht7eLhMyu3U1M0qlQvk5PonxzKOZcVVvbeWoihRr9R7OUc-nCXbxZQ</token>
   <customUserData>Custom User Data</customUserData>
</createEndpoint> 
```
#### Deleting an endpoint
This operation deletes the endpoint from Amazon SNS. This action is idempotent. For more information, see [Using Amazon SNS Mobile Push Notifications](https://docs.aws.amazon.com/sns/latest/dg/sns-mobile-application-as-subscriber.html). 

### deleteEndpoint

```xml
<amazonsns.deleteEndpoint>
    <endpointArn>{$ctx:endpointArn}</endpointArn>
</amazonsns.deleteEndpoint> 
```
###### Properties
* endpointArn: Required - EndpointArn of endpoint to delete. 

###### Sample request
Following is a sample REST/XML request that can be handled by the deleteEndpoint operation.

##### Sample Request for deleteEndpoint

```xml
<deleteEndpoint>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHFFDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2dfP3MaL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <endpointArn>arn:aws:sns:us-west-2:899940420354:endpoint/GCM/gcmpushapp/95d31fb9-cfa7-32f6-982c-40c5cb470dcd</endpointArn>
</deleteEndpoint>
```
#### Listing endpoints
This operation lists the endpoints and endpoint attributes for devices in a supported push notification service such as GCM and APNS. The results are paginated and return a limited list of endpoints, up to 100. If additional records are available after the first page of results, a NextToken string will be returned. To receive the next page, you call listEndpoints again using the NextToken string received from the previous call. When there are no more records to return, NextToken will be null. For more information, see Using Amazon SNS Mobile Push Notifications.

#### listEndpoints
```xml
<amazonsns.listEndpoints>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
    <nextToken>{$ctx:nextToken}</nextToken>
</amazonsns.listEndpoints> 
```
###### Properties

* platformApplicationArn: Required - PlatformApplicationArn for list endpoints.
* nextToken: Optional - NextToken string is used when calling the listEndpoints operation to retrieve additional records that are available after the first page results.

###### Sample request

Following is a sample REST/XML request that can be handled by the listEndpoints operation.

##### Sample Request for listEndpoints

```xml
<listEndpoints>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJWR2FFZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MadfL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <platformApplicationArn>arn:aws:sns:us-west-2:899940420354:app/GCM/gcmpushapp</platformApplicationArn>
   <nextToken></nextToken>
</listEndpoints>
```
#### Subscribing to an endpoint

This operation prepares to subscribe to an endpoint by sending the endpoint a confirmation message. To actually create a subscription, the endpoint owner must call the ConfirmSubscriptionaction with the token from the confirmation message. Confirmation tokens are valid for three days.

#### subscribe
```xml
<amazonsns.subscribe>
    <protocol>{$ctx:protocol}</protocol>
    <topicArn>{$ctx:topicArn}</topicArn>
    <endpoint>{$ctx:endpoint}</endpoint>
</amazonsns.subscribe> 
```

## Properties

* protocol: Required - The protocol you want to use. Supported protocols include:
* http - Delivery of a JSON-encoded message via HTTP POST.
* https - Delivery of a JSON-encoded message via HTTPS POST.
* email - Delivery of a message via SMTP.
* email-json - Delivery of a JSON-encoded message via SMTP.
* sms - Delivery of a message via SMS.
* sqs - Delivery of a JSON-encoded message to an Amazon SQS queue.
* application - Delivery of a JSON-encoded message to an EndpointArn for a mobile app and device.
* topicArn: Required - The ARN of the topic you want to subscribe to.
* endpoint: Optional - The endpoint that you want to receive notifications. 

Endpoints vary by protocol:

For the http protocol, the endpoint is a URL beginning with "http://".
For the https protocol, the endpoint is a URL beginning with "https://".
For the email protocol, the endpoint is an email address.
For the email-json protocol, the endpoint is an email address.
For the sms protocol, the endpoint is a telephone number of an SMS-enabled device.
For the sqs protocol, the endpoint is the ARN of an Amazon SQS queue.
For the application protocol, the endpoint is the EndpointArn of a mobile app and device.

### Observation

Endpoint is mandatory even though it is said to be optional.

## Sample request

Following is a sample REST/XML request that can be handled by the subscribe operation.

## Sample Request for subscribe

```xml
<subscribe>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJWDDR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7Ldfi1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <topicArn>arn:aws:sns:us-west-2:899940420354:TopicName2</topicArn>
   <protocol>email</protocol>
   <endpoint>user.wso2.connector@gmail.com</endpoint>
</subscribe>
```
### Sample request
Following is a sample REST/XML request that can be handled by the subscribe operation.

#### Sample Request for subscribe

```xml
<subscribe>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHDKJWDDR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7Ldfi1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <topicArn>arn:aws:sns:us-west-2:899940420354:TopicName2</topicArn>
   <protocol>email</protocol>
   <endpoint>user.wso2.connector@gmail.com</endpoint>
</subscribe>
```
#### Deleting a subscription
This operation deletes a subscription by unsubscribing. If the subscription requires authentication for deletion, only the owner of the subscription or the topic's owner can unsubscribe, and an AWS signature is required. If the unsubscribe call does not require authentication and the requester is not the subscription owner, a final cancellation message is delivered to the endpoint, so that the endpoint owner can easily resubscribe to the topic if the unsubscribe request was unintended.

#### unsubscribe

```xml
<amazonsns.unsubscribe>
    <subscriptionArn>{$ctx:subscriptionArn}</subscriptionArn>
</amazonsns.unsubscribe> 
```

##### Properties
* suscriptionArn: Required - The ARN of the subscription to be deleted.
Sample request
Following is a sample REST/XML request that can be handled by the unsubscribe operation.

#### Sample Request for unsubscribe
```xml
<unsubscribe>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXHSSDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3fdMaL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <subscriptionArn></subscriptionArn>
</unsubscribe>
```
#### Listing subscriptions

This operation returns a list of the requester's subscriptions. Each call returns a limited list of subscriptions, up to 100. If there are more subscriptions, a NextToken is also returned. Use the nextToken property in a new listSubscriptions call to get further results.

#### listSubscriptions
```xml
<amazonsns.listSubscriptions>
    <nextToken>{$ctx:nextToken}</nextToken>
</amazonsns.listSubscriptions>
```
### Properties
* nextToken: Optional - Token returned by the previous listSubscriptions request.

#### Sample request
Following is a sample REST/XML request that can be handled by the listSubscriptions operation.

#### Sample Request for listSubscriptions
```xml
<listSubscriptions>
   <region>us-west-2</region>
   <accessKeyId>AKISSAJXHDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3ddMaL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <nextToken></nextToken>
</listSubscriptions>
```
#### Listing subscriptions by topic
This operation returns a list of the subscriptions to a specific topic. Each call returns a limited list of subscriptions, up to 100. If there are more subscriptions, a NextToken is also returned. Use the nextToken property in a new listSubscriptionsByTopic call to get further results.

listSubscriptionsByTopic
```xml
<amazonsns.listSubscriptionsByTopic>
    <nextToken>{$ctx:nextToken}</nextToken>
    <topicArn>{$ctx:topicArn}</topicArn>
</amazonsns.listSubscriptionsByTopic> 
```
## Properties
* nextToken: Optional - Token returned by the previous listSubscriptionsByTopic request.
* topicArn: Required - The ARN of the topic for which you wish to find subscriptions.

### Sample request
Following is a sample REST/XML request that can be handled by the listSubscriptionsByTopic operation.

##### Sample Request for listSubscriptionsByTopic
```xml
<listSubscriptionsByTopic>
   <region>us-west-2</region>
   <accessKeyId>AKIAJXFFHDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2dfP3MaL7Li1P3hJu1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <version></version>
   <nextToken></nextToken>
   <topicArn>arn:aws:sns:us-west-2:899940420354:TopicName1</topicArn>
</listSubscriptionsByTopic>
```
#### Confirming a subscription
This verifies an endpoint owner's intent to receive messages by validating the token sent to the endpoint by an earlier subscribe action. If the token is valid, the action creates a new subscription and returns its Amazon Resource Name (ARN). This call requires an AWS signature only when the authenticateOnUnsubscribe flag is set to true.

#### confirmSubscription

```xml
<amazonsns.confirmSubscription>
    <token>{$ctx:token}</token>
    <topicArn>{$ctx:topicArn}</topicArn>
    <authenticateOnUnsubscribe>{$ctx:authenticateOnUnsubscribe}</authenticateOnUnsubscribe>
</amazonsns.confirmSubscription>
```

#### Properties
* token: Required - Short-lived token sent to an endpoint during the subscribe action.
* topicArn: Required - The ARN of the topic for which you wish to confirm a subscription.

authenticateOnUnsubscribe: Optional - Disallows unauthenticated unsubscribes of the subscription. If the value of this parameter is true and the request has an AWS signature, only the topic owner and the subscription owner can unsubscribe the endpoint. The unsubscribe action requires AWS authentication.

### Sample request
Following is a sample REST/XML request that can be handled by the confirmSubscription operation.

#### Sample Request for confirmSubscription
```xml
<confirmSubscription>
   <accessKeyId>AKIAJXHDDKJWR2ZVPEBTQ</accessKeyId>
   <secretAccessKey>N9VT2P3MaL7Li1P3hJudf1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
   <region>us-west-2</region>
   <version></version>   <token>2336412f37fb687f5d51e6e241d638b05824ea7d58914be9b44fc8177c4ade3c5dd278a3044c77050fdf1a8005b9acffcd98b48dccb004333b07c621104d39caf9fffa2e1eb0800024ca572551dff0ee75e438f4f2b48af310cf6ee7a38050ffc770eb37c16584f784c8169f02011c5a</token>
   <topicArn>arn:aws:sns:us-west-2:899940420354:TopicName2</topicArn>
   <authenticateOnUnsubscribe></authenticateOnUnsubscribe>
</confirmSubscription> 
```
#### Retrieving endpoint attributes 
The getEndpointAttributes operation retrieves the endpoint attributes for a device on one of the supported push notification services, such as GCM and APNS. For more information, see Using Amazon SNS Mobile Push Notifications.

#### getEndpointAttributes
```xml
<amazonsns.getEndpointAttributes>
   <endpointArn>{$ctx:endpointArn}</endpointArn>
</amazonsns.getEndpointAttributes>
```
# Properties
* endpointArn: Required - The EndpointArn to get input for retrieving endpoint attributes.
## Sample request
Following is a sample REST/XML request that can be handled by the getEndpointAttributes operation.

## Sample Request for getEndpointAttributes
```xml
<getEndpointAttributes>
    <accessKeyId>AKIAJXHDDKJWR2ZVPEBTQ</accessKeyId>
    <secretAccessKey>N9VT2P3MaL7Li1P3hJudf1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <region>us-west-2</region>
    <version></version>
    <endpointArn>arn:aws:sns:us-west-2:492228198692:endpoint/GCM/AppOptional/5b9c46a7-f8ef-3d2d-934a-08ce556aeab9</endpointArn>
</getEndpointAttributes> 
```

#### Setting Endpoint Attributes
The setEndpointAttributes operation sets endpoint attributes for a device on one of the supported push notification services, such as GCM and APNS.

#### setEndpointAttributes
```xml 
<amazonsns.setEndpointAttributes>
   <endpointArn>{$ctx:endpointArn}</endpointArn>
   <attributesEntryKey>{$ctx:attributesEntryKey}</attributesEntryKey>
   <attributesEntryValue>{$ctx:attributesEntryValue}</attributesEntryValue>
</amazonsns.setEndpointAttributes>
```
#### Properties
endpointArn: Required - The EndpointArn to set endpoint attributes.
attributesEntryKey: Required - The key attribute of endpoints. For example, CustomUserData.
attributesEntryValue: Required - The new value for the key attribute.

Info

attributesEntry is a map of endpoint attributes. Attributes in this map include the following:  

 * CustomUserData : arbitrary user data to associate with the endpoint. Amazon SNS does not use this data. The data must be in UTF-8 format and less than 2KB.   
 * Enabled : flag that enables/disables delivery to the endpoint. Amazon SNS will set this to false when a notification service indicates to Amazon SNS that the endpoint is invalid. Users can set it back to true, typically after updating Token. 
* Token : device token, also referred to as a registration id, for an app and mobile device. This is returned from the notification service when an app and mobile device are registered with the notification service.

### Sample request
Following is a sample REST/XML request that can be handled by the setEndpointAttributes operation.

### Sample Request for setEndpointAttributes
```xml
<setEndpointAttributes>
    <accessKeyId>AKIAJXHDDKJWR2ZVPEBTQ</accessKeyId>
    <secretAccessKey>N9VT2P3MaL7Li1P3hJudf1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <region>us-west-2</region>
    <version></version>
    <endpointArn>arn:aws:sns:us-west-2:492228198692:endpoint/GCM/AppOptional/5b9c46a7-f8ef-3d2d-934a-08ce556aeab9</endpointArn>
    <attributesEntryKey>CustomUserData</attributesEntryKey>
    <attributesEntryValue>My Custom User Data For Testing</attributesEntryValue>
 </setEndpointAttributes>
```

Get Subscription Attributes
The getSubscriptionAttributes operation allows you to returns all of the properties of a subscription.

#### getSubscriptionAttributes
```xml
<amazonsns.getSubscriptionAttributes>
   <subscriptionArn>{$ctx:subscriptionArn}</subscriptionArn>
</amazonsns.getSubscriptionAttributes>
```
Properties
subscriptionArn: Required - The ARN of the subscription whose properties you want to get.
Sample request
Following is a sample REST/XML request that can be handled by the getSubscriptionAttributes operation.

### Sample Request for getSubscriptionAttributes
```xml
<getSubscriptionAttributes>
    <accessKeyId>AKIAJXHDDKJWR2ZVPEBTQ</accessKeyId>
    <secretAccessKey>N9VT2P3MaL7Li1P3hJudf1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <region>us-west-2</region>
    <version></version>
    <subscriptionArn>arn:aws:sns:us-west-2:492228198692:Topic_A:57166721-f47a-49d3-82ed-5f0bae009437</subscriptionArn>
 </getSubscriptionAttributes>
```
Set Subscription Attributes
The setSubscriptionAttributes operation allows a subscription owner to set an attribute of the topic to a new value.

#### setSubscriptionAttributes
```xml
<amazonsns.setSubscriptionAttributes>
   <subscriptionArn>{$ctx:subscriptionArn}</subscriptionArn>
   <attributeName>{$ctx:attributeName}</attributeName>
   <attributeValue>{$ctx:attributeValue}</attributeValue>
</amazonsns.setSubscriptionAttributes>
```
### Properties
* subscriptionArn: Required - The ARN of the subscription to modify.
* attributeName: Required - The name of the attribute you want to set. Only a subset of the subscriptions attributes are mutable. (Valid values: DeliveryPolicy | RawMessageDelivery)
* attributeValue: Optional - The new value for the attribute in JSON format.

## Sample request
Following is a sample REST/XML request that can be handled by the setSubscriptionAttributes operation.

## Sample Request for setSubscriptionAttributes
```xml
<setSubscriptionAttributes>
    <accessKeyId>AKIAJXHDDKJWR2ZVPEBTQ</accessKeyId>
    <secretAccessKey>N9VT2P3MaL7Li1P3hJudf1GTtOO7Kd7NfPlyYG8f/6</secretAccessKey>
    <region>us-west-2</region>
    <version></version>
    <subscriptionArn>arn:aws:sns:us-west-2:492228198692:Topic_A:57166721-f47a-49d3-82ed-5f0bae009437</subscriptionArn>
    <attributeName>DeliveryPolicy</attributeName>
    <attributeValue></attributeValue> 
</setSubscriptionAttributes>
```

### Sample configuration

Following is a sample proxy service that illustrates how to connect to Amazon SNS with the init operation and use the createEndPoint operation. The sample request for this proxy can be found in createEndpoint sample request. You can use this sample as a template for using other operations in this category.

#### Sample Proxy
```xml
<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazonsns_createEndpoint"
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
         <property name="customUserData" expression="//customUserData/text()"/>
         <property name="platformApplicationArn" expression="//platformApplicationArn/text()"/>
         <property name="token" expression="//token/text()"/>
         <amazonsns.init>
            <region>{$ctx:region}</region>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <version>{$ctx:version}</version>
         </amazonsns.init>
         <amazonsns.createEndpoint>
            <customUserData>{$ctx:customUserData}</customUserData>
            <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
            <token>{$ctx:token}</token>
         </amazonsns.createEndpoint>
         <respond/>
      </inSequence>
      <outSequence>
         <send/>
      </outSequence>
   </target>
   <description/>
</proxy> 
```






