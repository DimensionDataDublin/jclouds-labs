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
package org.jclouds.dimensiondata.cloudcontrol.domain;

import com.google.common.base.CaseFormat;

import static com.google.common.base.Preconditions.checkNotNull;

public enum State {

   NORMAL, FAILED_ADD, FAILED_CHANGE, FAILED_DELETE, PENDING_DELETE, DELETED, UNRECOGNIZED;

   @Override
   public String toString() {
      return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
   }

   public static State fromValue(String state) {
      try {
         return valueOf(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, checkNotNull(state, "state")));
      } catch (IllegalArgumentException e) {
         return UNRECOGNIZED;
      }
   }

   public boolean isFailed() {
      return this == FAILED_ADD || this == FAILED_CHANGE || this == FAILED_DELETE;
   }
}
