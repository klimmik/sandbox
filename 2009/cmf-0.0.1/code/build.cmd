echo off

set JAVA_HOME=C:\Progra~1\Java\jdk1.5.0

set ANT_OPTS=-Xmx1024m

set CUR_PATH=%PATH%
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%

ant -f make/build.xml %*

set PATH=%CUR_PATH%

echo on
