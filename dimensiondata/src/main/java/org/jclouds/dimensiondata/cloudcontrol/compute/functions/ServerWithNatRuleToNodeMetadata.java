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

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.BaseImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.dimensiondata.cloudcontrol.domain.internal.ServerWithExternalIp;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Iterables.find;
import static org.jclouds.location.predicates.LocationPredicates.idEquals;

@Singleton
public class ServerWithNatRuleToNodeMetadata implements Function<ServerWithExternalIp, NodeMetadata> {

   public static final Map<State, NodeMetadata.Status> serverStateToNodeStatus = ImmutableMap.<State, NodeMetadata.Status>builder()
         .put(State.PENDING_DELETE, NodeMetadata.Status.PENDING).put(State.PENDING_CHANGE, NodeMetadata.Status.PENDING)
         .put(State.FAILED_ADD, NodeMetadata.Status.ERROR).put(State.FAILED_CHANGE, NodeMetadata.Status.ERROR)
         .put(State.FAILED_DELETE, NodeMetadata.Status.ERROR).put(State.DELETED, NodeMetadata.Status.TERMINATED)
         .put(State.NORMAL, NodeMetadata.Status.RUNNING).put(State.UNRECOGNIZED, NodeMetadata.Status.UNRECOGNIZED)
         .build();

   private final Supplier<Set<? extends Location>> locations;
   private final GroupNamingConvention nodeNamingConvention;
   private final BaseImageToImage baseImageToImage;
   private final BaseImageToHardware baseImageToHardware;
   private final Map<String, Credentials> credentialStore;
   private final DimensionDataCloudControlApi api;

   @Inject
   ServerWithNatRuleToNodeMetadata(@Memoized final Supplier<Set<? extends Location>> locations,
         final GroupNamingConvention.Factory namingConvention, final BaseImageToImage baseImageToImage,
         final BaseImageToHardware baseImageToHardware, final Map<String, Credentials> credentialStore,
         final DimensionDataCloudControlApi api) {
      this.nodeNamingConvention = checkNotNull(namingConvention, "namingConvention").createWithoutPrefix();
      this.locations = checkNotNull(locations, "locations");
      this.baseImageToImage = checkNotNull(baseImageToImage, "baseImageToImage");
      this.baseImageToHardware = checkNotNull(baseImageToHardware, "osImageToHardware");
      this.credentialStore = checkNotNull(credentialStore, "credentialStore cannot be null");
      this.api = checkNotNull(api, "api cannot be null");
   }

   @Override
   public NodeMetadata apply(final ServerWithExternalIp serverWithExternalIp) {
      NodeMetadataBuilder builder = new NodeMetadataBuilder();
      Server server = serverWithExternalIp.server();
      builder.ids(server.id());
      builder.name(server.name());
      builder.hostname(serverWithExternalIp.server().description());
      if (server.datacenterId() != null) {
         builder.location(find(locations.get(), idEquals(nullToEmpty(server.datacenterId()))));
      }
      builder.group(nodeNamingConvention.groupInUniqueNameOrNull(server.name()));
      BaseImage baseImage = getBaseImage(server.sourceImageId());
      builder.hardware(baseImageToHardware.apply(baseImage));
      Image image = baseImageToImage.apply(baseImage);
      if (image != null) {
         builder.imageId(image.getId());
         builder.operatingSystem(image.getOperatingSystem());
      }
      if (server.state() != null) {
         builder.status(serverStateToNodeStatus.get(server.state()));
      }

      String privateAddress = null;
      if (server.networkInfo() != null && server.networkInfo().primaryNic() != null
            && server.networkInfo().primaryNic().privateIpv4() != null) {
         privateAddress = server.networkInfo().primaryNic().privateIpv4();
         builder.privateAddresses(ImmutableSet.of(privateAddress));
      }
      if (privateAddress != null && serverWithExternalIp.externalIp() != null) {
         builder.publicAddresses(ImmutableSet.of(serverWithExternalIp.externalIp()));
      }

      // DimensionData does not provide a way to get the credentials.
      // Try to return them from the credential store
      Credentials credentials = credentialStore.get("node#" + server.id());
      if (credentials instanceof LoginCredentials) {
         builder.credentials(LoginCredentials.class.cast(credentials));
      }

      return builder.build();
   }

   BaseImage getBaseImage(final String sourceImageId) {
      BaseImage baseImage = api.getServerImageApi().getOsImage(sourceImageId);
      return baseImage == null ? api.getServerImageApi().getCustomerImage(sourceImageId) : baseImage;
   }
}
