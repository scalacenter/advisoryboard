# Accessible Scala

## Proposer

Bill Venners and Lars Hupel on behalf of the FLOSS community, written by Sam
Halliday with input from Jon Pretty and Rui Batista.

### Abstract

Scala is proudly a welcoming environment for all. One way to maintain and
demonstrate this would be to provide industry-leading support for blind and
partially-sighted developers.

### Proposal

The goal should be to reduce the noise from a literal character-by-character
reading of a fragment of code.

For example, the following code

```scala
def foo[A: Wibble](s: String, b: Wobble[A]): Int = ...
```

may be read by a text-to-speech engine, such as `espeak`, as

```
def space foo open square bracket capital s colon space
wibble close square bracket open bracket a colon space
string comma space b colon space wobble open square bracket
capital a close square bracket close bracket colon
space Int space equals space ...
```

Indeed such a description is necessary to enable precision editing.

However, the following would convey the same information more efficiently:

```
def foo
type A, context Wibble
from s String and b Wobble for A
to Int
```

and a more terse variant could be useful in a high-level summary report

```
method foo String Wobble to Int
```

The Scala AST has many constructs that could be similarly rendered into
verbally-meaningful prose. `scala.meta` allows us to access the AST relatively
easily and output textual descriptions in realtime, to be picked up by the text
editor.

Nested types pose a problem as it is not clear how to best verbally describe
them, for example

```scala
Either[Wobble[T], Option[Wobble[S]]]
```

or its higher-kinded form

```scala
_[_[_], _[_[_]]]
```

or scaladocs and line comments.

Research and experimentation is required to explore the best ways to describe
more complex code blocks. For example, it may be that users like to set up
aliases for common patterns in their codebases: the above may be easier to grok
as `wobble t or optwobble s`.

In this project, we propose that a proof-of-concept command line tool be written
that can generate English verbal descriptions, for source files or snippets of

Scala code. Key features would include

- description at point
- summary at point
- where am I?

#### Use cases

The user may issue a request to describe the current block of code (or symbol /
type), specifying the context: the current symbol, the current block, the
current method, etc.

The user may request a verbal "summary at point", obtaining a lossy but higher
level rendering of the content than the description.

The user may request "where am I?" and get a breadcrumb style description:
"method foo, inside class Foo, inside package com dot acme dot bar"

It may be possible to leverage existing work from the `scala.meta` [AST
Explorer](https://blog.buildo.io/exploring-scala-ast-in-your-browser-dc0b1fb743e0).
The tool could either generate ASTs on the fly, or read an intermediate file
format that is produced prior to compilation by the build tool.

Response time of the tool is paramount, it would make sense to write the tool in
scala-native or run with nailgun. The tool should be able to take input such:

- source scala file location (which may be inside an zip or jar archive)
- type of description (e.g. breadcrumb location, describe, summarise)
- cursor location (or region highlighted)
- how many layers of "expand" to jump

and return text that could be provided to the text editor buffer for conversion
to speech or braille.

The returned text should use a hypertext markup (or convention) to include links
to referenced content. For example, a summary of the above could be

```
[method foo signature][6,54] and its [implementation][54,60]
```

The screen reader will then be able to choose to omit the hyperlink data,
instead indicating to the user that a subsequent command could jump to the
relevant part of the source.

Future work may include integration with the compiler reporter, REPL and
scaladocs, along with further improvements and personalisations / localisations
of the verbal descriptions.

On the IDE / editor integration side, the tool should be easily integrated in a general porpose IDE or editor, in such a way that leverages existing accessibility support with minimal or no effort, except for user specific configuration or personalization.


## Cost

No external costs.

## Timescales

A single developer could deliver the proof of concept within a month. A binary
distribution of the tool is not required, preferring compile from source as
users are encouraged to improve and maintain the tool going forward.

Rui Batista has offered to provide code input alongside expected output, along
with general accessibility advice.


## Example output

Follows an additional set of inital attemps for speech output of some common scala constructs. We provide both verbose output and high level summary versions for each example. 


```scala
case class Person(name: String, address: Address, age: Int)
// case class Person containing name String, address address Address and age Int
// case class Person with name, address and age
```


```scala
trait Monad[F[_]] extends Applicative[F] {
    // five methods defined here...
}
// trait Monad with higher type F extending applicative of F with five declarations
// trait Monad higher F
```

```scala
val a = 5
// val a with value 5 
// val a
```

```scala
type ErrorsOr[A] = ValidatedNel[String, A]
// type alias ErrorsOr type A equal to ValidatedNel of String and type A
// alias ErrorsOr A
```


