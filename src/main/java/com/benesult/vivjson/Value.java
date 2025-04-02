/* Value for VivJson.
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
 * Value data class.
 * Set host language's value.
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
class Value extends Statement {
  /** Value that is assigned into variable. */
  final @Nullable Object value;
  /** Location for reporting error. For example, "2nd argument". */
  final @Nullable String location;

  /**
   * Makes Value data class's instance.
   *
   * @param value a value
   * @param location the location for reporting error.
   *                 For example, "2nd argument".
   */
  Value(@Nullable Object value, @Nullable String location) {
    this.value = value;
    this.location = location;
  }

  /**
   * Gets as string.
   *
   * @return representation of Value as String
   */
  @Override
  public String toString() {
    return (value == null) ? "null" : value.toString();
  }
}
