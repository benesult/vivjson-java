/* Unit-test for function in Evaluator
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.benesult.vivjson.Viv.Result;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Evaluator function test class.
 * Unit-test for function call and its definition
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-03-24
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class EvaluatorBinaryTest {
  @ParameterizedTest
  @CsvSource({
    // for Evaluator._add
    "'', '{\"a\": 3, \"b\": 100} + {\"0\": \"x\", \"#\": true, \"/*\": false}', '{\"a\": 3, \"b\": 100, \"0\": \"x\", \"#\": true, \"/*\": false}', block, false",  // block + block --> block
    "'', '{\"a\": 3, \"b\": 100} + {\"0\": \"x\", \"#\": true, \"//\": false, \"y\": -3}', null, null, true",  // block + block --> ERROR because max array size is limited as 5.
    "'', '{\"a\": 3, \"b\": 100} + {\"a\": -5}', '{\"a\": -2, \"b\": 100}', block, false",  // block + block --> block
    "'', '{\"a\": 3, \"b\": 100} + [3, 0]', '[{\"a\": 3, \"b\": 100}, 3, 0]', array, false",  // block + array --> array
    "'', '{\"a\": 3} + [3, \"a\"]', '[{\"a\": 3}, 3, \"a\"]', array, false",  // block + array --> array
    "'', '{\"a\": 3, \"b\": 100} + \"a\"', null, null, true",  // block + string --> ERROR
    "'', '{\"a\": 3, \"b\": 100} + 0', null, null, true",  // block + int --> ERROR
    "'', '{\"a\": 3, \"b\": 100} + 0.5', null, null, true",  // block + float --> ERROR
    "'', '{\"a\": 3, \"b\": 100} + false', null, null, true",  // block + boolean --> ERROR
    "'', '{\"a\": 3, \"b\": 100} + null', '{\"a\": 3, \"b\": 100}', block, false",  // block + null --> block  (Adding null is meaningless.)
    "'', '[3, \"a\"] + {\"a\": 3}', '[3, \"a\", {\"a\": 3}]', array, false",  // array + block --> array
    "'', '[true, -1] + [+1, false]', '[true, -1, [1, false]]', array, false",  // array + array --> array
    "'', '[true, -1, +1, false] + [\"a\", null]', '[true, -1, 1, false, [\"a\", null]]', array, false",  // array + array --> array
    "'', '[true, -1, +1, false, \"a\"] + [null]', null, null, true",  // array + array --> ERROR because max array size is limited as 5.
    "'', '[8.5, null] + \"*/\"', '[8.5, null, \"*/\"]', array, false",  // array + string --> array
    "'', '[0, false, ] + 8', '[0, false, 8]', array, false",  // array + int --> array
    "'', '[0, false, 1, 2] + 8', '[0, false, 1, 2, 8]', array, false",  // array + int --> array
    "'', '[0, false, 1, 2, 3] + 8', null, null, true",  // array + int --> ERROR because max array size is limited as 5.
    // "'', '[\"x\", \"y\"] + 0.000001', '[\"x\", \"y\", 0.000001]', array, false",  // array + float --> array
    "'', '[\"x\", \"y\"] + 0.000001', '[\"x\", \"y\", 1.0E-6]', array, false",  // array + float --> array
    "'', '[3, \"a\"] + true', '[3, \"a\", true]', array, false",  // array + boolean --> array
    "'', '[0] + null', '[0, null]', array, false",  // array + null --> array  (Append null as element even if adding null.)
    "'', '\"abc\" + {\"a\": 0}', null, null, true",  // string + block --> ERROR
    "'', '\"abc\" + [\"def\"]', '[\"abc\", \"def\"]', array, false",  // string + array --> array
    "'', '\"abc\" + \"def\"', abcdef, string, false",  // string + string --> string
    "'', '\"0.0\" + 1000', '0.01000', string, false",  // string + int --> string
    "'', '\"88.88\" + 1.75', '88.881.75', string, false",  // string + float --> string
    "'', '\"100\" + false', '100false', string, false",  // string + boolean --> string
    "'', '\"null\" + null', null, string, false",  // string + null --> string  (Adding null is meaningless.)
    "'', '10000 + {\"a\": 0}', null, null, true",  // int + block --> ERROR
    "'', '10000 + [0]', '[10000, 0]', array, false",  // int + array --> array
    "'', '-5 + \".0\"', '-5.0', string, false",  // int + str --> str
    "'', '3 + 2', 5, int, false",  // int + int --> int
    "'', '-3+-2', -5, int, false",  // int + int --> int
    "'', '1000000 + 0.1', 1000000.1, float, false",  // int + float --> float
    "'', '0 + false', false, boolean, false",  // int + boolean --> boolean
    "'', '0 + true', true, boolean, false",  // int + boolean --> boolean
    "'', '1 + false', true, boolean, false",  // int + boolean --> boolean
    "'', '1 + true', true, boolean, false",  // int + boolean --> boolean
    "'', '100 + null', 100, int, false",  // int + null --> int  (Adding null is meaningless.)
    "'', '1.5 + {\"x\": null}', null, null, true",  // float + block --> ERROR
    "'', '15e3 + [null, \"x\"]', '[15000.0, null, \"x\"]', array, false",  // float + array --> array
    "'', '1e-3+\"a\"', '0.001a', string, false",  // float + string --> string
    "'', '-0.01e+5+3', -997.0, float, false",  // float + int --> float
    "'', '5.6 + 8.2', 13.8, float, false",  // float + float --> float
    "'', '0.0 + false', false, boolean, false",  // float + boolean --> boolean
    "'', '0.0 + true', true, boolean, false",  // float + boolean --> boolean
    "'', '0.1 + false', true, boolean, false",  // float + boolean --> boolean
    "'', '0.1 + true', true, boolean, false",  // float + boolean --> boolean
    "'', '1e-10 + null', 1e-10, float, false",  // float + null --> float  (Adding null is meaningless.)
    "'', 'false + {\"a\": 3}', null, null, true",  // boolean + block --> ERROR
    "'', 'false + [3,]', '[false, 3]', array, false",  // boolean + array --> array
    "'', 'false + [3,2,1,0]', '[false, 3, 2, 1, 0]', array, false",  // boolean + array --> array
    "'', 'false + [4,3,2,1,0]', null, null, true",  // boolean + array --> ERROR because max array size is limited as 5.
    "'', 'true + \"0\"', 'true0', string, false",  // boolean + string --> string
    "'', 'false + 0', false, boolean, false",  // boolean + int --> boolean
    "'', 'false + 1', true, boolean, false",  // boolean + int --> boolean
    "'', 'true + 0', true, boolean, false",  // boolean + int --> boolean
    "'', 'true + 1', true, boolean, false",  // boolean + int --> boolean
    "'', 'false + 0.0', false, boolean, false",  // boolean + float --> boolean
    "'', 'false + 0.1', true, boolean, false",  // boolean + float --> boolean
    "'', 'true + 0.0', true, boolean, false",  // boolean + float --> boolean
    "'', 'true + 0.1', true, boolean, false",  // boolean + float --> boolean
    "'', 'false + false', false, boolean, false",  // boolean + boolean --> boolean
    "'', 'false + true', true, boolean, false",  // boolean + boolean --> boolean
    "'', 'true + false', true, boolean, false",  // boolean + boolean --> boolean
    "'', 'true + true', true, boolean, false",  // boolean + boolean --> boolean
    "'', 'true + null', true, boolean, false",  // boolean + null --> boolean  (Adding null is meaningless.)
    "'', 'null + {\"a\": 3}', {\"a\": 3}, block, false",  // null + block --> block  (Adding null is meaningless.)
    "'', 'null + [false, [1, 2]]', '[null, false, [1, 2]]', array, false",  // null + array --> array  (Append null as element even if adding null.)
    "'', 'null + \"\"', '', string, false",  // null + string --> string  (Adding null is meaningless.)
    "'', 'null + \"abc\"', 'abc', string, false",  // null + string --> string  (Adding null is meaningless.)
    "'', 'null + 0', 0, int, false",  // null + int --> int  (Adding null is meaningless.)
    "'', 'null + -1e2', -100.0, float, false",  // null + float --> float  (Adding null is meaningless.)
    "'', 'null + false', false, boolean, false",  // null + boolean --> boolean  (Adding null is meaningless.)
    "'', 'null + true', true, boolean, false",  // null + boolean --> boolean  (Adding null is meaningless.)
    "'x = len, y = len', 'x + y', null, null, true",  // function + function --> ERROR
    "'x = len, y = len', '3 + y', null, null, true",  // int + function --> ERROR
    "'x = len, y = len', 'x + 3', null, null, true",  // function + int --> ERROR
    "'function test() {return(1)}, x = test(), y = test()', 'x + y', 2, int, false",
    "'function test() {return(1)}, x = test, y = test', 'x + y', null, null, true",  // function + function --> ERROR
    "'function test() {return(1)}, x = test(), y = test', 'x + y', null, null, true",  // int + function --> ERROR
    "'function test() {return(1)}, x = test, y = test()', 'x + y', null, null, true",  // function + int --> ERROR

    // for Evaluator._subtract
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - {\"b\": 5, \"c\": 10}', '{\"a\": 10, \"b\": 15, \"c\": 20}', block, false",  // block - block --> block
    "'', '{\"a\": 10, \"b\": 20} - {\"b\": 5, \"c\": 10}', '{\"a\": 10, \"b\": 15, \"c\": -10}', block, false",  // block - block --> block
    "'', '{\"a\": 10, \"b\": 20} - {\"c\": 10, \"d\": -100, \"e\": 50, \"f\": 1}', null, null, true",  // block - block --> ERROR because max array size is limited as 5.
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - [\"b\", \"c\"]', '{\"a\": 10}', block, false",  // block - array --> block
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - [\"b\", \"c\", 3]', null, null, true",  // block - array --> ERROR because there is number in array
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - [true]', null, null, true",  // block - array --> ERROR because there is boolean in array
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - \"b\"', '{\"a\": 10, \"c\": 30}', block, false",  // block - string --> block
    "'', '{\"a\": 10, \"b\": 20, \"c\": 30} - \"d\"', '{\"a\": 10, \"b\": 20, \"c\": 30}', block, false",  // block - string --> block
    "'', '{\"a\": 10} - 3', null, null, true",  // block - int --> ERROR
    "'', '{\"a\": 10} - 1.5', null, null, true",  // block - float --> ERROR
    "'', '{\"a\": 10} - true', null, null, true",  // block - boolean --> ERROR
    "'', '{\"a\": 10} - null', '{\"a\": 10}', block, false",  // block - null --> block  (Removing null is meaningless.)
    "'', '[3, {\"a\": null}, {\"a\": null}] - {\"a\": null}', '[3]', array, false",  // array - block --> array
    "'', '[3, \"a\", null] - {\"a\": null}', '[3, \"a\", null]', array, false",  // array - block --> array
    "'', '[3, [\"a\", null], [\"a\", null]] - [\"a\", null]', '[3]', array, false",  // array - array --> array
    "'', '[3, [\"a\", null]] - [\"a\", null]', '[3]', array, false",  // array - array --> array
    "'', '[3, \"a\", null] - [\"a\", null]', '[3, \"a\", null]', array, false",  // array - array --> array
    "'', '[3, \"a\", \"a\"] - \"a\"', '[3]', array, false",  // array - string --> array
    "'', '[3, \"a\", \"a\"] - 3', '[\"a\", \"a\"]', array, false",  // array - int --> array
    "'', '[3, \"a\", \"a\"] - 3.0', '[\"a\", \"a\"]', array, false",  // array - float --> array
    "'', '[3, true, \"a\"] - true', '[3, \"a\"]', array, false",  // array - boolean --> array
    "'', '[3, false, \"a\"] - false', '[3, \"a\"]', array, false",  // array - boolean --> array
    "'', '[3, false, \"a\"] - true', '[3, false, \"a\"]', array, false",  // array - boolean --> array
    "'', '[3, \"a\", null] - null', '[3, \"a\"]', array, false",  // array - null --> array  (Remove null of element.)
    "'', '\"abc: 3\" - {\"abc\": 3}', null, null, true",  // string - block --> ERROR
    "'', '\"large-dog&small-dog&2cat\" - [\"large-\", \"small-\"]', 'dog&dog&2cat', string, false",  // string - array --> string
    "'', '\"large-dog&small-dog&2cat\" - \"dog\"', 'large-&small-&2cat', string, false",  // string - string --> string
    "'', '\"large-dog&small-dog&2cat\" - 2', null, null, true",  // string - int --> ERROR
    "'', '\"large-dog&small-dog&200.5t\" - 200.5', null, null, true",  // string - float --> ERROR
    "'', '\"large-dog&small-dog&true\" - true', null, null, true",  // string - boolean --> ERROR
    "'', '\"large-dog&small-dog&null\" - null', 'large-dog&small-dog&null', string, false",  // string - null --> string  (Removing null is meaningless.)
    "'', '100 - {\"100\": 100}', null, null, true",  // int - block --> ERROR
    "'', '100 - [100]', null, null, true",  // int - array --> ERROR
    "'', '100 - \"100\"', null, null, true",  // int - string --> ERROR
    "'', '100 - 100', 0, int, false",  // int - int --> int
    "'', '100 - 100.5', -0.5, float, false",  // int - float --> float
    "'', '100 - 100.0', 0.0, float, false",  // int - float --> float
    "'', '100 - true', null, null, true",  // int - boolean --> ERROR
    "'', '100 - null', 100, int, false",  // int - null --> int  (Removing null is meaningless.)
    "'', '100.5 - {\"100.5\": 100.5}', null, null, true",  // float - block --> ERROR
    "'', '100.5 - [100.5]', null, null, true",  // float - array --> ERROR
    "'', '100.5 - \"100.5\"', null, null, true",  // float - string --> ERROR
    "'', '100.5 - 100', 0.5, float, false",  // float - int --> float
    "'', '100.5 - 100.5', 0.0, float, false",  // float - float --> float
    "'', '100.5 - 100.0', 0.5, float, false",  // float - float --> float
    "'', '100.5 - true', null, null, true",  // float - boolean --> ERROR
    "'', '100.5 - null', 100.5, float, false",  // float - null --> float  (Removing null is meaningless.)
    "'', 'true - {\"true\": true}', null, null, true",  // boolean - block --> ERROR
    "'', 'true - [true]', null, null, true",  // boolean - array --> ERROR
    "'', 'true - \"true\"', null, null, true",  // boolean - string --> ERROR
    "'', 'true - 1', null, null, true",  // boolean - int --> ERROR
    "'', 'true - 1.0', null, null, true",  // boolean - float --> ERROR
    "'', 'true - true', null, null, true",  // boolean - boolean --> ERROR
    "'', 'true - null', true, boolean, false",  // boolean - null --> boolean  (Removing null is meaningless.)
    "'', 'null - {}', null, null, true",  // null - block --> ERROR
    "'', 'null - {\"null\": null}', null, null, true",  // null - block --> ERROR
    "'', 'null - []', null, null, true",  // null - array --> ERROR
    "'', 'null - [100, 5]', null, null, true",  // null - array --> ERROR
    "'', 'null - \"xyz\"', null, null, true",  // null - string --> ERROR
    "'', 'null - 100', null, null, true",  // null - int --> ERROR
    "'', 'null - 100.5', null, null, true",  // null - float --> ERROR
    "'', 'null - true', null, null, true",  // null - boolean --> ERROR
    "'', 'null - null', null, null, false",  // null - null --> null
    "'function test() {return(1)}, x = test, y = test', 'x - y', null, null, true",  // function - function --> ERROR

    // for Evaluator._multiply
    "'', '{\"a\": 2} * {\"a\": 10, \"b\": 3}', '{\"a\": 20, \"b\": null}', block, false",  // block * block --> block
    "'', '{\"a\": 2} * {\"b\": 3}', '{\"a\": null, \"b\": null}', block, false",  // block * block --> block
    "'', '{\"a\": 2, \"b\": 3, \"c\": 4, \"d\": 5, \"e\": 6} * {\"f\": 1}', null, null, true",  // block * block --> ERROR because max array size is limited as 5.
    "'', '{\"a\": 2} * [\"b\", 3]', null,  null, true",  // block * array --> ERROR
    "'', '{\"a\": 2} * \"b\"', null,  null, true",  // block * string --> ERROR
    "'', '{\"a\": 2} * 3', null,  null, true",  // block * int --> ERROR
    "'', '{\"a\": 2} * 3.5', null,  null, true",  // block * float --> ERROR
    "'', '{\"a\": 2} * false', null,  null, true",  // block * boolean --> ERROR
    "'', '{\"a\": 2} * null', null,  null, false",  // block * null --> null
    "'', '[100] * {\"b\": 3}', null,  null, true",  // array * block --> ERROR
    "'', '[100] * [3]', null,  null, true",  // array * array --> ERROR
    "'', '[100, 3, 1e+2] * \"|\"', '100|3|100.0', string, false",  // array * string --> string  (Concatenate elements with delimiter)
    "'', '[100] * 5', '[100, 100, 100, 100, 100]', array, false",  // array * int --> array  (for initialization of array)
    "'', '[100] * 6', null, null, true",  // array * int --> ERROR because max array size is limited as 5.
    "'', '[100, \"a\"] * 2', '[100, \"a\", 100, \"a\"]', array, false",  // array * int --> array  (for initialization of array)
    "'', '[100, \"a\"] * 3', null, null, true",  // array * int --> ERROR because max array size is limited as 5.
    "'', '[100] * 3.7', '[100, 100, 100]', array, false",  // array * float --> array  (for initialization of array)
    "'', '[100, \"a\"] * 2.7', '[100, \"a\", 100, \"a\"]', array, false",  // array * float --> array  (for initialization of array)
    "'', '[100, \"a\"] * 3.7', null, null, true",  // array * float --> ERROR because max array size is limited as 5.
    "'', '[100] * true', null, null, true",  // array * boolean --> ERROR
    "'', '[100] * false', null, null, true",  // array * boolean --> ERROR
    "'', '[100] * null', null, null, false",  // array * null --> null
    "'', '\"|\" * {\"a\", 100}', null, null, true",  // string * block --> ERROR
    "'', '\"|\" * [100, \"a\"]', '100|a', string, false",  // string * array --> string  (Concatenate elements with delimiter)
    "'', '\"3a\" * \"3a\"', null, null, true",  // string * string --> ERROR
    "'', '\"3a\" * 2', '3a3a', string, false",  // string * int --> string
    "'', '\"3a\" * 2.9', '3a3a', string, false",  // string * float --> string
    "'', '\"3a\" * true', null, null, true",  // string * boolean --> ERROR
    "'', '\"3a\" * false', null, null, true",  // string * boolean --> ERROR
    "'', '\"3a\" * null', null, null, false",  // string * null --> null
    "'', '3 * {\"a\": 50}', null, null, true",  // int * block --> ERROR
    "'', '3 * [50]', '[50, 50, 50]', array, false",  // int * array --> array  (for initialization of array)
    "'', '2 * \"3a\"', '3a3a', string, false",  // int * string --> string
    "'', '2 * 60', 120, int, false",  // int * int --> int
    "'', '2 * 60.0', 120.0, float, false",  // int * float --> float
    "'', '2 * 60.7', 121.4, float, false",  // int * float --> float
    "'', '2 * true', null, null, true",  // int * boolean --> ERROR
    "'', '2 * false', null, null, true",  // int * boolean --> ERROR
    "'', '2 * null', null, null, false",  // int * null --> null
    "'', '3.8 * {\"a\": 50}', null, null, true",  // float * block --> ERROR
    "'', '3.8 * [50]', '[50, 50, 50]', array, false",  // float * array --> array  (for initialization of array)
    "'', '2.8 * \"3a\"', '3a3a', string, false",  // float * string --> string
    "'', '2.8 * 60', 168.0, float, false",  // float * int --> float
    "'', '2.8 * 60.7', 169.96, float, false",  // float * float --> float
    "'', '2.8 * true', null, null, true",  // float * boolean --> ERROR
    "'', '2.8 * false', null, null, true",  // float * boolean --> ERROR
    "'', '2.8 * null', null, null, false",  // float * null --> null
    "'', 'true * {\"a\": 3}', null, null, true",  // boolean * block --> ERROR
    "'', 'true * [3, 2]', null, null, true",  // boolean * array --> ERROR
    "'', 'false * [3, 2]', null, null, true",  // boolean * array --> ERROR
    "'', 'true * \"x1\"', null, null, true",  // boolean * string --> ERROR
    "'', 'false * \"x1\"', null, null, true",  // boolean * string --> ERROR
    "'', 'true * 20', null, null, true",  // boolean * int --> ERROR
    "'', 'false * 20', null, null, true",  // boolean * int --> ERROR
    "'', 'true * 20.5', null, null, true",  // boolean * float --> ERROR
    "'', 'false * 20.5', null, null, true",  // boolean * float --> ERROR
    "'', 'true * true', null, null, true",  // boolean * boolean --> ERROR
    "'', 'true * false', null, null, true",  // boolean * boolean --> ERROR
    "'', 'false * true', null, null, true",  // boolean * boolean --> ERROR
    "'', 'false * false', null, null, true",  // boolean * boolean --> ERROR
    "'', 'null * {\"a\": 3}', null, null, false",  // null * block --> ERROR
    "'', 'null * [3, 2]', null, null, false",  // null * array --> null
    "'', 'null * \"a\"', null, null, false",  // null * string --> null
    "'', 'null * 3', null, null, false",  // null * int --> null
    "'', 'null * 3.8', null, null, false",  // null * float --> null
    "'', 'null * true', null, null, false",  // null * boolean --> null
    "'', 'null * false', null, null, false",  // null * boolean --> null
    "'', 'null * null', null, null, false",  // null * null --> null
    "'function test() {return(1)}, x = test, y = test', 'x * y', null, null, true",  // function * function --> ERROR

    // for Evaluator._divide
    "'', '{\"a\": 2} / {\"a\": 10, \"b\": 3}', '{\"a\": 0.2, \"b\": null}', block, false",  // block / block --> block
    "'', '{\"a\": 2} / {\"a\": 2, \"b\": 3, \"c\": 4, \"d\": 5, \"e\": 6, \"f\": 7}', null, null, true",  // block / block --> ERROR because max array size is limited as 5.
    "'', '{\"a\": 2} / {\"b\": 3}', null, null, true",  // block / block --> ERROR
    "'', '{\"a\": 2} / [2]', null, null, true",  // block / array --> ERROR
    "'', '{\"a\": 2} / \"a\"', null, null, true",  // block / string --> ERROR
    "'', '{\"a\": 2} / 2', null, null, true",  // block / int --> ERROR
    "'', '{\"a\": 2.0} / 2.0', null, null, true",  // block / float --> ERROR
    "'', '{\"a\": true} / true', null, null, true",  // block / boolean --> ERROR
    "'', '{\"a\": true} / null', null, null, true",  // block / null --> ERROR
    "'', '[3, 2] / [2]', null, null, true",  // array / array --> ERROR
    "'', '[3, 2] / \",\"', null, null, true",  // array / string --> ERROR
    "'', '[3, 2] / 2', null, null, true",  // array / int --> ERROR
    "'', '[3, 2] / 2.5', null, null, true",  // array / float --> ERROR
    "'', '[3, 2] / false', null, null, true",  // array / boolean --> ERROR
    "'', '[3, 2] / true', null, null, true",  // array / boolean --> ERROR
    "'', '[3, 2] / null', null, null, true",  // array / null --> ERROR
    "'', '\"a,b\" / {\"a\": \",\"}', null, null, true",  // string / block --> ERROR
    "'', '\"a,b\" / [\",\"]', null, null, true",  // string / array --> ERROR
    "'', '\"a,b\" / \",\"', '[\"a\", \"b\"]', array, false",  // string / string --> array
    "'', '\"a,b,\" / \",\"', '[\"a\", \"b\", \"\"]', array, false",  // string / string --> array
    "'', '\"a,b,c,d,e\" / \",\"', '[\"a\", \"b\", \"c\", \"d\", \"e\"]', array, false",  // string / string --> array
    "'', '\"a,b,c,d,e,f\" / \",\"', null, null, true",  // string / string --> ERROR because max array size is limited as 5.
    "'', '\"a,b\" / \"\"', '[\"a\", \",\", \"b\"]', array, false",  // string / string --> array
    "'', '\"xxwwwwyy\" / \"ww\"', '[\"xx\", \"\", \"yy\"]', array, false",  // string / string --> array
    "'', '\"xxwwwyy\" / \"ww\"', '[\"xx\", \"wyy\"]', array, false",  // string / string --> array
    "'', '\"abcde\" / \"\"', '[\"a\", \"b\", \"c\", \"d\", \"e\"]', array, false",  // string / string --> array
    "'', '\"abcdef\" / \"\"', null, null, true",  // string / string --> ERROR because max array size is limited as 5.
    "'', '\"a,b\" / 2', null, null, true",  // string / int --> ERROR
    "'', '\"a,b\" / 2.5', null, null, true",  // string / float --> ERROR
    "'', '\"a,b\" / true', null, null, true",  // string / boolean --> ERROR
    "'', '\"a,b\" / false', null, null, true",  // string / boolean --> ERROR
    "'', '\"a,b\" / null', null, null, true",  // string / null --> ERROR
    "'', '20 / {\"a\": 2}', null, null, true",  // int / block --> ERROR
    "'', '20 / [2, 5]', null, null, true",  // int / array --> ERROR
    "'', '20 / \"0\"', null, null, true",  // int / string --> ERROR
    "'', '20 / 5', 4, int, false",  // int / int --> int or float or ERROR
    "'', '20 / 8', 2.5, float, false",  // int / int --> int or float or ERROR
    "'', '20 / 0', null, null, true",  // int / 0 --> ERROR
    "'', '20 / 0.0', null, null, true",  // int / 0.0 --> ERROR
    "'', '20 / 2.5', 8, int, false",  // int / float --> int or float or ERROR
    "'', '20 / 8', 2.5, float, false",  // int / float --> int or float or ERROR
    "'', '20 / null', null, null, true",  // int / null --> ERROR
    "'', '3.5 / {\"a\": 2.5}', null, null, true",  // float / block --> ERROR
    "'', '3.5 / [1, 2]', null, null, true",  // float / array --> ERROR
    "'', '3.5 / \".\"', null, null, true",  // float / string --> ERROR
    "'', '3.5 / \"0.5\"', null, null, true",  // float / string --> ERROR
    "'', '3.5 / 5', 0.7, float, false",  // float / int --> int or float or ERROR
    "'', '3.0 / 3', 1, int, false",  // float / int --> int or float or ERROR
    "'', '3.0 / 0', null, null, true",  // float / 0 --> ERROR
    "'', '3.5 / 0.5', 7, int, false",  // float / float --> int or float or ERROR
    "'', '0.49 / 0.7', 0.7, float, false",  // float / float --> int or float or ERROR
    "'', '0.49 / 0.0', null, null, true",  // float / 0.0 --> ERROR
    "'', '3.5 / true', null, null, true",  // float / boolean --> ERROR
    "'', '3.5 / false', null, null, true",  // float / boolean --> ERROR
    "'', '3.5 / null', null, null, true",  // float / null --> ERROR
    "'', 'true / {\"true\": true}', null, null, true",  // boolean / block --> ERROR
    "'', 'true / [true]', null, null, true",  // boolean / array --> ERROR
    "'', 'false / [true]', null, null, true",  // boolean / array --> ERROR
    "'', 'true / [false]', null, null, true",  // boolean / array --> ERROR
    "'', 'false / [false]', null, null, true",  // boolean / array --> ERROR
    "'', 'true / \"true\"', null, null, true",  // boolean / string --> ERROR
    "'', 'false / \"false\"', null, null, true",  // boolean / string --> ERROR
    "'', 'true / 1', null, null, true",  // boolean / int --> ERROR
    "'', 'false / 1', null, null, true",  // boolean / int --> ERROR
    "'', 'true / 1.5', null, null, true",  // boolean / float --> ERROR
    "'', 'false / 1.5', null, null, true",  // boolean / float --> ERROR
    "'', 'true / true', null, null, true",  // boolean / boolean --> ERROR
    "'', 'false / true', null, null, true",  // boolean / boolean --> ERROR
    "'', 'true / null', null, null, true",  // boolean / null --> ERROR
    "'', 'false / null', null, null, true",  // boolean / null --> ERROR
    "'', 'null / {\"a\": null}', null, null, false",  // null / block --> null
    "'', 'null / [null]', null, null, false",  // null / array --> null
    "'', 'null / [1, 2]', null, null, false",  // null / array --> null
    "'', 'null / \"a\"', null, null, false",  // null / string --> null
    "'', 'null / 2', null, null, false",  // null / int --> null
    "'', 'null / 0', null, null, true",  // null / int --> ERROR
    "'', 'null / 8.8', null, null, false",  // null / float --> null
    "'', 'null / 0.0', null, null, true",  // null / float --> ERROR
    "'', 'null / false', null, null, false",  // null / boolean --> null
    "'', 'null / null', null, null, true",  // null / null --> ERROR
    "'function test() {return(1)}, x = test, y = test', 'x / y', null, null, true",  // function / function --> ERROR

    // for Evaluator._modulo
    "'', '{\"a\": 20} % {\"a\": 6, \"b\": 3}', '{\"a\": 2, \"b\": null}', block, false",  // block % block --> block
    "'', '{\"a\": 20} % {\"a\": 2, \"b\": 3, \"c\": 4, \"d\": 5, \"e\": 6, \"f\": 7}', null, null, true",  // block % block --> ERROR because max array size is limited as 5.
    "'', '{a: 20} % {a: 6, b: 3}', '{\"a\": 2, \"b\": null}', block, false",  // block % block --> block
    "'', '{\"a\": 2} % {\"b\": 3}', null, null, true",  // block % block --> ERROR
    "'', '{\"a\": 2} % [2]', null, null, true",  // block % array --> ERROR
    "'', '{\"a\": 2} % \"a\"', null, null, true",  // block % string --> ERROR
    "'', '{\"a\": 2} % 2', null, null, true",  // block % int --> ERROR
    "'', '{\"a\": 2} % 1.5', null, null, true",  // block % float --> ERROR
    "'', '{\"a\": 2} % true', null, null, true",  // block % boolean --> ERROR
    "'', '{\"a\": 2} % null', null, null, true",  // block % null --> ERROR
    "'', '[2, 3] % {\"a\": 2}', null, null, true",  // array % block --> ERROR
    "'', '[2, 3] % [2, 3]', null, null, true",  // array % array --> ERROR
    "'', '[2, 3] % \",\"', null, null, true",  // array % string --> ERROR
    "'', '[2, 3] % 2', null, null, true",  // array % int --> ERROR
    "'', '[2, 3] % 2.5', null, null, true",  // array % float --> ERROR
    "'', '[2, 3] % true', null, null, true",  // array % boolean --> ERROR
    "'', '[2, 3] % null', null, null, true",  // array % null --> ERROR
    "'', '\"a, 2\" % {\"a\", 2}', null, null, true",  // string % block --> ERROR
    "'', '\"2, 3\" % [2]', null, null, true",  // string % array --> ERROR
    "'', '\"2, 3\" % \",\"', null, null, true",  // string % string --> ERROR
    "'', '\"2, 3\" % 2', null, null, true",  // string % int --> ERROR
    "'', '\"2, 3\" % 2.0', null, null, true",  // string % float --> ERROR
    "'', '\"2, 3\" % true', null, null, true",  // string % boolean --> ERROR
    "'', '\"2, 3\" % null', null, null, true",  // string % null --> ERROR
    "'', '10 % {\"a\": 2}', null, null, true",  // int % block --> ERROR
    "'', '10 % [3]', null, null, true",  // int % array --> ERROR
    "'', '10 % \"3\"', null, null, true",  // int % string --> ERROR
    "'', '10 % 3', 1, int, false",  // int % int --> int or float or ERROR, 10 = 3 x 3 + 1
    "'', '-10 % 3', 2, int, false",  // int % int --> int or float or ERROR, -10 = 3 x (-4) + 2 (NG: -10 = 3 x (-3) - 1)
    "'', '10 % -3', -2, int, false",  // int % int --> int or float or ERROR, 10 = -3 x (-4) - 2 (NG: 10 = -3 x (-3) + 1)
    "'', '-10 % -3', -1, int, false",  // int % int --> int or float or ERROR, -10 = -3 x 3 - 1 (NG: -10 = -3 x 4 + 2)
    "'', '10 % 0', null, null, true",  // int % 0 --> ERROR
    "'', '10 % 3.0', 1, int, false",  // int % float --> int or float or ERROR
    "'', '10 % 1.7', 1.5, float, false",  // int % float --> int or float or ERROR
    "'', '10 % 0.0', null, null, true",  // int % 0.0 --> ERROR
    "'', '10 % true', null, null, true",  // int % boolean --> ERROR
    "'', '10 % null', null, null, true",  // int % null --> ERROR
    "'', '10.5 % {\"a\": 3.0}', null, null, true",  // float % block --> ERROR
    "'', '10.5 % [3.0]', null, null, true",  // float % array --> ERROR
    "'', '10.5 % \"a\"', null, null, true",  // float % string --> ERROR
    "'', '10.5 % 3', 1.5, float, false",  // float % int --> int or float or ERROR
    "'', '10.5 % 0', null, null, true",  // float % 0 --> ERROR
    "'', '17.5 % 1.5', 1, int, false",  // float % float --> int or float or ERROR
    "'', '10.5 % 1.7', 0.3, float, false",  // float % float --> int or float or ERROR
    "'', '10.5 % 0.0', null, null, true",  // float % 0.0 --> ERROR
    "'', '10.5 % true', null, null, true",  // float % boolean --> ERROR
    "'', '10.5 % null', null, null, true",  // float % null --> ERROR
    "'', 'true % {\"true\": true}', null, null, true",  // boolean % block --> ERROR
    "'', 'true % [true]', null, null, true",  // boolean % array --> ERROR
    "'', 'true % \"true\"', null, null, true",  // boolean % string --> ERROR
    "'', 'true % 3', null, null, true",  // boolean % int --> ERROR
    "'', 'true % 1.7', null, null, true",  // boolean % float --> ERROR
    "'', 'true % true', null, null, true",  // boolean % boolean --> ERROR
    "'', 'true % null', null, null, true",  // boolean % null --> ERROR
    "'', 'null % {\"a\": null}', null, null, false",  // null % block --> null
    "'', 'null % [null]', null, null, false",  // null % array --> null
    "'', 'null % \"null\"', null, null, false",  // null % string --> null
    "'', 'null % 3', null, null, false",  // null % int --> null
    "'', 'null % 0', null, null, true",  // null % int --> ERROR
    "'', 'null % 1.7', null, null, false",  // null % float --> null
    "'', 'null % 0.0', null, null, true",  // null % float --> ERROR
    "'', 'null % true', null, null, false",  // null % boolean --> null
    "'', 'null % null', null, null, true",  // null % null --> ERROR
    "'function test() {return(1)}, x = test, y = test', 'x % y', null, null, true",  // function % function --> ERROR

    // for Evaluator._logical_invert
    "'a = {}', 'not a', false, boolean, false",  // not block --> boolean (using truthy) (always false)
    "'a = {\"a\": 3}', 'not a', false, boolean, false",  // not block --> boolean (using truthy) (always false)
    "'', 'not {\"a\": 3}', null, null, true",  // not {"a": 3} is confused as function "not". --> ERROR
    "'a = []', 'not a', false, boolean, false",  // not array --> boolean (using truthy) (always false)
    "'a = [3]', 'not a', false, boolean, false",  // not array --> boolean (using truthy) (always false)
    "'', 'not [3]', null, null, true",  // not [3] is confused as variable "not". --> ERROR
    "'', 'not \"\"', false, boolean, false",  // not string --> boolean (using truthy) (always false)
    "'', 'not 0', true, boolean, false",  // not int --> boolean (using truthy)
    "'', 'not 10', false, boolean, false",  // not int --> boolean (using truthy)
    "'', 'not 0.0', true, boolean, false",  // not float --> boolean (using truthy)
    "'', 'not 5.0', false, boolean, false",  // not float --> boolean (using truthy)
    "'', 'not true', false, boolean, false",  // not boolean --> boolean
    "'', 'not false', true, boolean, false",  // not boolean --> boolean
    "'', 'not null', true, boolean, false",  // not null --> boolean (using truthy) (always true)
    "'', 'false not false', null, null, true",  // "not" is binary expression in grammar.
    "'function test() {return(1)}, x = test', 'not x', false, boolean, false",  // not function --> boolean (using truthy)

    // for Evaluator._logical_and
    "'', 'false and false', false, boolean, false",
    "'', 'false and true', false, boolean, false",
    "'', 'true and false', false, boolean, false",
    "'', 'true and true', true, boolean, false",
    "'', '{} and false', false, boolean, false",  // using truthy
    "'', '{} and true', true, boolean, false",  // using truthy
    "'', '{\"a\": 3} and false', false, boolean, false",  // using truthy
    "'', '{\"a\": 3} and true', true, boolean, false",  // using truthy
    "'', '[] and false', false, boolean, false",  // using truthy
    "'', '[] and true', true, boolean, false",  // using truthy
    "'', '[3] and false', false, boolean, false",  // using truthy
    "'', '[3] and true', true, boolean, false",  // using truthy
    "'', '\"a\" and false', false, boolean, false",  // using truthy
    "'', '\"a\" and true', true, boolean, false",  // using truthy
    "'', '10 and false', false, boolean, false",  // using truthy
    "'', '10 and true', true, boolean, false",  // using truthy
    "'', '0 and false', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '0 and true', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '10.5 and false', false, boolean, false",  // using truthy
    "'', '10.5 and true', true, boolean, false",  // using truthy
    "'', '0.0 and false', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '0.0 and true', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'null and false', false, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'null and true', false, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'false and {}', false, boolean, false",  // using truthy
    "'', 'true and {}', true, boolean, false",  // using truthy
    "'', 'false and {\"a\": 3}', false, boolean, false",  // using truthy
    "'', 'true and {\"a\": 3}', true, boolean, false",  // using truthy
    "'', 'false and []', false, boolean, false",  // using truthy
    "'', 'true and []', true, boolean, false",  // using truthy
    "'', 'false and [3]', false, boolean, false",  // using truthy
    "'', 'true and [3]', true, boolean, false",  // using truthy
    "'', 'false and \"a\"', false, boolean, false",  // using truthy
    "'', 'true and \"a\"', true, boolean, false",  // using truthy
    "'', 'false and 10', false, boolean, false",  // using truthy
    "'', 'true and 10', true, boolean, false",  // using truthy
    "'', 'false and 0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'true and 0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'false and 10.5', false, boolean, false",  // using truthy
    "'', 'true and 10.5', true, boolean, false",  // using truthy
    "'', 'false and 0.0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'true and 0.0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'false and null', false, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'true and null', false, boolean, false",  // using truthy, null is equivalent to false.
    "'y = 0; function y10() {y = 10, return(true)}, z = 0; if (false or y10()) {z = 20}', '[y, z]', '[10, 20]', array, false",
    "'y = 0; function y10() {y = 10, return(true)}, z = 0; if (true or y10()) {z = 20}', '[y, z]', '[0, 20]', array, false",  // Short-circuit evaluation
    "'y = 0; function y10() {y = 10, return(true)}, z = 0; if (false and y10()) {z = 20}', '[y, z]', '[0, 0]', array, false",  // Short-circuit evaluation
    "'y = 0; function y10() {y = 10, return(true)}, z = 0; if (true and y10()) {z = 20}', '[y, z]', '[10, 20]', array, false",
    "'function test() {return(1)}, x = test, y = test', 'x and y', true, boolean, false",  // using truthy

    // for Evaluator._logical_or
    "'', 'false or false', false, boolean, false",
    "'', 'false or true', true, boolean, false",
    "'', 'true or false', true, boolean, false",
    "'', 'true or true', true, boolean, false",
    "'', '{} or false', true, boolean, false",  // using truthy
    "'', '{} or true', true, boolean, false",  // using truthy
    "'', '{\"a\": 3} or false', true, boolean, false",  // using truthy
    "'', '{\"a\": 3} or true', true, boolean, false",  // using truthy
    "'', '[] or false', true, boolean, false",  // using truthy
    "'', '[] or true', true, boolean, false",  // using truthy
    "'', '[3] or false', true, boolean, false",  // using truthy
    "'', '[3] or true', true, boolean, false",  // using truthy
    "'', '\"a\" or false', true, boolean, false",  // using truthy
    "'', '\"a\" or true', true, boolean, false",  // using truthy
    "'', '10 or false', true, boolean, false",  // using truthy
    "'', '10 or true', true, boolean, false",  // using truthy
    "'', '0 or false', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '0 or true', true, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '10.5 or false', true, boolean, false",  // using truthy
    "'', '10.5 or true', true, boolean, false",  // using truthy
    "'', '0.0 or false', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', '0.0 or true', true, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'null or false', false, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'null or true', true, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'false or {}', true, boolean, false",  // using truthy
    "'', 'true or {}', true, boolean, false",  // using truthy
    "'', 'false or {\"a\": 3}', true, boolean, false",  // using truthy
    "'', 'true or {\"a\": 3}', true, boolean, false",  // using truthy
    "'', 'false or []', true, boolean, false",  // using truthy
    "'', 'true or []', true, boolean, false",  // using truthy
    "'', 'false or [3]', true, boolean, false",  // using truthy
    "'', 'true or [3]', true, boolean, false",  // using truthy
    "'', 'false or \"a\"', true, boolean, false",  // using truthy
    "'', 'true or \"a\"', true, boolean, false",  // using truthy
    "'', 'false or 10', true, boolean, false",  // using truthy
    "'', 'true or 10', true, boolean, false",  // using truthy
    "'', 'false or 0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'true or 0', true, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'false or 10.5', true, boolean, false",  // using truthy
    "'', 'true or 10.5', true, boolean, false",  // using truthy
    "'', 'false or 0.0', false, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'true or 0.0', true, boolean, false",  // using truthy, 0 is equivalent to false.
    "'', 'false or null', false, boolean, false",  // using truthy, null is equivalent to false.
    "'', 'true or null', true, boolean, false",  // using truthy, null is equivalent to false.
    "'function test() {return(1)}, x = test, y = test', 'x or y', true, boolean, false",  // using truthy

    // for Evaluator._is_equal
    "'', '{\"a\": 1, \"b\": 2} == {\"b\": 2, \"a\": 1}', true, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} == {\"a\": 1}', false, boolean, false",
    "'', '[1, 2] == [1, 2]', true, boolean, false",
    "'', '[1, 2] == [2, 1]', false, boolean, false",
    "'', '\"a\" == \"a\"', true, boolean, false",
    "'', '\"a\" == \"b\"', false, boolean, false",
    "'', '100 == 100', true, boolean, false",
    "'', '100 == 200', false, boolean, false",
    "'', '1.5 == 1.5', true, boolean, false",
    "'', '1.5 == 1.4', false, boolean, false",
    "'', 'true == true', true, boolean, false",
    "'', 'true == false', false, boolean, false",
    "'', 'null == null', true, boolean, false",
    "'a = null', 'null == a', true, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} == [\"a\", \"b\"]', false, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} == [1, 2]', false, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} == true', true, boolean, false",  // using truthy
    "'', '[1, 2] == true', true, boolean, false",  // using truthy
    "'', '[1, 2] == 1', false, boolean, false",  // using truthy
    "'', '\"a\" == true', true, boolean, false",  // using truthy
    "'', 'true == \"a\"', true, boolean, false",  // using truthy
    "'', '\"a\" == 1', false, boolean, false",  // using truthy
    "'', '100 == true', true, boolean, false",  // using truthy
    "'', 'true == 100', true, boolean, false",  // using truthy
    "'', '0 == false', true, boolean, false",  // using truthy
    "'', 'false == 0', true, boolean, false",  // using truthy
    "'', 'null == false', true, boolean, false",  // using truthy
    "'', 'false == null', true, boolean, false",  // using truthy
    "'function test() {return(1)}, x = test, y = test', 'x == y', true, boolean, false",

    // for Evaluator._is_not_equal
    "'', '{\"a\": 1, \"b\": 2} != {\"b\": 2, \"a\": 1}', false, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} != {\"a\": 1}', true, boolean, false",
    "'', '[1, 2] != [1, 2]', false, boolean, false",
    "'', '[1, 2] != [2, 1]', true, boolean, false",
    "'', '\"a\" != \"a\"', false, boolean, false",
    "'', '\"a\" != \"b\"', true, boolean, false",
    "'', '100 != 100', false, boolean, false",
    "'', '100 != 200', true, boolean, false",
    "'', '1.5 != 1.5', false, boolean, false",
    "'', '1.5 != 1.4', true, boolean, false",
    "'', 'true != true', false, boolean, false",
    "'', 'true != false', true, boolean, false",
    "'', 'null != null', false, boolean, false",
    "'a = null', 'null != a', false, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} != [\"a\", \"b\"]', true, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} != [1, 2]', true, boolean, false",
    "'', '{\"a\": 1, \"b\": 2} != false', true, boolean, false",  // using truthy
    "'', '{\"a\": 1, \"b\": 2} != true', false, boolean, false",  // using truthy
    "'', '[1, 2] != true', false, boolean, false",  // using truthy
    "'', '[1, 2] != 1', true, boolean, false",  // using truthy
    "'', '\"a\" != true', false, boolean, false",  // using truthy
    "'', 'true != \"a\"', false, boolean, false",  // using truthy
    "'', '\"a\" != 1', true, boolean, false",  // using truthy
    "'', '100 != true', false, boolean, false",  // using truthy
    "'', 'true != 100', false, boolean, false",  // using truthy
    "'', '0 != false', false, boolean, false",  // using truthy
    "'', 'false != 0', false, boolean, false",  // using truthy
    "'', 'null != false', false, boolean, false",  // using truthy
    "'', 'false != null', false, boolean, false",  // using truthy
    "'function test() {return(1)}, x = test, y = test', 'x != y', false, boolean, false",

    // for Evaluator._is_less_than
    "'', '3 < 5', true, boolean, false",
    "'', '3 < 3', false, boolean, false",
    "'', '-3 < 2', true, boolean, false",
    "'', '3 < -5', false, boolean, false",
    "'', '2.3 < 2.4', true, boolean, false",
    "'', '1e4 < 1e3', false, boolean, false",
    "'', '1e-4 < 1e-3', true, boolean, false",
    "'', '{} < 5', null, null, true",
    "'', '[] < 5', null, null, true",
    "'', '\"\" < 5', null, null, true",
    "'', '\"0\" < 5', null, null, true",
    "'', 'false < 5', null, null, true",
    "'', 'null < 5', null, null, true",
    "'function test() {return(1)}, x = test, y = test', 'x < y', null, null, true",

    // for Evaluator._is_less_than_or_equal
    "'', '3 <= 5', true, boolean, false",
    "'', '3 <= 3', true, boolean, false",
    "'', '-3 <= 2', true, boolean, false",
    "'', '3 <= -5', false, boolean, false",
    "'', '2.3 <= 2.3', true, boolean, false",
    "'', '2.3 <= 2.4', true, boolean, false",
    "'', '-1e+4 <= -1e+4', true, boolean, false",
    "'', '1e4 <= 1e3', false, boolean, false",
    "'', '1e-4 <= 1e-3', true, boolean, false",
    "'', '{} <= 5', null, null, true",
    "'', '[] <= 5', null, null, true",
    "'', '\"\" <= 5', null, null, true",
    "'', '\"0\" <= 5', null, null, true",
    "'', 'false <= 5', null, null, true",
    "'', 'null <= 5', null, null, true",
    "'function test() {return(1)}, x = test, y = test', 'x <= y', null, null, true",

    // for Evaluator._is_greater_than
    "'', '3 > 1', true, boolean, false",
    "'', '3 > 5', false, boolean, false",
    "'', '3 > 3', false, boolean, false",
    "'', '-3 > 2', false, boolean, false",
    "'', '3 > -5', true, boolean, false",
    "'', '2.3 < 2.4', true, boolean, false",
    "'', '1e4 > 1e3', true, boolean, false",
    "'', '1e-4 > 1e-3', false, boolean, false",
    "'', '{} > 5', null, null, true",
    "'', '[] > 5', null, null, true",
    "'', '\"\" > 5', null, null, true",
    "'', '\"0\" > 5', null, null, true",
    "'', 'false > 5', null, null, true",
    "'', 'null > 5', null, null, true",
    "'function test() {return(1)}, x = test, y = test', 'x > y', null, null, true",

    // for Evaluator._is_greater_than_or_equal
    "'', '3 >= 1', true, boolean, false",
    "'', '3 >= 5', false, boolean, false",
    "'', '3 >= 3', true, boolean, false",
    "'', '-3 >= 2', false, boolean, false",
    "'', '3 >= -5', true, boolean, false",
    "'', '2.3 < 2.4', true, boolean, false",
    "'', '1e3 >= 1e3', true, boolean, false",
    "'', '1e4 >= 1e3', true, boolean, false",
    "'', '1e-4 >= 1e-3', false, boolean, false",
    "'', '{} >= 5', null, null, true",
    "'', '[] >= 5', null, null, true",
    "'', '\"\" >= 5', null, null, true",
    "'', '\"0\" >= 5', null, null, true",
    "'', 'false >= 5', null, null, true",
    "'', 'null >= 5', null, null, true",
    "'function test() {return(1)}, x = test, y = test', 'x >= y', null, null, true",

    // for Evaluator._is_contained
    "'', '\"b\" in [\"a\", \"b\", \"c\"]', true, boolean, false",
    "'', '\"d\" in [\"a\", \"b\", \"c\"]', false, boolean, false",
    "'', '1 in [1, 2, \"c\"]', true, boolean, false",
    "'', '3 in [1, 2, \"c\"]', false, boolean, false",
    "'', 'true in [1, true, \"c\"]', true, boolean, false",
    "'', '[2, 3] in [1, [2,3], \"c\"]', true, boolean, false",
    "'', '{\"a\": 2, \"b\": false} in [1, {\"b\":false, \"a\":2}, \"c\"]', true, boolean, false",
    "'', '\"x\" in {\"x\": 3, \"y\": 10}', true, boolean, false",
    "'', '\"z\" in {\"x\": 3, \"y\": 10}', false, boolean, false",
    "'', '{\"x\": 3} in {\"x\": 3, \"y\": 10}', true, boolean, false",
    "'', '{\"x\": 2} in {\"x\": 3, \"y\": 10}', false, boolean, false",
    "'', '{\"x\": 3, \"y\": 10} in {\"x\": 3, \"y\": 10}', true, boolean, false",
    "'', '{\"x\": 3, \"y\": 1} in {\"x\": 3, \"y\": 10}', false, boolean, false",
    "'', '\"dog\" in \"cat&dog&bird\"', true, boolean, false",
    "'', '\"dogs\" in \"cat&dog&bird\"', false, boolean, false",
    "'', '\"23\" in \"123\"', true, boolean, false",
    "'', '23 in \"123\"', null, null, true",  // int in str --> ERROR
    "'', '1 in 5', null, null, true",  // int in int --> ERROR
    "'', '1.5 in 4.5', null, null, true",  // float in float --> ERROR
    "'', '[1, 2, \"c\"] in 1', null, null, true",  // array in int --> ERROR
    "'', 'true in true', null, null, true",  // boolean in boolean --> ERROR
    "'', 'null in null', null, null, true",  // null in null --> ERROR
    "'function test() {return(1)}, x = test, y = test', 'x in y', null, null, true",
    "'function test() {return(1)}, x = test, y = test', '1 in y', null, null, true",
    "'function test() {return(1)}, x = test, y = test', 'x in [y]', true, boolean, false",
    "'{\"foo\":3, \"bar\": 2}', '\"foo\" in .', true, boolean, false",
    "'{\"foo\":3, \"bar\": 2}', '\"baz\" in .', false, boolean, false",
  })
  public void test(String preparedExpression, String targetExpression,
                   String expectedValue, String expectedType,
                   boolean isError) {
    Config config = new Config();
    config.setMaxArraySize(5);

    String text = "return(" + targetExpression + ")";
    Result result = Viv.run(preparedExpression, text, config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    if (expectedType.equals("float")) {
      Double d1 = Double.valueOf(value);
      Double d2 = Double.valueOf(expectedValue);
      assertTrue(Math.abs((d2 == 0.0) ? (d1 - d2) : (1.0 - d1 / d2)) < 0.00001);
    } else if (expectedType.equals("block")) {
      assertTrue(UtilsForTest.isEqualMap(value, expectedValue));
    } else {
      assertEquals(value, expectedValue);
    }
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @Test
  public void testOther() {
    HashMap<String, Object> variable = new HashMap<>();
    // int + int --> int
    variable.put("a", 3);
    assertEquals(Viv.getInteger(variable, "return(a + 2)"), 5);

    variable.replace("a", new Config());
    Viv.Result result = Viv.run(variable, "return(a + 2)");
    assertFalse(result.errorMessage.isEmpty());

    variable.replace("a", "x");
    variable.put("b", "y");
    assertEquals(Viv.getString(variable, "return(a + b)"), "xy");
  }
}
