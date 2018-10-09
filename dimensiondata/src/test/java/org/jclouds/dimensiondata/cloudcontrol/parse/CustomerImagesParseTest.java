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
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.Cluster;
import org.jclouds.dimensiondata.cloudcontrol.domain.CustomerImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.CustomerImages;
import org.jclouds.dimensiondata.cloudcontrol.domain.Guest;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeController;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.ImageNic;
import org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataController;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiController;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.VirtualHardware;
import org.jclouds.dimensiondata.cloudcontrol.domain.VmTools;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class CustomerImagesParseTest extends BaseDimensionDataCloudControlParseTest<CustomerImages> {

   @Override
   public String resource() {
      return "/customerImages.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public CustomerImages expected() {

      List<IdeDeviceOrDisk> ideDisksOrDevices = new ArrayList<>();
      ideDisksOrDevices.add(IdeDeviceOrDisk.builder().device(
            IdeDevice.builder().id("11f3dfc4-06ec-4fe4-837c-2b87d8ac1920").slot("0").sizeGb(1)
                  .fileName("_deviceImage-0.iso").type("CDROM").build()).build());

      List<SataDeviceOrDisk> sataDisksOrDevices = Lists.newArrayList();
      sataDisksOrDevices.add(SataDeviceOrDisk.builder().device(
            SataDevice.builder().id("56ad67b1-5bf9-424c-9c6a-9e3f49decaeb").sataId("0").sizeGb(1).type("CDROM")
                  .fileName("_deviceImage-0.iso").build()).build());
      sataDisksOrDevices.add(SataDeviceOrDisk.builder().device(
            SataDevice.builder().id("e8479b19-ec65-45e8-a70d-24d6115e368c").sataId("1").sizeGb(0).type("CDROM").build())
            .build());

      List<ScsiDisk> scsiDisks = Lists.newArrayList(
            ScsiDisk.builder().id("9202553b-9f5a-4503-b87f-353fbc7e0751").scsiId("0").sizeGb(30).speed("STANDARD")
                  .sizeGb(4).build());

      final CustomerImage customerImage = CustomerImage.builder().id("f27b7ead-9cdc-4cee-be50-8f8e6cec8534")
            .name("CloneForDrs").cluster(Cluster.builder().id("QA1_N2_VMWARE_1-01").name("QA1_N2_VMWARE_1-01").build())
            .cpu(CPU.builder().count(2).speed("STANDARD").coresPerSocket(1).build()).memoryGb(4).scsiControllers(
                  Collections.singletonList(
                        ScsiController.builder().id("23f3237d-6059-4d64-886b-3fa3510546f5").key(1000).disks(scsiDisks)
                              .busNumber(0).adapterType("LSI_LOGIC_PARALLEL").build())).sataControllers(Arrays.asList(
                  SataController.builder().id("ee1cb5bb-65a2-4839-9d81-9e7a71b35915").key(15000).busNumber(0)
                        .adapterType("AHCI").build(),
                  SataController.builder().id("146187c0-3676-452a-a68d-e02e74daab60").key(15001)
                        .deviceOrDisks(sataDisksOrDevices).busNumber(1).adapterType("AHCI").build())).ideControllers(
                  Collections.singletonList(
                        IdeController.builder().id("03530062-1ee3-4c2a-8eaa-1541156cf3f6").key(200).channel(0)
                              .adapterType("IDE").deviceOrDisks(ideDisksOrDevices).build()))
            .createTime(new SimpleDateFormatDateService().iso8601DateParse("2016-07-17T23:53:48.000Z"))
            .datacenterId("QA1_N2_VMWARE_1").state(State.FAILED_ADD).guest(Guest.builder().operatingSystem(
                  OperatingSystem.builder().id("WIN2012DC64").displayName("WIN2012DC/64").family("WINDOWS").build())
                  .vmTools(VmTools.builder().versionStatus(VmTools.VersionStatus.CURRENT)
                        .runningStatus(VmTools.RunningStatus.NOT_RUNNING).apiVersion(9354)
                        .type(VmTools.Type.VMWARE_TOOLS).build()).osCustomization(true).build())
            .virtualHardware(VirtualHardware.builder().version("vmx-08").upToDate(false).build()).tags(ImmutableList
                  .of(CustomerImage.TagWithIdAndName.builder().tagKeyName("DdTest3")
                              .tagKeyId("ee58176e-305b-4ec2-85e0-330a33729a94").build(),
                        CustomerImage.TagWithIdAndName.builder().tagKeyName("Lukas11")
                              .tagKeyId("c5480364-d3cd-4391-9536-5c1af683a8f1").value("j").build(),
                        CustomerImage.TagWithIdAndName.builder().tagKeyName("Lukas5")
                              .tagKeyId("a3e869df-6427-404f-99c2-b50f526369aa").build()))
            .softwareLabels(ImmutableList.of()).nics(ImmutableList.<ImageNic>of()).source(CustomerImage.Source.builder()
                  .artifacts(ImmutableList
                        .of(CustomerImage.Artifact.builder().value("cb4b8674-09a4-4194-9593-9cdc81489de1")
                              .type(CustomerImage.Artifact.Type.SERVER_ID).build()))
                  .type(CustomerImage.Source.Type.CLONE).build()).build();
      assertEquals(customerImage.type, CustomerImage.TYPE, "CustomerImage type is no CUSTOMER_IMAGE");
      List<CustomerImage> customerImages = ImmutableList.of(customerImage);
      return new CustomerImages(customerImages, 1, 2, 2, 250);
   }
}
