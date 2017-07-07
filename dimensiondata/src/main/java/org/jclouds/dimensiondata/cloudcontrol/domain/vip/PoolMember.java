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

import java.util.Date;

@AutoValue
public abstract class PoolMember {
   PoolMember() {
   }

   public static Builder builder() {
      return new AutoValue_PoolMember.Builder();
   }

   @SerializedNames({
         "id",
         "datacenterId",
         "networkDomainId",
         "pool",
         "node",
         "port",
         "status",
         "state",
         "createTime"})
   public static PoolMember create(
         String id,
         String datacenterId,
         String networkDomainId,
         PoolMember.Pool pool,
         PoolMember.Node node,
         @Nullable Integer port,
         Status status,
         State state,
         Date createTime
   ) {
      return builder()
            .id(id)
            .datacenterId(datacenterId)
            .networkDomainId(networkDomainId)
            .pool(pool)
            .node(node)
            .port(port)
            .status(status)
            .state(state)
            .createTime(createTime)
            .build();
   }

   public abstract String id();

   public abstract String datacenterId();

   public abstract String networkDomainId();

   public abstract PoolMember.Pool pool();

   public abstract PoolMember.Node node();

   @Nullable
   public abstract Integer port();

   public abstract Status status();

   public abstract State state();

   public abstract Date createTime();

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

   @AutoValue
   public abstract static class Pool {
      @SerializedNames({ "id", "name" })
      public static Pool create(String id, String name) {
         return builder().id(id).name(name).build();
      }

      public static Builder builder() {
         return new AutoValue_PoolMember_Pool.Builder();
      }

      public abstract String id();

      public abstract String name();

      @AutoValue.Builder
      public abstract static class Builder {

         public abstract Builder id(String id);

         public abstract Builder name(String name);

         public abstract PoolMember.Pool build();
      }
   }

   @AutoValue
   public abstract static class Node {
      @SerializedNames({ "id", "name", "ipAddress", "status" })
      public static PoolMember.Node create(
            String id,
            String name,
            String ipAddress,
            Status status) {
         return builder()
               .id(id)
               .name(name)
               .ipAddress(ipAddress)
               .status(status)
               .build();
      }

      public static Builder builder() {
         return new AutoValue_PoolMember_Node.Builder();
      }

      public abstract String id();

      public abstract String name();

      public abstract String ipAddress();

      public abstract Status status();

      @AutoValue.Builder
      public abstract static class Builder {
         public abstract Builder id(String id);

         public abstract Builder name(String name);

         public abstract Builder ipAddress(String ipAddress);

         public abstract Builder status(Status status);

         public abstract PoolMember.Node build();
      }
   }

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder id(String id);

      public abstract Builder datacenterId(String datacenterId);

      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder pool(PoolMember.Pool pool);

      public abstract Builder node(PoolMember.Node node);

      @Nullable
      public abstract Builder port(Integer port);

      public abstract Builder status(Status status);

      public abstract Builder state(State state);

      public abstract Builder createTime(Date createTime);

      public abstract PoolMember build();
   }
}
