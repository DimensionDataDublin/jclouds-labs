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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class CreateNode {
   CreateNode() {
   }

   public static Builder builder() {
      return new AutoValue_CreateNode.Builder();
   }

   @SerializedNames({
         "networkDomainId",
         "name",
         "description",
         "ipv4Address",
         "ipv6Address",
         "status",
         "healthMonitorId",
         "connectionLimit",
         "connectionRateLimit" })
   public static CreateNode create(
         String networkDomainId,
         String name,
         @Nullable String description,

         // choice, specify either IPv4 or IPv6
         @Nullable String ipv4Address,
         @Nullable String ipv6Address,

         Node.Status status,
         String healthMonitorId,
         int connectionLimit,
         int connectionRateLimit) {

      return builder()
            .networkDomainId(networkDomainId)
            .name(name)
            .description(description)
            .ipv4Address(ipv4Address)
            .ipv6Address(ipv6Address)
            .status(status)
            .healthMonitorId(healthMonitorId)
            .connectionLimit(connectionLimit)
            .connectionRateLimit(connectionRateLimit)
            .build();
   }

   public abstract String networkDomainId();

   public abstract String name();

   @Nullable
   public abstract String description();

   @Nullable
   public abstract String ipv4Address();

   @Nullable
   public abstract String ipv6Address();

   public abstract Node.Status status();

   public abstract String healthMonitorId();

   public abstract int connectionLimit();

   public abstract int connectionRateLimit();

   public abstract Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder name(String name);

      @Nullable
      public abstract Builder description(String description);

      // choice, specify either IPv4 or IPv6
      @Nullable
      public abstract Builder ipv4Address(String ipv4Address);

      @Nullable
      public abstract Builder ipv6Address(String ipv6Address);

      public abstract Builder status(Node.Status status);

      public abstract Builder healthMonitorId(String healthMonitorId);

      public abstract Builder connectionLimit(int connectionLimit);

      public abstract Builder connectionRateLimit(int connectionRateLimit);

      public abstract CreateNode build();
   }

}
