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
import org.jclouds.dimensiondata.cloudcontrol.domain.FirewallRuleTarget;
import org.jclouds.dimensiondata.cloudcontrol.domain.IpRange;
import org.jclouds.dimensiondata.cloudcontrol.domain.Placement;
import org.jclouds.dimensiondata.cloudcontrol.domain.Response;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlApiLiveTest;
import org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils;
import org.jclouds.rest.ResourceAlreadyExistsException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static ch.qos.logback.core.net.ssl.SSL.DEFAULT_PROTOCOL;
import static org.jclouds.dimensiondata.cloudcontrol.features.NetworkApiMockTest.DEFAULT_ACTION;
import static org.jclouds.dimensiondata.cloudcontrol.features.NetworkApiMockTest.DEFAULT_IP_VERSION;
import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.generateFirewallRuleName;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "NetworkApiLiveTest", singleThreaded = true)
public class NetworkApiLiveTest extends BaseDimensionDataCloudControlApiLiveTest {

   private static final String DATACENTER = "NA9";

   private static final String DEFAULT_PRIVATE_IPV4_BASE_ADDRESS = "10.0.0.0";
   private static final Integer DEFAULT_PRIVATE_IPV4_PREFIX_SIZE = 24;

   private String networkDomainId;
   private String vlanId;
   private String portListId;
   private List<String> firewallRuleIds;

   @BeforeClass
   public void init() {
      firewallRuleIds = Lists.newArrayList();
   }

   @Test(dependsOnMethods = "testDeployVlan")
   public void testCreatePortList() {
      Response response = api()
            .createPortList(networkDomainId, this.getClass().getCanonicalName(), this.getClass().getCanonicalName(),
                  ImmutableList.of(FirewallRuleTarget.Port.create(22, null)), Lists.<String>newArrayList());
      portListId = DimensionDataCloudControlResponseUtils.tryFindInfoPropertyValue(response, "portListId");
   }

   @Test(dependsOnMethods = "testCreatePortList")
   public void testCreateFirewallRuleWithPortList() {
      Response createFirewallRuleResponse = api()
            .createFirewallRule(networkDomainId, generateFirewallRuleName("server-id"), DEFAULT_ACTION,
                  DEFAULT_IP_VERSION, DEFAULT_PROTOCOL,
                  FirewallRuleTarget.builder().ip(IpRange.create("ANY", null)).build(),
                  FirewallRuleTarget.builder().ip(IpRange.create("ANY", null)).portListId(portListId).build(),
                  Boolean.TRUE, Placement.builder().position("LAST").build());
      if (!createFirewallRuleResponse.error().isEmpty()) {
         Assert.fail();
      }
      firewallRuleIds.add(DimensionDataCloudControlResponseUtils
            .tryFindInfoPropertyValue(createFirewallRuleResponse, "firewallRuleId"));
   }

   @Test(dependsOnMethods = "testDeployNetworkDomain")
   public void testDeployVlan() {
      Response deployVlanResponse = api().deployVlan(networkDomainId, NetworkApiLiveTest.class.getSimpleName(),
            NetworkApiLiveTest.class.getSimpleName(), DEFAULT_PRIVATE_IPV4_BASE_ADDRESS,
            DEFAULT_PRIVATE_IPV4_PREFIX_SIZE);
      vlanId = DimensionDataCloudControlResponseUtils.tryFindInfoPropertyValue(deployVlanResponse, "vlanId");
      assertNotNull(vlanId);
   }

   @Test
   public void testDeployNetworkDomain() {
      String networkDomainName = NetworkApiLiveTest.class.getSimpleName();
      Response deployNetworkDomainResponse = api()
            .deployNetworkDomain(DATACENTER, networkDomainName, NetworkApiLiveTest.class.getSimpleName(), "ESSENTIALS");
      networkDomainId = DimensionDataCloudControlResponseUtils
            .tryFindInfoPropertyValue(deployNetworkDomainResponse, "networkDomainId");
      assertNotNull(networkDomainId);
   }

   @Test(expectedExceptions = ResourceAlreadyExistsException.class)
   public void testDeploySameNetworkDomain() {
      api().deployNetworkDomain(DATACENTER, NetworkApiLiveTest.class.getSimpleName(),
            NetworkApiLiveTest.class.getSimpleName(), "ESSENTIALS");
   }

   @Test(dependsOnMethods = "testDeployVlan")
   public void testAddPublicIpBlock() {
      Response addPublicIpBlockResponse = api.getNetworkApi().addPublicIpBlock(networkDomainId);
      String ipBlockId = DimensionDataCloudControlResponseUtils
            .tryFindInfoPropertyValue(addPublicIpBlockResponse, "ipBlockId");
      System.out.println(ipBlockId);
   }

   @AfterClass
   public void tearDown() {
      if (!firewallRuleIds.isEmpty()) {
         for (String firewallRuleId : firewallRuleIds) {
            api().deleteFirewallRule(firewallRuleId);
         }
      }
      if (portListId != null) {
         api().deletePortList(portListId);
      }
      if (vlanId != null) {
         api().deleteVlan(vlanId);
         // TODO wait for deletion
         api().getVlan(vlanId);
      }
      if (networkDomainId != null) {
         api().deleteNetworkDomain(networkDomainId);
      }

   }

   private NetworkApi api() {
      return api.getNetworkApi();
   }

}
