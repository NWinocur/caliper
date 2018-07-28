/*
 * Copyright (C) 2018 Nicolas Winocur
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.caliper.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Handles registering any necessary json serializers, deserializers, and/or type adapters when
 * working with {@link Gson}
 */
public final class GsonUtils {

  private static final Logger logger = LoggerFactory.getLogger(GsonUtils.class);

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

  public static void writeJsonStream(OutputStream out, Collection<? extends Object> messages)
      throws IOException {
    Preconditions.checkNotNull(out);
    Preconditions.checkNotNull(messages);
    Gson gson = provideGson();

    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
    writer.setIndent("  ");
    writer.beginArray();
    messages.forEach((message) -> gson.toJson(message, message.getClass(), writer));
    writer.endArray();
    writer.close();
  }

  public static <E extends GsonSerializable> ImmutableCollection<E> readJsonFrom(InputStream in,
      Class<E> type) throws UnsupportedEncodingException {
    Preconditions.checkNotNull(in);
    Preconditions.checkNotNull(type);
    Gson gson = provideGson();
    ArrayList<E> toReturn = new ArrayList<E>();
    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
    try {
      reader.beginArray();
      while (reader.hasNext()) {
        E latest = gson.fromJson(reader, type);

        /*
         * If input file is corrupt but error messages thrown aren't sufficient to narrow down
         * where, can use debug line to get a sense of what's being parsed okay until it hits the
         * problem.
         */
        logger.info("latest serialized object is: " + latest.toString());
        toReturn.add(latest);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ImmutableList.<E>copyOf(toReturn);
  }
}
