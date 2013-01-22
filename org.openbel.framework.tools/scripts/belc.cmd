@echo off
setlocal

REM This script compiles BEL Documents into KAM.......
REM
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.
REM
REM Copyright 2011 Selventa, Inc. All rights reserved.

call "%~dp0\setenv.cmd"

java %JAVA_OPTS% -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.PhaseZeroApplication %*
if %ERRORLEVEL% NEQ 0 (
    CALL :REPORT_ERROR %ERRORLEVEL% 0
    EXIT /B %ERRORLEVEL%
)

java %JAVA_OPTS% -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.PhaseOneApplication %*
if %ERRORLEVEL% NEQ 0 (
    CALL :REPORT_ERROR %ERRORLEVEL% I
    EXIT /B %ERRORLEVEL%
)

java %JAVA_OPTS% -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.PhaseTwoApplication %*
if %ERRORLEVEL% NEQ 0 (
    CALL :REPORT_ERROR %ERRORLEVEL% II
    EXIT /B %ERRORLEVEL%
)

java %JAVA_OPTS% -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.PhaseThreeApplication %*
if %ERRORLEVEL% NEQ 0 (
    CALL :REPORT_ERROR %ERRORLEVEL% III
    EXIT /B %ERRORLEVEL%
)

java %JAVA_OPTS% -Dderby.system.durability=test -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.PhaseFourApplication %*
if %ERRORLEVEL% NEQ 0 (
    CALL :REPORT_ERROR %ERRORLEVEL% IV
    EXIT /B %ERRORLEVEL%
)

goto END


:REPORT_ERROR
echo BEL Compiler exited with error code %1 in phase %2.

:END
endlocal
