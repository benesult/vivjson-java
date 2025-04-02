/* Block for VivJson.
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
 * Block data class.
 *
 * <p>For example,
 * <ul>
 *   <li>anonymous: {@code x = {a: 3, b: 2} }
 *   <li>pure: {@code function test() {return(10)} }
 *   <li>limited: {@code if (i > 10) {x="+"} else {x="-"} }
 * </ul>
 *
 * <p>In VivJson, any block is function.
 * The type of the outermost block (that is given from file/text) is
 * decided as anonymous function or class constructor by calling the
 * particular method.
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
class Block extends Statement {
  /** Block type for class. */
  static final String CLASS_CONSTRUCTOR = "Block_class";
  /** Block type for anonymous function. */
  static final String ANONYMOUS_FUNCTION = "Block_anonymous";
  /** Block type for pure function. */
  static final String PURE_FUNCTION = "Block_pure";
  /** Block type for limited function ("if"/"elseif"/"else"). */
  static final String LIMITED_FUNCTION = "Block_limited";

  /** A list of some statements. */
  final ArrayList<Statement> values;
  /** Type of Block. */
  final String type;

  /**
   * Makes Block data class's instance.
   *
   * @param values a list of some statements
   * @param type the type of Block.
   *             Block.CLASS_CONSTRUCTOR, Block.ANONYMOUS_FUNCTION,
   *             Block.PURE_FUNCTION, or Block.LIMITED_FUNCTION
   */
  Block(ArrayList<Statement> values, String type) {
    this.values = values;
    this.type = type;
  }

  /**
   * Makes Block data class's instance.
   *
   * @param values a list of some statements
   * @param type the type of Block.
   *             Block.CLASS_CONSTRUCTOR, Block.ANONYMOUS_FUNCTION,
   *             Block.PURE_FUNCTION, or Block.LIMITED_FUNCTION
   */
  Block(Statement[] values, String type) {
    this.values = new ArrayList<>();
    for (Statement value : values) {
      this.values.add(value);
    }
    this.type = type;
  }

  /**
   * Gets as string.
   *
   * @return representation of Block as String
   */
  @Override
  public String toString() {
    return Utils.concatenate("{", values, "}");
  }
}
