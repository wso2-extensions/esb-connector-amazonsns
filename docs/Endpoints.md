# Working with Endpoints in Amazon SNS

[[  Overview ]](#overview) [ Operation details ] [ Sample configuration ]

### Overview 

The following operations allow you to work with endpoints. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with endpoints, see Sample configuration.


# Operation details

This section provides details on each operation.

Creating an endpoint

This operation creates an endpoint for a device and mobile app on one of the supported push notification services such as GCM and APNS. The endpoint Amazon Resource Name (ARN) that is returned when using createEndpoint can then be used by the publish action to send a message to a mobile app or by the subscribe action for subscription to a topic. The action is idempotent, so if the requester already owns an endpoint with the same device token and attributes, that endpoint's ARN is returned without creating a new endpoint. For more information, see [Using Amazon SNS Mobile Push Notifications](https://docs.aws.amazon.com/sns/latest/dg/sns-mobile-application-as-subscriber.html).

When using this operation with Baidu, two attributes must be provided: ChannelId and UserId. The token field must also contain the ChannelId. For more information, see Creating an Amazon SNS Endpoint for Baidu. 

