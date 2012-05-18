#!/bin/bash

# This script provides utilities to manage the KAM store
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

. $(dirname $0)/../setenv.sh
java $JAVA_OPTS -Dderby.system.durability=test -classpath $BELCOMPILER_CLASSPATH org.openbel.framework.tools.kamstore.KamManager "KamManager.sh" "$@"
