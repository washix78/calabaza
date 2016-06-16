set JRE_HOME=./bin/${jre directory-name}
set PATH=;%JRE_HOME%/bin;%PATH%
set CALABAZA_VERSION=1.0.0
java -version
java -cp ;./bin;./bin/calabaza-%CALABAZA_VERSION%.jar;./bin/lib/*; calabaza.CalabazaMain
