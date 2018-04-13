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
public abstract class IdeDevice extends AbstractDrive{

   public abstract Integer slot();

   public abstract String type();

   @Nullable
   public abstract String fileName();

   public abstract IdeDevice.Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract IdeDevice.Builder id(String id);

      public abstract IdeDevice.Builder slot(Integer slot);

      public abstract IdeDevice.Builder type(String type);

      public abstract IdeDevice.Builder sizeGb(Integer sizeGb);

      public abstract IdeDevice.Builder fileName(String fileName);

      public abstract IdeDevice.Builder state(String state);

      public abstract IdeDevice build();
   }

   public static IdeDevice.Builder builder() {
      return new AutoValue_IdeDevice.Builder();
   }

   @SerializedNames({"id", "slot", "type", "sizeGb", "fileName", "state" })
   public static IdeDevice create(String id, Integer slot, String type, Integer sizeGb, String fileName, String state) {
      return builder().id(id).slot(slot).type(type).sizeGb(sizeGb).fileName(fileName).state(state).build();
   }
}
