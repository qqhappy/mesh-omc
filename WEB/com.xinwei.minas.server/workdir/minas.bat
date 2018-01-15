@echo off

pushd %~dp0

java -cp ./etc/launcher LauncherBootstrap -launchfile launcher_server.xml -executablename launcher server %*

popd

pause