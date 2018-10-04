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
package org.jclouds.dimensiondata.cloudcontrol.domain;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class IdeController {

   public abstract String id();

   public abstract int key();

   public abstract int channel();

   public abstract int virtualControllerId();

   public abstract State state();

   public abstract String adapterType();

   public abstract List<Object> deviceOrDisks();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder id(String id);

      public abstract Builder key(int key);

      public abstract Builder channel(int channel);

      public abstract Builder virtualControllerId(int virtualControllerId);

      public abstract Builder state(State state);

      public abstract Builder adapterType(String adapterType);

      public abstract Builder deviceOrDisks(List<Object> deviceOrDisks);

      abstract IdeController autoBuild();

      abstract List<Object> deviceOrDisks();

      public IdeController build() {
         deviceOrDisks(deviceOrDisks() != null ? ImmutableList.copyOf(deviceOrDisks()) : ImmutableList.of());
         return autoBuild();
      }
   }

   public static IdeController.Builder builder() {
      return new AutoValue_IdeController.Builder();
   }

   @SerializedNames({ "id", "key", "channel", "virtualControllerId", "state", "adapterType", "deviceOrDisks" })
   public static IdeController create(String id, int key, int channel, int virtualControllerId, State state,
         String adapterType, List<Object> deviceOrDisks) {
      return builder().id(id).key(key).channel(channel).virtualControllerId(virtualControllerId).state(state)
            .adapterType(adapterType).deviceOrDisks(deviceOrDisks).build();
   }

}
