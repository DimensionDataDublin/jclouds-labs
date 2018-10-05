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
import com.google.common.collect.Lists;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.Cluster;
import org.jclouds.dimensiondata.cloudcontrol.domain.Floppy;
import org.jclouds.dimensiondata.cloudcontrol.domain.Guest;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeController;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.NIC;
import org.jclouds.dimensiondata.cloudcontrol.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataController;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiController;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.ServerSource;
import org.jclouds.dimensiondata.cloudcontrol.domain.Servers;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.VirtualHardware;
import org.jclouds.dimensiondata.cloudcontrol.domain.VmTools;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Test(groups = "unit")
public class ServersParseTest extends BaseDimensionDataCloudControlParseTest<Servers> {

   @Override
   public String resource() {
      return "/servers.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public Servers expected() {
      List<IdeDeviceOrDisk> ideDisksOrDevices = new ArrayList<>();
      ideDisksOrDevices.add(IdeDeviceOrDisk.builder().device(
            IdeDevice.builder().id("fd70be3c-f068-4bbf-a55c-8c776a66ed5a").slot("0").sizeGb(1).fileName("WIN8DE")
                  .state(State.NORMAL).type("DVD").build()).build());
      ideDisksOrDevices.add(IdeDeviceOrDisk.builder()
            .disk(IdeDisk.builder().id("98299851-37a3-4ebe-9cf1-090da9ae42a0").slot("1").sizeGb(10).speed("ECONOMY")
                  .state(State.NORMAL).build()).build());

      List<SataDeviceOrDisk> sataDisksOrDevices = Lists.newArrayList();
      sataDisksOrDevices.add(SataDeviceOrDisk.builder()
            .disk(SataDisk.builder().id("98299853-37a3-4ebe-9cf1-090da9ae42a2").sataId("0").sizeGb(20).speed("STANDARD")
                  .state(State.NORMAL).build()).build());
      sataDisksOrDevices.add(SataDeviceOrDisk.builder().device(
            SataDevice.builder().id("98299854-37a3-4ebe-9cf1-090da9ae42a3").sataId("1").sizeGb(1).fileName("WIN10CE")
                  .state(State.NORMAL).type("DVD").build()).build());

      List<ScsiDisk> scsiDisks = Lists.newArrayList(
            ScsiDisk.builder().id("98299855-37a3-4ebe-9cf1-090da9ae42a4").scsiId("0").sizeGb(30)
                  .speed("HIGHPERFORMANCE").state(State.NORMAL).build());

      List<Server> servers = ImmutableList
            .of(Server.builder().id("b8246ba4-847d-475b-b296-f76787a69ca8").name("parser-test-server-name")
                  .description("parser-test-server-description").datacenterId("NA9").state(State.NORMAL)
                  .cluster(Cluster.builder().id("NA9-01").name("SAP Cluster").build())
                  .source(ServerSource.builder().type("IMAGE_ID").value("1e44ab3f-2426-45ec-a1b5-827b2ce58836").build())
                  .createTime(parseDate("2016-03-10T13:05:21.000Z")).started(true).deployed(true).guest(Guest.builder()
                        .operatingSystem(
                              OperatingSystem.builder().id("CENTOS564").displayName("CENTOS5/64").family("UNIX")
                                    .build()).vmTools(VmTools.builder().apiVersion(9354).type(VmTools.Type.VMWARE_TOOLS)
                              .versionStatus(VmTools.VersionStatus.NEED_UPGRADE)
                              .runningStatus(VmTools.RunningStatus.RUNNING).build()).osCustomization(true).build())
                  .cpu(CPU.builder().count(2).speed("STANDARD").coresPerSocket(1).build()).memoryGb(4).ideControllers(
                        ImmutableList.of(
                              IdeController.builder().id("5bdfc88c-5509-420c-8c5d-cc7876dacfbe")
                                    .adapterType("IDE_CONTROLLER_XXX").channel(0).key(3000)
                                    .state(State.NORMAL).build(),
                              IdeController.builder().id("def96a00-d1ee-48b9-b07d-3993594724d0")
                                    .adapterType("IDE_CONTROLLER_XXX").channel(1).key(3001)
                                    .deviceOrDisks(ideDisksOrDevices).state(State.NORMAL).build())).sataControllers(
                        ImmutableList.of(SataController.builder().id("def96a01-d1ee-48b9-b07d-3993594724d1")
                              .adapterType("AHCI_CONTROLLER").busNumber(0).key(15000).deviceOrDisks(sataDisksOrDevices)
                              .state(State.NORMAL).build())).scsiControllers(ImmutableList
                        .of(ScsiController.builder().id("def96a02-d1ee-48b9-b07d-3993594724d2").adapterType("BUS_LOGIC")
                              .busNumber(0).key(1000).disks(scsiDisks).state(State.NORMAL).build()))
                  .floppies(ImmutableList.of(Floppy.builder().id("afeafa7e-742d-4902-a3f8-67d9bec5c160").driveNumber(0)
                        .key(8000).sizeGb(0).state(State.NORMAL).build()))
                  .networkInfo(
                        NetworkInfo.builder().primaryNic(
                              NIC.builder().id("980a9fdd-4ea2-478b-85b4-f016349f1738").privateIpv4("10.0.0.8")
                                    .ipv6("2607:f480:111:1575:c47:7479:2af8:3f1a")
                                    .vlanId("6b25b02e-d3a2-4e69-8ca7-9bab605deebd")
                                    .vlanId("6b25b02e-d3a2-4e69-8ca7-9bab605deebd").vlanName("vlan1").state("NORMAL")
                                    .build()).additionalNic(null)
                              .networkDomainId("690de302-bb80-49c6-b401-8c02bbefb945").build())
                  .virtualHardware(VirtualHardware.builder().upToDate(false).version("vmx-08").build())
                  .softwareLabels(Collections.emptyList()).build());
      return new Servers(servers, 1, 5, 5, 250);
   }
}
