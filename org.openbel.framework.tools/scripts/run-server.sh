#!/bin/bash

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

. $(dirname $0)/setenv.sh 

if [ -z "$TOMCAT_HTTP_PORT" ]; then
    export TOMCAT_HTTP_PORT=8080
fi
export JAVA_OPTS="$JAVA_OPTS -Dtomcat.http.port=$TOMCAT_HTTP_PORT"

BASE_URL="http://localhost:$TOMCAT_HTTP_PORT"
echo "BEL Server successfully started. Default url is $BASE_URL/"
echo "    WebAPI is available. Default url is $BASE_URL/openbel-ws/"
echo "    WebAPI WSDL is available. Default url is $BASE_URL/openbel-ws/belframework.wsdl"

exec ${BELFRAMEWORK_HOME}/server/tomcat/bin/catalina.sh run
