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
package org.jclouds.dimensiondata.cloudcontrol.options;

import org.jclouds.http.options.BaseHttpRequestOptions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class PaginationOptions extends BaseHttpRequestOptions {

   public static final String PAGE_NUMBER = "pageNumber";
   public static final String ORDER_BY = "orderBy";
   public static final String PAGE_SIZE = "pageSize";

   public PaginationOptions pageNumber(int pageNumber) {
      this.queryParameters.put(PAGE_NUMBER, Integer.toString(pageNumber));
      return this;
   }

   public String pageNumber() {
      return getFirstQueryOrNull(PAGE_NUMBER);
   }

   public PaginationOptions orderBy(String orderBy) {
      this.queryParameters.put(ORDER_BY, checkNotNull(orderBy, ORDER_BY));
      return this;
   }

   public String orderBy() {
      return getFirstQueryOrNull(ORDER_BY);
   }

   public PaginationOptions pageSize(int pageSize) {
      checkState(pageSize >= 0, "pageSize must be >= 0");
      checkState(pageSize <= 10000, "limit must be <= 10000");
      queryParameters.put(PAGE_SIZE, Integer.toString(pageSize));
      return this;
   }

   public String pageSize() {
      return getFirstQueryOrNull(PAGE_SIZE);
   }

   public static class Builder {

      /**
       * @see PaginationOptions#pageNumber(int)
       */
      public static PaginationOptions pageNumber(Integer pageNumber) {
         PaginationOptions options = new PaginationOptions();
         return options.pageNumber(pageNumber);
      }

      /**
       * @see PaginationOptions#pageSize(int)
       */
      public static PaginationOptions pageSize(int pageSize) {
         PaginationOptions options = new PaginationOptions();
         return options.pageSize(pageSize);
      }

      /**
       * @see PaginationOptions#orderBy(String)
       */
      public static PaginationOptions orderBy(String orderBy) {
         PaginationOptions options = new PaginationOptions();
         return options.orderBy(orderBy);
      }
   }
}
