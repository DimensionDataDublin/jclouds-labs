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

import com.google.common.collect.ImmutableList;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.PoolMember;
import org.jclouds.dimensiondata.cloudcontrol.domain.vip.PoolMembers;
import org.jclouds.dimensiondata.cloudcontrol.internal.BaseDimensionDataCloudControlParseTest;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Test(groups = "unit")
public class PoolMembersParseTest extends BaseDimensionDataCloudControlParseTest<PoolMembers> {

   @Override
   public String resource() {
      return "/vip/poolMembers.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public PoolMembers expected() {
      return new PoolMembers(ImmutableList.of(
            PoolMember.builder()
                  .id("3dd806a2-c2c8-4c0c-9a4f-5219ea9266c0")
                  .datacenterId("NA9")
                  .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                  .pool(PoolMember.Pool.builder()
                        .id("6f2f5d7b-cdd9-4d84-8ad7-999b64a87978")
                        .name("myDevelopmentPool.1")
                        .build())
                  .node(PoolMember.Node.builder()
                        .id("3c207269-e75e-11e4-811f-005056806999")
                        .name("10.0.3.13")
                        .ipAddress("10.0.3.13")
                        .status(PoolMember.Status.ENABLED)
                        .build())
                  .port(9889)
                  .status(PoolMember.Status.ENABLED)
                  .state(PoolMember.State.NORMAL)
                  .createTime(parseDate("2015-06-09T11:02:50.000Z"))
                  .build(),
            PoolMember.builder()
                  .id("b977578b-a827-4172-b285-030c3ba15daa")
                  .datacenterId("NA9")
                  .networkDomainId("553f26b6-2a73-42c3-a78b-6116f11291d0")
                  .pool(PoolMember.Pool.builder()
                        .id("6f2f5d7b-cdd9-4d84-8ad7-999b64a87978")
                        .name("myDevelopmentPool.1")
                        .build())
                  .node(PoolMember.Node.builder()
                        .id("3c207269-e75e-11e4-811f-005056806999")
                        .name("10.0.3.13")
                        .ipAddress("10.0.3.13")
                        .status(PoolMember.Status.ENABLED)
                        .build())
                  .status(PoolMember.Status.DISABLED)
                  .state(PoolMember.State.PENDING_DELETE)
                  .createTime(parseDate("2015-06-09T10:43:29.000Z"))
                  .build()), 1, 2, 2, 250);
   }
}
