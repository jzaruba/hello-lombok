@ECHO Off

if "%~3" == "" (
  echo Error: Insufficient arguments 1>&2
  echo.
  echo Changes extensions from ^<old^> to ^<new^> in a given ZIP ^(or JAR^) archive.
  echo Usage:
  echo   %~n0 ^<JAR archive^> ^<old extension^> ^<new extension^>
  echo Example:
  echo   %~n0 lombok-1.16.18.jar .lombok "."
  echo   - strips the extension from *.lombok files
  echo   %~n0 lombok-1.16.18.jar .SCL .class
  echo   - changes the extension to .class on *.SCL files
  echo   %~n0 lombok-1.16.18.jar "" .class
  echo   - appends .class extension to *all* files
  echo.
  echo The ^<old extension^> will be used to find relevant files, then the ^<new extension^>
  echo will be applied instead of what DOS considers a filename extension on these files.
  pause
  exit /b 1
)

call env "%~f0"

set WORK_DIR=%~n1
set JAR=%~dpnx1
set OUT=%~dp0%~n1~%~x1

set X_OLD=%~2
set X_NEW=%~3

if exist "%WORK_DIR%\" (
  Error: Directory "%WORK_DIR%" already exists
  exit /b 1
)

mkdir "%WORK_DIR%"
if NOT %ERRORLEVEL% == 0 exit /b 1

pushd "%WORK_DIR%"
if NOT %ERRORLEVEL% == 0 exit /b 1

call "%Z7%" x "%JAR%"

echo Renaming...
setlocal EnableDelayedExpansion
for /f "usebackq" %%I in (`"dir /b /s *%X_OLD%"`) do (
  ren "%%I" "%%~nI!X_NEW!"
)
endLocal

echo Compressing...
call "%Z7%" a -r "%OUT%" *

echo Deleting...
del /s /q *

popd

rmdir /s /q "%WORK_DIR%"
