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
package org.jclouds.dimensiondata.cloudcontrol.compute;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.Volume;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.compute.function.ServerToServerWithExternalIp;
import org.jclouds.dimensiondata.cloudcontrol.compute.functions.CleanupServer;
import org.jclouds.dimensiondata.cloudcontrol.compute.options.DimensionDataCloudControlTemplateOptions;
import org.jclouds.dimensiondata.cloudcontrol.domain.Account;
import org.jclouds.dimensiondata.cloudcontrol.domain.BaseImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.CpuSpeed;
import org.jclouds.dimensiondata.cloudcontrol.domain.Datacenter;
import org.jclouds.dimensiondata.cloudcontrol.domain.Disk;
import org.jclouds.dimensiondata.cloudcontrol.domain.FirewallRuleTarget;
import org.jclouds.dimensiondata.cloudcontrol.domain.IpRange;
import org.jclouds.dimensiondata.cloudcontrol.domain.NIC;
import org.jclouds.dimensiondata.cloudcontrol.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontrol.domain.OsImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.Placement;
import org.jclouds.dimensiondata.cloudcontrol.domain.internal.ServerWithExternalIp;
import org.jclouds.dimensiondata.cloudcontrol.domain.options.CloneServerOptions;
import org.jclouds.dimensiondata.cloudcontrol.domain.options.CreateServerOptions;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.location.Zone;
import org.jclouds.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static org.jclouds.compute.reference.ComputeServiceConstants.COMPUTE_LOGGER;
import static org.jclouds.dimensiondata.cloudcontrol.config.DimensionDataCloudControlComputeServiceContextModule.SERVER_STARTED_PREDICATE;
import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.generateFirewallRuleName;
import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.generatePortListName;
import static org.jclouds.dimensiondata.cloudcontrol.utils.DimensionDataCloudControlResponseUtils.simplifyPorts;

public class DimensionDataCloudControlServiceAdapter
      implements ComputeServiceAdapter<ServerWithExternalIp, BaseImage, BaseImage, Datacenter> {

   private static final String DEFAULT_LOGIN_PASSWORD = "P$$ssWwrrdGoDd!";
   private static final String DEFAULT_LOGIN_USER = "root";
   public static final String DEFAULT_ACTION = "ACCEPT_DECISIVELY";
   public static final String DEFAULT_IP_VERSION = "IPV4";
   public static final String DEFAULT_PROTOCOL = "TCP";

   @Resource
   @Named(COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final DimensionDataCloudControlApi api;
   private final ComputeServiceConstants.Timeouts timeouts;
   protected final CleanupServer cleanupServer;
   private Predicate<String> provideServerStartedPredicate;
   private Supplier<Set<String>> datacenterIds;

   @Inject
   DimensionDataCloudControlServiceAdapter(final DimensionDataCloudControlApi api,
         final ComputeServiceConstants.Timeouts timeouts, final CleanupServer cleanupServer,
         @Named(SERVER_STARTED_PREDICATE) Predicate<String> provideServerStartedPredicate,
         @Zone Supplier<Set<String>> datacenterIds) {
      this.api = checkNotNull(api, "api");
      this.timeouts = checkNotNull(timeouts, "timeouts");
      this.cleanupServer = checkNotNull(cleanupServer, "cleanupServer");
      this.provideServerStartedPredicate = checkNotNull(provideServerStartedPredicate, "provideServerStartedPredicate");
      this.datacenterIds = checkNotNull(datacenterIds, "datacenterIds");
   }

   @Override
   public NodeAndInitialCredentials<ServerWithExternalIp> createNodeWithGroupEncodedIntoName(String group,
         final String name, Template template) {
      // Infer the login credentials from the VM, defaulting to "root" user
      LoginCredentials.Builder credsBuilder = LoginCredentials.builder().user(DEFAULT_LOGIN_USER)
            .password(DEFAULT_LOGIN_PASSWORD);
      // If login overrides are supplied in TemplateOptions, always prefer those.
      String loginPassword = Objects.firstNonNull(template.getOptions().getLoginPassword(), DEFAULT_LOGIN_PASSWORD);
      if (loginPassword != null) {
         credsBuilder.password(loginPassword);
      }

      String imageId = checkNotNull(template.getImage().getId(), "template image id must not be null");
      Image image = checkNotNull(template.getImage(), "template image must not be null");
      Hardware hardware = checkNotNull(template.getHardware(), "template hardware must not be null");

      DimensionDataCloudControlTemplateOptions templateOptions = DimensionDataCloudControlTemplateOptions.class
            .cast(template.getOptions());

      String networkDomainId = templateOptions.getNetworkDomainId();
      String vlanId = templateOptions.getVlanId();

      NetworkInfo networkInfo = NetworkInfo.create(networkDomainId, NIC.builder().vlanId(vlanId).build(),
            // TODO allow additional NICs - to do this we need more vlans deployed
            Lists.<NIC>newArrayList());

      List<Disk> disks = Lists.newArrayList();
      // TODO add all the volumes as disks
      if (template.getHardware().getVolumes() != null) {
         Volume volume = template.getHardware().getVolumes().get(0);
         disks.add(Disk.builder().scsiId(Integer.valueOf(volume.getDevice())).sizeGb(volume.getSize().intValue())
               .speed("STANDARD").build());
      }

      CreateServerOptions createServerOptions = CreateServerOptions.builder()
            .memoryGb(template.getHardware().getRam() / 1024)
            .cpu(buildCpuFromProcessor(template.getHardware().getProcessors())).build();

      String serverId = api.getServerApi()
            .deployServer(name, imageId, Boolean.TRUE, networkInfo, loginPassword, disks, createServerOptions);

      if (!provideServerStartedPredicate.apply(serverId)) {
         throw new IllegalStateException(
               format("Server(%s) is not ready within %d ms.", serverId, timeouts.nodeRunning));
      }

      if (group != null) {
         //         assignNodeToGroup(group, serverId);
      }

      ServerWithExternalIp.Builder serverWithExternalIpBuilder = ServerWithExternalIp.builder()
            .server(api.getServerApi().getServer(serverId));

      if (templateOptions.isAutoCreateNatRule()) {
         // addPublicIPv4AddressBlock
         String ipBlockId = api.getNetworkApi().addPublicIpBlock(networkDomainId);
         //manageResponse(response, format("Cannot add a publicIpBlock to networkDomainId %s", networkDomainId));
         String externalIp = api.getNetworkApi().getPublicIPv4AddressBlock(ipBlockId).baseIp();

         serverWithExternalIpBuilder.externalIp(externalIp);
         String internalIp = api.getServerApi().getServer(serverId).networkInfo().primaryNic().privateIpv4();
         String natRuleId = api.getNetworkApi().createNatRule(networkDomainId, internalIp, externalIp);
         if (natRuleId.isEmpty()) {
            // rollback
            String natRuleErrorMessage = String.format(
                  "Cannot create a NAT rule for internalIp %s (server %s) using externalIp %s. Rolling back ...",
                  internalIp, serverId, externalIp);
            logger.warn(natRuleErrorMessage);
            destroyNode(serverId);
            throw new IllegalStateException(natRuleErrorMessage);
         }

         List<FirewallRuleTarget.Port> ports = simplifyPorts(templateOptions.getInboundPorts());
         final String portListId = api.getNetworkApi()
               .createPortList(networkDomainId, generatePortListName(serverId), "port list created by jclouds", ports,
                     ImmutableList.<String>of());

         String firewallRuleId = api.getNetworkApi()
               .createFirewallRule(templateOptions.getNetworkDomainId(), generateFirewallRuleName(serverId),
                     DEFAULT_ACTION, DEFAULT_IP_VERSION, DEFAULT_PROTOCOL,
                     FirewallRuleTarget.builder().ip(IpRange.create("ANY", null)).build(),
                     FirewallRuleTarget.builder().ip(IpRange.create(externalIp, null)).portListId(portListId).build(),
                     Boolean.TRUE, Placement.builder().position("LAST").build());
         if (firewallRuleId.isEmpty()) {
            // rollback
            String firewallRuleErrorMessage = String
                  .format("Cannot create a firewall rule %s. Rolling back ...", portListId);
            logger.warn(firewallRuleErrorMessage);
            destroyNode(serverId);
            throw new IllegalStateException(firewallRuleErrorMessage);
         }
      }
      return new NodeAndInitialCredentials<ServerWithExternalIp>(serverWithExternalIpBuilder.build(), serverId,
            credsBuilder.build());
   }

   private CPU buildCpuFromProcessor(final List<? extends Processor> processor) {
      return CPU.builder().count(processor.size())
            .coresPerSocket(Double.valueOf(processor.get(0).getCores()).intValue())
            .speed(CpuSpeed.fromSpeed(processor.get(0).getSpeed()).getDimensionDataSpeed()).build();
   }

   @Override
   public Iterable<BaseImage> listHardwareProfiles() {
      return listImages();
   }

   @Override
   public Iterable<BaseImage> listImages() {
      Collection<BaseImage> osAndCustomerImages = new ArrayList<BaseImage>();
      osAndCustomerImages.addAll(
            api.getServerImageApi().listOsImagesForDatacenterId(datacenterIds.get().iterator().next()).concat()
                  .toList());
      //      osAndCustomerImages.addAll(api.getServerImageApi().listCustomerImages().concat().toList());
      return osAndCustomerImages;
   }

   @Override
   public BaseImage getImage(final String id) {
      final OsImage osImage = api.getServerImageApi().getOsImage(id);
      if (osImage == null) {
         return api.getServerImageApi().getCustomerImage(id);
      }
      return osImage;
   }

   @Override
   public Iterable<Datacenter> listLocations() {
      return api.getInfrastructureApi().listDatacenters().concat().toList();
   }

   @Override
   public ServerWithExternalIp getNode(String id) {
      return new ServerToServerWithExternalIp(api).apply(api.getServerApi().getServer(id));
   }

   @Override
   public void destroyNode(final String serverId) {
      checkState(cleanupServer.apply(serverId), "server(%s) still there after deleting!?", serverId);
   }

   @Override
   public void rebootNode(String id) {
      api.getServerApi().rebootServer(id);
   }

   @Override
   public void resumeNode(String id) {
      api.getServerApi().startServer(id);
   }

   @Override
   public void suspendNode(String id) {
      api.getServerApi().powerOffServer(id);
   }

   @Override
   public Iterable<ServerWithExternalIp> listNodes() {
      return api.getServerApi().listServers().concat().transform(new ServerToServerWithExternalIp(api)).toList();
   }

   public Account getAccount() {
      return api.getAccountApi().getMyAccount();
   }

   public void cloneNode(String id, String newImageName, CloneServerOptions cloneServerOptions) {
      api.getServerApi().cloneServer(id, newImageName, cloneServerOptions);
   }

   //   public void assignNodeToGroup(final String group, String serverId) {
   //      final PaginatedCollection<TagKey> tagKeys = api.getTagApi().listTagKeys(null);
   //      final Optional<TagKey> tagKeyOptional = tagKeys.firstMatch(new Predicate<TagKey>() {
   //         @Override
   //         public boolean apply(TagKey input) {
   //            return input.name().equals(group);
   //         }
   //      });
   //      String tagKeyId;
   //      if (tagKeyOptional.isPresent()) {
   //         tagKeyId = tagKeyOptional.get().id();
   //      } else {
   //         final Response response = api.getTagApi().createTagKey(group, "", false, false);
   //         tagKeyId = DimensionDataCloudControlUtils.tryFindPropertyValue(response, "tagKeyId");
   //      }
   //
   //      api.getTagApi()
   //            .applyTags(serverId, "SERVER", Collections.singletonList(TagInfo.builder().tagKeyId(tagKeyId).build()));
   //   }

   @Override
   public Iterable<ServerWithExternalIp> listNodesByIds(final Iterable<String> ids) {
      return Iterables.filter(listNodes(), new Predicate<ServerWithExternalIp>() {
         @Override
         public boolean apply(final ServerWithExternalIp input) {
            return Iterables.contains(ids, input.server().id());
         }
      });
   }
}
