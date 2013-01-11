/*
 * Copyright (C) 2012 Google Inc.
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

package com.google.caliper.bridge;

import com.google.common.base.Optional;

/**
 * A message signaling that the timing interval has started in the worker.
 */
// TODO(gak): rename in terms of measurement
public class StartTimingLogMessage extends CaliperControlLogMessage {
  private static final String MESSAGE = CONTROL_PREFIX + "starting//";

  public static final class Parser
      implements TryParser<StartTimingLogMessage>, Renderer<StartTimingLogMessage> {
    @Override
    public Optional<StartTimingLogMessage> tryParse(String text) {
      return text.equals(MESSAGE)
          ? Optional.of(new StartTimingLogMessage())
          : Optional.<StartTimingLogMessage>absent();
    }

    @Override
    public String render(StartTimingLogMessage object) {
      return MESSAGE;
    }
  }

  @Override
  public void accept(LogMessageVisitor visitor) {
    visitor.visit(this);
  }
}
