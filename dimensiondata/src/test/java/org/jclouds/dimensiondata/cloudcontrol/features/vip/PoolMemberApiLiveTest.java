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

import org.jclouds.dimensiondata.cloudcontrol.domain.vip.AddPoolMember;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateNode;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreatePool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.PoolMember;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test(groups = "live", testName = "PoolMemberApiLiveTest", singleThreaded = true)
public class PoolMemberApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {
   private String networkDomainId;
   private String poolId;
   private String nodeId;
   private String poolMemberId;

   @BeforeClass
   public void setUp() {
      super.setUp();
      String datacenterId = datacenters.iterator().next();
      String networkDomainName = PoolMemberApiLiveTest.class.getSimpleName() + System.currentTimeMillis();
      networkDomainId =
            api.getNetworkApi().deployNetworkDomain(datacenterId, networkDomainName, "VirtualListenerTest", "ADVANCED");
      assertTrue(networkDomainNormalPredicate.apply(networkDomainId), "Network Domain deploy timed out");
      poolId = createSimplePool();
      nodeId = createSimpleNode();
   }

   public void testAddPoolMember() {
      poolMemberId = api.getPoolMemberApi().addPoolMember(AddPoolMember.builder()
            .nodeId(nodeId)
            .poolId(poolId)
            .port(80)
            .status(PoolMember.Status.DISABLED)
            .build());
      assertNotNull(poolMemberId, "should return Pool Member ID");
   }

   @Test(dependsOnMethods = "testAddPoolMember")
   public void testGetPoolMember() {
      PoolMember poolMember = api.getPoolMemberApi().getPoolMember(poolMemberId);
      assertNotNull(poolMember, "should Get the Pool Member");
   }

   @Test(dependsOnMethods = "testGetPoolMember")
   public void testListPoolMembers() {
      List<PoolMember> virtualListeners = api.getPoolMemberApi().listPoolMembers().concat().toList();
      assertFalse(virtualListeners.isEmpty(), "should List the Pool Member");
   }

   @Test(dependsOnMethods = "testListPoolMembers")
   public void testRemovePoolMember() {
      api.getPoolMemberApi().removePoolMember(poolMemberId);
   }

   @AfterClass
   public void tearDown() {
      super.tearDown();
      api.getNodeApi().deleteNode(nodeId);
      api.getPoolApi().deletePool(poolId);
      api.getNetworkApi().deleteNetworkDomain(networkDomainId);
   }

   private String createSimplePool() {
      return api.getPoolApi().createPool(CreatePool.builder()
            .networkDomainId(networkDomainId)
            .name("PoolMemberApiLiveTest" + System.currentTimeMillis())
            .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .slowRampTime(10)
            .build());
   }

   private String createSimpleNode() {
      return api.getNodeApi().createNode(CreateNode.builder()
            .networkDomainId(networkDomainId)
            .name("PoolMemberApiLiveTest" + System.currentTimeMillis())
            .ipv6Address("FF::")
            .status(Node.Status.DISABLED)
            .connectionLimit(100)
            .connectionRateLimit(200)
            .build());
   }
}
