#!/bin/bash

# This script performs validation checks on a BEL or XBEL Document
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

. $(dirname $0)/../setenv.sh
java $JAVA_OPTS -classpath $BELCOMPILER_CLASSPATH org.openbel.framework.tools.BelCheck "$@"
