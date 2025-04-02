/* Exception for Evaluator of VivJson.
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
 * Exception for Evaluator of VivJson.
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
class EvaluateException extends VivException { 
  private static final long serialVersionUID = 1L;

  /**
   * Constructs Evaluator's exception.
   */
  EvaluateException() {
    super("EvaluateException");
  }

  /**
   * Constructs Evaluator's exception.
   *
   * @param message Error message
   */
  EvaluateException(String message) {
    super(message);
  }

  /**
   * Makes Evaluator's exception.
   * Report error information and return EvaluateException.
   * This method does not throw Exception. So caller should throw
   * Exception if it is needed.
   *
   * @param message error message
   * @param statement statement where error occurs. It is used to get
   *                  location. {@code null} is given when cause is not
   *                  statement.
   * @param config configuration. {@code null} if it is not needed.
   * @param eliminateTag eliminate Tag or not.
   *                     true if tag is not needed, false otherwise.
   * @return Evaluator's exception instance.
   */
  static EvaluateException makeEvaluateException(String message,
                                           @Nullable Statement statement,
                                           @Nullable Config config,
                                           boolean eliminateTag) {
    String location = getStatementLocation(statement);
    String report =
              report("Evaluator", message, location, config, eliminateTag);
    return new EvaluateException(report);
  }

  /**
   * Gets statement's location.
   *
   * @param statement statement as target of getting location
   * @return the location or {@code null}.
   *         {@code null} if location is not gotten.
   */
  private static @Nullable String
                    getStatementLocation(@Nullable Statement statement) {
    if (statement == null) {
      return null;
    }

    Class<?> c = statement.getClass();
    return (c == Literal.class)
      ? getTokenLocation(((Literal) statement).token)
      : ((c == Identifier.class)
        ? getTokenLocation(((Identifier) statement).name)
        : ((c == Keyword.class)
          ? getTokenLocation(((Keyword) statement).token)
          : ((c == Blank.class)
            ? getTokenLocation(((Blank) statement).token)
            : ((c == Array.class)
              ? getArrayLocation(((Array) statement).values)
              : ((c == Block.class)
                ? getArrayLocation(((Block) statement).values)
                : ((c == Binary.class)
                  ? getStatementLocation(((Binary) statement).left)
                  : ((c == Parameter.class)
                    ? ((((Parameter) statement).modifier != null)
                        ? getTokenLocation(((Parameter) statement).modifier)
                        : getStatementLocation(((Parameter) statement).name))
                    : ((c == Callee.class)
                      ? getStatementLocation(((Callee) statement).name)
                      : ((c == CalleeRegistry.class)
                        ? getStatementLocation(((CalleeRegistry) statement).callee)
                        : ((c == Call.class)
                          ? getStatementLocation(((Call) statement).name)
                          : ((c == Loop.class)
                            ? getStatementLocation(((Loop) statement).call)
                            : ((c == Get.class)
                              ? getArrayLocation(((Get) statement).members)
                              : ((c == Set.class)
                                ? getArrayLocation(((Set) statement).members)
                                : ((c == Remove.class)
                                  ? getTokenLocation(((Remove) statement).token)
                                  : ((c == Return.class)
                                    ? getTokenLocation(((Return) statement).token)
                                    : ((c == Injection.class)
                                      ? ((Injection) statement).location
                                      : ((c == Value.class)
                                        ? ((Value) statement).location
                                        : null
                                      )
                                    )
                                  )
                                )
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
      );
  }

  /**
   * Gets the location of statement's array.
   *
   * @param array statement's array as target of getting location
   * @return the location or {@code null}.
   *         {@code null} if location is not gotten.
   */
  private static @Nullable String getArrayLocation(ArrayList<Statement> array) {
    if (array.size() == 0) {
      return null;
    }
    return getStatementLocation(array.get(0));
  }
}