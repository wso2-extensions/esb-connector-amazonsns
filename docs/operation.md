# Configuring Amazon SNS Operations

To use the Amazon SNS connector, add the <amazonsns.init> element in your configuration before carrying out any other Amazon SNS operations. This uses the standard HTTP Authorization header to pass authentication information. Developers are issued an AWS access key ID and AWS secret access key when they register. For request authentication, the AWSAccessKeyId element identifies the access key ID that was used to compute the signature and, indirectly, the developer making the request. 

```xml
<amazonsns.init>
    <region>{$ctx:region}</region>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <version>{$ctx:version}</version>
    <enableSSL>{$ctx:enableSSL}</enableSSL>
    <disablePort>{$ctx:disablePort}</disablePort>
</amazonsns.init> 
```

Properties
* region: The regional endpoint to make your requests (e.g., us-east-1).
* secretAccessKey: The secret access key of the account.
* accessKeyId: The access key ID that corresponds to the secret access key that is used to sign the request.
* version: The API version that the request is written for.
* enableSSL: Optional -  Whether the amazon AWS URL should be http or https. Set to true if you want the URL to be https.
* disablePort: Optional - By default it sets to false and adds the port(80/443) in each request. If you want to disable the port, please set it to true

# Additional information

Ensure that the following Axis2 configurations are added and enabled.

Required message formatters

```xml
<messageFormatter contentType="application/x-www-form-urlencoded" class="org.apache.axis2.transport.http.XFormURLEncodedFormatter"/>
```

Required message builders

```xml
<messageBuilder contentType="application/x-www-form-urlencoded" class="org.apache.synapse.commons.builders.XFormURLEncodedBuilder"/>
```

Now that you have connected to Amazon SNS, see the information in the following topics to perform various operations with the connector.

* [Working with Endpoints in Amazon SNS](Endpoints.md)
* [Working with Topics in Amazon SNS](Topic.md)
* [Working with Permission in Amazon SNS](Permission.md)
* [Working with Platforms in Amazon SNS](Platforms.md)
