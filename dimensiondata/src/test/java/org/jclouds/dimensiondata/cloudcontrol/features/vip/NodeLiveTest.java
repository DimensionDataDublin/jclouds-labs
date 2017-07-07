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

import com.google.common.collect.FluentIterable;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateNode;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "NodeApiLiveTest", singleThreaded = true)
public class NodeLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   public void testCreateNodes() throws Exception {
      String nodeId = api.getNodeApi().createNode(CreateNode.builder()
            .networkDomainId("your-nd-id-here")
            .name("node1")
            .description("description")
            .healthMonitorId("hm-id-here")
            .ipv4Address("10.0.1.1")
            .connectionLimit(10)
            .connectionRateLimit(100)
            .status(Node.Status.DISABLED)
            .build());
      assertNotNull(nodeId);
   }

   public void testListNodes() throws Exception {
      FluentIterable<Node> nodes = api.getNodeApi().listNodes().concat();
      assertFalse(nodes.isEmpty());
      for (Node node : nodes) {
         assertNotNull(node);
      }
   }
}
