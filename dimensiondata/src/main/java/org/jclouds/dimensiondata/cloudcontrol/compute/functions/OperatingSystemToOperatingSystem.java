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
package org.jclouds.dimensiondata.cloudcontrol.compute.functions;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jclouds.compute.domain.OperatingSystem;

import javax.annotation.Nullable;

@Singleton
public class OperatingSystemToOperatingSystem
      implements Function<org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem, OperatingSystem> {

   private final OperatingSystemToOsFamily operatingSystemToOsFamily;

   @Inject
   OperatingSystemToOperatingSystem(final OperatingSystemToOsFamily operatingSystemToOsFamily) {
      this.operatingSystemToOsFamily = operatingSystemToOsFamily;
   }

   @Nullable
   @Override
   public OperatingSystem apply(@Nullable org.jclouds.dimensiondata.cloudcontrol.domain.OperatingSystem from) {

      OperatingSystem.Builder builder = OperatingSystem.builder();
      builder.name(from.displayName());
      builder.family(operatingSystemToOsFamily.apply(from));
      builder.is64Bit(from.id().endsWith("64"));
      builder.description(from.family() + " " + from.displayName());
      return builder.build();
   }
}
