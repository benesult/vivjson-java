/* Exception for VivJson.
 *
 * License:
 * Copyright 2025 benesult
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benesult.vivjson;

import java.lang.Exception;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Exception for VivJson.
 *
 * <p>Refer to:
 * <ul>
 *   <li> <a href="https://craftinginterpreters.com/">Crafting Interpreters</a>
 * </ul>
 * Note that this code is made from scratch. The source code
 * of the above WEB sites is not used.
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-29
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class VivException extends Exception {
  private static final long serialVersionUID = 1L;

  /** Title just ahead of error message. */
  static String TAG = "Viv";

  /**
   * Constructs VivJson's exception.
   *
   * @param message Error message
   */
  public VivException(String message) {
    super(message);
  }

  /**
   * Reports an error message.
   * Print an error message into stderr according as configuration.
   *
   * @param tag tag, such as "Lexer", "Parser", or "Evaluator".
   * @param message main message of Error
   * @param location the location of error.
   *                 Acceptable object is {@code Token}, {@code String},
   *                 or {@code null}.
   *                 Location it not included when {@code null} is given.
   * @param config configuration. {@code null} if it is not needed.
   * @param eliminateTag true if tag is not needed, false otherwise.
   * @return error message
   */
  static String report(String tag, String message,
                       @Nullable Object location,
                       @Nullable Config config,
                       boolean eliminateTag) {
    StringBuilder sb = new StringBuilder();
    if (!eliminateTag) {
      sb.append("[");
      sb.append(TAG);
      if (Config.getEnableTagDetail(config)) {
        sb.append(":");
        sb.append(tag);
      }
      sb.append("] Error: ");
    }
    sb.append(message);

    if (location != null) {
      if (location instanceof Token) {
        sb.append(" for ");
        sb.append(location.toString());
      } else if (location instanceof String) {
        sb.append(" in ");
        sb.append(location);
      }
    }

    String text = sb.toString();
    if (Config.getEnableStderr(config)) {
      System.err.println(text);
    }
    return text;
  }

  /**
   * Gets a location of token.
   * Mostly, it contains line number and column number.
   *
   * @param token Token
   * @return a location of token. {@code null} if the necessary information
   *         is not given.
   */
  static @Nullable String getTokenLocation(@Nullable Token token) {
    String location = null;
    if (token != null
        && token.lineNumber != null && token.columnNumber != null) {
      location = token.getLocation();
    }
    return location;
  }
}
