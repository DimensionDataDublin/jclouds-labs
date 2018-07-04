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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateVirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseAccountAwareCloudControlMockTest;
import org.jclouds.dimensiondata.cloudcontrol.options.DatacenterIdListFilters;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.jclouds.location.suppliers.ZoneIdsSupplier;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "VirtualListenerApiLiveTest", singleThreaded = true)
// FIXME rename all Mock tests to ApiMockTest
public class VirtualListenerMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testCreateVirtualListener() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/createVirtualListenerResponse.json"));
      String virtualListenerId = api.getVirtualListenerApi().createVirtualListener(CreateVirtualListener.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("Production.Load.Balancer")
            .description("Used as the load balancer for the production applications.")
            .type(VirtualListener.Type.STANDARD)
            .protocol(VirtualListener.Protocol.TCP)
            .listenerIpAddress("165.180.12.22")
            .port(80)
            .enabled(true)
            .connectionLimit(25000)
            .connectionRateLimit(2000)
            .sourcePortPreservation(VirtualListener.SourcePortPreservation.PRESERVE)
            .poolId("afb1fb1a-eab9-43f4-95c2-36a4cdda6cb8")
            .clientClonePoolId("033a97dc-ee9b-4808-97ea-50b06624fd16")
            .persistenceProfileId("a34ca25c-f3db-11e4-b010-005056806999")
            .fallbackPersistenceProfileId("6f2f5d7b-cdd9-4d84-8ad7-999b64a87978")
            .optimizationProfile(VirtualListener.OptimizationProfile.TCP)
            // .sslOffloadProfile(SslOffloadProfile.builder().....build())
            .iruleIds(Collections.singletonList("2b20abd9-ffdc-11e4-b010-005056806999"))
            .build());
      assertEquals(virtualListenerId, "virtualListenerId1");
      assertSentToCloudControlEndpoint(HttpMethod.POST, "/networkDomainVip/createVirtualListener",
            stringFromResource("/vip/createVirtualListenerRequest.json"));
   }

   @Test
   public void testGetVirtualListener() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/virtualListener.json"));
      VirtualListener virtualListener = api.getVirtualListenerApi().getVirtualListener("12345");
      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/virtualListener/12345");
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

      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/virtualListener");
   }

   @Test
   public void testListVirtualListeners_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/virtualListeners_page2.json"));
      ZoneIdsSupplier zoneIdsSupplier = ctx.utils().injector().getInstance(ZoneIdsSupplier.class);
      PaginatedCollection<VirtualListener> virtualListeners = api.getVirtualListenerApi().listVirtualListeners(DatacenterIdListFilters.Builder.paginationOptions(
              new PaginationOptions().pageNumber(2).pageSize(1)).datacenterIdsFromZoneIdsSupplier(zoneIdsSupplier));
      assertEquals(virtualListeners.size(), 1, "should only return 2nd element");
      assertEquals(virtualListeners.get(0).id(), "VirtualListenerIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = Uris.uriBuilder("/networkDomainVip/virtualListener");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSentToCloudControlEndpoint(HttpMethod.GET, uriBuilder.toString());
   }
}
