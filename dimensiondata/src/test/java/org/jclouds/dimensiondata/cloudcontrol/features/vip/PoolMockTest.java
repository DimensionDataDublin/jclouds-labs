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
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.Uris;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;

import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "PoolApiLiveTest", singleThreaded = true)
public class PoolMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testCreatePool() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/createPoolResponse.json"));
      String poolId = api.getPoolApi().createPool(CreatePool.builder()
            .networkDomainId("networkDomainId")
            .name("name")
            .description("description")
            .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
            .healthMonitorIds(ImmutableList.of("healthMonitorId1", "healthMonitorId2"))
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .slowRampTime(10)
            .build());
      assertEquals(poolId, "poolId1");
      assertSentToCloudControlEndpoint(
            POST, "networkDomainVip/createPool", stringFromResource("/vip/createPoolRequest.json"));
   }

   @Test
   public void testGetPool() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/pool.json"));
      Pool pool = api.getPoolApi().getPool("12345");
      assertSent(GET, getBasicApiUri("networkDomainVip/pool/12345").toString());
      assertNotNull(pool);
   }

   @Test
   public void testGetPoolResourceNotFound() throws InterruptedException {
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
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }

   @Test
   public void testListPools_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/pools_page2.json"));
      PaginatedCollection<Pool> pools = api.getPoolApi().listPools(
            new PaginationOptions().pageNumber(2).pageSize(1));
      assertEquals(pools.size(), 1, "should only return 2nd element");
      assertEquals(pools.get(0).id(), "poolIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/pool");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }
}
