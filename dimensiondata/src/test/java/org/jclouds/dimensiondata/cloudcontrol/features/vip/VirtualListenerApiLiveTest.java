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
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateVirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test(groups = "live", testName = "VirtualListenerApiLiveTest", singleThreaded = true)
public class VirtualListenerApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {
   private String networkDomainId;
   private String virtualListenerId;

   @BeforeClass
   public void setUp() {
      super.setUp();
      String datacenterId = datacenters.iterator().next();
      String networkDomainName = VirtualListenerApiLiveTest.class.getSimpleName() + System.currentTimeMillis();
      networkDomainId =
            api.getNetworkApi().deployNetworkDomain(datacenterId, networkDomainName, "VirtualListenerTest", "ADVANCED");
      assertTrue(networkDomainNormalPredicate.apply(networkDomainId), "Network Domain deploy timed out");
   }

   public void testCreateVirtualListener() {
      virtualListenerId = api.getVirtualListenerApi().createVirtualListener(CreateVirtualListener.builder()
            .networkDomainId(networkDomainId)
            .name("VirtualListenerApiLiveTest")
            .type(VirtualListener.Type.STANDARD)
            .protocol(VirtualListener.Protocol.HTTP)
            .listenerIpAddress("165.180.12.22")
            .port(80)
            .enabled(false)
            .connectionLimit(25000)
            .connectionRateLimit(2000)
            .sourcePortPreservation(VirtualListener.SourcePortPreservation.PRESERVE)
            .optimizationProfile(VirtualListener.OptimizationProfile.TCP)
            .build());
      assertNotNull(virtualListenerId);
   }

   @Test(dependsOnMethods = "testCreateVirtualListener")
   public void testGetVirtualListener() {
      VirtualListener virtualListener = api.getVirtualListenerApi().getVirtualListener(virtualListenerId);
      assertNotNull(virtualListener, "should return the Virtual Listener");
   }

   @Test(dependsOnMethods = "testGetVirtualListener")
   public void testListVirtualListeners() {
      FluentIterable<VirtualListener> virtualListeners = api.getVirtualListenerApi().listVirtualListeners().concat();
      assertFalse(virtualListeners.isEmpty(), "should List the Virtual Listener");
   }

   @Test(dependsOnMethods = "testListVirtualListeners")
   public void testDeleteVirtualListener() {
      api.getVirtualListenerApi().deleteVirtualListener(virtualListenerId);
   }

   @AfterClass
   public void tearDown() {
      super.tearDown();
      api.getNetworkApi().deleteNetworkDomain(networkDomainId);
   }
}
