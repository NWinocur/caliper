/*
 * Created 2018 by Nicolas Winocur
 * Largely based on code Copyright (C) 2012 Google Inc.
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

package com.nicolaswinocur.json;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Serializes and deserializes {@link ImmutableSet} instances using a {@link LinkedHashSet} as an
 * intermediary.
 */
final class ImmutableSetTypeAdapterFactory implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Type type = typeToken.getType();
    if (typeToken.getRawType() != ImmutableSet.class || !(type instanceof ParameterizedType)) {
      return null;
    }

    com.google.common.reflect.TypeToken<ImmutableSet<?>> betterToken =
        (com.google.common.reflect.TypeToken<ImmutableSet<?>>)
            com.google.common.reflect.TypeToken.of(typeToken.getType());
    final TypeAdapter<LinkedHashSet<?>> linkedHashSetAdapter =
        (TypeAdapter<LinkedHashSet<?>>)
            gson.getAdapter(
                TypeToken.get(
                    betterToken.getSupertype(Set.class).getSubtype(LinkedHashSet.class).getType()));
    return new TypeAdapter<T>() {
      @Override
      public void write(JsonWriter out, T value) throws IOException {
        LinkedHashSet<?> linkedHashSet = Sets.newLinkedHashSet((Set<?>) value);
        linkedHashSetAdapter.write(out, linkedHashSet);
      }

      @Override
      public T read(JsonReader in) throws IOException {
        LinkedHashSet<?> linkedHashSet = linkedHashSetAdapter.read(in);
        return (T) ImmutableSet.copyOf(linkedHashSet);
      }
    };
  }
}
