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
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.HealthMonitor;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.SslOffloadProfile;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListeners;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.util.Collections;

@Test(groups = "unit")
public class VirtualListenersParseTest extends BaseDimensionDataCloudControlParseTest<VirtualListeners> {

   @Override
   public String resource() {
      return "/vip/virtualListeners.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public VirtualListeners expected() {
      return new VirtualListeners(ImmutableList.of(
            VirtualListener.builder()
                  .networkDomainId("033a97dc-ee9b-4808-97ea-50b06624fd13")
                  .name("PdrnVirtualListener")
                  .state(State.NORMAL)
                  .description("Production Virtual Listener")
                  .createTime(DatatypeConverter.parseDateTime("2017-08-29T02:49:45Z").getTime())
                  .type(VirtualListener.Type.STANDARD)
                  .protocol(VirtualListener.Protocol.TCP)
                  .listenerIpAddress("8.8.8.8")
                  .listenerIpAddressability(VirtualListener.Addressability.PRIVATE_RFC1918)
                  .port(80)
                  .enabled(true)
                  .connectionLimit(2000)
                  .connectionRateLimit(200)
                  .sourcePortPreservation(VirtualListener.SourcePortPreservation.PRESERVE)
                  .pool(VirtualListener.PoolSummary.builder()
                        .id("80276678-58ab-476d-af8a-bbb8f4ce10f6")
                        .name("MyPool")
                        .loadBalanceMethod(Pool.LoadBalanceMethod.LEAST_CONNECTIONS_MEMBER)
                        .serviceDownAction(Pool.ServiceDownAction.RESELECT)
                        .slowRampTime(200)
                        .healthMonitors(ImmutableList.of(
                              HealthMonitor.builder()
                                    .id("01683574-d487-11e4-811f-005056806999")
                                    .name("CCDEFAULT.Http")
                                    .build()))
                        .build())
                  .clientClonePool(VirtualListener.PoolSummary.builder()
                        .id("80276678-58ab-476d-af8a-bbb8f4ce10f8")
                        .name("MyPool2")
                        .loadBalanceMethod(Pool.LoadBalanceMethod.ROUND_ROBIN)
                        .serviceDownAction(Pool.ServiceDownAction.DROP)
                        .slowRampTime(100)
                        .build())
                  .persistenceProfile(VirtualListener.PersistenceProfile.builder()
                        .id("9e6b496d-5261-4542-91aa-b50c7f569c54")
                        .name("source_addr")
                        .build())
                  .fallbackPersistenceProfile(VirtualListener.PersistenceProfile.builder()
                        .id("9e6b496d-5261-4542-91aa-b50c7f569c55")
                        .name("dest_addr")
                        .build())
                  .optimizationProfile(VirtualListener.OptimizationProfile.TCP)
                  .sslOffloadProfile(SslOffloadProfile.builder()
                        .id("9e6b496d-5261-4542-91aa-b50c7f569c55")
                        .name("mySslProfile")
                        .build())
                  .irules(Collections.singletonList(VirtualListener.Irule.builder()
                        .id("9e6b496d-5261-4542-91aa-b50c7f569982")
                        .name("defaultiRule20")
                        .build()))
                  .id("c8c92ea3-2da8-4d51-8153-f39bec794d69")
                  .datacenterId("NA9")
                  .build(),
            VirtualListener.builder()
                  .networkDomainId("443f26b6-2a73-42c3-a66c-6116f11291d1")
                  .name("vl3")
                  .state(State.PENDING_ADD)
                  .createTime(DatatypeConverter.parseDateTime("2017-09-11T13:06:06.000Z").getTime())
                  .type(VirtualListener.Type.PERFORMANCE_LAYER_4)
                  .protocol(VirtualListener.Protocol.ANY)
                  .listenerIpAddress("10.0.1.4")
                  .listenerIpAddressability(VirtualListener.Addressability.PRIVATE_NON_RFC1918)
                  .port(90)
                  .enabled(false)
                  .connectionLimit(3000)
                  .connectionRateLimit(100)
                  .sourcePortPreservation(VirtualListener.SourcePortPreservation.CHANGE)
                  .optimizationProfile(VirtualListener.OptimizationProfile.TCP_LEGACY)
                  .id("d9c01fb4-3eb9-5e62-9064-02acdb805e7a")
                  .datacenterId("NA10")
                  .build()
      ), 1, 2, 2, 250);
   }
}
