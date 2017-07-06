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
package org.jclouds.dimensiondata.cloudcontrol.parse;

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.HealthMonitor;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pools;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

@Test(groups = "unit")
public class PoolsParseTest extends BaseDimensionDataCloudControlParseTest<Pools> {

   @Override
   public String resource() {
      return "/vip/pools.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public Pools expected() {
      return new Pools(ImmutableList.of(
            Pool.builder()
                        .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                        .name("myDevelopmentPool.1")
                        .description("Pool for load balancing development application servers.")
                        .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
                        .healthMonitors(ImmutableList.of(
                              HealthMonitor.builder()
                                    .id("01683574-d487-11e4-811f-005056806999")
                                    .name("CCDEFAULT.Http")
                                    .build(),
                              HealthMonitor.builder()
                                    .id("0168546c-d487-11e4-811f-005056806999")
                                    .name("CCDEFAULT.Https")
                                    .build()))
                        .serviceDownAction(Pool.ServiceDownAction.RESELECT)
                        .slowRampTime(10)
                        .state(Pool.State.NORMAL)
                        .createTime(DatatypeConverter.parseDateTime("2015-06-04T09:15:07.000Z").getTime())
                        .id("4d360b1f-bc2c-4ab7-9884-1f03ba2768f7")
                        .datacenterId("NA9")
                        .build(),
            Pool.builder()
                        .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                        .name("myDevelopmentPool.2")
                        .loadBalanceMethod(Pool.LoadBalanceMethod.PREDICTIVE_NODE)
                        .serviceDownAction(Pool.ServiceDownAction.DROP)
                        .slowRampTime(11)
                        .state(Pool.State.PENDING_DELETE)
                        .createTime(DatatypeConverter.parseDateTime("2015-06-05T09:15:07.000Z").getTime())
                        .id("5e471c20-cd3d-5bc8-a995-2014cb387908")
                        .datacenterId("NA9")
                        .build()
            ), 1, 2, 2, 250);
   }
}
