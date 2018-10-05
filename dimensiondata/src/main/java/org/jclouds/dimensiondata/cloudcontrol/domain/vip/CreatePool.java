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
public abstract class CreatePool {
   CreatePool() {
   }

   public static Builder builder() {
      return new AutoValue_CreatePool.Builder();
   }

   @SerializedNames({
         "networkDomainId",
         "name",
         "description",
         "loadBalanceMethod",
         "healthMonitorId",
         "serviceDownAction",
         "slowRampTime" })
   public static CreatePool create(
         String networkDomainId,
         String name,
         @Nullable String description,
         Pool.LoadBalanceMethod loadBalanceMethod,
         @Nullable List<String> healthMonitorIds,
         Pool.ServiceDownAction serviceDownAction,
         int slowRampTime
   ) {
      return builder()
            .networkDomainId(networkDomainId)
            .name(name)
            .description(description)
            .loadBalanceMethod(loadBalanceMethod)
            .healthMonitorIds(healthMonitorIds)
            .serviceDownAction(serviceDownAction)
            .slowRampTime(slowRampTime)
            .build();
   }

   public abstract String networkDomainId();

   public abstract String name();

   @Nullable
   public abstract String description();

   public abstract Pool.LoadBalanceMethod loadBalanceMethod();

   @Nullable
   public abstract List<String> healthMonitorIds();

   public abstract Pool.ServiceDownAction serviceDownAction();

   public abstract int slowRampTime();

   public abstract Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder networkDomainId(String networkDomainId);

      public abstract Builder name(String name);

      @Nullable
      public abstract Builder description(String description);

      public abstract Builder loadBalanceMethod(Pool.LoadBalanceMethod loadBalanceMethod);

      @Nullable
      public abstract Builder healthMonitorIds(List<String> healthMonitorIds);

      public abstract Builder serviceDownAction(Pool.ServiceDownAction serviceDownAction);

      public abstract Builder slowRampTime(int slowRampTime);

      public abstract CreatePool autoBuild();

      abstract List<String> healthMonitorIds();

      public CreatePool build() {
         healthMonitorIds(healthMonitorIds() != null ? ImmutableList.copyOf(healthMonitorIds()) : null);
         return autoBuild();
      }
   }
}
