/* Return for VivJson.
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
 * Return data class.
 * {@code "return" [ "(" value ")" ] }
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
class Return extends Statement {
  /**
   * Token of Return statement.
   * This is used to get its location when reporting error is needed.
   */
  final Token token;
  /** The returned value. */
  final @Nullable Statement value;

  /**
   * Makes Return data class's instance.
   *
   * @param token a token of Return statement. This is used to get
   *              its location when reporting error is needed.
   * @param value the returned value
   */
  Return(Token token, @Nullable Statement value) {
    this.token = token;
    this.value = value;
  }

  /**
   * Gets as string.
   *
   * @return representation of Return as String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("return");
    if (value != null) {
      String text = value.toString();
      boolean hasParenthesis = (text.length() >= 2
                                && text.charAt(0) == '('
                                && text.charAt(text.length() - 1) == ')');
      if (!hasParenthesis) {
        sb.append("(");
      }
      sb.append(text);
      if (!hasParenthesis) {
        sb.append(")");
      }
    }
    return sb.toString();
  }
}
