/* Exception for Lexer of VivJson.
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
 * Exception for Lexer of VivJson.
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
class LexException extends VivException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs Lexer's exception.
   *
   * @param message Error message
   */
  LexException(String message) {
    super(message);
  }

  /**
   * Makes Lexer's exception.
   * Report error information and return LexException.
   * This method does not throw Exception. So caller should throw
   * Exception if it is needed.
   *
   * @param message error message
   * @param medium the name of object that has source code. It is used to
   *               report error. For example, "test.viv", "1st argument".
   *               May be {@code null}.
   * @param line line number of location where error occurs
   * @param column column number of location where error occurs.
   * @param config configuration. {@code null} if it is not needed.
   * @return Lexer's exception instance.
   */
  static LexException makeLexException(String message,
                                       @Nullable String medium,
                                       int line, int column,
                                       @Nullable Config config) {
    String location = Token.getLocation(medium, line, column);
    String report = report("Lexer", message, location, config, false);
    return new LexException(report);
  }
}