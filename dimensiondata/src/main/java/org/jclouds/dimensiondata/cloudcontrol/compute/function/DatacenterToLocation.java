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
package org.jclouds.dimensiondata.cloudcontrol.compute.function;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jclouds.dimensiondata.cloudcontrol.domain.Datacenter;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.inject.internal.util.$Iterables.getOnlyElement;
import static com.google.inject.internal.util.$Preconditions.checkNotNull;

@Singleton
public class DatacenterToLocation implements Function<Datacenter, Location> {

   private final JustProvider justProvider;

   @Inject
   public DatacenterToLocation(JustProvider justProvider) {
      this.justProvider = checkNotNull(justProvider, "justProvider");
   }

   @Override
   public Location apply(final Datacenter datacenter) {
      return new LocationBuilder().id(datacenter.id()).description(datacenter.displayName())
            .parent(getOnlyElement(justProvider.get())).scope(LocationScope.ZONE)
            .iso3166Codes(ImmutableSet.<String>of()).metadata(
                  ImmutableMap.<String, Object>of("name", datacenter.displayName(), "city", datacenter.city(), "state",
                        datacenter.state(), "country", datacenter.country())).build();
   }

}
