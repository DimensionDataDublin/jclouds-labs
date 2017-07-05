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
import java.util.List;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pool {
   Pool() {
   }

   public static Builder builder() {
      return new AutoValue_Pool.Builder();
   }

   @SerializedNames({
           "networkDomainId",
           "name",
           "description",
           "loadBalanceMethod",
           "healthMonitor",
           "serviceDownAction",
           "slowRampTime",
           "state",
           "createTime",
           "id",
           "datacenterId"})
   public static Pool create(
           String networkDomainId,
           String name,
           @Nullable String description,
           LoadBalanceMethod loadBalanceMethod,
           @Nullable List<HealthMonitor> healthMonitor,
           String serviceDownAction,
           int slowRampTime,
           String state,
           Date createTime,
           String id,
           String datacenterId
   ) {

      return builder()
              .networkDomainId(networkDomainId)
              .name(name)
              .description(description)
              .loadBalanceMethod(loadBalanceMethod)
              .healthMonitor(healthMonitor)
              .serviceDownAction(serviceDownAction)
              .slowRampTime(slowRampTime)
              .state(state)
              .createTime(createTime)
              .id(id)
              .datacenterId(datacenterId)
              .build();
   }

   public abstract String networkDomainId();

   public abstract String name();

   @Nullable
   public abstract String description();

   public abstract LoadBalanceMethod loadBalanceMethod();

   @Nullable
   public abstract List<HealthMonitor> healthMonitor();

   public abstract String serviceDownAction();

   public abstract int slowRampTime();

   public abstract String state();

   public abstract Date createTime();

   public abstract String id();

   public abstract String datacenterId();

   public abstract Builder toBuilder();

   public enum State {
      NORMAL,
      PENDING_ADD, PENDING_CHANGE, PENDING_DELETE,
      FAILED_ADD, FAILED_CHANGE, FAILED_DELETE,
      REQUIRES_SUPPORT
   }

   public enum LoadBalanceMethod {
      ROUND_ROBIN,
      LEAST_CONNECTIONS_MEMBER,
      LEAST_CONNECTIONS_NODE,
      OBSERVED_MEMBER,
      OBSERVED_NODE,
      PREDICTIVE_MEMBER,
      PREDICTIVE_NODE
   }

   @AutoValue.Builder
   interface Builder {
      Builder networkDomainId(String networkDomainId);

      Builder name(String name);

      @Nullable
      Builder description(String description);

      Builder loadBalanceMethod(LoadBalanceMethod loadBalanceMethod);

      @Nullable
      Builder healthMonitor(List<HealthMonitor> healthMonitor);

      Builder serviceDownAction(String serviceDownAction);

      Builder slowRampTime(int slowRampTime);

      Builder state(String state);

      Builder createTime(Date createTime);

      Builder id(String id);

      Builder datacenterId(String datacenterId);

      Pool build();
   }
}
