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
package org.jclouds.dimensiondata.cloudcontrol.compute.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Volume;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.CpuSpeed;
import org.jclouds.dimensiondata.cloudcontrol.domain.Floppy;
import org.jclouds.dimensiondata.cloudcontrol.domain.Guest;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeController;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.NIC;
import org.jclouds.dimensiondata.cloudcontrol.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataController;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiController;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.ServerSource;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

@Test(groups = "unit", testName = "ServerToHardwareTest")
public class ServerToHardwareTest {

   private ServerToHardware serverToHardware;

   @BeforeMethod
   public void setUp() throws Exception {
      serverToHardware = new ServerToHardware();
   }

   @Test
   public void testApplyServer() {

      List ideDisksOrDevices = Lists.newArrayList(
            IdeDisk.builder().id("98299851-37a3-4ebe-9cf1-090da9ae42a0").slot("0").sizeGb(10).speed("ECONOMY").state(State.NORMAL).build(),
            IdeDevice.builder().id("98299852-37a3-4ebe-9cf1-090da9ae42a1").slot("1").sizeGb(1).fileName("WIN8DE").state(State.NORMAL)
                  .type("DVD").build());

      List sataDisksOrDevices = Lists.newArrayList(
            SataDisk.builder().id("98299853-37a3-4ebe-9cf1-090da9ae42a2").sataId("0").sizeGb(20).speed("STANDARD").state(State.NORMAL)
                  .build(),
            SataDevice.builder().id("98299854-37a3-4ebe-9cf1-090da9ae42a3").sataId("1").sizeGb(1).fileName("WIN10CE").state(State.NORMAL)
                  .type("DVD").build());

      List scsiDisks = Lists.newArrayList(
            ScsiDisk.builder().id("98299855-37a3-4ebe-9cf1-090da9ae42a4").scsiId("0").sizeGb(30).speed("HIGHPERFORMANCE").state(State.NORMAL)
                  .build());

      final Server server = Server.builder().id("12ea8472-6e4e-4068-b2cb-f04ecacd3962").name("CentOS 5 64-bit")
            .description("DRaaS CentOS Release 5.9 64-bit").guest(Guest.builder().osCustomization(false)
                  .operatingSystem(
                        OperatingSystem.builder().id("CENTOS564").displayName("CENTOS5/64").family("UNIX").build())
                  .build()).cpu(CPU.builder().count(1).speed("STANDARD").coresPerSocket(1).build()).memoryGb(4)
            .networkInfo(NetworkInfo.builder().primaryNic(
                  NIC.builder().id("def96a04-d1ee-48b9-b07d-3993594724d2").privateIpv4("192.168.1.2")
                        .vlanId("19737c24-259a-49e2-a5b7-a8a042a96108").build())
                  .additionalNic(Lists.<NIC>newArrayList()).networkDomainId("testNetworkDomain").build())
            .ideControllers(ImmutableList
                  .of(IdeController.builder().id("def96a00-d1ee-48b9-b07d-3993594724d0").adapterType("IDE_CONTROLLER_XXX").channel(0).key(3001)
                        .deviceOrDisks(ideDisksOrDevices).virtualControllerId(3001).state(State.NORMAL).build()))
            .sataControllers(ImmutableList
                  .of(SataController.builder().id("def96a01-d1ee-48b9-b07d-3993594724d1").adapterType("AHCI_CONTROLLER").busNumber(0).key(15000)
                        .deviceOrDisks(sataDisksOrDevices).virtualControllerId(15000).state(State.NORMAL).build()))
            .scsiControllers(ImmutableList
                              .of(ScsiController.builder().id("def96a02-d1ee-48b9-b07d-3993594724d2").adapterType("BUS_LOGIC").busNumber(0).key(1000)
                                    .disks(scsiDisks).virtualControllerId(1000).state(State.NORMAL).build()))
            .floppies(ImmutableList.of(Floppy.builder().id("def96a03-d1ee-48b9-b07d-3993594724d3").driveNumber(0).key(8000).fileName("floppy.flp").sizeGb(1).state(State.NORMAL).build()))
            .softwareLabels(Lists.newArrayList())
            .createTime(DatatypeConverter.parseDateTime("2016-06-09T17:36:31.000Z").getTime()).datacenterId("EU6")
            .state(State.NORMAL).source(ServerSource.builder().type("IMAGE_ID").value("1806fe4a-0400-46ad-a6ab-1fe3c9ebc947").build()).started(false).deployed(true)
            .build();
      applyAndAssert(server);
   }

   private void applyAndAssert(Server server) {
      final Hardware hardware = serverToHardware.apply(server);
      assertEquals(server.memoryGb() * 1024, hardware.getRam());
      assertEquals("vmx", hardware.getHypervisor());
      assertEquals(server.id(), hardware.getId());
      assertEquals(server.id(), hardware.getProviderId());
      assertEquals(server.name(), hardware.getName());

      assertEquals(6, hardware.getVolumes().size());
      assertEquals(10F, hardware.getVolumes().get(0).getSize());
      assertEquals(Volume.Type.LOCAL, hardware.getVolumes().get(0).getType());
      assertEquals("98299851-37a3-4ebe-9cf1-090da9ae42a0", hardware.getVolumes().get(0).getId());
      assertEquals("3001:0", hardware.getVolumes().get(0).getDevice());
      assertEquals("8000:0", hardware.getVolumes().get(5).getDevice());
      assertEquals(server.cpu().count(), hardware.getProcessors().size());
      assertEquals((double) server.cpu().coresPerSocket(), hardware.getProcessors().get(0).getCores());
      assertEquals(CpuSpeed.STANDARD.getSpeed(), hardware.getProcessors().get(0).getSpeed());
   }

}
