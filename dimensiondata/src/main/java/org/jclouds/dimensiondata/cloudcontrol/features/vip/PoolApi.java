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
package org.jclouds.dimensiondata.cloudcontrol.features.vip;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.collect.internal.Arg0ToPagedIterable;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pool;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Pools;
import org.jclouds.dimensiondata.cloudcontrol.filters.OrganisationIdFilter;
import org.jclouds.dimensiondata.cloudcontrol.options.DatacenterIdListFilters;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.Transform;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;

@RequestFilters({ BasicAuthentication.class, OrganisationIdFilter.class })
@Consumes(MediaType.APPLICATION_JSON)
@Path("/{jclouds.api-version}/networkDomainVip")
public interface PoolApi {

   // FIXME JCLOUD-90 handle RESOURCE_NOT_FOUND and write Mock test
   @Named("pool:get")
   @GET
   @Path("/pool/{id}")
   @Fallback(Fallbacks.NullOnNotFoundOr404.class)
   Pool getPool(@PathParam("id") String id);

   @Named("pool:list")
   @GET
   @Path("/pool")
   @ResponseParser(ParsePools.class)
   Pools listPools(DatacenterIdListFilters datacenterIdListFilters);

   @Named("pool:list")
   @GET
   @Path("/pool")
   @Transform(ParsePools.ToPagedIterable.class)
   @ResponseParser(ParsePools.class)
   PagedIterable<Pool> listPools();

   final class ParsePools extends ParseJson<Pools> {

      @Inject
      ParsePools(Json json) {
         super(json, TypeLiteral.get(Pools.class));
      }

      private static class ToPagedIterable extends Arg0ToPagedIterable<Pool, ToPagedIterable> {

         private DimensionDataCloudControlApi api;

         @Inject
         ToPagedIterable(DimensionDataCloudControlApi api) {
            this.api = api;
         }

         @Override
         protected Function<Object, IterableWithMarker<Pool>> markerToNextForArg0(final Optional<Object> arg0) {
            return new Function<Object, IterableWithMarker<Pool>>() {
               @Override
               public IterableWithMarker<Pool> apply(Object input) {
                  DatacenterIdListFilters datacenterIdListFilters = arg0.isPresent() ?
                        ((DatacenterIdListFilters) arg0.get()).paginationOptions(PaginationOptions.class.cast(input)) :
                        DatacenterIdListFilters.Builder.paginationOptions(PaginationOptions.class.cast(input));
                  return api.getPoolApi().listPools(datacenterIdListFilters);
               }
            };
         }
      }
   }
}
