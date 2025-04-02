/* Binary for VivJson.
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
 * Binary data class.
 * For example, 3 * 2 are stored into left, operator, and right.
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
class Binary extends Statement {
  /** The left operand. */
  final Statement left;
  /** A operator, such as "+", "-", ... */
  final Token operator;
  /** The right operand. */
  final Statement right;

  /**
   * Makes Binary data class's instance.
   * For example, 3 * 2 are stored into left, operator, and right.
   *
   * @param left the left operand
   * @param operator a operator, such as "+", "-", ...
   * @param right the right operand
   */
  Binary(Statement left, Token operator, Statement right) {
    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  /**
   * Gets as string.
   *
   * @return representation of Binary as String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    if (operator.type != Token.Type.NOT) {
      sb.append(left.toString());
      sb.append(" ");
      sb.append(operator.lexeme);
    } else {
      sb.append("not ");
    }
    sb.append(" ");
    sb.append(right.toString());
    sb.append(")");
    return sb.toString();
  }
}
