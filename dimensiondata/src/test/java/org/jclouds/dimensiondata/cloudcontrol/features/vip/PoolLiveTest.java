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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreatePool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.Test;

import com.google.common.collect.FluentIterable;

@Test(groups = "live", testName = "PoolApiLiveTest", singleThreaded = true)
public class PoolLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   public void testCreatePools() throws Exception {
      String poolId = api.getPoolApi().createPool(CreatePool.builder()
            .networkDomainId("your-nd-id-here")
            .name("pool1")
            .description("description")
            .loadBalanceMethod(Pool.LoadBalanceMethod.LEAST_CONNECTIONS_MEMBER)
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .healthMonitorIds(
                  ImmutableList.of("hm-id1-here", "hm-id2-here"))
            .slowRampTime(100)
            .build());
      assertNotNull(poolId);
   }

   public void testListPools() throws Exception {
      FluentIterable<Pool> pools = api.getPoolApi().listPools().concat();
      assertFalse(pools.isEmpty());
      for (Pool pool : pools) {
         assertNotNull(pool);
      }
   }

}
