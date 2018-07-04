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
package org.jclouds.dimensiondata.cloudcontrol.domain.vip;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class CreateVirtualListener {
   CreateVirtualListener() {
   }

   public static Builder builder() {
      return new AutoValue_CreateVirtualListener.Builder();
   }

   @SerializedNames({
         "networkDomainId",
         "name",
         "description",
         "type",
         "protocol",
         "listenerIpAddress",
         "port",
         "enabled",
         "connectionLimit",
         "connectionRateLimit",
         "sourcePortPreservation",
         "poolId",
         "clientClonePoolId",
         "persistenceProfileId",
         "fallbackPersistenceProfileId",
         "optimizationProfile",
         // "sslOffloadProfileId",
         "iruleId"
   })
   public static CreateVirtualListener create(
         String networkDomainId,
         String name,
         @Nullable String description,
         VirtualListener.Type type,
         VirtualListener.Protocol protocol,
         String listenerIpAddress,
         @Nullable Integer port,
         boolean enabled,
         int connectionLimit,
         int connectionRateLimit,
         VirtualListener.SourcePortPreservation sourcePortPreservation,
         @Nullable String poolId,
         @Nullable String clientClonePoolId,
         @Nullable String persistenceProfileId,
         @Nullable String fallbackPersistenceProfileId,
         @Nullable VirtualListener.OptimizationProfile optimizationProfile,
         // @Nullable String sslOffloadProfileId,
         List<String> iruleIds
   ) {

      return builder()
            .networkDomainId(networkDomainId)
            .name(name)
            .description(description)
            .type(type)
            .protocol(protocol)
            .listenerIpAddress(listenerIpAddress)
            .port(port)
            .enabled(enabled)
            .connectionLimit(connectionLimit)
            .connectionRateLimit(connectionRateLimit)
            .sourcePortPreservation(sourcePortPreservation)
            .poolId(poolId)
            .clientClonePoolId(clientClonePoolId)
            .persistenceProfileId(persistenceProfileId)
            .fallbackPersistenceProfileId(fallbackPersistenceProfileId)
            .optimizationProfile(optimizationProfile)
            // .sslOffloadProfileIdsslOffloadProfileId)
            .iruleIds(iruleIds)
            .build();
   }

   public abstract String networkDomainId();

   public abstract String name();

   @Nullable
   public abstract String description();

   public abstract VirtualListener.Type type();

   public abstract VirtualListener.Protocol protocol();

   public abstract String listenerIpAddress();

   @Nullable
   public abstract Integer port();

   public abstract boolean enabled();

   public abstract int connectionLimit();

   public abstract int connectionRateLimit();

   public abstract VirtualListener.SourcePortPreservation sourcePortPreservation();

   @Nullable
   public abstract String poolId();

   @Nullable
   public abstract String clientClonePoolId();

   @Nullable
   public abstract String persistenceProfileId();

   @Nullable
   public abstract String fallbackPersistenceProfileId();

   @Nullable
   public abstract VirtualListener.OptimizationProfile optimizationProfile();

   //   @Nullable
   //   public abstract String sslOffloadProfileId();

   public abstract List<String> iruleIds();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder name(String name);

      @Nullable
      public abstract Builder description(String description);

      public abstract Builder type(VirtualListener.Type type);

      public abstract Builder protocol(VirtualListener.Protocol protocol);

      public abstract Builder listenerIpAddress(String listenerIpAddress);

      @Nullable
      public abstract Builder port(Integer port);

      public abstract Builder enabled(boolean enabled);

      public abstract Builder connectionLimit(int connectionLimit);

      public abstract Builder connectionRateLimit(int connectionRateLimit);

      public abstract Builder sourcePortPreservation(VirtualListener.SourcePortPreservation sourcePortPreservation);

      @Nullable
      public abstract Builder poolId(String poolId);

      @Nullable
      public abstract Builder clientClonePoolId(String clientClonePoolId);

      @Nullable
      public abstract Builder persistenceProfileId(String persistenceProfileId);

      @Nullable
      public abstract Builder fallbackPersistenceProfileId(String fallbackPersistenceProfileId);

      @Nullable
      public abstract Builder optimizationProfile(VirtualListener.OptimizationProfile optimizationProfile);

//      @Nullable
//      public abstract Builder sslOffloadProfileId(String sslOffloadProfileId);

      public abstract Builder iruleIds(List<String> iruleIds);

      public abstract List<String> iruleIds();

      abstract CreateVirtualListener autoBuild();

      public CreateVirtualListener build() {
         iruleIds(iruleIds() == null ? null : ImmutableList.copyOf(iruleIds()));
         return autoBuild();
      }
   }
}
