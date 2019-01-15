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
public abstract class IdeDisk {

   public abstract String id();

   public abstract String slot();

   public abstract int sizeGb();

   public abstract String speed();

   @Nullable
   public abstract State state();

   public abstract IdeDisk.Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract IdeDisk.Builder id(String id);

      public abstract IdeDisk.Builder slot(String slot);

      public abstract IdeDisk.Builder sizeGb(int sizeGb);

      public abstract IdeDisk.Builder speed(String speed);

      public abstract IdeDisk.Builder state(State state);

      public abstract IdeDisk build();
   }

   public static IdeDisk.Builder builder() {
      return new AutoValue_IdeDisk.Builder();
   }

   @SerializedNames({ "id", "slot", "sizeGb", "speed", "state" })
   public static IdeDisk create(String id, String slot, int sizeGb, String speed, State state) {
      return builder().id(id).slot(slot).sizeGb(sizeGb).speed(speed).state(state).build();
   }
}
