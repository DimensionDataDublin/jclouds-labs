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

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class Server {

   Server() {
   }

   public static Builder builder() {
      return new AutoValue_Server.Builder();
   }

   public abstract String id();

   public abstract String name();

   @Nullable
   public abstract String description();

   public abstract String datacenterId();

   @Nullable
   public abstract Cluster cluster();

   public abstract State state();

   public abstract ServerSource source();

   public abstract Date createTime();

   public abstract Boolean started();

   public abstract Boolean deployed();

   public abstract Guest guest();

   public abstract CPU cpu();

   public abstract int memoryGb();

   @Nullable
   public abstract NetworkInfo networkInfo();

   @Nullable
   public abstract List<ScsiController> scsiControllers();

   @Nullable
   public abstract List<SataController> sataControllers();

   @Nullable
   public abstract List<IdeController> ideControllers();

   @Nullable
   public abstract List<Floppy> floppies();

   @Nullable
   public abstract List<Object> softwareLabels();

   @Nullable
   public abstract Progress progress();

   @Nullable
   public abstract VirtualHardware virtualHardware();

   @SerializedNames({"id", "name", "description", "datacenterId", "cluster", "state", "source", "createTime", "started",
         "deployed", "guest", "cpu", "memoryGb", "networkInfo", "scsiController", "sataController", "ideController",
         "floppy", "softwareLabel", "progress", "virtualHardware"})
   public static Server create(String id, String name, String description, String datacenterId, Cluster cluster, State state,
         ServerSource source, Date createTime, Boolean started, Boolean deployed, Guest guest, CPU cpu, int memoryGb,
         NetworkInfo networkInfo, List<ScsiController> scsiControllers, List<SataController> sataControllers,
         List<IdeController> ideControllers, List<Floppy> floppies, List<Object> softwareLabels, Progress progress,
         VirtualHardware virtualHardware) {
      return builder().id(id).name(name).description(description).datacenterId(datacenterId).cluster(cluster).state(state)
            .source(source).createTime(createTime).started(started).deployed(deployed).guest(guest)
            .cpu(cpu).memoryGb(memoryGb).networkInfo(networkInfo).scsiControllers(scsiControllers)
            .sataControllers(sataControllers).ideControllers(ideControllers).floppies(floppies).softwareLabels(softwareLabels)
            .progress(progress).virtualHardware(virtualHardware).build();
   }

   public abstract Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder id(String id);

      public abstract Builder name(String name);

      public abstract Builder description(String description);

      public abstract Builder datacenterId(String datacenterId);

      public abstract Builder state(State state);

      public abstract Builder source(ServerSource source);

      public abstract Builder createTime(Date createTime);

      public abstract Builder started(Boolean started);

      public abstract Builder deployed(Boolean deployed);

      public abstract Builder guest(Guest guest);

      public abstract Builder cpu(CPU cpu);

      public abstract Builder memoryGb(int memoryGb);

      public abstract Builder networkInfo(NetworkInfo networkInfo);

      public abstract Builder softwareLabels(List<Object> softwareLabels);

      public abstract Builder progress(Progress progress);

      public abstract Builder virtualHardware(VirtualHardware virtualHardware);

      public abstract Builder cluster(Cluster cluster);

      abstract Server autoBuild();

      abstract List<Object> softwareLabels();

      abstract List<ScsiController> scsiControllers();

      abstract List<SataController> sataControllers();

      abstract List<IdeController> ideControllers();

      abstract List<Floppy> floppies();

      public abstract Builder scsiControllers(List<ScsiController> scsiControllers);

      public abstract Builder sataControllers(List<SataController> sataControllers);

      public abstract Builder ideControllers(List<IdeController> ideControllers);

      public abstract Builder floppies(List<Floppy> floppies);

      public Server build() {
         scsiControllers(scsiControllers() != null ? ImmutableList.copyOf(scsiControllers()) : ImmutableList.<ScsiController>of());
         sataControllers(sataControllers() != null ? ImmutableList.copyOf(sataControllers()) : ImmutableList.<SataController>of());
         ideControllers(ideControllers() != null ? ImmutableList.copyOf(ideControllers()) : ImmutableList.<IdeController>of());
         floppies(floppies() != null ? ImmutableList.copyOf(floppies()) : ImmutableList.<Floppy>of());

         softwareLabels(softwareLabels() != null ? ImmutableList.copyOf(softwareLabels()) : ImmutableList.of());

         return autoBuild();
      }
   }
}
