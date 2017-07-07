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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.AddPoolMember;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.PoolMember;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "PoolMemberApiLiveTest", singleThreaded = true)
public class PoolMemberLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   public void testCreatePools() throws Exception {
      String poolId = api.getPoolMemberApi().addPoolMember(AddPoolMember.builder()
            .poolId("your-pool-id-here")
            .nodeId("your-node-id-here")
            .port(12)
            .status(PoolMember.Status.ENABLED)
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
