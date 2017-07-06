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
package org.jclouds.dimensiondata.cloudcontrol.parse;

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.HealthMonitor;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Node;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.Nodes;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

@Test(groups = "unit")
public class NodesParseTest extends BaseDimensionDataCloudControlParseTest<Nodes> {

   @Override
   public String resource() {
      return "/vip/nodes.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public Nodes expected() {
      return new Nodes(ImmutableList.of(
            Node.builder()
                  .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                  .name("ProductionNode.2")
                  .description("Production Server 2")
                  .ipv4Address("10.10.10.101")
                  .state(Node.State.NORMAL)
                  .status(Node.Status.ENABLED)
                  .healthMonitor(HealthMonitor.builder()
                        .id("0168b83a-d487-11e4-811f-005056806999")
                        .name("ICMP")
                        .build())
                  .connectionLimit(10000)
                  .connectionRateLimit(2000)
                  .createTime(DatatypeConverter.parseDateTime("2015-05-27T13:56:13.000Z").getTime())
                  .id("34de6ed6-46a4-4dae-a753-2f8d3840c6f9")
                  .datacenterId("NA9")
                  .build(),
            Node.builder()
                  .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                  .name("ProductionNode.3")
                  .description("Production Server 3")
                  .ipv4Address("10.10.10.102")
                  .state(Node.State.PENDING_DELETE)
                  .status(Node.Status.FORCED_OFFLINE)
                  .createTime(DatatypeConverter.parseDateTime("2015-05-28T13:56:14.000Z").getTime())
                  .id("45ef7fe7-57b5-5ebf-b864-309e4951d70a")
                  .datacenterId("NA9")
                  .build()
            ), 1, 2, 2, 250);
   }
}
