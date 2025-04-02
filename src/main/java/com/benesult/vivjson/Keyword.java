/* Keyword for VivJson.
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

/**
 * Keyword data class.
 * It is used as return, break, and so on.
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
 * <p>Last modified: 2025-03-20
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Keyword extends Statement {
  /** Token of Keyword. */
  final Token token;

  /**
   * Makes Keyword data class's instance.
   * It is used as return, break, and so on.
   *
   * @param token a Token of Keyword
   */
  Keyword(Token token) {
    this.token = token;
  }

  /**
   * Gets as string.
   *
   * @return representation of Keyword as String
   */
  @Override
  public String toString() {
    return token.lexeme;
  }
}
