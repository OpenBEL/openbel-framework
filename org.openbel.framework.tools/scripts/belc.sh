#!/bin/bash

# This script compiles BEL Documents into KAM.......
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

. $(dirname $0)/setenv.sh

java $JAVA_OPTS -classpath "$BELCOMPILER_CLASSPATH" org.openbel.framework.tools.PhaseZeroApplication "$@"
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo -en "BEL Compiler exited with error code ${EC} in phase 0.\n"
    exit ${EC}
fi

java $JAVA_OPTS -classpath "$BELCOMPILER_CLASSPATH" org.openbel.framework.tools.PhaseOneApplication "$@"
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo -en "BEL Compiler exited with error code ${EC} in phase I.\n"
    exit ${EC}
fi

java $JAVA_OPTS -classpath "$BELCOMPILER_CLASSPATH" org.openbel.framework.tools.PhaseTwoApplication "$@"
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo -en "BEL Compiler exited with error code ${EC} in phase II.\n"
    exit ${EC}
fi

java $JAVA_OPTS -classpath "$BELCOMPILER_CLASSPATH" org.openbel.framework.tools.PhaseThreeApplication "$@"
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo -en "BEL Compiler exited with error code ${EC} in phase III.\n"
    exit ${EC}
fi

java $JAVA_OPTS -classpath "$BELCOMPILER_CLASSPATH" org.openbel.framework.tools.PhaseFourApplication "$@"
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo -en "BEL Compiler exited with error code ${EC} in phase IV.\n"
    exit ${EC}
fi
