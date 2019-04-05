# Maven dependency conflicts demo

## Problem demo

Two maven projects (`guavam1` and `guavam2`) use two different and incompatible versions of Guava (versions 27 and 15, respectively).

Used separately the two projects work:

```
$  mvn -DskipTests package exec:java -am -pl guavam1
...
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ guavam1 ---
Optional.absent()
```
(it prints `Optional.fromJavaUtil(java.util.Optional.empty()).toString()`)


```
$  mvn -DskipTests package exec:java -am -pl guavam2
...
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ guavam2 ---
Not null
```

(this is `MapConstraints.notNull().toString()`)

Projects `guavam12` and `guavam12r` depend on these two previos projects and try to invoke both test methods.
The difference between the integrator projects is the order of the dependencies: `guavam12r` has `guavam2` before `guavam1`.

The output of the integrator projects:
```
$ mvn -DskipTests package exec:java -am -pl guavam12
...
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ guavam12 ---
App1 test: Optional.absent()
App2 test: java.lang.NoClassDefFoundError: com/google/common/collect/MapConstraints
```

```
$ mvn -DskipTests package exec:java -am -pl guavam12r
...
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ guavam12r ---
App1 test: java.lang.NoSuchMethodError: com.google.common.base.Optional.fromJavaUtil(Ljava/util/Optional;)Lcom/google/common/base/Optional;
App2 test: Not null
```

Depending on the order in which the dependencies are declared, of the tests fails because there can be only one Guava library on the classpath.


 ## Solution attempt

Projects `guavab1` and `guavab2` are bundles that embed the previous Maven projects together with their dependcies.
Project `guavab12` uses Eclipse Concierge to load both bundles and then invokes the tests. Output:
```
$ mvn -DskipTests package exec:exec -am -pl guavab12
...
[INFO] --- exec-maven-plugin:1.6.0:exec (default-cli) @ guavab12 ---
---------------------------------------------------------
  Concierge OSGi 5.1.0 on Mac OS X 10.13.6 starting ... (default) startlevel=3
---------------------------------------------------------
---------------------------------------------------------
  Framework started in 0.0 seconds.
---------------------------------------------------------
user.dir=/Users/ckoncz/work/git/hwx/maven-dependency-conflict/guavab12
[com.ck.guavab1-0.0.1.SNAPSHOT] says: Optional.absent()
[com.ck.guavab2-0.0.1.SNAPSHOT] says: Not null
```
The Guava libraries now coexist in the JVM.
