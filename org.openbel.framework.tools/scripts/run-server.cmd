@echo off
setlocal

REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.
REM
REM Copyright 2011 Selventa, Inc. All rights reserved.

call "%~dp0\setenv.cmd"
cd "%BELFRAMEWORK_HOME%\server\tomcat\bin"

if not defined TOMCAT_HTTP_PORT (
    set TOMCAT_HTTP_PORT=8080
)
set JAVA_OPTS=%JAVA_OPTS% -Dtomcat.http.port=%TOMCAT_HTTP_PORT%

set BASE_URL="http://localhost:%TOMCAT_HTTP_PORT%"
echo BEL Server successfully started. Default url is %BASE_URL%/
echo     WebAPI is available. Default url is %BASE_URL%/openbel-ws/
echo     WebAPI WSDL is available. Default url is %BASE_URL%/openbel-ws/belframework.wsdl
call catalina.bat start

goto END

:END
endlocal

