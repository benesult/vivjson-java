/* Callee for VivJson.
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
 * Callee data class.
 * This is used to define function entity.
 * The example is shown as below.
 * <pre>
 *       name   parameters[0]   parameters[1]
 *        |         |             |
 *        V         V             v
 *   _____________ ____  ______________
 *   function test(data, function block) {
 *       :
 *       :
 *   }
 * </pre>
 * In "name", its "modifier" of Parameter is essential.<br>
 * In "parameters", its "modifier" of Parameter is optional.
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
class Callee extends Statement {
  /** Modifier and name of function. */
  final Parameter name;
  /** Parameters of function. */
  final Array parameters;

  /**
   * Makes Callee data class's instance.
   *
   * @param name modifier and name of function.
   * @param parameters parameters of function.
   */
  Callee(Parameter name, Array parameters) {
    this.name = name;
    this.parameters = parameters;
  }

  /**
   * Gets as string.
   *
   * @return representation of Callee as String
   */
  @Override
  public String toString() {
    return Utils.concatenate(name.toString() + "(", parameters.values, ")");
  }
}
