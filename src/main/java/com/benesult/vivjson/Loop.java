/* Loop for VivJson.
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
import org.eclipse.jdt.annotation.Nullable;

/**
 * Loop data class.
 * <pre>{@code
 *   For example, for (i = 0; i < 10; i += 1) { print(i) }
 *   "initial" is [i = 0; i < 10].
 *   "continuous" is [i += 1; i < 10].
 *   "statements" is [print(i)].
 *   "each" is null.
 *   "iterator" is null.
 *
 *   For example, for (i in [1, 2, 3]) { print(i) }
 *   "initial" is [true].
 *   "continuous" is [true].
 *   "statements" is [print(i)].
 *   "each" is i.
 *   "iterator" is [1, 2, 3].
 *
 *   For example, for (i in {"a": 1, "b": 2, "c": 3}) { print(i) }
 *   "initial" is [true].
 *   "continuous" is [true].
 *   "statements" is [print(i)].
 *   "each" is i.
 *   "iterator" is {"a": 1, "b": 2, "c": 3}.
 * }</pre>
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
class Loop extends Statement {
  /**
   * Call statement in program.
   * This is not used for actual control.
   * It may be used to print function name.
   */
  final Call call;
  /**
   * A sequence of statements that are evaluated before starting loop.
   * Then the boolean value of last statement proposes whether loop
   * should be done or not.
   */
  final ArrayList<Statement> initial;
  /**
   * A sequence of statements that are evaluated after 1st loop.
   * Then the boolean value of last statement proposes whether loop
   * should be continued or not.
   */
  final ArrayList<Statement> continuous;
  /** A sequence of statements that are main operation. */
  final ArrayList<Statement> statements;
  /**
   * An identifier of variable that receives each element of iterator.
   * It is used for "for (i in [1, 2, 3]) {...}" style.
   */
  final @Nullable Identifier each;
  /**
   * A iterator of array or block.
   * It is used for "for (i in [1, 2, 3]) {...}" style.
   */
  final @Nullable Object iterator;

  /**
   * Makes Loop data class's instance.
   *
   * @param call a Call statement in program.
   *             This is not used for actual control.
   *             It may be used to print function name.
   * @param initial a sequence of statements that are evaluated before
   *                starting loop.
   *                Then the boolean value of last statement proposes
   *                whether loop should be done or not.
   * @param continuous a sequence of statements that are evaluated
   *                   after 1st loop.
   *                   Then the boolean value of last statement
   *                   proposes whether loop should be continued or
   *                   not.
   * @param statements a sequence of statements that are main operation
   * @param each an identifier of variable that receives each element
   *             of iterator.
   *             It is used for "for (i in [1, 2, 3]) {...}" style.
   * @param iterator a iterator of array or block.
   *                 It is used for "for (i in [1, 2, 3]) {...}" style.
   */
  Loop(Call call,
       ArrayList<Statement> initial,
       ArrayList<Statement> continuous,
       ArrayList<Statement> statements,
       @Nullable Identifier each, @Nullable Object iterator) {
    this.call = call;
    this.initial = initial;
    this.continuous = continuous;
    this.statements = statements;
    this.each = each;
    this.iterator = iterator;
  }

  /**
   * Gets as string.
   *
   * @return representation of Loop as String
   */
  @SuppressWarnings("null")
  @Override
  public String toString() {
    return (each != null && iterator != null)
           ? Utils.concatenate(
                  Utils.concatenate(call.name.toString(),
                                    "(", each.name.lexeme, " in ",
                                    iterator.toString()),
                  Utils.concatenate(") {",
                    statements,
                  "}"))
           : Utils.concatenate(
                  call.name.toString() + "(",
                    Utils.concatenate("init={", initial, "}"),
                    Utils.concatenate("continue={", continuous, "}"),
                  Utils.concatenate(") {",
                    statements,
                  "}"));
  }
}
