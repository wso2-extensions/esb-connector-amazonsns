/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.amazonsns;

import org.apache.axiom.om.OMElement;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.core.ProductConstant;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonsnsConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();

    private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

    private String apiEndPoint;

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        init("amazonsns-connector-1.0.0");

        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
        apiRequestHeadersMap.put("Accept-Charset", "UTF-8");
        apiRequestHeadersMap.put("Content-Type", "application/x-www-form-urlencoded");

        apiEndPoint = "http://sns." + connectorProperties.getProperty("region") + ".amazonaws.com";
    }

    /**
     * Positive test case for createTopic method with mandatory parameters.
     */
    @Test(priority = 1, groups = {"wso2.esb"},
            description = "AmazonSNS {createTopic} integration test with mandatory parameters.")
    public void testCreateTopicWithMandatoryParameters() throws JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException, IOException {
        esbRequestHeadersMap.put("Action", "urn:createTopic");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createTopic_mandatory.xml");

        final String topicArn = getValueByExpression("//*[local-name()='TopicArn']/text()", esbRestResponse.getBody());
        connectorProperties.setProperty("topicArn", topicArn);

        generateApiRequest("api_createTopic_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertEquals(getValueByExpression("//*[local-name()='TopicArn']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='TopicArn']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for createTopic method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateTopicWithMandatoryParameters"},
            description = "AmazonSNS {createTopic} integration test with negative case.")
    public void testCreateTopicNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createTopic");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createTopic_negative.xml");

        generateApiRequest("api_createTopic_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for deleteTopic method with mandatory parameters.
     */
    @Test(priority = 2, groups = {"wso2.esb"}, dependsOnMethods = {"testCreateTopicNegativeCase"},
            description = "AmazonSNS {deleteTopic} integration test with mandatory parameters.")
    public void testDeleteTopicWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:deleteTopic");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteTopic_mandatory.xml");

        generateApiRequest("api_deleteTopic_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for deleteTopic method.
     */
    @Test(priority = 2, groups = {"wso2.esb"}, dependsOnMethods = {"testCreateTopicNegativeCase"},
            description = "AmazonSNS {deleteTopic} integration test with negative case.")
    public void testDeleteTopicNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:deleteTopic");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteTopic_negative.xml");

        generateApiRequest("api_deleteTopic_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for listTopics method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testDeleteTopicNegativeCase"},
            description = "AmazonSNS {listTopics} integration test with mandatory parameters.")
    public void testListTopicsWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listTopics");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listTopics_mandatory.xml");

        generateApiRequest("api_listTopics_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(getValueByExpression("count(//*[local-name()='member'])", esbRestResponse.getBody()),
                getValueByExpression("count(//*[local-name()='member'])", apiRestResponse.getBody()));

        final QName topicsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Topics");
        final QName topicArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "TopicArn");

        final OMElement apiMemberElem =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(topicsQName).getFirstElement();

        final OMElement esbMemberElem =
                esbRestResponse.getBody().getFirstElement().getFirstChildWithName(topicsQName).getFirstElement();

        Assert.assertEquals(apiMemberElem.getFirstChildWithName(topicArnQName).getText(), esbMemberElem
                .getFirstChildWithName(topicArnQName).getText());
    }

    /**
     * Negative test case for listTopics method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListTopicsWithMandatoryParameters"},
            description = "AmazonSNS {listTopics} integration test with negative case.")
    public void testListTopicsNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listTopics");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listTopics_negative.xml");

        generateApiRequest("api_listTopics_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for getTopicAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListTopicsNegativeCase"},
            description = "AmazonSNS {getTopicAttributes} integration test with mandatory parameters.")
    public void testGetTopicAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getTopicAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getTopicAttributes_mandatory.xml");

        generateApiRequest("api_getTopicAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
        Assert.assertEquals(getValueByExpression("//*[local-name()='key']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='key']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for getTopicAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetTopicAttributesWithMandatoryParameters"},
            description = "AmazonSNS {getTopicAttributes} integration test with negative case.")
    public void testGetTopicAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getTopicAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getTopicAttributes_negative.xml");

        generateApiRequest("api_getTopicAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for setTopicAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetTopicAttributesNegativeCase"},
            description = "AmazonSNS {setTopicAttributes} integration test with mandatory parameters.")
    public void testSetTopicAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setTopicAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setTopicAttributes_mandatory.xml");

        generateApiRequest("api_setTopicAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }


    /**
     * Negative test case for setTopicAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetTopicAttributesWithMandatoryParameters"},
            description = "AmazonSNS {setTopicAttributes} integration test with negative case.")
    public void testSetTopicAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setTopicAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setTopicAttributes_negative.xml");

        generateApiRequest("api_setTopicAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for publish method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetTopicAttributesNegativeCase"},
            description = "AmazonSNS {publish} integration test with mandatory parameters.")
    public void testPublishWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:publish");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_publish_mandatory.xml");

        generateApiRequest("api_publish_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for publish method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testPublishWithMandatoryParameters"},
            description = "AmazonSNS {publish} integration test with mandatory parameters.")
    public void testPublishWithOptionalParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:publish");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_publish_optional.xml");

        generateApiRequest("api_publish_optional.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for publish method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testPublishWithOptionalParameters"},
            description = "AmazonSNS {publish} integration test with negative case.")
    public void testPublishNegativeCase() throws IOException, JSONException, XMLStreamException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalStateException, XPathExpressionException, SAXException,
            ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:publish");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_publish_negative.xml");

        generateApiRequest("api_publish_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for subscribe method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testPublishNegativeCase"},
            description = "AmazonSNS {subscribe} integration test with mandatory parameters.")
    public void testSubscribeWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:subscribe");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_subscribe_mandatory.xml");

        generateApiRequest("api_subscribe_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        connectorProperties.setProperty("subscriptionArn",
                getValueByExpression("//*[local-name()='SubscriptionArn']/text()", esbRestResponse.getBody()));

        Assert.assertEquals(getValueByExpression("//*[local-name()='SubscriptionArn']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='SubscriptionArn']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for subscribe method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSubscribeWithMandatoryParameters"},
            description = "AmazonSNS {subscribe} integration test with negative case.")
    public void testSubscribeNegativeCase() throws IOException, JSONException, XMLStreamException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalStateException, XPathExpressionException, SAXException,
            ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listSubscriptionsByTopic");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_subscribe_negative.xml");

        generateApiRequest("api_subscribe_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for listSubscriptions method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSubscribeNegativeCase"},
            description = "AmazonSNS {listSubscriptions} integration test with mandatory parameters.")
    public void testListSubscriptionsWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listSubscriptions");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listSubscriptions_mandatory.xml");

        generateApiRequest("api_listSubscriptions_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(getValueByExpression("count(//*[local-name()='member'])", esbRestResponse.getBody()),
                getValueByExpression("count(//*[local-name()='member'])", apiRestResponse.getBody()));

        final QName subscriptionsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Subscriptions");
        final QName protocolQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Protocol");
        final QName endpointQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoint");

        final OMElement apiMemberElem =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName).getFirstElement();

        final OMElement esbMemberElem =
                esbRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName).getFirstElement();

        Assert.assertEquals(apiMemberElem.getFirstChildWithName(protocolQName).getText(), esbMemberElem
                .getFirstChildWithName(protocolQName).getText());
        Assert.assertEquals(apiMemberElem.getFirstChildWithName(endpointQName).getText(), esbMemberElem
                .getFirstChildWithName(endpointQName).getText());
    }

    /**
     * Negative test case for listSubscriptions method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListSubscriptionsWithMandatoryParameters"},
            description = "AmazonSNS {listSubscriptions} integration test with negative case.")
    public void testListSubscriptionsNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listSubscriptions");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listSubscriptions_negative.xml");

        generateApiRequest("api_listSubscriptions_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for listSubscriptionsByTopic method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListSubscriptionsNegativeCase"},
            description = "AmazonSNS {listSubscriptionsByTopic} integration test with mandatory parameters.")
    public void testListSubscriptionsByTopicWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listSubscriptionsByTopic");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listSubscriptionsByTopic_mandatory.xml");

        generateApiRequest("api_listSubscriptionsByTopic_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(getValueByExpression("count(//*[local-name()='member'])", esbRestResponse.getBody()),
                getValueByExpression("count(//*[local-name()='member'])", apiRestResponse.getBody()));

        final QName subscriptionsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Subscriptions");
        final QName protocolQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Protocol");
        final QName endpointQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoint");

        final OMElement apiMemberElem =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName).getFirstElement();

        final OMElement esbMemberElem =
                esbRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName).getFirstElement();

        Assert.assertEquals(apiMemberElem.getFirstChildWithName(protocolQName).getText(), esbMemberElem
                .getFirstChildWithName(protocolQName).getText());
        Assert.assertEquals(apiMemberElem.getFirstChildWithName(endpointQName).getText(), esbMemberElem
                .getFirstChildWithName(endpointQName).getText());
    }

    /**
     * Negative test case for listSubscriptionsByTopic method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListSubscriptionsByTopicWithMandatoryParameters"},
            description = "AmazonSNS {listSubscriptionsByTopic} integration test with negative case.")
    public void testListSubscriptionsByTopicNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listSubscriptionsByTopic");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listSubscriptionsByTopic_negative.xml");

        generateApiRequest("api_listSubscriptionsByTopic_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for setSubscriptionAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListSubscriptionsByTopicNegativeCase"},
            description = "AmazonSNS {setSubscriptionAttributes} integration test with mandatory parameters.")
    public void testSetSubscriptionAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setSubscriptionAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setSubscriptionAttributes_mandatory.xml");

        generateApiRequest("api_setSubscriptionAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }


    /**
     * Negative test case for setSubscriptionAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetSubscriptionAttributesWithMandatoryParameters"},
            description = "AmazonSNS {setSubscriptionAttributes} integration test with negative case.")
    public void testSetSubscriptionAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setSubscriptionAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setSubscriptionAttributes_negative.xml");

        generateApiRequest("api_setSubscriptionAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for getSubscriptionAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetSubscriptionAttributesNegativeCase"},
            description = "AmazonSNS {getSubscriptionAttributes} integration test with mandatory parameters.")
    public void testGetSubscriptionAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getSubscriptionAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubscriptionAttributes_mandatory.xml");
        generateApiRequest("api_getSubscriptionAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
        Assert.assertEquals(getValueByExpression("//*[local-name()='key']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='key']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for getSubscriptionAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetSubscriptionAttributesWithMandatoryParameters"},
            description = "AmazonSNS {getSubscriptionAttributes} integration test with negative case.")
    public void testGetSubscriptionAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getSubscriptionAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubscriptionAttributes_negative.xml");

        generateApiRequest("api_getSubscriptionAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }


    /**
     * Positive test case for createPlatformApplication method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetSubscriptionAttributesNegativeCase"},
            description = "AmazonSNS {createPlatformApplication} integration test with mandatory parameters.")
    public void testCreatePlatformApplicationWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createPlatformApplication");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createPlatformApplication_mandatory.xml");

        connectorProperties.setProperty("platformApplicationArn",
                getValueByExpression("//*[local-name()='PlatformApplicationArn']/text()", esbRestResponse.getBody()));

        generateApiRequest("api_createPlatformApplication_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final QName platformApplicationsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplications");
        final QName platformApplicationArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplicationArn");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(platformApplicationsQName).getChildElements();

        boolean memberFound = false;
        OMElement selectedMember;

        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();

            if (connectorProperties.getProperty("platformApplicationArn").equals(
                    selectedMember.getFirstChildWithName(platformApplicationArnQName).getText())) {
                memberFound = true;
            }
        }

        Assert.assertTrue(memberFound);
    }

    /**
     * Negative test case for createPlatformApplication method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreatePlatformApplicationWithMandatoryParameters"},
            description = "AmazonSNS {CreatePlatformApplication} integration test with negative case.")
    public void testCreatePlatformApplicationNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createPlatformApplication");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createPlatformApplication_negative.xml");

        generateApiRequest("api_createPlatformApplication_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }


    /**
     * Positive test case for getPlatformApplicationAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreatePlatformApplicationNegativeCase"},
            description = "AmazonSNS {getPlatformApplicationAttributes} integration test with mandatory parameters.")
    public void testGetPlatformApplicationAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getPlatformApplicationAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPlatformApplicationAttributes_mandatory.xml");

        generateApiRequest("api_getPlatformApplicationAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
        Assert.assertEquals(getValueByExpression("//*[local-name()='key']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='key']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for getPlatformApplicationAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetPlatformApplicationAttributesWithMandatoryParameters"},
            description = "AmazonSNS {getPlatformApplicationAttributes} integration test with negative case.")
    public void testGetPlatformApplicationAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getPlatformApplicationAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPlatformApplicationAttributes_negative.xml");

        generateApiRequest("api_getPlatformApplicationAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for setPlatformApplicationAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetPlatformApplicationAttributesNegativeCase"},
            description = "AmazonSNS {setPlatformApplicationAttributes} integration test with mandatory parameters.")
    public void testSetPlatformApplicationAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException, InterruptedException {

        esbRequestHeadersMap.put("Action", "urn:setPlatformApplicationAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setPlatformApplicationAttributes_mandatory.xml");
        Thread.sleep(5000);

        generateApiRequest("api_setPlatformApplicationAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }

    /**
     * Negative test case for setPlatformApplicationAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetPlatformApplicationAttributesWithMandatoryParameters"},
            description = "AmazonSNS {setPlatformApplicationAttributes} integration test with negative case.")
    public void testSetPlatformApplicationAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setPlatformApplicationAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setPlatformApplicationAttributes_negative.xml");

        generateApiRequest("api_setPlatformApplicationAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for listPlatformApplications method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetPlatformApplicationAttributesNegativeCase"},
            description = "AmazonSNS {listPlatformApplications} integration test with mandatory parameters.")
    public void testListPlatformApplicationsWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listPlatformApplications");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listPlatformApplications_mandatory.xml");

        generateApiRequest("api_listPlatformApplications_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");


        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(getValueByExpression("count(//*[local-name()='member'])", esbRestResponse.getBody()),
                getValueByExpression("count(//*[local-name()='member'])", apiRestResponse.getBody()));

        final QName platformApplicationsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplications");
        final QName platformApplicationArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplicationArn");

        final OMElement apiMemberElem =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(platformApplicationsQName).getFirstElement();

        final OMElement esbMemberElem =
                esbRestResponse.getBody().getFirstElement().getFirstChildWithName(platformApplicationsQName).getFirstElement();

        Assert.assertEquals(apiMemberElem.getFirstChildWithName(platformApplicationArnQName).getText(), esbMemberElem
                .getFirstChildWithName(platformApplicationArnQName).getText());
    }

    /**
     * Negative test case for listPlatformApplications method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListPlatformApplicationsWithMandatoryParameters"},
            description = "AmazonSNS {listPlatformApplications} integration test with negative case.")
    public void testListPlatformApplicationsNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listPlatformApplications");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listPlatformApplications_negative.xml");

        generateApiRequest("api_listPlatformApplications_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for createEndpoint method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListPlatformApplicationsNegativeCase"},
            description = "AmazonSNS {createEndpoint} integration test with mandatory parameters.")
    public void testCreateEndpointWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createEndpoint");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createEndpoint_mandatory.xml");

        connectorProperties.setProperty("endpointArn",
                getValueByExpression("//*[local-name()='EndpointArn']/text()", esbRestResponse.getBody()));

        generateApiRequest("api_createEndpoint_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final QName endpointsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoints");
        final QName endpointArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "EndpointArn");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getChildElements();

        boolean memberFound = false;
        OMElement selectedMember;

        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();

            if (connectorProperties.getProperty("endpointArn").equals(
                    selectedMember.getFirstChildWithName(endpointArnQName).getText())) {
                memberFound = true;
            }
        }

        Assert.assertTrue(memberFound);
    }

    /**
     * Positive test case for createEndpoint method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateEndpointWithMandatoryParameters"},
            description = "AmazonSNS {createEndpoint} integration test with optional parameters.")
    public void testCreateEndpointWithOptionalParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createEndpoint");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createEndpoint_optional.xml");

        connectorProperties.setProperty("endpointArnOpt",
                getValueByExpression("//*[local-name()='EndpointArn']/text()", esbRestResponse.getBody()));

        generateApiRequest("api_createEndpoint_optional.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final QName endpointsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoints");
        final QName endpointArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "EndpointArn");
        final QName attributeQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Attributes");
        final QName keyQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "key");
        final QName valueQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "value");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            Iterator entryIterator = selectedMember.getFirstChildWithName(attributeQName).getChildElements();
            String entryValue = "";
            while (entryIterator.hasNext()) {
                OMElement selectEntry = (OMElement) entryIterator.next();
                if ("CustomUserData".equals(selectEntry.getFirstChildWithName(keyQName).getText())) {
                    entryValue = selectEntry.getFirstChildWithName(valueQName).getText();
                }
            }
            if (connectorProperties.getProperty("endpointArnOpt").equals(
                    selectedMember.getFirstChildWithName(endpointArnQName).getText())
                    && connectorProperties.getProperty("customUserData").equals(entryValue)) {
                memberFound = true;
            }
        }
        Assert.assertTrue(memberFound);
    }

    /**
     * Negative test case for createEndpoint method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateEndpointWithOptionalParameters"},
            description = "AmazonSNS {createEndpoint} integration test with negative case.")
    public void testCreateEndpointNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:createEndpoint");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createEndpoint_negative.xml");

        generateApiRequest("api_createEndpoint_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for listEndpoints method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateEndpointNegativeCase"},
            description = "AmazonSNS {listEndpoints} integration test with mandatory parameters.")
    public void testListEndpointsWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listEndpoints");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listEndpoints_mandatory.xml");

        generateApiRequest("api_listEndpoints_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(getValueByExpression("count(//*[local-name()='member'])", esbRestResponse.getBody()),
                getValueByExpression("count(//*[local-name()='member'])", apiRestResponse.getBody()));

        final QName endpointsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoints");
        final QName endpointArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "EndpointArn");

        final OMElement apiMemberElem =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getFirstElement();
        final OMElement esbMemberElem =
                esbRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getFirstElement();

        Assert.assertEquals(apiMemberElem.getFirstChildWithName(endpointArnQName).getText(), esbMemberElem
                .getFirstChildWithName(endpointArnQName).getText());

    }

    /**
     * Negative test case for listEndpoints method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListEndpointsWithMandatoryParameters"},
            description = "AmazonSNS {listEndpoints} integration test with negative case.")
    public void testListEndpointsNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:listEndpoints");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_listEndpoints_negative.xml");

        generateApiRequest("api_listEndpoints_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for getEndpointAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListEndpointsNegativeCase"},
            description = "AmazonSNS {getEndpointAttributes} integration test with mandatory parameters.")
    public void testGetEndpointAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getEndpointAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getEndpointAttributes_mandatory.xml");

        generateApiRequest("api_getEndpointAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
        Assert.assertEquals(getValueByExpression("//*[local-name()='key']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='key']/text()", apiRestResponse.getBody()));
    }

    /**
     * Negative test case for getEndpointAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetEndpointAttributesWithMandatoryParameters"},
            description = "AmazonSNS {getEndpointAttributes} integration test with negative case.")
    public void testGetEndpointAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:getEndpointAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getEndpointAttributes_negative.xml");

        generateApiRequest("api_getEndpointAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for setEndpointAttributes method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetEndpointAttributesNegativeCase"},
            description = "AmazonSNS {setEndpointAttributes} integration test with mandatory parameters.")
    public void testSetEndpointAttributesWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setEndpointAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setEndpointAttributes_mandatory.xml");

        generateApiRequest("api_setEndpointAttributes_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final QName endpointsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoints");
        final QName attributeQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Attributes");
        final QName keyQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "key");
        final QName valueQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "value");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            Iterator entryIterator = selectedMember.getFirstChildWithName(attributeQName).getChildElements();
            String entryValue = "";
            while (entryIterator.hasNext()) {
                OMElement selectEntry = (OMElement) entryIterator.next();
                if ("CustomUserData".equals(selectEntry.getFirstChildWithName(keyQName).getText())) {
                    entryValue = selectEntry.getFirstChildWithName(valueQName).getText();
                }
            }
            if (connectorProperties.getProperty("setAttributesEntryValue").equals(entryValue)) {
                memberFound = true;
            }
        }
        Assert.assertTrue(memberFound);
    }


    /**
     * Negative test case for setEndpointAttributes method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetEndpointAttributesWithMandatoryParameters"},
            description = "AmazonSNS {setEndpointAttributes} integration test with negative case.")
    public void testSetEndpointAttributesNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:setEndpointAttributes");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_setEndpointAttributes_negative.xml");

        generateApiRequest("api_setEndpointAttributes_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for deleteEndpoint method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testSetEndpointAttributesNegativeCase"},
            description = "AmazonSNS {deleteEndpoint} integration test with mandatory parameters.")
    public void testDeleteEndpointMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException, InterruptedException {

        esbRequestHeadersMap.put("Action", "urn:deleteEndpoint");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteEndpoint_mandatory.xml");

        Thread.sleep(5000);

        generateApiRequest("api_deleteEndpoint_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final QName endpointsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Endpoints");
        final QName endpointArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "EndpointArn");
        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(endpointsQName).getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            if (connectorProperties.getProperty("endpointArn").equals(
                    selectedMember.getFirstChildWithName(endpointArnQName).getText())) {
                memberFound = true;
            }
        }
        Assert.assertFalse(memberFound);
    }

    /**
     * Negative test case for deleteEndpoint method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testDeleteEndpointMandatoryParameters"},
            description = "AmazonSNS {deleteEndpoint} integration test with negative case.")
    public void testDeleteEndpointNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:deleteEndpoint");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteEndpoint_negative.xml");

        generateApiRequest("api_deleteEndpoint_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for deletePlatformApplication method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testDeleteEndpointNegativeCase"},
            description = "AmazonSNS {deletePlatformApplication} integration test with mandatory parameters.")
    public void testDeletePlatformApplicationMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException, InterruptedException {

        esbRequestHeadersMap.put("Action", "urn:deletePlatformApplication");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deletePlatformApplication_mandatory.xml");
        Thread.sleep(5000);
        generateApiRequest("api_deletePlatformApplication_mandatory.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        final QName platformApplicationsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplications");
        final QName platformApplicationArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "PlatformApplicationArn");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(platformApplicationsQName).getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            if (connectorProperties.getProperty("platformApplicationArn").equals(
                    selectedMember.getFirstChildWithName(platformApplicationArnQName).getText())) {
                memberFound = true;
            }
        }
        Assert.assertFalse(memberFound);
    }

    /**
     * Negative test case for deletePlatformApplication method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testDeletePlatformApplicationMandatoryParameters"},
            description = "AmazonSNS {deletePlatformApplication} integration test with negative case.")
    public void testDeletePlatformApplicationNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:deletePlatformApplication");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deletePlatformApplication_negative.xml");

        generateApiRequest("api_deletePlatformApplication_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for addPermission method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testDeletePlatformApplicationNegativeCase"},
            description = "AmazonSNS {addPermission} integration test with mandatory parameters.")
    public void testAddPermissionWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:addPermission");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_addPermission_mandatory.xml");

        generateApiRequest("api_addPermission_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }

    /**
     * Negative test case for addPermission method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testAddPermissionWithMandatoryParameters"},
            description = "AmazonSNS {addPermission} integration test with negative case.")
    public void testAddPermissionNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:addPermission");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_addPermission_negative.xml");

        generateApiRequest("api_addPermission_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for removePermission method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testAddPermissionNegativeCase"},
            description = "AmazonSNS {removePermission} integration test with mandatory parameters.")
    public void testRemovePermissionWithMandatoryParameters() throws IOException, JSONException,
            XMLStreamException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
            XPathExpressionException, SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:removePermission");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_removePermission_mandatory.xml");

        generateApiRequest("api_removePermission_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }

    /**
     * Negative test case for removePermission method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testRemovePermissionWithMandatoryParameters"},
            description = "AmazonSNS {addPermission} integration test with negative case.")
    public void testRemovePermissionNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:removePermission");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_removePermission_negative.xml");

        generateApiRequest("api_removePermission_negative.json");
        RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for confirmSubscription method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testRemovePermissionNegativeCase"},
            description = "AmazonSNS {confirmSubscription} integration test with mandatory parameters.")
    public void testConfirmSubscriptionWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:confirmSubscription");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_confirmSubscription_mandatory.xml");

        generateApiRequest("api_confirmSubscription_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        final String subscriptionArn =
                getValueByExpression("//*[local-name()='SubscriptionArn']/text()", esbRestResponse.getBody());
        connectorProperties.setProperty("subscriptionArnMandatory", subscriptionArn);
        Assert.assertNotEquals(subscriptionArn, "pending confirmation");
        final QName subscriptionsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Subscriptions");
        final QName subscriptionArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "SubscriptionArn");
        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName)
                        .getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            if (subscriptionArn.equals(selectedMember.getFirstChildWithName(subscriptionArnQName).getText())) {
                memberFound = true;
            }
        }
        Assert.assertTrue(memberFound);
    }

    /**
     * Positive test case for confirmSubscription method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testConfirmSubscriptionWithMandatoryParameters"},
            description = "AmazonSNS {confirmSubscription} integration test with optional parameters.")
    public void testConfirmSubscriptionWithOptionalParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:confirmSubscription");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_confirmSubscription_optional.xml");
        generateApiRequest("api_confirmSubscription_optional.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        final String subscriptionArn =
                getValueByExpression("//*[local-name()='SubscriptionArn']/text()", esbRestResponse.getBody());
        connectorProperties.setProperty("subscriptionArnOptional", subscriptionArn);

        Assert.assertNotEquals(subscriptionArn, "pending confirmation");

        final QName subscriptionsQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "Subscriptions");
        final QName subscriptionArnQName = new QName("http://sns.amazonaws.com/doc/2010-03-31/", "SubscriptionArn");

        final Iterator apiMemberElemIterator =
                apiRestResponse.getBody().getFirstElement().getFirstChildWithName(subscriptionsQName).getChildElements();
        boolean memberFound = false;
        OMElement selectedMember;
        while (apiMemberElemIterator.hasNext()) {
            selectedMember = (OMElement) apiMemberElemIterator.next();
            if (subscriptionArn.equals(selectedMember.getFirstChildWithName(subscriptionArnQName).getText())) {
                memberFound = true;
            }
        }
        Assert.assertTrue(memberFound);
    }

    /**
     * Negative test case for confirmSubscription method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testConfirmSubscriptionWithOptionalParameters"},
            description = "AmazonSNS {confirmSubscription} integration test with negative case.")
    public void testConfirmSubscriptionNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:confirmSubscription");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_confirmSubscription_negative.xml");

        generateApiRequest("api_confirmSubscription_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }

    /**
     * Positive test case for unsubscribe method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testConfirmSubscriptionNegativeCase"},
            description = "AmazonSNS {unsubscribe} integration test with mandatory parameters.")
    public void testUnsubscribeWithMandatoryParameters() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:unsubscribe");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_unsubscribe_mandatory.xml");
        generateApiRequest("api_unsubscribe_mandatory.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", esbRestResponse.getBody()),
                "");
        Assert.assertNotEquals(getValueByExpression("//*[local-name()='RequestId']/text()", apiRestResponse.getBody()),
                "");
    }

    /**
     * Negative test case for unsubscribe method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testUnsubscribeWithMandatoryParameters"},
            description = "AmazonSNS {unsubscribe} integration test with negative case.")
    public void testUnsubscribeNegativeCase() throws IOException, JSONException, XMLStreamException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, XPathExpressionException,
            SAXException, ParserConfigurationException {

        esbRequestHeadersMap.put("Action", "urn:unsubscribe");
        final RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_unsubscribe_negative.xml");

        generateApiRequest("api_unsubscribe_negative.json");
        final RestResponse<OMElement> apiRestResponse =
                sendXmlRestRequest(apiEndPoint, "POST", apiRequestHeadersMap, "common_api_request.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 400);
        Assert.assertEquals(getValueByExpression("//*[local-name()='Message']/text()", esbRestResponse.getBody()),
                getValueByExpression("//*[local-name()='Message']/text()", apiRestResponse.getBody()));
    }


    /**
     * Generate API request.
     *
     * @param signatureRequestFile the signature request file
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JSONException the jSON exception
     * @throws InvalidKeyException the invalid key exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws IllegalStateException the illegal state exception
     */
    public void generateApiRequest(String signatureRequestFile) throws IOException, JSONException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalStateException {

        String requestData;
        Map<String, String> responseMap;
        AmazonSNSAuthConnector authConnector = new AmazonSNSAuthConnector();

        String signatureRequestFilePath =
                ProductConstant.SYSTEM_TEST_SETTINGS_LOCATION + File.separator + "artifacts" + File.separator + "ESB"
                        + File.separator + "config" + File.separator + "restRequests" + File.separator + "amazonsns"
                        + File.separator + signatureRequestFile;

        requestData = loadRequestFromFile(signatureRequestFilePath);
        JSONObject signatureRequestObject = new JSONObject(requestData);
        responseMap = authConnector.getRequestPayload(signatureRequestObject);

        apiRequestHeadersMap.put("Authorization", responseMap.get(AmazonSNSConstants.AUTHORIZATION_HEADER));
        apiRequestHeadersMap.put("x-amz-date", responseMap.get(AmazonSNSConstants.AMZ_DATE));
        connectorProperties.put("xFormUrl", responseMap.get(AmazonSNSConstants.REQUEST_PAYLOAD));
    }

    /**
     * Load request from file.
     *
     * @param requestFileName the request file name
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String loadRequestFromFile(String requestFileName) throws IOException {

        String requestFilePath;
        String requestData;
        requestFilePath = requestFileName;
        requestData = getFileContent(requestFilePath);
        Properties prop = (Properties) connectorProperties.clone();

        Matcher matcher = Pattern.compile("%s\\(([A-Za-z0-9]*)\\)", Pattern.DOTALL).matcher(requestData);
        while (matcher.find()) {
            String key = matcher.group(1);
            requestData = requestData.replaceAll("%s\\(" + key + "\\)", prop.getProperty(key));
        }
        return requestData;
    }

    /**
     * Gets the file content.
     *
     * @param path the path
     * @return the file content
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String getFileContent(String path) throws IOException {

        String fileContent;
        BufferedInputStream bfist = new BufferedInputStream(new FileInputStream(path));

        byte[] buf = new byte[bfist.available()];
        bfist.read(buf);
        fileContent = new String(buf);

        if (bfist != null) {
            bfist.close();
        }
        return fileContent;
    }
}
