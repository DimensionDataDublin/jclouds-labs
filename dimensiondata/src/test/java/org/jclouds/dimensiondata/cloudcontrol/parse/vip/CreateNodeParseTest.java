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

import org.jclouds.dimensiondata.cloudcontrol.domain.vip.CreateNode;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Test(groups = "unit")
public class CreateNodeParseTest extends BaseDimensionDataCloudControlParseTest<CreateNode> {

   @Override
   public String resource() {
      return "/vip/createNodeRequest.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public CreateNode expected() {
      return CreateNode.builder()
            .networkDomainId("networkDomainId")
            .name("name")
            .description("description")
            .ipv4Address("10.5.2.14")
            .status(Node.Status.ENABLED)
            .healthMonitorId("healthMonitorId")
            .connectionLimit(100)
            .connectionRateLimit(200)
            .build();
   }
}
