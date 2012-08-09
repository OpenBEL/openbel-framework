#!/bin/bash

# This script sets the BEL_FRAMEWORK environment variable if it is not
# already set.......
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

if $(dirname $0 >/dev/null 2>&1); then
    DIR=$(dirname $0)

    if [[ "${DIR}" == *tools* ]]; then
        export BASE_DIR=${DIR}/../
    elif [ -f "../setenv.sh" ]; then
        # Running a framework tool from outside tools/
        export BASE_DIR=${DIR}/../
    elif [ -f "setenv.sh" ]; then
        # Running from distribution base directory
        export BASE_DIR=${DIR}
    else
        # Assume we're running a framework tool
        export BASE_DIR=${DIR}
    fi

    if [ -z "${BELFRAMEWORK_HOME}" ]; then
        export BELFRAMEWORK_HOME=${BASE_DIR}
    fi
    
    if [ ! -d "${BELFRAMEWORK_HOME}" ]; then
        echo "Could not locate BELFRAMEWORK_HOME, path was: ${BELFRAMEWORK_HOME}"
        exit 1
    fi
    
    BELCOMPILER_DIR="${BASE_DIR}/lib"
    if [ ! -d "${BELCOMPILER_DIR}" ]; then
        echo "Could not locate BEL Compiler classpath, path was: ${BELCOMPILER_DIR}"
        exit 1
    fi
    
    export BELCOMPILER_CLASSPATH=".:${BELCOMPILER_DIR}/*"
    
    if [ -z "${JAVA_OPTS}" ]; then
        export JAVA_OPTS="-Xmx1024m -Dderby.stream.error.field=org.openbel.framework.common.enums.DatabaseType.NULL_OUTPUT_STREAM"
    fi
else
    echo 'Please use belc.sh.'
fi

