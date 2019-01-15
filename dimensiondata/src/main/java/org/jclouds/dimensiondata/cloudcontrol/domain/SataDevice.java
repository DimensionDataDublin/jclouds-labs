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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class SataDevice {

   public abstract String id();

   public abstract String sataId();

   public abstract String type();

   public abstract int sizeGb();

   @Nullable
   public abstract String fileName();

   @Nullable
   public abstract State state();

   public abstract SataDevice.Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract SataDevice.Builder id(String id);

      public abstract SataDevice.Builder sataId(String sataId);

      public abstract SataDevice.Builder type(String type);

      public abstract SataDevice.Builder sizeGb(int sizeGb);

      public abstract SataDevice.Builder fileName(String fileName);

      public abstract SataDevice.Builder state(State state);

      public abstract SataDevice build();
   }

   public static SataDevice.Builder builder() {
      return new AutoValue_SataDevice.Builder();
   }

   @SerializedNames({ "id", "sataId", "type", "sizeGb", "fileName", "state" })
   public static SataDevice create(String id, String sataId, String type, int sizeGb, String fileName, State state) {
      return builder().id(id).sataId(sataId).type(type).sizeGb(sizeGb).fileName(fileName).state(state).build();
   }
}
