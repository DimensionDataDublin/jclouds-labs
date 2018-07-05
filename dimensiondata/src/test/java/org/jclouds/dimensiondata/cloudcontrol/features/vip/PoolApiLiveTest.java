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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreatePool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test(groups = "live", testName = "PoolApiLiveTest", singleThreaded = true)
public class PoolApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   private String networkDomainId;
   private String poolId;

   @BeforeClass
   public void setUp() {
      super.setUp();
      String datacenterId = datacenters.iterator().next();
      String networkDomainName = PoolApiLiveTest.class.getSimpleName() + System.currentTimeMillis();
      networkDomainId = api.getNetworkApi()
            .deployNetworkDomain(datacenterId, networkDomainName, "PoolLiveTest", "ADVANCED");
      assertTrue(networkDomainNormalPredicate.apply(networkDomainId), "Network Domain deploy timed out");
   }

   public void testCreatePool() {
      poolId = api.getPoolApi().createPool(CreatePool.builder()
            .networkDomainId(networkDomainId)
            .name("poolApiLiveTest" + System.currentTimeMillis())
            .description("Pool for log balancing")
            .loadBalanceMethod(Pool.LoadBalanceMethod.OBSERVED_MEMBER)
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .slowRampTime(10)
            .build());
      assertNotNull(poolId, "should return Pool ID");
   }

   @Test(dependsOnMethods = "testCreatePool")
   public void testGetPool() {
      Pool pool = api.getPoolApi().getPool(poolId);
      assertNotNull(pool, "should Get the Pool");
   }

   @Test(dependsOnMethods = "testGetPool")
   public void testListPools() {
      FluentIterable<Pool> pools = api.getPoolApi().listPools().concat();
      assertFalse(pools.isEmpty(), "should List the Pool");
   }

   @Test(dependsOnMethods = "testListPools")
   public void testDeletePool() {
      api.getPoolApi().deletePool(poolId);
   }

   @AfterClass
   public void tearDown() {
      super.tearDown();
      api.getNetworkApi().deleteNetworkDomain(networkDomainId);
   }
}
