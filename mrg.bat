@ECHO Off

if "%~3" == "" (
  echo Error: Insufficient arguments 1>&2
  echo.
  echo Merges ^<first^> archive with ^<second^> archive into a ^<third^> archive.
  echo Usage:
  echo   %~n0 ^<first^> ^<second^> ^<third^>
  echo Example:
  echo   %~n0 base.jar updates.jar new.jar
  echo.
  echo Files from ^<one^> wll be overwritten with files from ^<two^> in case of conflict.
  pause
  exit /b 1
)

call env "%~f0"

set ONE=%~dpnx1
set TWO=%~dpnx2
set OUT=%~dpnx3
set WORK_DIR=%~dpn3

if exist "%WORK_DIR%\" (
  Error: Directory "%WORK_DIR%" already exists
  exit /b 1
)

mkdir "%WORK_DIR%"
if NOT %ERRORLEVEL% == 0 exit /b 1

pushd "%WORK_DIR%"
if NOT %ERRORLEVEL% == 0 exit /b 1

call "%Z7%" x -y "%ONE%"
call "%Z7%" x -y "%TWO%"

echo Compressing...
call "%Z7%" a -r "%OUT%" *

echo Deleting...
del /s /q *

popd

rmdir /s /q "%WORK_DIR%"
