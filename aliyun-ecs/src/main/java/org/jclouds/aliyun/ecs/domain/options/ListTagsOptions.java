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
package org.jclouds.aliyun.ecs.domain.options;

import org.jclouds.http.options.BaseHttpRequestOptions;

public class ListTagsOptions extends BaseHttpRequestOptions {
   public static final String RESOURCE_ID_PARAM = "ResourceId";
   public static final String RESOURCE_TYPE_PARAM = "ResourceType";

   public ListTagsOptions resourceId(String resourceId) {
      queryParameters.put(RESOURCE_ID_PARAM, resourceId);
      return this;
   }

   public ListTagsOptions resourceType(String resourceType) {
      queryParameters.put(RESOURCE_TYPE_PARAM, resourceType);
      return this;
   }

   public ListTagsOptions paginationOptions(final PaginationOptions paginationOptions) {
      this.queryParameters.putAll(paginationOptions.buildQueryParameters());
      return this;
   }

   public ListTagsOptions tagOptions(final TagOptions tagOptions) {
      this.queryParameters.putAll(tagOptions.buildQueryParameters());
      return this;
   }

   public static final class Builder {

      /**
       * @see {@link ListTagsOptions#resourceId(String)}
       */
      public static ListTagsOptions resourceId(String resourceId) {
         return new ListTagsOptions().resourceId(resourceId);
      }

      /**
       * @see {@link ListTagsOptions#resourceType(String)}
       */
      public static ListTagsOptions resourceType(String resourceType) {
         return new ListTagsOptions().resourceType(resourceType);
      }

      /**
       * @see ListTagsOptions#paginationOptions(PaginationOptions)
       */
      public static ListTagsOptions paginationOptions(PaginationOptions paginationOptions) {
         return new ListTagsOptions().paginationOptions(paginationOptions);
      }

      /**
       * @see ListTagsOptions#tagOptions(TagOptions)
       */
      public static ListTagsOptions tagOptions(TagOptions tagOptions) {
         return new ListTagsOptions().tagOptions(tagOptions);
      }
   }
}
