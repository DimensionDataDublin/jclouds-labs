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
package org.jclouds.dimensiondata.cloudcontrol.parse.vip;

import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateVirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.VirtualListener;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Test(groups = "unit")
public class CreateVirtualListenerParseTest extends BaseDimensionDataCloudControlParseTest<CreateVirtualListener> {

   @Override
   public String resource() {
      return "/vip/createVirtualListenerRequest.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public CreateVirtualListener expected() {
      return CreateVirtualListener.builder()
            .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
            .name("Production.Load.Balancer")
            .description("Used as the load balancer for the production applications.")
            .type(VirtualListener.Type.STANDARD)
            .protocol(VirtualListener.Protocol.TCP)
            .listenerIpAddress("165.180.12.22")
            .port(80)
            .enabled(true)
            .connectionLimit(25000)
            .connectionRateLimit(2000)
            .sourcePortPreservation(VirtualListener.SourcePortPreservation.PRESERVE)
            .poolId("afb1fb1a-eab9-43f4-95c2-36a4cdda6cb8")
            .clientClonePoolId("033a97dc-ee9b-4808-97ea-50b06624fd16")
            .persistenceProfileId("a34ca25c-f3db-11e4-b010-005056806999")
            .fallbackPersistenceProfileId("6f2f5d7b-cdd9-4d84-8ad7-999b64a87978")
            .optimizationProfile(VirtualListener.OptimizationProfile.TCP)
            // .sslOffloadProfile(SslOffloadProfile.builder().....build())
            .iruleIds(Collections.singletonList("2b20abd9-ffdc-11e4-b010-005056806999"))
            .build();
   }
}
