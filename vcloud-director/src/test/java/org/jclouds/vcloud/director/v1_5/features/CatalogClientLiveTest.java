/*
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import static org.jclouds.vcloud.director.v1_5.domain.Checks.checkTask;
import static org.testng.Assert.assertFalse;

import java.net.URI;

import org.jclouds.vcloud.director.v1_5.domain.OrgList;
import org.jclouds.vcloud.director.v1_5.domain.Reference;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.TasksList;
import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorClientLiveTest;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;

/**
 * Tests live behavior of {@link CatalogClient}.
 * 
 * @author grkvlt@apache.org
 */
@Test(groups = { "live", "apitests" }, testName = "CatalogClientLiveTest")
public class CatalogClientLiveTest extends BaseVCloudDirectorClientLiveTest {

   /*
    * Convenience references to API clients.
    */
  
   private final CatalogClient catalogClient = context.getApi().getCatalogClient();
   private final QueryClient queryClient = context.getApi().getQueryClient();
  
   /*
    * Shared state between dependant tests.
    */
  
   private Reference catalogRef;
   private Catalog catalog;
  
   @Test(testName = "GET /catalog/{id}")
   public void testGetTaskList() {
      catalogRef = null;
      catalog = catalogClient.getCatalog(catalogRef);
   }
}
