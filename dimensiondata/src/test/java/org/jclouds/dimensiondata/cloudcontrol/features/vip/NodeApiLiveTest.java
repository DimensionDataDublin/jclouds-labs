/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.dimensiondata.cloudcontrol.features.vip;

import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateNode;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test(groups = "live", testName = "NodeApiLiveTest", singleThreaded = true)
public class NodeApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   private String networkDomainId;
   private String nodeId;

   @BeforeClass
   public void setUp() {
      super.setUp();
      String datacenterId = datacenters.iterator().next();
      String networkDomainName = NodeApiLiveTest.class.getSimpleName() + System.currentTimeMillis();
      networkDomainId = api.getNetworkApi()
            .deployNetworkDomain(datacenterId, networkDomainName, "NodeLiveTest", "ADVANCED");
      assertTrue(networkDomainNormalPredicate.apply(networkDomainId), "Network Domain deploy timed out");
   }

   public void testCreateNode() {
      nodeId = api.getNodeApi().createNode(CreateNode.builder()
            .networkDomainId(networkDomainId)
            .name("nodeApiLiveTest" +  + System.currentTimeMillis())
            .description("description")
            .ipv4Address("10.5.2.14")
            .status(Node.Status.ENABLED)
            .connectionLimit(100)
            .connectionRateLimit(200)
            .build());
      assertNotNull(nodeId, "should return Node ID");
   }

   @Test(dependsOnMethods = "testCreateNode")
   public void testGetNode() {
      Node node = api.getNodeApi().getNode(nodeId);
      assertNotNull(node, "should Get the Node");
   }

   @Test(dependsOnMethods = "testGetNode")
   public void testListNodes() {
      List<Node> nodes = api.getNodeApi().listNodes().concat().toList();
      assertFalse(nodes.isEmpty(), "should List the Node");
   }

   @Test(dependsOnMethods = "testListNodes")
   public void testDeleteNode() {
      api.getNodeApi().deleteNode(nodeId);
   }

   @AfterClass
   public void tearDown() {
      super.tearDown();
      api.getNetworkApi().deleteNetworkDomain(networkDomainId);
   }
}