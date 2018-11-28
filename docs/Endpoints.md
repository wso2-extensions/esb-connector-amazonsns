# Working with Endpoints in Amazon SNS

[[  Overview ]](#overview) [ Operation details ] [ Sample configuration ]

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


# Operation details

This section provides details on each operation.

Creating an endpoint

This operation creates an endpoint for a device and mobile app on one of the supported push notification services such as GCM and APNS. The endpoint Amazon Resource Name (ARN) that is returned when using createEndpoint can then be used by the publish action to send a message to a mobile app or by the subscribe action for subscription to a topic. The action is idempotent, so if the requester already owns an endpoint with the same device token and attributes, that endpoint's ARN is returned without creating a new endpoint. For more information, see [Using Amazon SNS Mobile Push Notifications](https://docs.aws.amazon.com/sns/latest/dg/sns-mobile-application-as-subscriber.html).

When using this operation with Baidu, two attributes must be provided: ChannelId and UserId. The token field must also contain the ChannelId. For more information, see [Creating an Amazon SNS Endpoint for Baidu](https://docs.aws.amazon.com/sns/latest/dg/SNSMobilePushBaiduEndpoint.html). 

### createEndpoint
```xml
<amazonsns.createEndpoint>
    <customUserData>{$ctx:customUserData}</customUserData>
    <platformApplicationArn>{$ctx:platformApplicationArn}</platformApplicationArn>
    <token>{$ctx:token}</token>
</amazonsns.createEndpoint>
```
# Properties
* customUserData: Optional - Arbitrary user data to associate with the endpoint. Amazon SNS does not use this data. The data must be in UTF-8 format and less than 2KB.
* platformApplicationArn: Required - PlatformApplicationArn returned from CreatePlatformApplication is used to create an endpoint.
* token: Required - Unique identifier created by the notification service for an app on a device. The specific name for the token will vary depending on the notification service being used. For example, when using APNS as the notification service, you need the device token. Alternatively, when using GCM or ADM, the device token equivalent is called the registration ID.
Sample request

Following is a sample REST/XML request that can be handled by the createEndPoint operation.




