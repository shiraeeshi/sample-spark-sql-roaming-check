# sample-spark-sql-roaming-check

## How to run via "spark-submit"

First you need to package the application:

```
sbt package
```

This command will create a jar file (in my case it was ```target/scala-2.11/simple-spark-sql-roaming-check_2.11-1.0.jar```).

All that's left is just to submit that jar to spark. The ```spark-submit``` command is located in the "bin" directory under the directory where the downloaded spark bundle was extracted into.

```
spark-submit --class spark-submit --class com.example.roamingcheck.RoamingCheckApp target/scala-2.11/simple-spark-sql-roaming-check_2.11-1.0.jar
```
