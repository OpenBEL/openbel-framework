@echo off

REM This script sets the BEL_FRAMEWORK environment variable if it is not
REM already set.......
REM
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.
REM
REM Copyright 2011 Selventa, Inc. All rights reserved.

set DIR=%~dp0
if "%DIR%"=="tools" (
    REM Running a framework tool from outside tools\
    set BASE_DIR=%DIR%\..\
) else (
    set BASE_DIR=%DIR%
)

if not defined BELFRAMEWORK_HOME (
    set BELFRAMEWORK_HOME=%BASE_DIR%
)

if not exist "%BELFRAMEWORK_HOME%" (
    echo "Could not locate BELFRAMEWORK_HOME, path was: %BELFRAMEWORK_HOME%"
    exit /B 1
)

set BELCOMPILER_DIR=%BASE_DIR%\lib
if not exist "%BELCOMPILER_DIR%" (
    echo "Could not locate BEL Compiler lib directory, path was: %BELCOMPILER_DIR%"
    exit /B 1
)

set BELCOMPILER_CLASSPATH=%BELCOMPILER_DIR%\BELFrameworkTools-*.jar;%BELCOMPILER_DIR%\*

if not defined JAVA_OPTS (
    set JAVA_OPTS=-Xmx1024m -Dderby.stream.error.field=org.openbel.framework.common.enums.DatabaseType.NULL_OUTPUT_STREAM
)
