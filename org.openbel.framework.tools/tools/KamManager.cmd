@echo off
setlocal

REM This script provides utilities to manage the KAM store
REM
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.
REM
REM Copyright 2011 Selventa, Inc. All rights reserved.

call "%~dp0\..\setenv.cmd"
java %JAVA_OPTS% -Dderby.system.durability=test -classpath "%BELCOMPILER_CLASSPATH%" org.openbel.framework.tools.kamstore.KamManager "KamManager.cmd" %*

:END
endlocal

