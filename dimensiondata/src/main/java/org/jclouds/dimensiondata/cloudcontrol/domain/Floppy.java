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
public abstract class Floppy {

   public abstract String id();

   public abstract Integer driveNumber();

   public abstract Integer virtualFloppyId();

   @Nullable
   public abstract Integer sizeGb();

   @Nullable
   public abstract String fileName();

   public abstract String state();

   public abstract Floppy.Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Floppy.Builder id(String id);

      public abstract Floppy.Builder driveNumber(Integer driveNumber);

      public abstract Floppy.Builder virtualFloppyId(Integer virtualFloppyId);

      public abstract Floppy.Builder sizeGb(Integer sizeGb);

      public abstract Floppy.Builder fileName(String fileName);

      public abstract Floppy.Builder state(String state);

      public abstract Floppy build();
   }

   public static Floppy.Builder builder() {
      return new AutoValue_Floppy.Builder();
   }

   @SerializedNames({"id", "driveNumber", "virtualFloppyId", "sizeGb", "fileName", "state" })
   public static Floppy create(String id, Integer driveNumber, Integer virtualFloppyId, Integer sizeGb, String fileName, String state) {
      return builder().id(id).driveNumber(driveNumber).virtualFloppyId(virtualFloppyId).sizeGb(sizeGb).fileName(fileName).state(state).build();
   }
}
