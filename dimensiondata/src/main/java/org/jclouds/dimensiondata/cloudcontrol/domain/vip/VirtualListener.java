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
import org.jclouds.dimensiondata.cloudcontrol.domain.State;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class VirtualListener {
   VirtualListener() {
   }

   public static Builder builder() {
      return new AutoValue_VirtualListener.Builder();
   }

   @SerializedNames({
         "id",
         "datacenterId",
         "networkDomainId",
         "name",
         "state",
         "description",
         "createTime",
         "type",
         "protocol",
         "listenerIpAddress",
         "listenerIpAddressability",
         "port",
         "enabled",
         "connectionLimit",
         "connectionRateLimit",
         "sourcePortPreservation",

         "pool",
         "clientClonePool",

         "persistenceProfile",
         "fallbackPersistenceProfile",
         "optimizationProfile",
         //         "sslOffloadProfile",
         "irule"
   })
   public static VirtualListener create(
         String id,
         String datacenterId,
         String networkDomainId,
         String name,
         State state,
         @Nullable String description,
         Date createTime,
         Type type,
         Protocol protocol,
         String listenerIpAddress,
         Addressability listenerIpAddressability,
         @Nullable Integer port,
         boolean enabled,
         int connectionLimit,
         int connectionRateLimit,
         SourcePortPreservation sourcePortPreservation,
         @Nullable PoolSummary pool,
         @Nullable PoolSummary clientClonePool,
         @Nullable PersistenceProfile persistenceProfile,
         @Nullable PersistenceProfile fallbackPersistenceProfile,
         @Nullable OptimizationProfile optimizationProfile,
         //         @Nullable SslOffloadProfile sslOffloadProfile,
         List<Irule> irules
   ) {

      return builder()
            .id(id)
            .datacenterId(datacenterId)
            .networkDomainId(networkDomainId)
            .name(name)
            .state(state)
            .description(description)
            .createTime(createTime)
            .type(type)
            .protocol(protocol)
            .listenerIpAddress(listenerIpAddress)
            .listenerIpAddressability(listenerIpAddressability)
            .port(port)
            .enabled(enabled)
            .connectionLimit(connectionLimit)
            .connectionRateLimit(connectionRateLimit)
            .sourcePortPreservation(sourcePortPreservation)
            .pool(pool)
            .clientClonePool(clientClonePool)
            .persistenceProfile(persistenceProfile)
            .fallbackPersistenceProfile(fallbackPersistenceProfile)
            .optimizationProfile(optimizationProfile)
            //            .sslOffloadProfile(sslOffloadProfile)
            .irules(irules)
            .build();
   }

   public abstract String id();

   public abstract String datacenterId();

   public abstract String networkDomainId();

   public abstract String name();

   public abstract State state();

   @Nullable
   public abstract String description();

   public abstract Date createTime();

   public abstract Type type();

   public abstract Protocol protocol();

   public abstract String listenerIpAddress();

   public abstract Addressability listenerIpAddressability();

   @Nullable
   public abstract Integer port();

   public abstract boolean enabled();

   public abstract int connectionLimit();

   public abstract int connectionRateLimit();

   public abstract SourcePortPreservation sourcePortPreservation();

   @Nullable
   public abstract PoolSummary pool();

   @Nullable
   public abstract PoolSummary clientClonePool();

   @Nullable
   public abstract PersistenceProfile persistenceProfile();

   @Nullable
   public abstract PersistenceProfile fallbackPersistenceProfile();

   @Nullable
   public abstract OptimizationProfile optimizationProfile();

   //   @Nullable
   //   public abstract SslOffloadProfile sslOffloadProfile();

   @Nullable
   public abstract List<Irule> irules();

   public enum Type {
      STANDARD, PERFORMANCE_LAYER_4
   }

   public enum Addressability {
      PRIVATE_RFC1918, PRIVATE_NON_RFC1918, PUBLIC_IP_BLOCK
   }

   public enum Protocol {
      ANY, TCP, UDP, HTTP, FTP, SMTP
   }

   public enum SourcePortPreservation {
      PRESERVE, PRESERVE_STRICT, CHANGE
   }

   public enum OptimizationProfile {
      TCP, LAN_OPT, WAN_OPT, MOBILE_OPT, TCP_LEGACY, SMTP, SIP
   }

   @AutoValue
   public abstract static class PoolSummary {
      @SerializedNames({
            "id",
            "name",
            "loadBalanceMethod",
            "serviceDownAction",
            "slowRampTime",
            "healthMonitor"
      })

      public static PoolSummary create(
            String id,
            String name,
            Pool.LoadBalanceMethod loadBalanceMethod,
            Pool.ServiceDownAction serviceDownAction,
            int slowRampTime,
            @Nullable List<HealthMonitor> healthMonitors
      ) {
         return builder()
               .id(id)
               .name(name)
               .loadBalanceMethod(loadBalanceMethod)
               .serviceDownAction(serviceDownAction)
               .slowRampTime(slowRampTime)
               .healthMonitors(healthMonitors)
               .build();
      }

      public static Builder builder() {
         return new AutoValue_VirtualListener_PoolSummary.Builder();
      }

      public abstract String id();

      public abstract String name();

      public abstract Pool.LoadBalanceMethod loadBalanceMethod();

      public abstract Pool.ServiceDownAction serviceDownAction();

      public abstract int slowRampTime();

      @Nullable
      public abstract List<HealthMonitor> healthMonitors();

      @AutoValue.Builder
      public abstract static class Builder {
         public abstract Builder id(String id);

         public abstract Builder name(String name);

         public abstract Builder loadBalanceMethod(Pool.LoadBalanceMethod loadBalanceMethod);

         public abstract Builder serviceDownAction(Pool.ServiceDownAction serviceDownAction);

         public abstract Builder slowRampTime(int slowRampTime);

         @Nullable
         public abstract Builder healthMonitors(List<HealthMonitor> healthMonitors);

         public abstract PoolSummary build();
      }
   }

   @AutoValue
   public abstract static class PersistenceProfile {
      @SerializedNames({ "id", "name" })

      public static PersistenceProfile create(String id, String name) {
         return builder().id(id).name(name).build();
      }

      public static PersistenceProfile.Builder builder() {
         return new AutoValue_VirtualListener_PersistenceProfile.Builder();
      }

      public abstract String id();

      public abstract String name();

      @AutoValue.Builder
      public abstract static class Builder {

         public abstract PersistenceProfile.Builder id(String id);

         public abstract PersistenceProfile.Builder name(String name);

         public abstract PersistenceProfile build();
      }
   }

   @AutoValue
   public abstract static class Irule {
      @SerializedNames({ "id", "name" })

      public static Irule create(String id, String name) {
         return builder().id(id).name(name).build();
      }

      public static Irule.Builder builder() {
         return new AutoValue_VirtualListener_Irule.Builder();
      }

      public abstract String id();

      public abstract String name();

      @AutoValue.Builder
      public abstract static class Builder {

         public abstract Irule.Builder id(String id);

         public abstract Irule.Builder name(String name);

         public abstract Irule build();
      }
   }

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder id(String id);

      public abstract Builder datacenterId(String datacenterId);

      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder name(String name);

      public abstract Builder state(State state);

      @Nullable
      public abstract Builder description(String description);

      public abstract Builder createTime(Date createTime);

      public abstract Builder type(Type type);

      public abstract Builder protocol(Protocol protocol);

      public abstract Builder listenerIpAddress(String listenerIpAddress);

      public abstract Builder listenerIpAddressability(Addressability listenerIpAddressability);

      @Nullable
      public abstract Builder port(Integer port);

      public abstract Builder enabled(boolean enabled);

      public abstract Builder connectionLimit(int connectionLimit);

      public abstract Builder connectionRateLimit(int connectionRateLimit);

      public abstract Builder sourcePortPreservation(SourcePortPreservation sourcePortPreservation);

      @Nullable
      public abstract Builder pool(PoolSummary pool);

      @Nullable
      public abstract Builder clientClonePool(PoolSummary clientClonePool);

      @Nullable
      public abstract Builder persistenceProfile(PersistenceProfile persistenceProfile);

      @Nullable
      public abstract Builder fallbackPersistenceProfile(PersistenceProfile fallbackPersistenceProfile);

      @Nullable
      public abstract Builder optimizationProfile(OptimizationProfile optimizationProfile);

      //      public abstract Builder sslOffloadProfile(SslOffloadProfile sslOffloadProfile);

      public abstract Builder irules(List<Irule> irules);

      public abstract List<Irule> irules();

      abstract VirtualListener autoBuild();

      public VirtualListener build() {
         irules(irules() == null ? null : ImmutableList.copyOf(irules()));
         return autoBuild();
      }
   }
}
