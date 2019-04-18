# Questions
> Animal, Vegetable, or Minimal?

This project models the problem of finding the shortest lists of questions that can identify a creature.  Think "20 Questions", except where the list of questions is fixed, i.e. determined in advance.  See the wiki for background and analysis.

## Installing / Getting started / Building

This is pure java whose only dependency is junit.  Standard gradle tasks apply, mainly '```build```' and custom project tasks.

To see project execution tasks, run
```shell
gradle tasks --all
```

### Terminology

There are a few concepts one must understand to use this project:
   * Creature: Any thing in a list of such things that are to be distinguished from each other by asking questions.
   * Attribute Question (sometimes just attribute, or just question): A yes/no question that can be asked about each creature, in a list of such questions.
   * Identity Question: If given a list of creatures but no attributes, one can still distinguish among them by asking, "Is it creature 1?  Is it creature 2?...".  Identity questions exist for free for any list of creatures.
   * Truth Table: The answer to every attribute question for every creature.  When convenient, truth tables may be thought of as implicitly including the answers to all the identity questions as well.
   * Distinguishing Question List: A subset of the attribute and identity questions that is guaranteed to identify any creature in the list of creatures.  I emphasize that this is not a dichotomy key--it is a fixed list of questions.
   * Minimal Question List: A distinguishing list that is the shortest possible, i.e. has the fewest questions, for a given truth table.
   * Minimal Question List Set: The set of all minimal question lists for a truth table.  It is possible that multiple question lists are tied for length.

## Usage

To see project execution tasks, run
```shell
gradle tasks --all
```

Every gradle project target takes some number of files as arguments.  Execute them without arguments to see usage statements.  Note that to pass arguments to a gradle target one must use --args='arg1 arg2'.  Also be aware that interactive input (prompt/response) gets jumbled a little when inside a gradle target task execution.

There is a create-table target that walks the user through building a truth table by asking for creatures, questions, and answers.  The result is a file.

There is a find-minimal-lists target that will take a truth-table file as input and produce a minimal question list set as output.  The result is a file.

There is a target that will take a truth-table file and a question-list-set file and emit information about the question lists.  It can also emit a particular question list in long form.  It emits to stdout.

There is a quiz-me target that actually asks the user the questions in a list and identifies the correct creature.

It may be enlightening to look through the sample test files.

## Complexity

Complexity goes through the roof pretty fast, worst-case is roughly n^3 * 2^n.  The state-birds truth table remains unsolved.  I killed it after 11 hours.

Figuring out reduced-complexity approach is the real problem.  This project currently represents all the objects


## Contributing

This is personal sample code.  Do not contribute.  Contact me regarding exceptions, especially if you have a fast algorithm.

## Links
TBA

## Licensing

I reserve all rights.  You may look at it and execute it.  Do not copy, fork, or modify any parts.
