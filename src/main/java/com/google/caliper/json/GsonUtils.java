/*
 * Copyright (C) 2018 Nicolas Winocur
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.caliper.json;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

/**
 * Handles registering any necessary json serializers, deserializers, and/or type 
 * adapters when working with {@link Gson}
 */
public class GsonUtils {
  private GsonUtils() {}
  
  static private final ImmutableSet<TypeAdapterFactory> typeAdapterFactories =
      ImmutableSet.of(new ImmutableListTypeAdapterFactory(), new ImmutableMapTypeAdapterFactory(),
          new NaturallySortedMapTypeAdapterFactory(), new ImmutableMultimapTypeAdapterFactory(),
          new ImmutableSetTypeAdapterFactory());
  
  public static Gson provideGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    typeAdapterFactories.forEach(gsonBuilder::registerTypeAdapterFactory);
    return gsonBuilder.create();
  }
}
