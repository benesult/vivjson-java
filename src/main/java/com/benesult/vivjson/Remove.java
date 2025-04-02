/* Remove for VivJson.
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
 * Remove data class.
 * For example, remove(x["y"]["z"]), remove(x.y.z)
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
class Remove extends Statement {
  /**
   * Token of Remove statement.
   * This is used to get its location when reporting error is needed.
   */
  final Token token;
  /** 
   * Representation of variable.
   * For example, x["y"]["z"] and x.y.z are represented as
   * ["x", "y", "z"].
   */
  final ArrayList<Statement> members;

  /**
   * Makes Remove data class's instance.
   *
   * @param token a token of Remove statement. This is used to get
   *              its location when reporting error is needed.
   * @param members the representation of variable.
   *                For example, x["y"]["z"] and x.y.z are represented
   *                as ["x", "y", "z"].
   */
  Remove(Token token, ArrayList<Statement> members) {
    this.token = token;
    this.members = members;
  }

  /**
   * Makes Remove data class's instance.
   *
   * @param token a token of Remove statement. This is used to get
   *              its location when reporting error is needed.
   * @param members the representation of variable.
   *                For example, x["y"]["z"] and x.y.z are represented
   *                as ["x", "y", "z"].
   */
  Remove(Token token, Statement[] members) {
    this.token = token;
    this.members = new ArrayList<>();
    for (Statement member : members) {
      this.members.add(member);
    }
  }

  /**
   * Gets as string.
   *
   * @return representation of Remove as String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("remove(");
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
    sb.append(")");
    return sb.toString();
  }
}
