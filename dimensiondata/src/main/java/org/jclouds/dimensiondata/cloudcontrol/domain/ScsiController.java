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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class ScsiController extends AbstractBaseController {

   public abstract Integer busNumber();

   @Nullable
   public abstract List<ScsiDisk> disks();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder id(String id);

      public abstract Builder adapterType(String adapterType);

      public abstract Builder key(Integer key);

      public abstract Builder busNumber(Integer busNumber);

      public abstract Builder virtualControllerId(Integer virtualControllerId);

      public abstract Builder disks(List<ScsiDisk> disks);

      public abstract Builder state(State state);

      abstract ScsiController autoBuild();

      abstract List<ScsiDisk> disks();

      public ScsiController build() {
         disks(disks() != null ? ImmutableList.copyOf(disks()) : null);
         return autoBuild();
      }

   }

   public static ScsiController.Builder builder() {
      return new AutoValue_ScsiController.Builder();
   }

   @SerializedNames({ "id", "key", "busNumber", "virtualControllerId", "state", "adapterType", "disks" })
   public static ScsiController create(String id, Integer key, Integer busNumber, Integer virtualControllerId,
         State state, String adapterType, List<ScsiDisk> disks) {
      return builder().id(id).key(key).busNumber(busNumber).virtualControllerId(virtualControllerId)
            .adapterType(adapterType).state(state).disks(disks).build();
   }
}
