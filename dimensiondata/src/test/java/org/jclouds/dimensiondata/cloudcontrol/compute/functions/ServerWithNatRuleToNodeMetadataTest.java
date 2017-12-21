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

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.easymock.EasyMock;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.BaseImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.CustomerImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.Guest;
import org.jclouds.dimensiondata.cloudcontrol.domain.NIC;
import org.jclouds.dimensiondata.cloudcontrol.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontrol.domain.OsImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.internal.ServerWithExternalIp;
import org.jclouds.dimensiondata.cloudcontrol.features.ServerImageApi;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.LoginCredentials;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.expect;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test(groups = "unit", testName = "ServerWithNatRuleToNodeMetadataTest")
public class ServerWithNatRuleToNodeMetadataTest {

   private ServerWithNatRuleToNodeMetadata serverWithNatRuleToNodeMetadata;
   private ServerWithExternalIp serverWithExternalIp;

   private GroupNamingConvention nodeNamingConvention;
   private BaseImageToImage baseImageToImage;
   private BaseImageToHardware baseImageToHardware;
   private Map<String, Credentials> credentialStore;
   private DimensionDataCloudControlApi api;
   private ServerImageApi serverImageApi;
   private OsImage osImage;
   private CustomerImage customerImage;
   private Image image;
   private Hardware hardware;
   private OperatingSystem os;
   private NIC nic;
   private Credentials credentials;
   private Location location;
   private CPU cpu;
   private Server server;

   private String imageId = "imageId";
   private String externalIp = "10.20.122.1";
   private String datacenterId = "NA01";
   private String serverName = "serverName";
   private String networkDomainId = "NetworkDomain1";

   @BeforeMethod
   public void setUp() throws Exception {

      location = new LocationBuilder().scope(LocationScope.REGION).id(datacenterId)
            .iso3166Codes(new ArrayList<String>())
            .metadata(ImmutableMap.<String, Object>of("version", "MCP 2.0", "state", "AVAILABLE"))
            .description("locationDescription")
            .parent(new LocationBuilder().id("us").description("USA").scope(LocationScope.PROVIDER).build()).build();
      Supplier<Set<? extends Location>> locations = new Supplier<Set<? extends Location>>() {
         @Override
         public Set<? extends Location> get() {
            return ImmutableSet.of(location);
         }
      };

      nic = EasyMock.createNiceMock(NIC.class);
      cpu = EasyMock.createNiceMock(CPU.class);
      os = EasyMock.createNiceMock(OperatingSystem.class);
      hardware = EasyMock.createNiceMock(Hardware.class);

      GroupNamingConvention.Factory conventionFactory = EasyMock.createNiceMock(GroupNamingConvention.Factory.class);
      nodeNamingConvention = EasyMock.createNiceMock(GroupNamingConvention.class);
      baseImageToImage = EasyMock.createNiceMock(BaseImageToImage.class);
      baseImageToHardware = EasyMock.createNiceMock(BaseImageToHardware.class);
      credentials = LoginCredentials.builder().user("testUser").noPassword().build();
      credentialStore = new HashMap<String, Credentials>();
      api = EasyMock.createNiceMock(DimensionDataCloudControlApi.class);
      serverImageApi = EasyMock.createNiceMock(ServerImageApi.class);
      osImage = EasyMock.createNiceMock(OsImage.class);
      customerImage = EasyMock.createNiceMock(CustomerImage.class);

      expect(conventionFactory.createWithoutPrefix()).andReturn(nodeNamingConvention);
      EasyMock.replay(conventionFactory);

      server = Server.builder().id("serverId").name(serverName).datacenterId(datacenterId)
            .networkInfo(NetworkInfo.create(networkDomainId, nic, new ArrayList<NIC>())).cpu(cpu).deployed(true)
            .state(State.NORMAL).sourceImageId("imageId").started(false).createTime(new Date()).memoryGb(1024)
            .guest(Guest.builder().osCustomization(false).operatingSystem(os).build()).build();

      serverWithNatRuleToNodeMetadata = new ServerWithNatRuleToNodeMetadata(locations, conventionFactory,
            baseImageToImage, baseImageToHardware, credentialStore, api);
   }

   @Test
   public void testApply() {

      serverWithExternalIp = ServerWithExternalIp.create(server, externalIp);
      credentialStore.put("node#" + server.id(), credentials);

      org.jclouds.compute.domain.OperatingSystem operatingSystem = org.jclouds.compute.domain.OperatingSystem.builder()
            .description("Windows 10 x64").name("Win10x64").is64Bit(true).family(OsFamily.WINDOWS).build();

      image = EasyMock.createNiceMock(Image.class);

      expect(api.getServerImageApi()).andReturn(serverImageApi);
      expect(serverImageApi.getOsImage(imageId)).andReturn(osImage);
      expect(baseImageToImage.apply(osImage)).andReturn(image);
      expect(baseImageToHardware.apply(osImage)).andReturn(hardware);
      expect(image.getId()).andReturn("imageId");
      expect(image.getOperatingSystem()).andReturn(operatingSystem);
      expect(nic.privateIpv4()).andReturn("192.168.1.1").anyTimes();
      expect(nodeNamingConvention.groupInUniqueNameOrNull(serverName)).andReturn("[" + serverName + "]").anyTimes();

      EasyMock.replay(nodeNamingConvention, baseImageToImage, baseImageToHardware, api, serverImageApi, image, nic);

      assertNodeMetadata(serverWithNatRuleToNodeMetadata.apply(serverWithExternalIp), operatingSystem,
            serverWithExternalIp.server().sourceImageId(), NodeMetadata.Status.RUNNING,
            ImmutableSet.of(nic.privateIpv4()), ImmutableSet.of(externalIp), credentials);
   }

   @Test(dependsOnMethods = "testApply")
   public void testApplyWithNullables() {

      server = Server.builder().id("serverId").name(serverName).datacenterId(datacenterId)
            .networkInfo(NetworkInfo.create(networkDomainId, nic, new ArrayList<NIC>())).cpu(cpu).deployed(true)
            .state(State.DELETED).sourceImageId("imageId").started(false).createTime(new Date()).memoryGb(1024)
            .guest(Guest.builder().osCustomization(false).operatingSystem(os).build()).build();

      serverWithExternalIp = ServerWithExternalIp.create(server, externalIp);

      image = EasyMock.createNiceMock(Image.class);

      expect(api.getServerImageApi()).andReturn(serverImageApi);
      expect(serverImageApi.getOsImage(imageId)).andReturn(osImage);
      expect(baseImageToImage.apply(osImage)).andReturn(null);
      expect(baseImageToHardware.apply(osImage)).andReturn(hardware);
      expect(image.getId()).andReturn("imageId");
      expect(nic.privateIpv4()).andReturn(null).anyTimes();
      expect(nodeNamingConvention.groupInUniqueNameOrNull(serverName)).andReturn("[" + serverName + "]").anyTimes();

      EasyMock.replay(nodeNamingConvention, baseImageToImage, baseImageToHardware, api, serverImageApi, image, nic);

      assertNodeMetadata(serverWithNatRuleToNodeMetadata.apply(serverWithExternalIp), null, null,
            NodeMetadata.Status.TERMINATED, ImmutableSet.<String>of(), ImmutableSet.<String>of(), null);
   }

   @Test(dependsOnMethods = "testApplyWithNullables")
   public void testGetBaseImage() {
      serverWithExternalIp = ServerWithExternalIp.create(server, externalIp);
      expect(api.getServerImageApi()).andReturn(serverImageApi).anyTimes();
      expect(serverImageApi.getCustomerImage(imageId)).andReturn(customerImage);

      EasyMock.replay(api, serverImageApi, customerImage);

      BaseImage baseImage = serverWithNatRuleToNodeMetadata.getBaseImage(serverWithExternalIp.server().sourceImageId());
      assertNotNull(baseImage);
   }

   private void assertNodeMetadata(NodeMetadata result, org.jclouds.compute.domain.OperatingSystem os, String imageId,
         NodeMetadata.Status status, ImmutableSet<String> privateIpAddresses, ImmutableSet<String> publicIpAddresses,
         Credentials credentials) {
      assertNotNull(result);
      assertEquals(result.getId(), serverWithExternalIp.server().id());
      assertEquals(result.getName(), serverWithExternalIp.server().name());
      assertEquals(result.getHostname(), serverWithExternalIp.server().description());
      assertEquals(result.getGroup(), "[" + serverName + "]");
      assertEquals(result.getHardware(), hardware);
      assertEquals(result.getOperatingSystem(), os);
      assertEquals(result.getLocation(), location);
      assertEquals(result.getImageId(), imageId);
      assertEquals(result.getStatus(), status);
      assertEquals(result.getPrivateAddresses(), privateIpAddresses);
      assertEquals(result.getPublicAddresses(), publicIpAddresses);
      assertEquals(result.getCredentials(), credentials);
   }
}
