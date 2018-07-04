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
package org.jclouds.dimensiondata.cloudcontrol.parse.vip;

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreatePool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Test(groups = "unit")
public class CreatePoolParseTest extends BaseDimensionDataCloudControlParseTest<CreatePool> {

   @Override
   public String resource() {
      return "/vip/createPoolRequest.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public CreatePool expected() {
      return CreatePool.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("myDevelopmentPool.1")
            .description("Pool for load balancing development application servers.")
            .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
            .healthMonitorIds(
                  ImmutableList.of("01683574-d487-11e4-811f-005056806999", "0168546c-d487-11e4-811f-005056806999"))
            .serviceDownAction(Pool.ServiceDownAction.RESELECT)
            .slowRampTime(10)
            .build();
   }
}
