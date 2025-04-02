/* Set for VivJson.
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

import java.util.ArrayList;

/**
 * Set data class.
 * For example, x["y"]["z"] = 3 is represented as below.
 * <ul>
 *   <li> members: ["x", "y", "z"]
 *   <li> operator: =
 *   <li> value: 3
 * </ul>
 * Similarly, x.y.z = 3 is represented.
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
class Set extends Statement {
  /** 
   * Representation of variable.
   * For example, x["y"]["z"] is represented as ["x", "y", "z"].
   * Similarly, x.y.z is represented.
   */
  final ArrayList<Statement> members;
  /**
   * Operator.
   * "=", ":", "+=", ...
   */
  final Token operator;
  /** Value that is assigned into variable. */
  final Statement value;

  /**
   * Makes Set data class's instance.
   *
   * @param members the representation of variable.
   *                For example,  x["y"]["z"] is represented as
   *                ["x", "y", "z"].
   *                Similarly, x.y.z is represented.
   * @param operator the operator, such as "=", ":", "+=", ...
   * @param value a value that is assigned into variable.
   */
  Set(ArrayList<Statement> members, Token operator, Statement value) {
    this.members = members;
    this.operator = operator;
    this.value = value;
  }

  /**
   * Makes Set data class's instance.
   *
   * @param members the representation of variable.
   *                For example,  x["y"]["z"] is represented as
   *                ["x", "y", "z"].
   *                Similarly, x.y.z is represented.
   * @param operator the operator, such as "=", ":", "+=", ...
   * @param value a value that is assigned into variable.
   */
  Set(Statement[] members, Token operator, Statement value) {
    this.members = new ArrayList<>();
    for (Statement member : members) {
      this.members.add(member);
    }
    this.operator = operator;
    this.value = value;
  }

  /**
   * Gets as string.
   *
   * @return representation of Set as String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (Statement member : members) {
      if (!isFirst) {
        sb.append("[");
      }
      sb.append(member.toString());
      if (!isFirst) {
        sb.append("]");
      }
      isFirst = false;
    }
    if (operator.type != Token.Type.SET) {
      sb.append(" ");
    }
    sb.append(operator.lexeme);
    sb.append(" ");
    sb.append(value.toString());
    return sb.toString();
  }
}
