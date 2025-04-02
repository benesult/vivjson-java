#!/bin/sh

# Build script of VivJson for Java
# 
# Last modified: 2025-04-02
#
# author: Fumiaki Motegi (motegi@benesult.com)
#
# License:
# Copyright 2025 benesult
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

SCRIPT_FILE_PATH="build.sh"

MODES=( \
    "clean" \
    "jar" \
    "checksum" \
    "signature" \
    "zip" \
)

for mode in ${MODES[@]}; do
    ./${SCRIPT_FILE_PATH} $mode
done
