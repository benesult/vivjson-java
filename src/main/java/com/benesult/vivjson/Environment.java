/* Environment for VivJson.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Environment for VivJson.
 *
 * <ul>
 *   <li> {@link #Environment() Environment } : Constructor.
 *   <li> {@link #get() Environment#get }
 *     <ul>
 *       <li> Get a variable's value if argument is variable name.
 *       <li> Get a block if argument is empty.
 *            A block has all variable without variables of the
 *            prefix "_" and definitions of pure function or closure.
 *       <li> Get a result value that is given with ":=" statement.
 *            It is returned if argument is empty and a result value
 *            is registered.
 *      </ul>
 *   <li> {@link #set(String, Object) Environment#set } : Set a variable.
 *   <li> {@link #remove(String) Environment#remove } : Remove a variable.
 * </ul>
 *
 * <p>The following types are stored.
 * <ul>
 *   <li> Host native primitive/Object
 *     <ul>
 *       <li> {@code null }
 *       <li> {@code String }
 *       <li> {@code Long }
 *       <li> {@code Double }
 *       <li> {@code Boolean }
 *       <li> {@code ArrayList<@Nullable Object> }
 *       <li> {@code HashMap<String, @Nullable Object> }
 *     </ul>
 *   <li> Definition of pure function or closure (CalleeRegistry)
 *   <li> Variable whose value is standard library's function (Identifier)
 *   <li> Alias (Get)
 * </ul>
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
class Environment {
  /** The representation of the undefined variable. */
  static final Object UNDEFINED = new Object();
  /**
   * The name of variable that is assigned particular value as result by ':='.
   */
  static final String RESULT_NAME = "___#RESULT#___";
  /**
   * The name of variable that is used for "return" statement.
   *
   * <p>Firstly, when anonymous/pure function starts, it creates this
   * variable whose value is UNDEFINED.<br>
   * Secondly, "return" statement sets value and evaluation goes back
   * until finishing function. When the returned value is given like
   * "return(xxxx)", its value is set. Otherwise, this name is set as
   * value.<br>
   * For example, the function "test" creates this variable. Then
   * "return 'zero'" sets "zero" into this variable and evaluation
   * goes back to assignment of "x".
   * <pre>
   *   function test(a) {
   *       if (a == 0) {
   *           return 'zero'
   *       }
   *       :
   *       :
   *   }
   *   
   *   x = test(0)
   * </pre>
   */
  static final String FUNCTION_RETURN_NAME = "___#RETURN#___";
  /**
   * The name of variable that is used for "break" statement.
   *
   * <p>Firstly, when loop starts, it creates this variable whose value
   * is UNDEFINED.<br>
   * Secondly, "break" statement sets any value and evaluation goes
   * back until finishing loop.
   */
  static final String BREAK_NAME = "___#BREAK#___";
  /**
   * The name of variable that is used for "continue" statement.
   *
   * <p>Firstly, when loop starts, it creates this variable whose value
   * is UNDEFINED.<br>
   * Secondly, "continue" statement sets any value and evaluation goes
   * back until starting loop.
   */
  static final String CONTINUE_NAME = "___#CONTINUE#___";

  /**
   * The parent environment.
   * {@code null} is given when this instance is root.
   */
  private final @Nullable Environment enclosing;
  /** Variable name and value are stored. */
  private final Map<String, @Nullable Object> variables;

  /**
   * Makes Environment as root.
   */
  Environment() {
    this(null);
  }

  /**
   * Makes Environment.
   *
   * @param enclosing the parent environment.
   *                  {@code null} is given when this instance is root.
   */
  Environment(@Nullable Environment enclosing) {
    this.enclosing = enclosing;
    this.variables = new HashMap<>();
  }

  /**
   * Gets the whole variable's value from current scope.
   * However it is excluded when the prefix of variable is "_" or
   * value is the function (CalleeRegistry).<br>
   * Note that a result value of ":=" statement had been registered,
   * it is returned.
   *
   * @return the whole variable's value as Map or
   *          a result value of ":=".
   */
  @Nullable Object getOnlyThisScope() {
    return get(null, true);
  }

  /**
   * Gets the whole variable's value.
   * However it is excluded when the prefix of variable is "_" or
   * value is the function (CalleeRegistry).<br>
   * Note that a result value of ":=" statement had been registered,
   * it is returned.
   *
   * @return the whole variable's value as Map or
   *          a result value of ":=".
   */
  @Nullable Object get() {
    return get(null, false);
  }

  /**
   * Gets variable's value.
   *
   * <p>When it is not existed in this scope, it is tried in the parent
   * scope.<br>
   * {@code null} is returned if it is not existed in the whole scope.
   *
   * <p>When name is {@code null}, the whole values are returned as Map.
   * However it is excluded when the prefix of variable is "_" or value
   * is the function (CalleeRegistry).<br>
   * Note that a result value of ":=" statement had been registered,
   * it is returned.
   *
   * @param name the name of variable.
   *             When this is {@code null}, the whole values are
   *             returned as Map. However it is excluded when the
   *             prefix of variable is "_" or value is the function
   *             (CalleeRegistry).<br>
   *             Note that a result value of ":=" statement had been
   *             registered, it is returned.
   * @return Its value.
   *         Environment.UNDEFINED is returned if the given name's
   *         variable is not existed.
   */
  @Nullable Object get(@Nullable String name) {
    return get(name, false);
  }

  /**
   * Gets variable's value.
   *
   * <p>When it is not existed in this scope, it is tried in the parent
   * scope.<br>
   * {@code null} is returned if it is not existed in the whole scope.
   *
   * <p>When name is {@code null}, the whole values are returned as Map.
   * However it is excluded when the prefix of variable is "_" or value
   * is the definition of function (CalleeRegistry).<br>
   * Note that a result value of ":=" statement had been registered,
   * it is returned.
   *
   * @param name the name of variable. When this is {@code null}, the
   *             whole values are returned as Map. However it is excluded
   *             when the prefix of variable is "_" or value is the
   *             function (CalleeRegistry).<br>
   *             Note that a result value of ":=" statement had been
   *             registered, it is returned.
   * @param onlyThisScope a flag of scope's limitation.
   *                      When this is True, getting scope is limited
   *                      only here.
   * @return Its value.
   *         Environment.UNDEFINED is returned if the given name's
   *         variable is not existed.
   */
  @Nullable Object get(@Nullable String name, boolean onlyThisScope) {
    if (name == null) {
      // :=
      @Nullable Object value = variables.getOrDefault(RESULT_NAME, UNDEFINED);
      if (value != UNDEFINED) {
        return value;
      }

      // Whole values
      Map<String, @Nullable Object> result = new HashMap<>();
      Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                              variables.entrySet().iterator();
      while (iterator.hasNext()) {
        try {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = iterator.next();
          @Nullable String key = entry.getKey();
          value = entry.getValue();
          if (key.charAt(0) == '_') {
            continue;
          }
          if (value instanceof CalleeRegistry
              && !((CalleeRegistry) value).isReference) {
            continue;
          }
          result.put(key, value);
        } catch (NoSuchElementException ignored) {
          break;
        }
      }
      return result;
    }

    // Try to get from current scope.
    if (variables.containsKey(name)) {
      return variables.get(name);
    }

    // Try to get from parent scope.
    if (!onlyThisScope && enclosing != null) {
      return enclosing.get(name, false);
    }

    return UNDEFINED;
  }

  /**
   * Sets a variable.
   * Firstly, modifying is tried. When the given variable is not
   * existed in this scope, it is tried in the parent scope.
   * When it is not existed in the whole scope, it is assigned in
   * this scope newly.
   *
   * @param name the variable name.
   *             When {@code null} is given, value is set as the
   *             returned value.
   * @param value its value
   */
  void set(@Nullable String name, @Nullable Object value) {
    set(name, value, false);
  }

  /**
   * Sets a variable.
   *
   * <p>When onlyThisScope is true, the given variable/value is stored
   * into current scope. In other words, it is local variable.
   *
   * <p>Otherwise, trying to set is not only current scope but also
   * parent scope.<br>
   * Firstly, modifying is tried. When the given variable is not
   * existed in this scope, it is tried in the parent scope.
   * When it is not existed in the whole scope, it is assigned in
   * this scope newly.
   *
   * @param name the variable name.
   *             When {@code null} is given, value is set as the
   *             returned value.
   * @param value its value
   * @param onlyThisScope a flag of scope's limitation.
   *                      When this is True, setting scope is limited
   *                      only here.
   */
  void set(@Nullable String name, @Nullable Object value,
           boolean onlyThisScope) {
    if (name == null) {
      name = RESULT_NAME;  // :=
    }

    if (!onlyThisScope) {
      boolean isCompleted = modify(name, value);
      if (isCompleted) {
        return;
      }
    }
    setIntoThisScope(name, value);  // New assignment
  }

  /**
   * Modify a variable.
   * When the given variable is not existed in this scope, it is
   * tried in the parent scope.
   *
   * @param name the variable name
   * @param value its value
   * @return true if modifying is completed, false otherwise
   */
  boolean modify(String name, @Nullable Object value) {
    if (variables.containsKey(name)) {
      return setIntoThisScope(name, value);  // Modify
    }

    if (enclosing != null) {
      return enclosing.modify(name, value);
    }

    return false;
  }

  /**
   * Sets a variable into current scope.
   *
   * @param name the variable name
   * @param value its value
   * @return true if success, false otherwise
   */
  private boolean setIntoThisScope(String name, @Nullable Object value) {
    try {
      variables.put(name, value);
    } catch (ClassCastException ignored) {
      return false;
    } catch (NullPointerException ignored) {
      return false;
    } catch (IllegalArgumentException ignored) {
      return false;
    }
    return true;
  }

  /**
   * Remove a variable.
   *
   * @param name the variable name.
   * @return true if removing is completed, false otherwise
   */
  boolean remove(String name) {
    return remove(name, false);
  }

  /**
   * Remove a variable.
   *
   * @param name the variable name.
   * @param onlyThisScope a flag of scope's limitation.
   *                      When this is True, removing scope is limited
   *                      only here.
   * @return true if removing is completed, false otherwise
   */
  boolean remove(String name, boolean onlyThisScope) {
    if (variables.containsKey(name)) {
      try {
        variables.remove(name);
      } catch (ClassCastException ignored) {
        ;
      } catch (NullPointerException ignored) {
        ;
      }
      return true;
    }
    if (!onlyThisScope && enclosing != null) {
      return enclosing.remove(name);
    }
    return false;
  }

  /**
   * Gets enclosing.
   *
   * @return {@link #enclosing} that is the parent Environment.<br>
   *         {@null} if enclosing is not available. In other words,
   *         this is root Environment if {@null} is returned.
   */
  @Nullable Environment getEnclosing() {
    return enclosing;
  }
}
