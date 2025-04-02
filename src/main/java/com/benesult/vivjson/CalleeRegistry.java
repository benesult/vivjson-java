/* CalleeRegistry for VivJson.
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
 * Callee Registry data class.
 * This is made in Evaluator. This is not used in Parser.
 *
 * <p>Member "environment" is Environment instance or {@code null}.
 * The former (Environment instance) is set if callee is Closure.
 *
 * <p>Member "isReference" is true if this is not the definition of
 * function.
 *
 * <p>For example, "enclosure", "z1", and "z2" are registered as this
 * data class.<br>
 * "environment" of "enclosure" is {@code null}.<br>
 * On the other hand, "environment" of "z1" and "z2" is Environment
 * instance.<br>
 * "isReference" of "enclosure" is false. "isReference" of "z1" and
 * "z2" is true.
 * <pre>{@code
 *   function enclosure(a) {
 *       x = a
 *       function closure(y) {
 *           return(x + y)
 *       }
 *       return(closure)
 *   }
 *   z1 = enclosure(100)
 *   z2 = enclosure(200)
 *   print(z1(5))  # 105
 *   print(z2(10))  # 210
 * }</pre>
 *
 * <p>By the way, this data class is used even if assignment is simple.
 * The following "y" is also this instance.<br>
 * "environment" of "x2" and "y" is {@code null}.<br>
 * "isReference" of "x2" is false. "isReference" of "y" is true.
 * <pre>{@code
 *   function x2(a) {
 *       return(a * 2)
 *   }
 *   y = x2
 * }</pre>
 *
 * <p>"isReference" decides whether the variable is remained or not
 * after evaluating block.<br>
 * In the following sample, member of "k" will be only "y" after
 * evaluation. Because the definition of function is not remained
 * as result. Although value of "x2" and "y" is this instance,
 * since "isReference" of "y" is true, only "y" will be remained.
 * <pre>{@code
 *   k = {
 *       function x2(a) {
 *           return(a * 2)
 *       }
 *       y = x2
 *   }
 * }</pre>
 *
 * <p>Similarly, the following "table" can keep its members.
 * <pre>{@code
 *   function add(a, b) {
 *       return(a + b)
 *   }
 *   function sub(a, b) {
 *       return(a - b)
 *   }
 *   table = {
 *       "+": add,
 *       "-": sub
 *   }
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
 * <p>Last modified: 2025-03-29
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class CalleeRegistry extends Statement {
  /** Callee that is function's entity. */
  final Callee callee;
  /**
   * Environment instance.
   * {@code null} is given when callee is not Closure.
   */
  final @Nullable Environment environment;
  /**
   * Indicator that is whether reference or not.
   * true is given when this is reference.
   */
  final boolean isReference;

  /**
   * Makes CalleeRegistry data class's instance.
   *
   * @param callee callee that is function's entity.
   * @param environment Environment instance if callee is Closure,
   *                    {@code null} otherwise
   * @param isReference Indicator that is whether reference or not.
   *                    true is given when this is reference.
   */
  CalleeRegistry(Callee callee, @Nullable Environment environment,
                 boolean isReference) {
    this.callee = callee;
    this.environment = environment;
    this.isReference = isReference;
  }

  /**
   * Gets as string.
   *
   * @return representation of CalleeRegistry as String
   */
  @Override
  public String toString() {
    return callee.toString();
  }
}
