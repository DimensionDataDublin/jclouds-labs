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
package org.jclouds.dimensiondata.cloudcontrol;

import com.google.auto.service.AutoService;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.internal.BaseProviderMetadata;

import java.net.URI;
import java.util.Properties;

import static org.jclouds.Constants.PROPERTY_ISO3166_CODES;
import static org.jclouds.compute.config.ComputeServiceProperties.IMAGE_ID;
import static org.jclouds.compute.config.ComputeServiceProperties.TEMPLATE;
import static org.jclouds.location.reference.LocationConstants.ISO3166_CODES;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGION;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGIONS;

/**
 * Implementation of {@link ProviderMetadata} for DimensionData CloudController.
 */
@AutoService(ProviderMetadata.class)
public class DimensionDataCloudControlProviderMetadata extends BaseProviderMetadata {

   public static Builder builder() {
      return new Builder();
   }

   @Override
   public Builder toBuilder() {
      return builder().fromProviderMetadata(this);
   }

   public DimensionDataCloudControlProviderMetadata() {
      super(builder());
   }

   public DimensionDataCloudControlProviderMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = DimensionDataCloudControlApiMetadata.defaultProperties();
      properties.put(TEMPLATE, "osVersionMatches=16,osFamily=UBUNTU,os64Bit=true");
      properties.put(IMAGE_ID, "1f7c4960-527c-4e80-871a-152fe2a416ff");
      properties.setProperty(PROPERTY_REGIONS, "na,eu,au,mea,ap,canada");
      properties.setProperty(PROPERTY_ISO3166_CODES, "NA,EU,AU,AF,AP,CA");
      properties.setProperty(PROPERTY_REGION + ".na." + ISO3166_CODES, "NA");
      properties.setProperty(PROPERTY_REGION + ".eu." + ISO3166_CODES, "EU");
      properties.setProperty(PROPERTY_REGION + ".au." + ISO3166_CODES, "AU");
      properties.setProperty(PROPERTY_REGION + ".mea." + ISO3166_CODES, "AF");
      properties.setProperty(PROPERTY_REGION + ".ap." + ISO3166_CODES, "AP");
      properties.setProperty(PROPERTY_REGION + ".canada." + ISO3166_CODES, "CA");

      return properties;
   }

   public static class Builder extends BaseProviderMetadata.Builder {

      protected Builder() {
         id("dimensiondata-cloudcontrol").name("DimensionData Cloud Control")
               .apiMetadata(new DimensionDataCloudControlApiMetadata())
               .homepage(URI.create("https://na-cloud.dimensiondata.com/"))
               .console(URI.create("https://na-cloud.dimensiondata.com/"))
               .endpoint("https://api-na.dimensiondata.com/caas")
               .defaultProperties(DimensionDataCloudControlProviderMetadata.defaultProperties());
      }

      @Override
      public DimensionDataCloudControlProviderMetadata build() {
         return new DimensionDataCloudControlProviderMetadata(this);
      }

      @Override
      public Builder fromProviderMetadata(ProviderMetadata in) {
         super.fromProviderMetadata(in);
         return this;
      }
   }
}
