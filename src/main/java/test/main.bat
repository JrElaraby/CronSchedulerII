@echo off
setlocal enabledelayedexpansion

set "folderName=NewFolder"

mkdir "!folderName!"

if !errorlevel! equ 0 (
    echo Folder '!folderName!' created successfully.
) else (
    echo Failed to create folder '!folderName!'.
    exit /b 1
)

endlocal
