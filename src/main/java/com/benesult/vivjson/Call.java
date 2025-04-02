/* Call for VivJson.
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
 * Call data class.
 * This is used to call function, such as len("abc").
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
class Call extends Statement {
  /** The name of function. */
  final Statement name;
  /** Arguments of function. */
  final Array arguments;

  /**
   * Makes Call data class's instance.
   *
   * @param name a name of function.
   * @param arguments arguments of function.
   */
  Call(Statement name, Array arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  /**
   * Gets as string.
   *
   * @return representation of Call as String
   */
  @Override
  public String toString() {
    return Utils.concatenate(name.toString() + "(", arguments.values, ")");
  }
}
