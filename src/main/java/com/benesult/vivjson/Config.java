/* Configuration of VivJson.
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
 * Configuration of VivJson.
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-04-04
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class Config {
  /**
   * Version of VivJson's specification.
   * It is represented as {@literal <Major> "." <Minor> "." <Patch>}.
   * For example, "1.0.0".
   */
  public static final String SPEC_VERSION = "1.0.0";
  /**
   * Version of this interpreter.
   * It is represented as {@literal <Major> "." <Minor> "." <Patch>}.
   * For example, "1.0.0".<br>
   * This {@literal <Major> "." <Minor>} is equal to
   * {@link #SPEC_VERSION}'s one.
   */
  public static final String INTERPRETER_VERSION = "1.0.1";
  /** The default of enabling error message's stderr output. */
  static final boolean ENABLE_STDERR_DEFAULT = false;
  /** The default of enabling detail of error message's tag. */
  static final boolean ENABLE_TAG_DETAIL_DEFAULT = false;
  /** The default of enabling only JSON. */
  static final boolean ENABLE_ONLY_JSON = false;
  /** The default of Infinity. */
  @Nullable static final String INFINITY_DEFAULT = null;
  /** The default of NaN (Not a Number). */
  @Nullable static final String NAN_DEFAULT = null;
  /** The default of maximum array/block size. */
  static final int MAX_ARRAY_SIZE_DEFAULT = 1000;
  /** The default of maximum recursive called times of evaluate method. */
  static final int MAX_DEPTH_DEFAULT = 200;
  /** The default of maximum loop times of "for", "while", and so on. */
  static final int MAX_LOOP_TIMES_DEFAULT = 1000;

  /**
   * Enabling stderr.
   * When true is given, error message is outputted into stderr.
   * Otherwise, it is not outputted.
   */
  private boolean enableStderr;
  /**
   * Enabling the detail of tag.
   * When true is given, error message's tag contains either of
   * "Lexer", "Parser", or "Evaluator".
   */
  private boolean enableTagDetail;
  /**
   * Enabling only JSON.
   * When true is given, the given data is parsed as JSON.
   * In other words, script is disabled.
   */
  private boolean enableOnlyJson;
  /**
   * Infinity's representation of string.
   * When string is given, Infinity is allowed in JSON. Then the given
   * string is used to input/output Infinity from/to JSON. (Note that
   * it is not surrounded with quotation mark.)
   * When null is given and Infinity is happen, error is occurred.
   */
  private @Nullable String infinity;
  /**
   * NaN (Not a Number)'s representation of string.
   * When string is given, NaN (Not a Number) is allowed in JSON. Then
   * the given string is used to input/output NaN from/to JSON. (Note
   * that it is not surrounded with quotation mark.)
   * When null is given and NaN is happen, error is occurred.
   */
  private @Nullable String nan;
  /** Maximum array/block size. */
  private int maxArraySize;
  /** Maximum recursive called times of evaluate method. */
  private int maxDepth;
  /** Maximum loop times of "for", "while", and so on. */
  private int maxLoopTimes;

  /**
   * Makes configuration instance of default values.
   */
  public Config() {
    this(ENABLE_STDERR_DEFAULT,
         ENABLE_TAG_DETAIL_DEFAULT,
         ENABLE_ONLY_JSON,
         INFINITY_DEFAULT,
         NAN_DEFAULT,
         MAX_ARRAY_SIZE_DEFAULT,
         MAX_DEPTH_DEFAULT,
         MAX_LOOP_TIMES_DEFAULT
    );
  }

  /**
   * Makes configuration instance that is based on the given instance.
   *
   * @param config a configuration instance
   */
  public Config(Config config) {
    this(config.enableStderr,
         config.enableTagDetail,
         config.enableOnlyJson,
         config.infinity,
         config.nan,
         config.maxArraySize,
         config.maxDepth,
         config.maxLoopTimes
    );
  }

  /**
   * Makes configuration instance with all custom values.
   *
   * @param enableStderr enabling stderr.
   *                     When true is given, error message is outputted
   *                     into stderr.
   *                     Otherwise, it is not outputted.
   * @param enableTagDetail enabling the detail of tag.
   *                        When true is given, error message's tag
   *                        contains either of "Lexer", "Parser", or
   *                        "Evaluator".
   * @param enableOnlyJson enabling only JSON.
   *                       When true is given, the given data is parsed
   *                       as JSON. In other words, script is disabled.
   * @param infinity Infinity's representation of string.
   *                 When string is given, Infinity is allowed in JSON.
   *                 Then the given string is used to input/output
   *                 Infinity from/to JSON. (Note that it is not
   *                 surrounded with quotation mark.)
   *                 When {@code null} is given and Infinity is happen,
   *                 error may be occurred.
   * @param nan NaN (Not a Number)'s representation of string.
   *            When string is given, NaN (Not a Number) is allowed in
   *            JSON. Then the given string is used to input/output NaN
   *            from/to JSON. (Note that it is not surrounded with
   *            quotation mark.)
   *            When {@code null} is given and NaN is happen, error may
   *            be occurred.
   * @param maxArraySize maximum array/block size
   * @param maxDepth maximum recursive called times of evaluate method
   * @param maxLoopTimes maximum loop times of "for", "while", and so
   *                     on
   */
  public Config(boolean enableStderr,
                boolean enableTagDetail,
                boolean enableOnlyJson,
                @Nullable String infinity,
                @Nullable String nan,
                int maxArraySize,
                int maxDepth,
                int maxLoopTimes) {
    this.enableStderr = enableStderr;
    this.enableTagDetail = enableTagDetail;
    this.enableOnlyJson = enableOnlyJson;
    this.infinity = infinity;
    this.nan = nan;
    this.maxArraySize = maxArraySize;
    this.maxDepth = maxDepth;
    this.maxLoopTimes = maxLoopTimes;
  }

  /** Enables that error message outputs into stderr. */
  public void enableStderr() {
    enableStderr(true);
  }

  /**
   * Enables/disables that error message outputs into stderr.
   *
   * @param enable enabling stderr.
   *               When True is given, error message is outputted into
   *               stderr.
   *               Otherwise, it is not outputted.
   */
  public void enableStderr(boolean enable) {
    enableStderr = enable;
  }

  /**
   * Gets whether outputting error message into stderr is enable.
   *
   * @return outputting error message into stderr is enable or not
   */
  public boolean getEnableStderr() {
    return enableStderr;
  }

  /**
   * Gets whether outputting error message into stderr is enable.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return outputting error message into stderr is enable or not
   */
  public static boolean getEnableStderr(@Nullable Config config) {
    return (config == null)
           ? Config.ENABLE_STDERR_DEFAULT : config.getEnableStderr();
  }

  /** Enables that detail of error message's tag. */
  public void enableTagDetail() {
    enableTagDetail(true);
  }

  /**
   * Enables that detail of error message's tag.
   *
   * @param enable enabling the detail of tag.
   *               When true is given, error message's tag contains 
   *               either of "Lexer", "Parser", or "Evaluator".
   */
  public void enableTagDetail(boolean enable) {
    enableTagDetail = enable;
  }

  /**
   * Gets whether detail of error message's tag is enable or not.
   *
   * @return detail of error message's tag is enable or not.
   */
  public boolean getEnableTagDetail() {
    return enableTagDetail;
  }

  /**
   * Gets whether detail of error message's tag is enable or not.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return detail of error message's tag is enable or not.
   */
  public static boolean getEnableTagDetail(@Nullable Config config) {
    return (config == null)
           ? Config.ENABLE_TAG_DETAIL_DEFAULT : config.getEnableTagDetail();
  }

  /** Enables only JSON. */
  public void enableOnlyJson() {
    enableOnlyJson(true);
  }

  /**
   * Enables only JSON.
   *
   * @param enable enabling only JSON.
   *               When true is given, the given data is parsed as JSON.
   *               In other words, script is disabled.
   */
  public void enableOnlyJson(boolean enable) {
    enableOnlyJson = enable;
  }

  /**
   * Gets whether parsing target is limited only JSON or not.
   *
   * @return parsing target is limited only JSON or not.
   */
  public boolean getEnableOnlyJson() {
    return enableOnlyJson;
  }

  /**
   * Gets whether parsing target is limited only JSON or not.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return parsing target is limited only JSON or not.
   */
  public static boolean getEnableOnlyJson(@Nullable Config config) {
    return (config == null)
           ? Config.ENABLE_ONLY_JSON : config.getEnableOnlyJson();
  }

  /**
   * Sets infinity's string.
   * When string is given, Infinity is allowed in JSON. Then the given
   * string is used to input/output Infinity from/to JSON. (Note that
   * it is not surrounded with quotation mark.)
   * When {@code null} is given and Infinity is happen, error may be
   * occurred.
   *
   * @param infinity Infinity's string or {@code null}.
   */
  public void setInfinity(@Nullable String infinity) {
    this.infinity = infinity;
  }

  /**
   * Gets infinity's string.
   *
   * @return Infinity's string or {@code null}.
   *         When this is {@code null}, Infinity is not allowed.
   */
  public @Nullable String getInfinity() {
    return infinity;
  }

  /**
   * Gets infinity's string.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return Infinity's string or {@code null}.
   *         When this is {@code null}, Infinity is not allowed.
   */
  public static @Nullable String getInfinity(@Nullable Config config) {
    return (config == null)
           ? Config.INFINITY_DEFAULT : config.getInfinity();
  }

  /**
   * Sets NaN(Not a Number)'s string.
   * When string is given, NaN (Not a Number) is allowed in JSON. Then
   * the given string is used to input/output NaN from/to JSON. (Note
   * that it is not surrounded with quotation mark.)
   * When {@code null} is given and NaN is happen, error may be
   * occurred.
   *
   * @param nan NaN(Not a Number)'s string or {@code null}.
   */
  public void setNaN(@Nullable String nan) {
    this.nan = nan;
  }

  /**
   * Gets NaN(Not a Number)'s string.
   *
   * @return NaN(Not a Number)'s string or {@code null}.
   *         When this is {@code null}, NaN is not allowed.
   */
  public @Nullable String getNaN() {
    return nan;
  }

  /**
   * Get NaN(Not a Number)'s string.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return NaN(Not a Number)'s string or {@code null}.
   *         When this is {@code null}, NaN is not allowed.
   */
  public static @Nullable String getNaN(@Nullable Config config) {
    return (config == null)
           ? Config.NAN_DEFAULT : config.getNaN();
  }

  /**
   * Sets maximum array/block size.
   *
   * @param size maximum array/block size
   */
  public void setMaxArraySize(int size) {
    maxArraySize = size;
  }

  /**
   * Gets maximum array/block size.
   *
   * @return maximum array/block size
   */
  public int getMaxArraySize() {
    return maxArraySize;
  }

  /**
   * Gets maximum array/block size.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return maximum array/block size
   */
  public static int getMaxArraySize(@Nullable Config config) {
    return (config == null)
           ? Config.MAX_ARRAY_SIZE_DEFAULT : config.getMaxArraySize();
  }

  /**
   * Set maximum recursive called times of evaluate method.
   *
   * @param depth maximum recursive called times of evaluate method
   */
  public void setMaxDepth(int depth) {
    maxDepth = depth;
  }

  /**
   * Gets maximum recursive called times of evaluate method.
   *
   * @return maximum recursive called times of evaluate method
   */
  public int getMaxDepth() {
    return maxDepth;
  }

  /**
   * Gets maximum recursive called times of evaluate method.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return maximum recursive called times of evaluate method
   */
  public static int getMaxDepth(@Nullable Config config) {
    return (config == null)
           ? Config.MAX_DEPTH_DEFAULT : config.getMaxDepth();
  }

  /**
   * Sets maximum loop times of "for", "while", and so on.
   *
   * @param times maximum loop times of "for", "while", and so on
   */
  public void setMaxLoopTimes(int times) {
    maxLoopTimes = times;
  }

  /**
   * Gets maximum loop times of "for", "while", and so on.
   *
   * @return maximum loop times of "for", "while", and so on
   */
  public int getMaxLoopTimes() {
    return maxLoopTimes;
  }

  /**
   * Gets maximum loop times of "for", "while", and so on.
   *
   * @param config Instance of configuration.
   *               When it is {@code null}, default value is returned.
   * @return maximum loop times of "for", "while", and so on
   */
  public static int getMaxLoopTimes(@Nullable Config config) {
    return (config == null)
           ? Config.MAX_LOOP_TIMES_DEFAULT : config.getMaxLoopTimes();
  }
}
