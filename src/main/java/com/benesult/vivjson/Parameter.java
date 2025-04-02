/* Parameter for VivJson.
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
 * Parameter data class.
 * This is used to assist the definition of function entity.
 * The example is shown as below. There are 3 Parameters.
 * <pre>
 *   (1) modifier + name   (3) modifier + name
 *        |                     |
 *        |    (2) only name    |
 *        |          |          |
 *        V          V          v
 *   _____________ ____  ______________
 *   function test(data, function block) {
 *       :
 *       :
 *   }
 * </pre>
 * <ul>
 *   <li> "modifier" is essential at (1).
 *   <li> "modifier" is optional at (2) and (3).
 * </ul>
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
class Parameter extends Statement {
  /**
   * Modifier of function.
   * For example, "function", "class".
   * {@code null} is given when the identifier is not function.
   */
  final @Nullable Token modifier;
  /** A string of identifier. */
  final Identifier name;

  /**
   * Makes Parameter data class's instance.
   * For example, 3 * 2 are stored into left, operator, and right.
   *
   * @param modifier a modifier of .
   *                 For example, "function", "class"
   *                 {@code null} is given when the identifier is not
   *                 function.
   * @param name a string of identifier.
   */
  Parameter(@Nullable Token modifier, Identifier name) {
    this.modifier = modifier;
    this.name = name;
  }

  /**
   * Gets as string.
   *
   * @return representation of Parameter as String
   */
  @SuppressWarnings("null")
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (modifier != null) {
      sb.append(modifier.lexeme);
      sb.append(" ");
    }
    sb.append(name.name.lexeme);
    return sb.toString();
  }
}
