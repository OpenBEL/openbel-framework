#!/bin/bash

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Copyright 2011 Selventa, Inc. All rights reserved.

. $(dirname $0)/setenv.sh 

${BELFRAMEWORK_HOME}/server/tomcat/bin/catalina.sh start
EC="$?"
if [ "${EC}" -ne 0 ]; then
    echo "Unable to start server."
    exit 1
else
    echo "BEL Server successfully started. Default url is http://localhost:8080/"
    echo "    WebAPI is available. Default url is http://localhost:8080/openbel-ws/"
    echo "    WebAPI WSDL is available. Default url is http://localhost:8080/openbel-ws/belframework.wsdl"
fi
