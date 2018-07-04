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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateNode;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseAccountAwareCloudControlMockTest;
import org.jclouds.dimensiondata.cloudcontrol.options.DatacenterIdListFilters;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.jclouds.location.suppliers.ZoneIdsSupplier;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "NodeApiLiveTest", singleThreaded = true)
public class NodeApiMockTest extends BaseAccountAwareCloudControlMockTest {

   public void testCreateNode() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/createNodeResponse.json"));
      String nodeId = api.getNodeApi().createNode(CreateNode.builder()
            .networkDomainId("networkDomainId")
            .name("name")
            .description("description")
            .ipv4Address("10.5.2.14")
            .status(Node.Status.ENABLED)
            .healthMonitorId("healthMonitorId")
            .connectionLimit(100)
            .connectionRateLimit(200)
            .build());
      assertEquals(nodeId, "nodeId1");
      assertSentToCloudControlEndpoint(
            HttpMethod.POST, "/networkDomainVip/createNode", stringFromResource("/vip/createNodeRequest.json"));
   }

   public void testGetNode() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/node.json"));
      Node node = api.getNodeApi().getNode("12345");
      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/node/12345");
      assertNotNull(node);
   }

   public void testGetNodeResourceNotFound() {
      server.enqueue(responseResourceNotFound());
      Node found = api.getNodeApi().getNode("12345");
      assertNull(found);
   }

   public void testListNodes_ReadAll() throws Exception {
      server.enqueue(jsonResponse("/vip/nodes.json"));
      Iterable<Node> nodes = api.getNodeApi().listNodes().concat();
      assertEquals(consumeIteratorAndReturnSize(nodes), 2,
              "should return all nodes defined in enqueued response");

      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/node");
   }

   public void testListNodes_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/nodes_page2.json"));
      ZoneIdsSupplier zoneIdsSupplier = ctx.utils().injector().getInstance(ZoneIdsSupplier.class);
      PaginatedCollection<Node> nodes = api.getNodeApi().listNodes(DatacenterIdListFilters.Builder.paginationOptions(
              new PaginationOptions().pageNumber(2).pageSize(1)).datacenterIdsFromZoneIdsSupplier(zoneIdsSupplier));
      assertEquals(nodes.size(), 1, "should only return 2nd element");
      assertEquals(nodes.get(0).id(), "nodeIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = Uris.uriBuilder("networkDomainVip/node");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSentToCloudControlEndpoint(HttpMethod.GET, uriBuilder.toString());
   }

   public void testDeleteNode() throws Exception {
      server.enqueue(jsonResponse("/vip/deleteNodeResponse.json"));
      api.getNodeApi().deleteNode("12345");
      assertSentToCloudControlEndpoint(HttpMethod.POST, "/networkDomainVip/deleteNode");
   }
}
