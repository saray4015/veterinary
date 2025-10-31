@echo off
echo Limpiando proyecto...
call mvnw.cmd clean
rmdir /s /q target
rmdir /s /q .mvn\wrapper\downloads
rmdir /s /q "%USERPROFILE%\.m2\repository\org\projectlombok"
echo Limpieza completada.
pause
