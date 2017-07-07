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

import java.util.Date;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Node {
   Node() {
   }

   public static Builder builder() {
      return new AutoValue_Node.Builder();
   }

   @SerializedNames({
           "id",
           "datacenterId",
           "networkDomainId",
           "name",
           "description",
           "ipv4Address",
           "state",
           "status",
           "healthMonitor",
           "connectionLimit",
           "connectionRateLimit",
           "createTime",
           "loggingEnabled"})
   public static Node create(
           String id,
           String datacenterId,
           String networkDomainId,
           String name,
           @Nullable String description,
           String ipv4Address,
           State state,
           Status status,
           @Nullable HealthMonitor healthMonitor,
           @Nullable Integer connectionLimit,
           @Nullable Integer connectionRateLimit,
           Date createTime,
           @Nullable Boolean loggingEnabled) {

      return builder()
              .id(id)
              .datacenterId(datacenterId)
              .networkDomainId(networkDomainId)
              .name(name)
              .description(description)
              .ipv4Address(ipv4Address)
              .state(state)
              .status(status)
              .healthMonitor(healthMonitor)
              .connectionLimit(connectionLimit)
              .connectionRateLimit(connectionRateLimit)
              .createTime(createTime)
              .loggingEnabled(loggingEnabled)
              .build();
   }

   public abstract String id();

   public abstract String datacenterId();

   public abstract String networkDomainId();

   public abstract String name();

   @Nullable
   public abstract String description();

   public abstract String ipv4Address();

   public abstract State state();

   public abstract Status status();

   @Nullable
   public abstract HealthMonitor healthMonitor();

   @Nullable
   public abstract Integer connectionLimit();

   @Nullable
   public abstract Integer connectionRateLimit();

   public abstract Date createTime();

   @Nullable
   public abstract Boolean loggingEnabled();

   public boolean isEnabled() {
      return status() == Status.ENABLED;
   }

   public abstract Builder toBuilder();

   public enum State {
      NORMAL,
      PENDING_ADD, PENDING_CHANGE, PENDING_DELETE,
      FAILED_ADD, FAILED_CHANGE, FAILED_DELETE,
      REQUIRES_SUPPORT
   }

   public enum Status {
      ENABLED, DISABLED, FORCED_OFFLINE
   }

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder id(String id);

      public abstract Builder datacenterId(String datacenterId);

      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder name(String name);

      @Nullable
      public abstract Builder description(String description);

      public abstract Builder ipv4Address(String ipv4Address);

      public abstract Builder state(State state);

      public abstract Builder status(Status status);

      @Nullable
      public abstract Builder healthMonitor(HealthMonitor healthMonitor);

      @Nullable
      public abstract Builder connectionLimit(Integer connectionLimit);

      @Nullable
      public abstract Builder connectionRateLimit(Integer connectionRateLimit);

      public abstract Builder createTime(Date createTime);

      @Nullable
      public abstract Builder loggingEnabled(Boolean loggingEnabled);

      public abstract Node build();
   }

}
