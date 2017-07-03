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
package org.jclouds.dimensiondata.cloudcontrol.features;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jclouds.dimensiondata.cloudcontrol.domain.Disk;
import org.jclouds.dimensiondata.cloudcontrol.domain.NIC;
import org.jclouds.dimensiondata.cloudcontrol.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.options.CloneServerOptions;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.waitForServerState;
import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.waitForServerStatus;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "ServerApiLiveTest", singleThreaded = true)
public class ServerApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   private String serverId;
   private String imageId;

   @Test(dependsOnMethods = "testDeployAndStartServer")
   public void testListServers() {
      List<Server> servers = api().listServers().concat().toList();
      assertNotNull(servers);
      for (Server s : servers) {
         assertNotNull(s);
      }
      // TODO assert that serverId is contained.
   }

   @Test
   public void testDeployAndStartServer() {
      Boolean started = Boolean.TRUE;
      NetworkInfo networkInfo = NetworkInfo.create("690de302-bb80-49c6-b401-8c02bbefb945",
            NIC.builder().vlanId("6b25b02e-d3a2-4e69-8ca7-9bab605deebd").build(), Lists.<NIC>newArrayList());
      List<Disk> disks = ImmutableList.of(Disk.builder().scsiId(0).speed("STANDARD").build());
      serverId = api()
            .deployServer(ServerApiLiveTest.class.getSimpleName(), "4c02126c-32fc-4b4c-9466-9824c1b5aa0f", started,
                  networkInfo, "P$$ssWwrrdGoDd!", disks, null);
      assertNotNull(serverId);
      waitForServerStatus(api(), serverId, true, true, 30 * 60 * 1000, "Error");
   }

   @Test(dependsOnMethods = "testDeployAndStartServer")
   public void testPowerOffServer() {
      api().powerOffServer(serverId);
      waitForServerStatus(api(), serverId, false, true, 30 * 60 * 1000, "Error");
   }

   @Test
   public void testCloneServer() {
      CloneServerOptions options = CloneServerOptions.builder().clusterId("").description("")
            .guestOsCustomization(false).build();

      imageId = api().cloneServer(serverId, "ServerApiLiveTest", options);
      assertNotNull(imageId);
   }

   @AfterClass
   public void testDeleteServer() {
      if (serverId != null) {
         api().deleteServer(serverId);
         waitForServerState(api(), serverId, State.DELETED, 30 * 60 * 1000, "Error");
      }
   }

   private ServerApi api() {
      return api.getServerApi();
   }

}
