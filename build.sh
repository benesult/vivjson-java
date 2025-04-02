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

# Carefully, The following file paths should be given as environment variable.
# - "MAVEN_BIN_PATH" for "mvn".
# - "GPG_PASS_PHRASE_PATH_FOR_VIVJSON_JAVA" for pass phrase of GPG.

FILE_NAME="vivjson"
GROUP="com.benesult"

POM_PATH="pom.xml"

CONFIGS=( \
    "clean,clean" \
    "jar,package|source:jar-no-fork|javadoc:jar" \
    "checksum,-" \
    "signature,-" \
    "zip,-" \
)

GPG_USER_ID="benesult"

maven=`eval echo $MAVEN_BIN_PATH`

gpg_pass_phrase_path=`eval echo $GPG_PASS_PHRASE_PATH_FOR_VIVJSON_JAVA`

# Show usage of this script.
show_usage() {
    echo "Usage: $0 <mode>"
    echo
    echo "mode: clean, jar, checksum, signature"
}

# Get config.
#   1st arg: mode (clean, jar, and so on)
get_config() {
    result=""
    for config in "${CONFIGS[@]}"; do
        mode=`echo $config | cut -d "," -f 1`
        if [ "$1" = "$mode" ]; then
            result=`echo $config | cut -d "," -f 2`
            break
        fi
    done
    echo $result
}

# Make checksum file.
#   1st arg: target file path
make_check_sum_file() {
    for codec in "sha1" "md5"; do
        openssl $codec $1 | cut -d " " -f 2 | tee $1.$codec
    done
}

# Get GPG pass phrase.
get_gpg_pass_phrase() {
    # Remove new line codes
    pass_phrase=`sed -e "s/\r//g" -e "s/\n//g" $gpg_pass_phrase_path`
    echo $pass_phrase
}

# Get version from source jar file.
get_version() {
    jar=`find ./target -maxdepth 1 -type f -name "$FILE_NAME*-sources.jar"`
    if [ "$jar" = "" ]; then
        echo ""
        exit
    fi
    echo `echo $jar | cut -d "-" -f 2`
}

if [ $# -eq 0 ]; then
    show_usage
    exit
fi
mode=$1

config=$(get_config $mode)
if [ "$config" = "" ]; then
    show_usage
    exit
fi

# Make check-sum files without maven.
if [ "$mode" = "checksum" ]; then
    # Get version
    version=$(get_version)
    if [ "$version" = "" ]; then
        echo "Error: No jar file"
        exit
    fi
    echo $version

    if [ ! -d "target" ]; then
        echo "Error: No target directory"
        exit
    fi
    cd target

    # Copy a pom file.
    cp -p ../$POM_PATH $FILE_NAME-$version.pom

    # Make check-sum files.
    files=`find . -maxdepth 1 -type f -name "$FILE_NAME*.jar"`
    files+=("$FILE_NAME-$version.pom")
    for file in ${files[@]}; do
        echo "Progress: $file"
        make_check_sum_file $file
    done

    cd ..
    exit
fi

# Make signature files without maven.
if [ "$mode" = "signature" ]; then
    if [ ! -d "target" ]; then
        echo "Error: No target directory"
        exit
    fi
    cd target

    # Make signature files.
    pass_phrase=$(get_gpg_pass_phrase)
    files=`find . -maxdepth 1 -type f -name "$FILE_NAME*.jar"`
    files+=(`find . -maxdepth 1 -type f -name "$FILE_NAME*.pom"`)
    for file in ${files[@]}; do
        echo "Progress: $file"
        echo $pass_phrase | gpg --batch --pinentry-mode loopback --passphrase-fd 0 -u $GPG_USER_ID -ab $file
    done

    cd ..
    exit
fi

# Make a zip file.
if [ "$mode" = "zip" ]; then
    directories=()

    # Collect directories (1/3).
    previous_directory=""
    for i in `seq 1 10`; do
        directory=`echo $GROUP| cut -d "." -f $i`
        if [ "$directory" = "" ]; then
            break
        fi
        directories+=($directory)
    done
    # Collect directories (2/3).
    directories+=($FILE_NAME)
    # Collect directories (3/3).
    version=$(get_version)
    if [ "$version" = "" ]; then
        echo "Error: No jar file"
        exit
    fi
    directories+=($version)

    current_directory=`pwd`

    if [ ! -d "target" ]; then
        echo "Error: No target directory"
        exit
    fi
    cd target

    # Make directory and go into it.
    first=""
    for directory in ${directories[@]}; do
        mkdir -p $directory
        cd $directory
        if [ "$first" = "" ]; then
            first=$directory
        fi
    done

    # Copy files.
    files=`find $current_directory/target -maxdepth 1 -type f -name "$FILE_NAME-$version*"`
    for file in ${files[@]}; do
        cp -p $file .
    done

    cd $current_directory/target

    # Make a zip file.
    rm -f "$FILE_NAME-$version.zip"
    zip -r "$FILE_NAME-$version.zip" $first

    cd ..

    exit
fi

# Using mvn
previous_command=""
for i in `seq 1 10`; do
    command=`echo $config | cut -d "|" -f $i`
    if [ "$command" = "" ] || [ "$command" = "$previous_command" ]; then
        break
    fi

    $maven $command -f $POM_PATH

    previous_command=$command
done
