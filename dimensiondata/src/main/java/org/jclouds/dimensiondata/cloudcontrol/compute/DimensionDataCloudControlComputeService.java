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
package org.jclouds.dimensiondata.cloudcontrol.compute;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.callables.RunScriptOnNode;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.extensions.ImageExtension;
import org.jclouds.compute.extensions.SecurityGroupExtension;
import org.jclouds.compute.internal.BaseComputeService;
import org.jclouds.compute.internal.PersistNodeCredentials;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.compute.strategy.CreateNodesInGroupThenAddToSet;
import org.jclouds.compute.strategy.DestroyNodeStrategy;
import org.jclouds.compute.strategy.GetImageStrategy;
import org.jclouds.compute.strategy.GetNodeMetadataStrategy;
import org.jclouds.compute.strategy.InitializeRunScriptOnNodeOrPlaceInBadMap;
import org.jclouds.compute.strategy.ListNodesStrategy;
import org.jclouds.compute.strategy.RebootNodeStrategy;
import org.jclouds.compute.strategy.ResumeNodeStrategy;
import org.jclouds.compute.strategy.SuspendNodeStrategy;
import org.jclouds.dimensiondata.cloudcontrol.compute.functions.CleanupServer;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.scriptbuilder.functions.InitAdminAccess;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class DimensionDataCloudControlComputeService extends BaseComputeService {

   private final CleanupServer cleanupServer;

   @Inject
   DimensionDataCloudControlComputeService(ComputeServiceContext context, Map<String, Credentials> credentialStore,
         Supplier<Set<? extends Image>> images, Supplier<Set<? extends Hardware>> hardwareProfiles,
         Supplier<Set<? extends Location>> locations, ListNodesStrategy listNodesStrategy,
         GetImageStrategy getImageStrategy, GetNodeMetadataStrategy getNodeMetadataStrategy,
         CreateNodesInGroupThenAddToSet runNodesAndAddToSetStrategy, RebootNodeStrategy rebootNodeStrategy,
         DestroyNodeStrategy destroyNodeStrategy, ResumeNodeStrategy resumeNodeStrategy,
         SuspendNodeStrategy suspendNodeStrategy, Provider<TemplateBuilder> templateBuilderProvider,
         Provider<TemplateOptions> templateOptionsProvider, Predicate<AtomicReference<NodeMetadata>> nodeRunning,
         Predicate<AtomicReference<NodeMetadata>> nodeTerminated,
         Predicate<AtomicReference<NodeMetadata>> nodeSuspended,
         InitializeRunScriptOnNodeOrPlaceInBadMap.Factory initScriptRunnerFactory, InitAdminAccess initAdminAccess,
         RunScriptOnNode.Factory runScriptOnNodeFactory, PersistNodeCredentials persistNodeCredentials,
         ComputeServiceConstants.Timeouts timeouts, ListeningExecutorService userExecutor,
         Optional<ImageExtension> imageExtension, Optional<SecurityGroupExtension> securityGroupExtension,
         CleanupServer cleanupServer) {
      super(context, credentialStore, images, hardwareProfiles, locations, listNodesStrategy, getImageStrategy,
            getNodeMetadataStrategy, runNodesAndAddToSetStrategy, rebootNodeStrategy, destroyNodeStrategy,
            resumeNodeStrategy, suspendNodeStrategy, templateBuilderProvider, templateOptionsProvider, nodeRunning,
            nodeTerminated, nodeSuspended, initScriptRunnerFactory, initAdminAccess, runScriptOnNodeFactory,
            persistNodeCredentials, timeouts, userExecutor, imageExtension, securityGroupExtension);
      this.cleanupServer = checkNotNull(cleanupServer, "cleanupServer");
   }

   @Override
   protected void cleanUpIncidentalResourcesOfDeadNodes(Set<? extends NodeMetadata> deadNodes) {
      for (NodeMetadata deadNode : deadNodes) {
         cleanupServer.apply(deadNode.getId());
      }
   }

}