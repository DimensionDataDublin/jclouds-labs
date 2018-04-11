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
package org.jclouds.dimensiondata.cloudcontrol.suppliers;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.Datacenter;
import org.jclouds.location.Region;
import org.jclouds.location.suppliers.RegionIdToZoneIdsSupplier;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class ZonesForRegion implements RegionIdToZoneIdsSupplier {

   private final DimensionDataCloudControlApi api;
   private final Supplier<Set<String>> regions;

   @Inject
   ZonesForRegion(DimensionDataCloudControlApi api, @Region Supplier<Set<String>> regions) {
      this.api = api;
      this.regions = regions;
   }

   @Override
   public Map<String, Supplier<Set<String>>> get() {
      Builder<String, Supplier<Set<String>>> regionToZones = ImmutableMap.builder();
      for (String region : regions.get()) {
         ImmutableSet.Builder<String> zones = ImmutableSet.builder();
         for (Datacenter datacenter : api.getInfrastructureApi().listDatacenters().concat()) {
            zones.add(datacenter.id());
         }
         regionToZones.put(region, Suppliers.<Set<String>>ofInstance(zones.build()));
      }
      return regionToZones.build();
   }

}