/**
 * VivJson is the embedded scripting language and the extension of JSON.
 * <ul>
 *   <li> Deserialize JSON flexibly in Java.
 *   <li> Minimal function for Serialization of JSON
 *   <li> Tiny language
 *       <ul>
 *           <li> The embedded scripting language 
 *           <li> Dynamically typing
 *           <li> Lightweight language
 *       </ul>
 *   <li> The extension of JSON
 *        (JSON's object is valid statements as script.)
 * </ul>
 *
 * <p>JSON offers flexible data structure. Its manipulation is so easy in
 * dynamically typed language. On the other hand, in statically typed
 * language, such as Java, it is so difficult.<br>
 * Thus, this embedded script empowers to manipulate JSON in Java.
 *
 * <p>Use-case
 * <ul>
 *     <li> In Java Application
 *         <ul>
 *             <li> Read/Write JSON's value.
 *             <li> Change the behavior of Application with downloaded
 *                  script.
 *             <li> Change the operation of data with embedded script
 *                  within data.
 *         </ul>
 *     <li> In command line
 *         <ul>
 *             <li> Manipulate JSON's value with script.
 *         </ul>
 * </ul>
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-27
 *
 * <p>License:
 * <pre>
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
 * </pre>
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
@org.eclipse.jdt.annotation.NonNullByDefault
package com.benesult.vivjson;
