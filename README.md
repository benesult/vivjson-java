# VivJson for Java

## Overview

- Deserialize JSON flexibly in Java.
- Minimal function for Serialization of JSON
- Tiny language
    - The embedded scripting language 
    - Dynamically typing
    - Lightweight language
- The extension of JSON (JSON's object is valid statements as script.)

JSON offers flexible data structure. Its manipulation is so easy in
dynamically typed language. On the other hand, in statically typed
language, such as Java, it is so difficult.  
Thus, this embedded script empowers to manipulate JSON in Java.


## Use-case

- In Java Application
    - Read/Write JSON's value.
    - Change the behavior of Application with downloaded script.
    - Change the operation of data with embedded script within data.
- In command line
    - Manipulate JSON's value with script.


## Example

In Java,

```java
// {"foo": 10, "bar": 30, "baz": 20}
String data = "{\"foo\": 10, \"bar\": 30, \"baz\": 20}";

// Key-Value pairs
//   The printed order is unknown.
//   The appended order into key-value pairs is ignored.
Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(data);
for (Viv.KeyValue<Integer> pair : pairs) {  // String key, Integer value
    System.out.println(pair.key + ": " + pair.value);  // foo: 10
                                                       // bar: 30
                                                       // baz: 20
}

// Values
//   The printed order is unknown.
//   The appended order into key-value pairs is ignored.
int[] values = Viv.getIntegers(data);
for (int value : values) {
    System.out.println(value);  // 10
                                // 30
                                // 20
}

// The specific variable's value
Integer foo = Viv.getInteger(data, "return(foo)");
System.out.println(foo);  // 10

// The undefined value is evaluated as null.
Viv.Result result = Viv.run(data, "return(qux)");
System.out.println(result.errorMessage.isEmpty());  // true
System.out.println(result.value);  // null
// The existence can be checked with "in" operator.
// Note that "." of the right hand side indicates the whole scope.
// "." as the operand is permitted only the right hand side of
// "in" operator.
// Carefully, it can't check about the variable whose prefix is "_".
Boolean isContained = Viv.getBoolean(data, "return('qux' in .)");
System.out.println(isContained);  // false

// The calculated value
Integer add = Viv.getInteger(data, "return(foo + bar + baz)");
System.out.println(add);  // 60

// Find maximum value.
// 1. Assignment: pairs = {"foo": 10, "bar": 30, "baz": 20}
// 2. for-loop with iterator: for (pair in pairs) {...}
//    - pair[0] is the above key.
//    - pair[1] is the above value.
// 3. Update max.
// 4. Return it.
String code = "max=-1, for (pair in pairs) {if (max < pair[1]) {max = pair[1]}}, return(max)";
Integer max = Viv.getInteger("pairs = ", "+", data, code);
System.out.println(max);  // 30
// Note that "+" of arguments is concatenation. Of course, the following 
// representation can be accepted.
max = Viv.getInteger("pairs = " + data, code);
System.out.println(max);  // 30
// This is realized as below. However, such code may generate the unexpected
// result because "." represents this block that has the variable "max".
code = "max=-1, for (pair in .) {if (max < pair[1]) {max = pair[1]}}, return(max)";
max = Viv.getInteger(data, code);
System.out.println(max);  // 30
// When "_max" is used instead of "max", it is improved.
// Because "." does not treat the variable whose prefix is "_".
code = "_max=-1, for (pair in .) {if (_max < pair[1]) {_max = pair[1]}}, return(_max)";
max = Viv.getInteger(data, code);
System.out.println(max);  // 30

// In default, both of script's code and JSON value are accepted.
// Json class is useful if you want to accept as JSON value rather
// than script's code.
String value = "{\"foo\": 3}";
code = "return(foo)";
result = Viv.run(new Viv.Json(value), code);
System.out.println(result.value);  // 3

// Using class instance, implicit variable, and member's access
// - When the given value is not JSON object (key-value pairs),
//   it is assigned into implicit variable.
//   The implicit variable is "_" if there is one value. Otherwise,
//   "_[0]", "_[1]", ... are used.
// - A class member is selected with the array of Object[].
//   new Object[]{"foo", "bar"}, "return(foo.bar)", and
//   "return(foo['bar'])" are equivalent.
//   In the following example, new Object[]{"_", 2}, "return(_.2)",
//   and "return(_[2])" are equivalent.
// ["foo", 10, [{"bar": null, "baz": "test"}, false]]
Viv.Instance instance = Viv.makeInstance(
    "[\"foo\", 10, [{\"bar\": null, \"baz\": \"test\"}, false]]");
try {
  System.out.println(Viv.runEx(instance, new Object[]{"_", 2, 0, "bar"}));  // null
  System.out.println(Viv.runEx(instance, "return(_[2][0]['bar'])"));  // null
  System.out.println(Viv.runEx(instance, new Object[]{"_", 2, -2, "baz"}));  // "test"
  System.out.println(Viv.runEx(instance, "return(_.2.-2.baz)"));  // "test"
} catch (VivException e) {
  System.err.println(e.getMessage());
}
// Calling class method
// - It is represented as the array of Object[].
//   The 1st element is the method name as String.
//   The following elements are its argument.
//   In the following example, new Object[] {"add", 10, 20}.
//   It is equivalent to "return(add(10, 20))".
code = "function add(a, b) {"
     + "  return(a + b)"
     + "}";
instance = Viv.makeInstance(code);
System.out.println(Viv.getInteger(instance, new Object[] {"add", 10, 20}));  // 30
System.out.println(Viv.getInteger(instance, "return(add(10, 20))"));  // 30
```

In command-line,

```shell
# The specific variable's value
java -jar vivjson.jar '{"foo": 10, "bar": 30, "baz": 20}' \
'return(foo)'  # 10

# Using PIPE (-i option)
echo '{"foo": 10, "bar": 30, "baz": 20}' | \
java -jar vivjson.jar -i 'return(foo)'  # 10

# The calculated value
echo '{"foo": 10, "bar": 30, "baz": 20}' | \
java -jar vivjson.jar -i 'return(foo + bar + baz)'  # 60

# Find maximum value.
echo '{"foo": 10, "bar": 30, "baz": 20}' | \
java -jar vivjson.jar -i=pairs \
"max=-1, for (pair in pairs) {if (max < pair[1]) {max = pair[1]}}, return(max)"  # 30

# Find maximum value without PIPE.
java -jar vivjson.jar "pairs=" + '{"foo": 10, "bar": 30, "baz": 20}' \
"max=-1, for (pair in pairs) {if (max < pair[1]) {max = pair[1]}}, return(max)"  # 30
# Note that "+" of arguments is concatenation. Of course, the following 
# representation can be accepted.
java -jar vivjson.jar 'pairs={"foo": 10, "bar": 30, "baz": 20}' \
"max=-1, for (pair in pairs) {if (max < pair[1]) {max = pair[1]}}, return(max)"  # 30

# Getting member's value
# "return(foo.bar)" and "return(foo['bar'])" are equivalent.
java -jar vivjson.jar '{"foo": [1, {"bar": true}, "test"]}' \
'return(foo[0])'  # 1
java -jar vivjson.jar '{"foo": [1, {"bar": true}, "test"]}' \
'return(foo.1.bar)'  # true
java -jar vivjson.jar '{"foo": [1, {"bar": true}, "test"]}' \
'return(foo.-1)'  # test

# Using implicit variable
# When the given value is not JSON object (key-value pairs),
# it is assigned into implicit variable.
# The implicit variable is "_" if there is one value. Otherwise,
# "_[0]", "_[1]", ... are used.
java -jar vivjson.jar 1.5 'return(_)'  # 1.5
java -jar vivjson.jar 1.5 2 'return(_[0] + _[1])'  # 3.5
echo '[{"name": "dog", "number": 2}, {"name": "cat", "number": 3}]' | \
java -jar vivjson.jar -i 'result = {}' \
'for (data in _) {result[data.name] = data.number}' \
'return(result)'
# {"dog": 2, "cat": 3}

# Help
java -jar vivjson.jar
```


## Installation

Refer to [VivJson of Maven central repository](https://central.sonatype.com/artifact/com.benesult/vivjson).  

For example, the following setting is needed in "build.gradle" if you implement it into Android App.

```
dependencies {
    implementation 'com.benesult:vivjson:1.0.1'
}
```

Furthermore, the following configuration is needed in "proguard-project.txt" if you implement it into Android App.
Because VivJson's Standard library uses reflection in order to call method.

```
-keepclassmembers class com.benesult.vivjson.Standard {
    public static <methods>;
}
```


## API

| Pattern                                                                 | Consumed memory | Next running speed |
|-------------------------------------------------------------------------|-----------------|--------------------|
| [Direct running](#direct-running)                                       | Low             | Slow               |
| [Parsing and Running](#parsing-and-running)                             | Middle          | Middle             |
| [Making class instance and Running](#making-class-instance-and-running) | High            | Fast               |

When class instance is made, class method can be called and member's variable can be updated.


### Direct running

It is suitable if running times is only one.

```
            +------------------------------+
            |                              |
            | Viv                          |
            |                              |
            |  +------------------------+  |
Java's      |  |                        |  |     Java's
value   ------>| run, runEx,            |------> value
            |  | getBoolean,            |  |       or
JSON's      |  | getBooleanEx,          |  |     JSON's
value   ------>| getBooleans,           |  |     value 
            |  | getBooleansEx,         |  |
Script      |  | getKeyValueBooleans,   |  |
code    ------>| getKeyValueBooleansEx, |  |
            |  |   :                    |  |
            |  |   :                    |  |
            |  |                        |  |
            |  +------------------------+  |
            |                              |
            +------------------------------+
```

For example,

```java
Viv.Result result = Viv.run("{\"foo\":3, \"bar\": 2}", "return(foo + bar)");
System.out.println(result.value);  // 5
```


### Parsing and Running

It is suitable that same running is repeated.  
Because parsing is done only one time.

```
            +--------------------+              +------------------------------+
            |                    |              |                              |
            | Viv                |              | Viv                          |
            |                    |              |                              |
            |  +--------------+  |  Parsed      |  +------------------------+  |
Java's      |  |              |  |  value/code  |  |                        |  |     Java's
value   ------>| parse,       |------------------->| run, runEx,            |------> value
            |  | parseEx,     |  |              |  | getBoolean,            |  |       or
JSON's      |  | parseFile,   |  |  Additional  |  | getBooleanEx,          |  |     JSON's
value   ------>| parseFileEx, |  |  Java/JSON's |  | getBooleans,           |  |     value 
            |  | parseText,   |  |  value   ------>| getBooleansEx,         |  |
Script      |  | parseTextEx  |  |              |  | getKeyValueBooleans,   |  |
code    ------>|              |  |  Additional --->| getKeyValueBooleansEx, |  |
            |  +--------------+  |  Script code |  |   :                    |  |
            |                    |              |  |   :                    |  |
            +--------------------+              |  |                        |  |
                                                |  +------------------------+  |
                                                |                              |
                                                +------------------------------+
```

For example,

```java
Viv.Parsed parsed = Viv.parse("return(foo + bar)");
Viv.Result result = Viv.run("{\"foo\":3, \"bar\": 2}", parsed);
System.out.println(result.value);  // 5
```


### Making class instance and Running

It is suitable that same running is repeated.  
Because parsing and initialization are done only one time.

```
            +----------------------+              +------------------------------+
            |                      |              |                              |
            | Viv                  |              | Viv                          |
            |                      |              |                              |
            |  +----------------+  |              |  +------------------------+  |
Java's      |  |                |  |  Instance    |  |                        |  |     Java's
value   ------>| makeInstance,  |------------------->| run, runEx,            |------> value
            |  | makeInstanceEx |  |              |  | getBoolean,            |  |       or
JSON's      |  |                |  |  Additional  |  | getBooleanEx,          |  |     JSON's
value   ------>|                |  |  Java/JSON's |  | getBooleans,           |  |     value 
            |  |                |  |  value   ------>| getBooleansEx,         |  |
Script      |  |                |  |              |  | getKeyValueBooleans,   |  |
code    ------>|                |  |  Additional --->| getKeyValueBooleansEx, |  |
            |  +----------------+  |  Script code |  |   :                    |  |
            |                      |              |  |   :                    |  |
            +----------------------+  Calling ------>|                        |  |
                                      Method or   |  +------------------------+  |
                                      Getting     |                              |
                                      member      +------------------------------+
```

For example,

```java
String code = "function add(a, b) {"
            + "  return(a + b)"
            + "}"
            + "c = [20, false]";
Viv.Instance instance = Viv.makeInstance(code);

Integer value = Viv.getInteger(instance, "{\"foo\":3, \"bar\": 2}", "return(add(foo, bar))");
System.out.println(value);  // 5
value = Viv.getInteger(instance, new Object[] {"add", 3, 2});
System.out.println(value);  // 5

value = Viv.getInteger(instance, "return(c[0])");
System.out.println(value);  // 20
value = Viv.getInteger(instance, new Object[] {"c", 0});
System.out.println(value);  // 20
```


### Viv class

There are two types for managing error.  
1st type does not throw exception. Instead of it, the returned value
indicates error if error occurs.  
2nd type throw exception if error occurs. And the returned value has
only the actual returned value.  
The suffix of 2nd type's method name is **"Ex"**.

Furthermore, there are two types for unexpected element in the array.  
In 1st type, the returned whole value is `null` or exception is thrown
if there is unexpected element.  
In 2nd type, the returned value of unexpected element is `null`
if there is unexpected element.  
2nd type's method name contains **"OrNull"**.

The following methods are available.

- Running/Deserialization function
    - `run(Object...)` or  
      `runEx(Object...)` :
                             Run VivJson's code or deserialize JSON objects.
    - `parse(Object...)` or  
      `parseEx(Object...)` :
                             Parse VivJson's code and JSON object.
    - `parseFile(String, Config)` or  
      `parseFileEx(String, Config)` :
                             Parse a file that contains VivJson's code or JSON object.
    - `parseText(String, Config)` or  
      `parseTextEx(String, Config)` :
                             Parse a text that is VivJson's code or JSON object.
    - `makeInstance(Object...)` or  
      `makeInstanceEx(Object...)` :
                             Makes a class instance.
- Monolithic conversion (with running/deserialization function)
    - `getBoolean(Object...)` or  
      `getBooleanEx(Object...)` :
                             Get a boolean.
    - `getInteger(Object...)` or  
      `getIntegerEx(Object...)` :
                             Get an integer.
    - `getLong(Object...)` or  
      `getLongEx(Object...)` :
                             Get a long integer.
    - `getFloat(Object...)` or  
      `getFloatEx(Object...)` :
                             Get a floating-point number.
    - `getDouble(Object...)` or  
      `getDoubleEx(Object...)` :
                             Get a double-precision floating-point number.
    - `getString(Object...)` or  
      `getStringEx(Object...)` :
                             Get a string.
- Array conversion (with running/deserialization function)
    - `getBooleans(Object...)` or  
      `getBooleansEx(Object...)` :
                             Get an array of boolean.
    - `getBooleanOrNulls(Object...)` or  
      `getBooleanOrNullsEx(Object...)` :
                             Get an array of `@Nullable Boolean`.
    - `getIntegers(Object...)` or  
      `getIntegersEx(Object...)` :
                             Get an array of integer.
    - `getIntegerOrNulls(Object...)` or  
      `getIntegerOrNullsEx(Object...)` :
                             Get an array of `@Nullable Integer`.
    - `getLongs(Object...)` or  
      `getLongsEx(Object...)` :
                             Get an array of long integer.
    - `getLongOrNulls(Object...)` or  
      `getLongOrNullsEx(Object...)` :
                             Get an array of `@Nullable Long`.
    - `getFloats(Object...)` or  
      `getFloatsEx(Object...)` :
                             Get an array of floating-point number.
    - `getFloatOrNulls(Object...)` or  
      `getFloatOrNullsEx(Object...)` :
                             Get an array of `@Nullable Float`.
    - `getDoubles(Object...)` or  
      `getDoublesEx(Object...)` :
                             Get an array of double-precision floating-point number.
    - `getDoubleOrNulls(Object...)` or  
      `getDoubleOrNullsEx(Object...)` :
                             Get an array of `@Nullable Double`.
    - `getStrings(Object...)` or  
      `getStringsEx(Object...)` :
                             Get an array of String.
    - `getStringOrNulls(Object...)` or  
      `getStringOrNullsEx(Object...)` :
                             Get an array of `@Nullable String`.
    - `getObjects(Object...)` or  
      `getObjectsEx(Object...)` :
                             Get an array of Object.
    - `getObjectOrNulls(Object...)` or  
      `getObjectOrNullsEx(Object...)` :
                             Get an array of `@Nullable Object`.
- Key-Value pairs conversion (with running/deserialization function)
    - `getKeyValueBooleans(Object...)` or  
      `getKeyValueBooleansEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Boolean>`.
    - `getKeyValueBooleanOrNulls(Object...)` or  
      `getKeyValueBooleanOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Boolean>`.
    - `getKeyValueIntegers(Object...)` or  
      `getKeyValueIntegersEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Integer>`.
    - `getKeyValueIntegerOrNulls(Object...)` or  
      `getKeyValueIntegerOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Integer>`.
    - `getKeyValueLongs(Object...)` or  
      `getKeyValueLongsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Long>`.
    - `getKeyValueLongOrNulls(Object...)` or  
      `getKeyValueLongOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Long>`.
    - `getKeyValueFloats(Object...)` or  
      `getKeyValueFloatsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Float>`.
    - `getKeyValueFloatOrNulls(Object...)` or  
      `getKeyValueFloatOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Float>`.
    - `getKeyValueDoubles(Object...)` or  
      `getKeyValueDoublesEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Double>`.
    - `getKeyValueDoubleOrNulls(Object...)` or  
      `getKeyValueDoubleOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Double>`.
    - `getKeyValueStrings(Object...)` or  
      `getKeyValueStringsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<String>`.
    - `getKeyValueStringOrNulls(Object...)` or  
      `getKeyValueStringOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable String>`.
    - `getKeyValueObjects(Object...)` or  
      `getKeyValueObjectsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<Object>`.
    - `getKeyValueObjectOrNulls(Object...)` or  
      `getKeyValueObjectOrNullsEx(Object...)` :  
            &nbsp;&nbsp;&nbsp;&nbsp; Get key-value pairs as an array of `Viv.KeyValue<@Nullable Object>`.
- Other conversion (with running/deserialization function)
    - `getArrayList(Object...)` or  
      `getArrayListEx(Object...)` :
                             Get an `ArrayList<@Nullable Object>`.
    - `getHashMap(Object...)` or  
      `getHashMapEx(Object...)` :
                             Get a `HashMap<String, @Nullable Object>`.
- String conversion (without running/deserialization function)
    - `makeString(Object)` :
                             Convert into String. Serialize into JSON string.

The following arguments can be given into all methods except
`parseFile(String, Config)`, `parseFileEx(String, Config)`,
`parseText(String, Config)`, `parseTextEx(String, Config)`, and
`makeString(Object)`.

| Argument type                                | Java Object type    | Example                                                         |
|----------------------------------------------|---------------------|-----------------------------------------------------------------|
| A VivJson's code                             | `String`            | `"foo = bar / 2"`, `"result = test(100)"`                       |
| A JSON value                                 | `String`            | `"{\"foo\": 10, \"bar\": true}"`, `"[1, 2]"`, `"dog"`, `"null"` |
| A JSON value                                 | `Viv.Json`          | `new Viv.Json("{\"foo\": 10, \"bar\": true}")`, `new Viv.Json("[1, 2]")`, `new Viv.Json("dog")`, `new Viv.Json("null")` |
| A file path                                  | `String`            | `"data/events.json"`, `"calc.viv"`                              |
| Some VivJson's codes, JSON values, file paths, variables, Parsed objects | `ArrayList<Object>` | `ArrayList<Object> array = new ArrayList<>(); array.add(new Json("{\"foo\": 10, \"bar\": 1.5}")); array.add("baz = foo + bar"); array.add("return(baz)");` |
| Some variables (name/value pairs)            | `HashMap<String, @Nullable Object>` | `HashMap<Object, Object> variables = new HashMap<>(); variables.put("foo", "alpha"); variables.put("bar", true);` |
| Some configurations                          | `Config`            | `Config config = new Config(); config.setInfinity("Infinity");` |
| Some parsed statements                       | `Viv.Parsed`        | `Viv.Parsed parsed = Viv.parse("return(a+b)");`         |
| A class instance                             | `Viv.Instance`      | `Viv.Instance instance = Viv.makeInstance("{\"a\": 3, \"b\": 2}");` |
| A class member                               | `Object[]`          | `new Object[] {"foo", "bar"}`<br> It is equivalent to `"return(foo.bar)"` and `"return(foo['bar'])"`. |
| A calling class-method                       | `Object[]`          | `new Object[] {"add", 100, -10}`<br> The 1st element is the method name as `String`. The following elements are its arguments. |

The class member or calling class-method needs class instance in arguments.

```java
String code = "function add(a, b) {"
            + "  return(a + b)"
            + "}";
Viv.Instance instance = Viv.makeInstance(code);
Integer value = Viv.getInteger(instance, new Object[] {"add", 100, -10});
System.out.println(value);  // 90
```

Multiple arguments can be given into all methods except
`parseFile(String, Config)`, `parseFileEx(String, Config)`,
`parseText(String, Config)`, `parseTextEx(String, Config)`, and
`makeString(Object)`.  
Furthermore, an array of arguments can be given too.
However, `Config` object or `null` must be given.
For example, `Viv.Result result = Viv.run("{a:3,b:2}", "return(a+b)");` is equivalent to
`String[] strings = new String[] {"{a:3,b:2}", "return(a+b)"}; Viv.Result result = Viv.run(strings, null);`.
Note that `null` of 2nd argument indicates the default configuration.

The returned value is different between `*()` and `*Ex()` as below.  
The returned value of the former method has error information if error occurs.  
The returned value of the later method does not have error information even if error occurs. Because exception occurs if error occurs.  
<small>(Note that the returned value of `parse*Ex()` and `makeInstanceEx()` have error information even if exception occurs. The same information is stored into the returned value's error-message and exception. The reason is convenience of the implementation.)</small>

| Method                      | `*()`                                          | `*Ex()`                             |
|-----------------------------|------------------------------------------------|-------------------------------------|
| `run`                       | `Viv.Result`                                   | `@Nullable Object`                  |
| `parse`                     | `Viv.Parsed`                                   | `Viv.Parsed`                        |
| `parseFile`                 | `Viv.Parsed`                                   | `Viv.Parsed`                        |
| `parseText`                 | `Viv.Parsed`                                   | `Viv.Parsed`                        |
| `makeInstance`              | `Viv.Instance`                                 | `Viv.Instance`                      |
| `getBoolean`                | `@Nullable Boolean`                            | `boolean`                           |
| `getBooleans`               | `boolean @Nullable []`                         | `boolean[]`                         |
| `getBooleanOrNulls`         | `@Nullable Boolean @Nullable []`               | `@Nullable Boolean[]`               |
| `getInteger`                | `@Nullable Integer`                            | `int`                               |
| `getIntegers`               | `int @Nullable []`                             | `int[]`                             |
| `getIntegerOrNulls`         | `@Nullable Integer @Nullable []`               | `@Nullable Integer[]`               |
| `getLong`                   | `@Nullable Long`                               | `long`                              |
| `getLongs`                  | `long @Nullable []`                            | `long[]`                            |
| `getLongOrNulls`            | `@Nullable Long @Nullable[]`                   | `@Nullable Long[]`                  |
| `getFloat`                  | `@Nullable Float`                              | `float`                             |
| `getFloats`                 | `float @Nullable []`                           | `float[]`                           |
| `getFloatOrNulls`           | `@Nullable Float @Nullable []`                 | `@Nullable Float[]`                 |
| `getDouble`                 | `@Nullable Double`                             | `double`                            |
| `getDoubles`                | `double @Nullable []`                          | `double[]`                          |
| `getDoubleOrNulls`          | `@Nullable Double @Nullable []`                | `@Nullable Double[]`                |
| `getString`                 | `@Nullable String`                             | `String`                            |
| `getStrings`                | `String @Nullable []`                          | `String[]`                          |
| `getStringOrNulls`          | `@Nullable String @Nullable []`                | `@Nullable String[]`                |
| `getObjects`                | `Object @Nullable []`                          | `Object[]`                          |
| `getObjectOrNulls`          | `@Nullable Object @Nullable []`                | `@Nullable Object[]`                |
| `getKeyValueBooleans`       | `Viv.KeyValue<Boolean> @Nullable []`           | `Viv.KeyValue<Boolean>[]`           |
| `getKeyValueBooleanOrNulls` | `Viv.KeyValue<@Nullable Boolean> @Nullable []` | `Viv.KeyValue<@Nullable Boolean>[]` |
| `getKeyValueIntegers`       | `Viv.KeyValue<Integer> @Nullable []`           | `Viv.KeyValue<Integer>[]`           |
| `getKeyValueIntegerOrNulls` | `Viv.KeyValue<@Nullable Integer> @Nullable []` | `Viv.KeyValue<@Nullable Integer>[]` |
| `getKeyValueLongs`          | `Viv.KeyValue<Long> @Nullable []`              | `Viv.KeyValue<Long>[]`              |
| `getKeyValueLongOrNulls`    | `Viv.KeyValue<@Nullable Long> @Nullable []`    | `Viv.KeyValue<@Nullable Long>[]`    |
| `getKeyValueFloats`         | `Viv.KeyValue<Float> @Nullable []`             | `Viv.KeyValue<Float>[]`             |
| `getKeyValueFloatOrNulls`   | `Viv.KeyValue<@Nullable Float> @Nullable []`   | `Viv.KeyValue<@Nullable Float>[]`   |
| `getKeyValueDoubles`        | `Viv.KeyValue<Double> @Nullable []`            | `Viv.KeyValue<Double>[]`            |
| `getKeyValueDoubleOrNulls`  | `Viv.KeyValue<@Nullable Double> @Nullable []`  | `Viv.KeyValue<@Nullable Double>[]`  |
| `getKeyValueStrings`        | `Viv.KeyValue<String> @Nullable []`            | `Viv.KeyValue<String>[]`            |
| `getKeyValueStringOrNulls`  | `Viv.KeyValue<@Nullable String> @Nullable []`  | `Viv.KeyValue<@Nullable String>[]`  |
| `getKeyValueObjects`        | `Viv.KeyValue<Object> @Nullable []`            | `Viv.KeyValue<Object>[]`            |
| `getKeyValueObjectOrNulls`  | `Viv.KeyValue<@Nullable Object> @Nullable []`  | `Viv.KeyValue<@Nullable Object>[]`  |
| `getArrayList`              | `@Nullable ArrayList<@Nullable Object>`        | `ArrayList<@Nullable Object>`       |
| `getHashMap`                | `@Nullable HashMap<String, @Nullable Object>`  | `HashMap<String, @Nullable Object>` |
| `makeString`                | `String`                                       | -----                               |

The returned value of `run()` is `Viv.Result` that has
`@Nullable Object value` and `String errorMessage`.  
When running is success, `value` has the result and `errorMessage` is `""` (empty).  
Otherwise, `value` is null and `errorMessage` has an error message.  
This `@Nullable Object value` and the returned value of `runEx()` is same.  
It will be the following value.

- `Boolean`
- `Long`
- `Double`
- `String`
- `ArrayList<@Nullable Object>`
- `HashMap<String, @Nullable Object>`
- `null`

The returned value of `parse*()` and `makeInstance*()` is
`Viv.Parsed` and `Viv.Instance`. They also have `String errorMessage`.

When the specific object is expected, the corresponded method is
recommended rather than `run()` or `runEx()`.  
For example, `getBoolean()` is recommended if `boolean` is expected.

Similarly, `getArrayList` is recommended if array is expected.  
However, the more suitable methods are available.  
For example, `getBooleans()` or `getBooleanOrNulls()` is recommended
if an array of `boolean` is expected.

`getHashMap` is also recommended if key-value pairs are expected.  
However, the more suitable methods are available.  
For example, `getKeyValueBooleans()` or `getKeyValueBooleanOrNulls()`
is recommended if it is expected that the value of key-value pairs is
`boolean`.  
Its returned value is `Viv.KeyValue<Boolean> @Nullable []` or
`Viv.KeyValue<@Nullable Boolean> @Nullable []`.  
In `Viv.KeyValue<T>` class, key is represented as `String` and value is
represented as `T`.


### Config class

The following configurations are available.

| Name            | Object type        | Default value | Description                                                   |
|-----------------|--------------------|---------------|---------------------------------------------------------------|
| enableStderr    | `boolean`          | `false`       | When `true` is given, error message is outputted into stderr. |
| enableTagDetail | `boolean`          | `false`       | When `true` is given, error message's tag contains either of "Lexer", "Parser", or "Evaluator". |
| enableOnlyJson  | `boolean`          | `false`       | When `true` is given, the given data is parsed as JSON. In other words, script is disabled.     |
| infinity        | `@Nullable String` | `null`        | When string is given, Infinity is allowed in JSON. Then the given string is used to input/output Infinity from/to JSON. (Note that it is not surrounded with quotation mark.)<br>When `null` is given and Infinity is happen, error is occurred. |
| nan             | `@Nullable String` | `null`        | When string is given, NaN (Not a Number) is allowed in JSON. Then the given string is used to input/output NaN from/to JSON. (Note that it is not surrounded with quotation mark.)<br>When `null` is given and NaN is happen, error is occurred. |
| maxArraySize    | `int`              | `1000`        | Maximum array/block size.                                     |
| maxDepth        | `int`              |  `200`        | Maximum recursive called times of evaluate method.            |
| maxLoopTimes    | `int`              | `1000`        | Maximum loop times of "for", "while", and so on.              |

Each configuration is set/gotten with the following method.

| Name            | get method         | set method      |
|-----------------|--------------------|-----------------|
| enableStderr    | getEnableStderr    | enableStderr    |
| enableTagDetail | getEnableTagDetail | enableTagDetail |
| enableOnlyJson  | getEnableOnlyJson  | enableOnlyJson  |
| infinity        | getInfinity        | setInfinity     |
| nan             | getNaN             | setNaN          |
| maxArraySize    | getMaxArraySize    | setMaxArraySize |
| maxDepth        | getMaxDepth        | setMaxDepth     |
| maxLoopTimes    | getMaxLoopTimes    | setMaxLoopTimes |

For example,

```java
Config config = new Config();
config.enableStderr();
config.setInfinity("Infinity");
config.setNaN("NaN");
config.setMaxDepth(10);
config.setMaxLoopTimes(100);
Viv.Result result = Viv.run(new Viv.Json(text), code, config);
```

By the way, the following JSON object is invalid because JSON's number can't treat Infinity and NaN. (See [RFC 8259 Section 6][RFC 8259 Section 6])  
However, VivJson can treat it using `Config#setInfinity` and `Config#setNaN`.

```json
{
    "normal": 1.5,
    "inf": Infinity,
    "negative_inf": -Infinity,
    "nan": NaN,
    "str": "1.5"
}
```

[RFC 8259 Section 6]:https://datatracker.ietf.org/doc/html/rfc8259#section-6


## Implementation

### Diagram

```
            +-----------------------------------------------------+
            |                                                     |
            | Viv                                                 |
            |                             +-------------+         |
Java's  --------------+------------------>|             |         |
value       |         |                   | Evaluator   |         |
            |         V                   |             |         |
            |  +-------------+            |             |         |
JSON's  ------>|             |            |             |         |     Java's
value       |  | Parser      | Statements |             |         |     value
            |  |             |----------->|             |------------->   or
            |  |  +-------+  |            |             |         |     JSON's
Script  ------>|  |       |  |            +-------------+         |     value
code        |  |  | Lexer |  |              |      |              |
            |  |  |       |  |    Variables |      | Function     |
            |  |  +-------+  |              |      | call         |
Other   ------>|             |              |      |              |
            |  +-------------+              |      |              |
            |                               V      V              |
            |  +-------------+  +-------------+  +-------------+  |
            |  |             |  |             |  |             |  |
            |  | Config      |  | Environment |  | Standard    |  |
            |  |             |  |             |  |             |  |
            |  +-------------+  +-------------+  +-------------+  |
            +-----------------------------------------------------+
```


### Class

- **Viv**: API
    - **Viv.Json**: JSON data class. In default, both of script's code and JSON value are accepted. This class is used if you want to accept as JSON value rather than script's code.
    - **Viv.Result**: Result data class. This is used as the returned value of `run()`.
    - **Viv.Parsed**: Parsed data class. This is used as the returned value of `parse*()`.
    - **Viv.Instance**: Instance data class. This is used as the returned value of `makeInstance*()`.
    - **Viv.KeyValue<T>**: Key-Value pair data class. Mostly, this will be elements of the array. Then it is used to represent HashMap.
- **Lexer**: Source code is resolved into some tokens.
- **Parser**: Then statements are built from several tokens. One statement represents a unit of processing.
- **Evaluator**: Statements are operated.
- **Environment**: Variable's value and the definition of function are stored.
    1. New block (includes function) creates new instance of Environment as new scope.
    2. The basic value (number, string, boolean, and null) is stored as host language native literal.
- **Token**: Its instance is created from source code in Lexer.
- **Statement**: Mostly, its instance is created from tokens in Parser.
    - **Array**: Array data class
    - **Binary**: Binary data class. For example, `3 * 2` are stored into left, operator, and right.
    - **Blank**: Blank data class.
    - **Block**: Block data class. For example,
        1. anonymous: `x = {a: 3, b: 2}`
        2. pure: `function test() {return(10)}`
        3. limited: `if (i > 10) {x="+"} else {x="-"}`
    - **Call**: Call data class. This is used to call function, such as `len("abc")`.
    - **Callee**: Callee data class. This is used to define function entity.
    - **CalleeRegistry**: Callee Registry data class. This is made in Evaluator. This is not used in Parser.
    - **Get**: Get data class. For example, `x["y"]["z"]` and `x.y.z` are represented as `["x", "y", "z"]`.
    - **Identifier**: Identifier data class. It is used as name of variable, function, and class.
    - **Injection**: Injection data class. Set host language's variable.
    - **Keyword**: Keyword data class. It is used as return, break, and so on.
    - **Literal**: Literal data class. string, number (int and float), true, false, and null are accepted.
    - **Loop**: Loop data class.
    - **Parameter**: Parameter data class. This is used to assist the definition of function entity.
    - **Remove**: Remove data class. For example, `remove(x["y"]["z"])`, `remove(x.y.z)`
    - **Return**: Return data class. `"return" [ "(" value ")" ]`
    - **Set**: Set data class. For example, `x["y"]["z"] = 3` is represented.
    - **Value**: Value data class. Set host language's value.
- **Standard**: Standard library
- **Utils**: Utilities
- **Config**: Configuration of runtime
- **VivException**: Exception and report
    - **VivException**: Exception for VivJson
    - **LexException**: Exception for Lexer
    - **ParseException**: Exception for Parser
    - **EvaluateException**: Exception for Evaluator
- **CommandLine**: Command-line client


## Related links

- [VivJson](https://github.com/benesult/vivjson-spec)
    - [VivJson's quick reference](https://github.com/benesult/vivjson-spec/blob/main/quick_reference.md)
    - [VivJson's specification](https://github.com/benesult/vivjson-spec/blob/main/specification.md)
    - [VivJson's sample code](https://github.com/benesult/vivjson-spec/blob/main/sample_codes.md)
- [VivJson for Python](https://github.com/benesult/vivjson-python)
