# CRON Parser

This command line application parses a cron string and expands each field
to show the times at which it will run.

We only consider the standard cron format with five time fields (minute, hour, day of
month, month, and day of week) plus a command. The input will be on a single line.
The cron string is passed as a single argument to the application.

```~$ your-program "*/15 0 1,15 * 1-5 /usr/bin/find"```


## Limitations
- This does not handle the special time strings such as "@yearly"
- This does not handle if the month has less than 31 days or not


## Run Instructions

#### IntelliJ

- open repo in IntelliJ and use the provided IntelliJ run/debug configuration

#### Docker

```
$ docker run -it $(docker build -q .)
```

#### Shell Script

```
$ ./run.sh "*/15 0 1,15 * 1-5 /usr/bin/find"
```

The shell script checks if there is a java installation available. If not it throws and error.
Else it checks if the version is >= 17. If yes, then it compiles using gradle and runs the main class from there.

#### Command Line
- build the jar using <br>
  `./gradlew clean build`
- Built jar will be in `build/libs/` directory and by the name `CRON-Parser-1.0-SNAPSHOT.jar`
- Run the jar using<br>
  `java -cp build/libs/CRON-Parser-1.0-SNAPSHOT.jar com.cron.Parser <CRON EXPRESSION>`


## Sample command and output

#### Command
`java -cp build/libs/CRON-Parser-1.0-SNAPSHOT.jar com.cron.Parser "*/15 0 1,15  * 1-5 /usr/bin/find"`

#### Output
```
minute          0 15 30 45
hour            0
day of month    1 15
month           1 2 3 4 5 6 7 8 9 10 11 12
day of week     1 2 3 4 5
command         /usr/bin/find
```