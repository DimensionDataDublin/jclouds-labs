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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import javax.ws.rs.HttpMethod;

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseAccountAwareCloudControlMockTest;
import org.jclouds.dimensiondata.cloudcontrol.options.DatacenterIdListFilters;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.jclouds.location.suppliers.ZoneIdsSupplier;
import org.testng.annotations.Test;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.HealthMonitor;

@Test(groups = "live", testName = "PoolApiLiveTest", singleThreaded = true)
public class PoolMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testGetPool() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/pool.json"));
      Pool pool = api.getPoolApi().getPool("12345");
      assertSent(HttpMethod.GET, getBasicApiUri("networkDomainVip/pool/12345").toString());
      assertEquals(pool, Pool.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("myDevelopmentPool.1")
            .description("Pool for load balancing development application servers.")
            .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
            .healthMonitor(ImmutableList.<HealthMonitor>of(
                  HealthMonitor.builder()
                        .id("01683574-d487-11e4-811f-005056806999")
                        .name("CCDEFAULT.Http")
                        .build(),
                  HealthMonitor.builder()
                        .id("0168546c-d487-11e4-811f-005056806999")
                        .name("CCDEFAULT.Https")
                        .build()))
            .serviceDownAction("RESELECT")
            .slowRampTime(10)
            .state(Pool.State.NORMAL)
            .createTime(pool.createTime())
            .id("4d360b1f-bc2c-4ab7-9884-1f03ba2768f7")
            .datacenterId("NA9")
            .build());
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

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/pool");
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }

   @Test
   public void testListPools_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/pools_page2.json"));
      ZoneIdsSupplier zoneIdsSupplier = ctx.utils().injector().getInstance(ZoneIdsSupplier.class);
      PaginatedCollection<Pool> pools = api.getPoolApi().listPools(DatacenterIdListFilters.Builder.paginationOptions(
              new PaginationOptions().pageNumber(2).pageSize(1)).datacenterIdsFromZoneIdsSupplier(zoneIdsSupplier));
      assertEquals(pools.size(), 1, "should only return 2nd element");
      assertEquals(pools.get(0).id(), "poolIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/pool");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }
}
