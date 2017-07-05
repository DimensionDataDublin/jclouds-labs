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

import org.jclouds.dimensiondata.cloudcontrol.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.HealthMonitor;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseAccountAwareCloudControlMockTest;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.testng.annotations.Test;

import static javax.ws.rs.HttpMethod.GET;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "NodeApiLiveTest", singleThreaded = true)
public class NodeMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testGetNode() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/node.json"));
      Node node = api.getNodeApi().getNode("12345");
      assertSent(GET, getBasicApiUri("networkDomainVip/node/12345").toString());
      assertEquals(node, Node.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("ProductionNode.2")
            .description("Production Server 2")
            .ipv4Address("10.10.10.101")
            .state(Node.State.NORMAL)
            .status(Node.Status.ENABLED)
            .healthMonitor(HealthMonitor.builder()
                  .id("0168b83a-d487-11e4-811f-005056806999")
                  .name("ICMP")
                  .build())
            .connectionLimit(10000)
            .connectionRateLimit(2000)
            .createTime(node.createTime()) // TODO set date from string in XML format
            .id("34de6ed6-46a4-4dae-a753-2f8d3840c6f9")
            .datacenterId("NA9")
            .build());
   }

   @Test
   public void testGetNodeResourceNotFound() throws InterruptedException {
      server.enqueue(responseResourceNotFound());
      Node found = api.getNodeApi().getNode("12345");
      assertNull(found);
   }

   @Test
   public void testListNodes_ReadAll() throws Exception {
      server.enqueue(jsonResponse("/vip/nodes.json"));
      Iterable<Node> nodes = api.getNodeApi().listNodes().concat();
      assertEquals(consumeIteratorAndReturnSize(nodes), 2, "should return all nodes defined in enqueued response");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/node");
      addDatacenterFilters(uriBuilder);
      assertSent(GET, uriBuilder.toString());
   }

   @Test
   public void testListNodes_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/nodes_page2.json"));
      PaginatedCollection<Node> nodes = api.getNodeApi().listNodes(new PaginationOptions().pageNumber(2).pageSize(1));
      assertEquals(nodes.size(), 1, "should only return 2nd element");
      assertEquals(nodes.get(0).id(), "nodeIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/node");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSent(GET, uriBuilder.toString());
   }
}
