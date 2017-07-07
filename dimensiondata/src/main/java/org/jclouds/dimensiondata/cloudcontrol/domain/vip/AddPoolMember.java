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
public abstract class AddPoolMember {
   AddPoolMember() {
   }

   public static Builder builder() {
      return new AutoValue_AddPoolMember.Builder();
   }

   @SerializedNames({
         "poolId",
         "nodeId",
         "port",
         "status" })
   public static AddPoolMember create(
         String poolId,
         String nodeId,
         @Nullable Integer port,
         PoolMember.Status status) {
      return builder()
            .poolId(poolId)
            .nodeId(nodeId)
            .port(port)
            .status(status)
            .build();
   }

   public abstract String poolId();

   public abstract String nodeId();

   @Nullable
   public abstract Integer port();

   public abstract PoolMember.Status status();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder poolId(String poolId);

      public abstract Builder nodeId(String nodeId);

      @Nullable
      public abstract Builder port(Integer port);

      public abstract Builder status(PoolMember.Status status);

      public abstract AddPoolMember build();
   }

}
