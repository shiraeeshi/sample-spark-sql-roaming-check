# sample-spark-sql-roaming-check

## Input/Output

It reads two files ```data/input/local.csv``` and ```data/input/roaming.csv``` and produces a result file in ```data/output-****/``` directory (```****``` means current time converted to a number; the suffix was added just to avoid name conflicts).

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
## Performance considerations

The code sets ```spark.sql.crossJoin.enabled``` configuration value to ```true```. The downside is that cross join operation doesn't perform well on RDDs. Another option is to broadcast one of RDDs (a smaller one, we know that roaming RDD has less records), but we don't know for sure whether this RDD will fit into memory or not. That's why I decided to use cross joins.
