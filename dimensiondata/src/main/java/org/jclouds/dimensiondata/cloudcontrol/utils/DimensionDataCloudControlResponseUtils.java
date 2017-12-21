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
package org.jclouds.dimensiondata.cloudcontrol.utils;

import com.google.common.collect.Lists;
import org.jclouds.dimensiondata.cloudcontrol.domain.FirewallRuleTarget;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class DimensionDataCloudControlResponseUtils {

   private static String convertServerId(String serverId) {
      return serverId.replaceAll("-", "_");
   }

   public static String generateFirewallRuleName(String serverId) {
      return String.format("fw.%s", convertServerId(serverId));
   }

   public static List<FirewallRuleTarget.Port> simplifyPorts(int[] ports) {
      if ((ports == null) || (ports.length == 0)) {
         return null;
      }
      List<FirewallRuleTarget.Port> output = Lists.newArrayList();
      Arrays.sort(ports);

      int range_start = ports[0];
      int range_end = ports[0];

      for (int i = 1; i < ports.length; i++) {
         if ((ports[i - 1] == ports[i] - 1) || (ports[i - 1] == ports[i])) {
            // Range continues.
            range_end = ports[i];
         } else {
            // Range ends.
            output.addAll(formatRange(range_start, range_end));
            range_start = ports[i];
            range_end = ports[i];
         }
      }
      // Make sure we get the last range.
      output.addAll(formatRange(range_start, range_end));
      return output;
   }

   // Helper function for simplifyPorts. Formats port range strings.
   public static List<FirewallRuleTarget.Port> formatRange(int range_start, int range_end) {
      List<FirewallRuleTarget.Port> ports = Lists.newArrayList();
      checkArgument(range_start > 0);
      checkArgument(range_end < 65536);
      formatRange(range_start, range_end, ports);
      return ports;
   }

   // Helper function for simplifyPorts.
   public static List<FirewallRuleTarget.Port> formatRange(int range_start, int range_end,
         List<FirewallRuleTarget.Port> ports) {
      int allowed_range = 1024;

      if (range_end > 65535) {
         ports.add(FirewallRuleTarget.Port.create(range_start, 65535));
      } else if (range_end - range_start > allowed_range) {
         while (range_start <= range_end) {
            formatRange(range_start, range_start + allowed_range, ports);
            range_start = range_start + allowed_range + 1;
         }
      } else if (range_end == range_start) {
         ports.add(FirewallRuleTarget.Port.create(range_start, null));
      } else {
         ports.add(FirewallRuleTarget.Port.create(range_start, range_end));
      }
      return ports;
   }

   public static String generatePortListName(String serverId) {
      return String.format("pl.%s", convertServerId(serverId));
   }
}
