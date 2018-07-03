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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseAccountAwareCloudControlMockTest;
import org.jclouds.dimensiondata.cloudcontrol.options.DatacenterIdListFilters;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.jclouds.location.suppliers.ZoneIdsSupplier;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;

import static javax.ws.rs.HttpMethod.GET;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "VirtualListenerApiLiveTest", singleThreaded = true)
public class VirtualListenerMockTest extends BaseAccountAwareCloudControlMockTest {

/* FIXME add create
   @Test
   public void testCreateVirtualListener() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/createVirtualListenerResponse.json"));
      String VirtualListenerId = api.getVirtualListenerApi().createVirtualListener(CreateVirtualListener.builder()
            .networkDomainId("networkDomainId")
            .name("name")
            .description("description")
            .ipv4Address("10.5.2.14")
            .status(VirtualListener.Status.ENABLED)
            .healthMonitorId("healthMonitorId")
            .connectionLimit(100)
            .connectionRateLimit(200)
            .build());
      assertEquals(VirtualListenerId, "VirtualListenerId1");
      assertSentToCloudControlEndpoint(
            POST, "networkDomainVip/createVirtualListener", stringFromResource("/vip/createVirtualListenerRequest.json"));
   }
*/

   @Test
   public void testGetVirtualListener() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/virtualListener.json"));
      VirtualListener virtualListener = api.getVirtualListenerApi().getVirtualListener("12345");
      assertSent(GET, getBasicApiUri("networkDomainVip/virtualListener/12345").toString());
      assertNotNull(virtualListener);
   }

   @Test
   public void testGetVirtualListenerResourceNotFound() {
      server.enqueue(responseResourceNotFound());
      VirtualListener found = api.getVirtualListenerApi().getVirtualListener("12345");
      assertNull(found);
   }

   @Test
   public void testListVirtualListeners_ReadAll() throws Exception {
      server.enqueue(jsonResponse("/vip/virtualListeners.json"));
      Iterable<VirtualListener> virtualListeners = api.getVirtualListenerApi().listVirtualListeners().concat();
      assertEquals(consumeIteratorAndReturnSize(virtualListeners), 2,
              "should return all virtualListeners defined in enqueued response");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/virtualListener");
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }

   @Test
   public void testListVirtualListeners_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/virtualListeners_page2.json"));
      ZoneIdsSupplier zoneIdsSupplier = ctx.utils().injector().getInstance(ZoneIdsSupplier.class);
      PaginatedCollection<VirtualListener> virtualListeners = api.getVirtualListenerApi().listVirtualListeners(DatacenterIdListFilters.Builder.paginationOptions(
              new PaginationOptions().pageNumber(2).pageSize(1)).datacenterIdsFromZoneIdsSupplier(zoneIdsSupplier));
      assertEquals(virtualListeners.size(), 1, "should only return 2nd element");
      assertEquals(virtualListeners.get(0).id(), "VirtualListenerIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/virtualListener");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }
}
