/* Exception for Parser of VivJson.
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

import org.eclipse.jdt.annotation.Nullable;

/**
 * Exception for Parser of VivJson.
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
class ParseException extends VivException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs Parser's exception.
   *
   * @param message Error message
   */
  ParseException(String message) {
    super(message);
  }

  /**
   * Makes Parser's exception.
   * Report error information and return ParseException.
   * This method does not throw Exception. So caller should throw
   * Exception if it is needed.
   *
   * @param message error message
   * @param token a Token or {@code null}. {@code null} is given when
   *              the cause of error is not Token.
   * @param config configuration. {@code null} if it is not needed.
   * @return Parser's exception instance.
   */
  static ParseException makeParseException(String message,
                                           @Nullable Token token,
                                           @Nullable Config config) {
    String report = report("Parser", message, token, config, false);
    return new ParseException(report);
  }
}