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
public abstract class OsImage extends BaseImage {
   public static final String TYPE = "OS_IMAGE";

   OsImage() {
      type = TYPE;
   }

   @Nullable
   public abstract String osImageKey();

   @SerializedNames({ "id", "name", "description", "cluster", "guest", "datacenterId", "cpu", "memoryGb", "nic",
         "softwareLabel", "createTime", "osImageKey", "scsiController", "sataController", "ideController", "floppies" })
   public static OsImage create(String id, String name, String description, Cluster cluster, Guest guest,
         String datacenterId, CPU cpu, int memoryGb, List<ImageNic> nics, List<Object> softwareLabels, Date createTime,
         String osImageKey, List<ScsiController> scsiControllers, List<SataController> sataControllers,
         List<IdeController> ideControllers, List<Floppy> floppies) {
      return builder().id(id).name(name).description(description).cluster(cluster).guest(guest)
            .datacenterId(datacenterId).osImageKey(osImageKey).createTime(createTime).cpu(cpu).memoryGb(memoryGb)
            .nics(nics).scsiControllers(scsiControllers).sataControllers(sataControllers).ideControllers(ideControllers)
            .softwareLabels(softwareLabels).build();
   }

   public abstract Builder toBuilder();

   public static Builder builder() {
      return new AutoValue_OsImage.Builder();
   }

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder id(String id);

      public abstract Builder name(String name);

      public abstract Builder description(String description);

      public abstract Builder cluster(Cluster cluster);

      public abstract Builder guest(Guest guest);

      public abstract Builder datacenterId(String datacenterId);

      public abstract Builder osImageKey(String osImageKey);

      public abstract Builder createTime(Date createTime);

      public abstract Builder cpu(CPU cpu);

      public abstract Builder memoryGb(int memoryGb);

      public abstract Builder nics(List<ImageNic> nics);

      public abstract Builder softwareLabels(List<Object> softwareLabels);

      abstract List<ScsiController> scsiControllers();

      abstract List<SataController> sataControllers();

      abstract List<IdeController> ideControllers();

      abstract List<Floppy> floppies();

      public abstract OsImage.Builder scsiControllers(List<ScsiController> scsiControllers);

      public abstract OsImage.Builder sataControllers(List<SataController> sataControllers);

      public abstract OsImage.Builder ideControllers(List<IdeController> ideControllers);

      public abstract OsImage.Builder floppies(List<Floppy> floppies);

      abstract OsImage autoBuild();

      abstract List<Object> softwareLabels();

      abstract List<ImageNic> nics();

      public OsImage build() {
         scsiControllers(scsiControllers() != null ? ImmutableList.copyOf(scsiControllers()) : ImmutableList.<ScsiController>of());
         sataControllers(sataControllers() != null ? ImmutableList.copyOf(sataControllers()) : ImmutableList.<SataController>of());
         ideControllers(ideControllers() != null ? ImmutableList.copyOf(ideControllers()) : ImmutableList.<IdeController>of());
         floppies(floppies() != null ? ImmutableList.copyOf(floppies()) : ImmutableList.<Floppy>of());
         softwareLabels(softwareLabels() != null ? ImmutableList.copyOf(softwareLabels()) : ImmutableList.of());
         nics(nics() != null ? ImmutableList.copyOf(nics()) : ImmutableList.<ImageNic>of());
         return autoBuild();
      }
   }
}
