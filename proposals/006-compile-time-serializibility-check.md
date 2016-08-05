# SCP-[006]: [Compile Time Check of Serializability]

## Proposer

Proposed by Spark Technology Center, IBM, August 9, 2016

## Abstract

In the use of Apache Spark RDDs, users specify closures to higher order
functions that are shipped to (possibly) remote nodes. If said closure
references a symbol that is not serialized by Spark, the user would only
find out at runtime with a NotSerializableException thrown by the Spark engine.
This is obviously a waste of the user's time and resources.

For instance, in this code snippet:
```
class Adder(val y: Int) {
  def add(rdd: RDD[Int]): RDD[Int] = rdd.map(x => x + y)
}
```
The lambda `x => x + y` references the class member/getter `y`. This snippet
would compile but the Spark engine would throw an error.

Heather Miller worked on the Spores project that, with appropriate usage, could
inform the user at compile time about this error. It must be used like this:
```
class Adder(y: Int) {
  def add(rdd: RDD[Int]): RDD[Int] = rdd.map(
    spore {
      val y = this.y // will not compile if this is omitted
      (x: Int) => x + y
    }
  )
}
```

This is a great start. Such a mechanism would also benefit other projects
like Akka and any other distributed/concurrent framework.

A slightly different representation of this check, such as an annotation,
might be more elegant.


## Proposal

The Scala Center should evaluate the potential benefit of the availability of
such a safe serialization construct. If determined to be useful, it could also explore alternate
ways of expressing this check.
Also a discussion of whether this becomes part of the language, core libraries
or is maintained as a separate project would be useful.

## Cost

Uncertain, but depends on how it is adopted.

## Timescales

Probably less than 6 months.
