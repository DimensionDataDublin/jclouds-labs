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

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreatePool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
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

@Test(groups = "live", testName = "PoolApiLiveTest", singleThreaded = true)
public class PoolApiMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testCreatePool() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/createPoolResponse.json"));
      String poolId = api.getPoolApi().createPool(CreatePool.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("myDevelopmentPool.1")
            .description("Pool for load balancing development application servers.")
            .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
            .healthMonitorIds(
                  ImmutableList.of("01683574-d487-11e4-811f-005056806999", "0168546c-d487-11e4-811f-005056806999"))
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .slowRampTime(10)
            .build());
      assertEquals(poolId, "poolId1");
      assertSentToCloudControlEndpoint(
            HttpMethod.POST, "/networkDomainVip/createPool", stringFromResource("/vip/createPoolRequest.json"));
   }

   @Test
   public void testGetPool() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/pool.json"));
      Pool pool = api.getPoolApi().getPool("12345");
      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/pool/12345");
      assertNotNull(pool);
   }

   @Test
   public void testGetPoolResourceNotFound() {
      server.enqueue(responseResourceNotFound());
      Pool found = api.getPoolApi().getPool("12345");
      assertNull(found);
   }

   @Test
   public void testListPools_ReadAll() throws Exception {
      server.enqueue(jsonResponse("/vip/pools.json"));
      Iterable<Pool> pools = api.getPoolApi().listPools().concat();
      assertEquals(consumeIteratorAndReturnSize(pools), 2,
            "should return all pools defined in enqueued response");
      assertSentToCloudControlEndpoint(HttpMethod.GET, "/networkDomainVip/pool");
   }

   @Test
   public void testListPools_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/pools_page2.json"));
      ZoneIdsSupplier zoneIdsSupplier = ctx.utils().injector().getInstance(ZoneIdsSupplier.class);
      PaginatedCollection<Pool> pools = api.getPoolApi().listPools(DatacenterIdListFilters.Builder.paginationOptions(
            new PaginationOptions().pageNumber(2).pageSize(1)).datacenterIdsFromZoneIdsSupplier(zoneIdsSupplier));
      assertEquals(pools.size(), 1, "should only return 2nd element");
      assertEquals(pools.get(0).id(), "poolIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder expectedUriBuilder = Uris.uriBuilder("networkDomainVip/pool");
      expectedUriBuilder.addQuery("pageNumber", Integer.toString(2));
      expectedUriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(expectedUriBuilder);
      assertSentToCloudControlEndpoint(HttpMethod.GET, expectedUriBuilder.toString());
   }
}
