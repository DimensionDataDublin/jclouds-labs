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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.AddPoolMember;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.PoolMember;
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
public class PoolMemberMockTest extends BaseAccountAwareCloudControlMockTest {

   @Test
   public void testAddPoolMember() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/addPoolMemberResponse.json"));
      String poolMemberId = api.getPoolMemberApi().addPoolMember(AddPoolMember.builder()
            .poolId("poolId")
            .nodeId("nodeId")
            .port(10)
            .status(PoolMember.Status.ENABLED)
            .build());
      assertEquals(poolMemberId, "poolMemberId1");
      assertSentToCloudControlEndpoint(
            POST, "networkDomainVip/addPoolMember", stringFromResource("/vip/addPoolMemberRequest.json"));
   }

   @Test
   public void testGetPoolMember() throws InterruptedException {
      server.enqueue(jsonResponse("/vip/poolMember.json"));
      PoolMember poolMember = api.getPoolMemberApi().getPoolMember("12345");
      assertSent(GET, getBasicApiUri("networkDomainVip/poolMember/12345").toString());
      assertNotNull(poolMember);
   }

   @Test
   public void testGetPoolMemberResourceNotFound() throws InterruptedException {
      server.enqueue(responseResourceNotFound());
      PoolMember found = api.getPoolMemberApi().getPoolMember("12345");
      assertNull(found);
   }

   @Test
   public void testListPoolMembers_ReadAll() throws Exception {
      server.enqueue(jsonResponse("/vip/poolMembers.json"));
      Iterable<PoolMember> poolMembers = api.getPoolMemberApi().listPoolMembers().concat();
      assertEquals(consumeIteratorAndReturnSize(poolMembers), 2,
            "should return all poolMembers defined in enqueued response");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/poolMember");
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }

   @Test
   public void testListPoolMembers_WithPagination() throws Exception {
      server.enqueue(jsonResponse("/vip/poolMembers_page2.json"));
      PaginatedCollection<PoolMember> poolMembers = api.getPoolMemberApi().listPoolMembers(
            new PaginationOptions().pageNumber(2).pageSize(1));
      assertEquals(poolMembers.size(), 1, "should only return 2nd element");
      assertEquals(poolMembers.get(0).id(), "poolMemberIdFrom2ndPage", "Should return 2nd element (from 2nd page).");

      Uris.UriBuilder uriBuilder = getBasicApiUri("networkDomainVip/poolMember");
      uriBuilder.addQuery("pageNumber", Integer.toString(2));
      uriBuilder.addQuery("pageSize", Integer.toString(1));
      addDatacenterFilters(uriBuilder);
      assertSent(HttpMethod.GET, uriBuilder.toString());
   }
}
