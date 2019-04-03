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
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class ServerSource {

   public ServerSource() {
   }

   public static ServerSource.Builder builder() {
      return new AutoValue_ServerSource.Builder();
   }

   public abstract String type();

   public abstract String value();

   public abstract Builder toBuilder();

   @SerializedNames({ "type", "value" })
   public static ServerSource create(String type, String value) {
      return builder().type(type).value(value).build();
   }

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder type(String id);

      public abstract Builder value(String name);

      abstract ServerSource autoBuild();

      public ServerSource build() {
         return autoBuild();
      }
   }
}
