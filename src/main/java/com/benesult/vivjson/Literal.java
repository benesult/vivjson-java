/* Literal for VivJson.
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
 * Literal data class.
 * string, number (int and float), true, false, and null are accepted.
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
class Literal extends Statement {
  /** Token of literal. */
  final Token token;

  /**
   * Makes Literal data class's instance.
   * string, number (int and float), true, false, and null are accepted.
   *
   * @param token a Token of literal
   */
  Literal(Token token) {
    this.token = token;
  }

  /**
   * Gets as string.
   *
   * @return representation of Literal as String
   */
  @Override
  public String toString() {
    if (token.type == Token.Type.NULL
        || token.type == Token.Type.TRUE || token.type == Token.Type.FALSE) {
      return token.type.value;
    }
    if (token.type == Token.Type.STRING) {
      return Utils.concatenate("\"", token.lexeme, "\"");
    }
    return token.lexeme;
  }
}
