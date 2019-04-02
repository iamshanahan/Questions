# Animal, Vegetable, or Minimal?
Given a set of creatures, how do you construct the best question list to uniquely identify them?

# Motivation

There is a pleasant intersection between discrete math problems and computation problems where the answer is an algorithm.  A 
better answer is a more efficient algorithm. Examples include searches, sorts, and graph traversals.  An engineering-student friend mentioned this problem to me and I found it interesting.

In the children's game "twenty questions", one asks a series of boolean questions to eventually identify a creature.  It is obvious to anyone who's played it that the questions are sequentially conditional, that is, what one asks for question 3 will depend on the answers to 1 and 2.  If you first ask, "Is it a fish?", you will only ask, "Is it a shark?" if the answer is "yes".

This project explores the idea that the questions are fixed.  That is, you have to pick all the questions in advance.  So you have some creatures, and you have some yes/no questions or attributes, and you know the answer to the question for each creature.  Your job is to come up with a fixed list of questions whose answers are guaranteed to identify any creature.

For example, if you have "cat", "bat", "cassowary", and "killdeer", you could ask, "Does it have feathers?, and Does it fly?".  Then "yes, no" would yield cassowary, while "no, yes" would yield bat.  You could also ask "Is it a cat?, Is it a bat?, and Is it a cassowary?".  Then any "yes" would give you your answer and "no" would mean killdeer.  We'll call these identity questions.  You always have identity questions, even if I give you no attributes about the creatures.  But in this case asking only identity questions resulted in a longer question list.  You'd kind of like to use identity questions as a last resort.

Now if you add "beaver" to the possibilities, then "no, no" is ambiguous between cat and beaver.  So in order to have enough questions you must either add another piece of information ("Does it have feathers?, does it fly?, and is it the Oregon state animal?") or degenerate to a identity question ( "Does it have feathers, does it fly, and is it a beaver?").  Those question lists are tied for length at three questions.

Let's define some terms.  We have C creatures, named a, b, c, etc.  We have A attributes represented by the natural numbers, 1, 2, ... (1-based because I'm a human).  For each creature-attribute pair, we have a boolean result.  This matrix of booleans is our input, Z (um, for zoo?).  We can picture this as a vector of bitmaps.  In addition, for any Z there are C implicit identity attributes (e.g. "a?"), each of which is false for every creature save one.  We can picture this as an identity matrix tacked onto the right side of that vector of bitmaps.  For any Z there is some set Q of question lists, each containing N questions, and each being sufficient to distinguish among all C creatures, where N is the smallest length a sufficient question list can have.

To summarize:
   * C = number of creatures
   * a = some creature
   * A = number of attributes or boolean questions
   * 1? = some attribute or question
   * Z = the truth table for C X A (with identity matrix for C column-appended).
   * a? = some identity question
   * Q = the set of minimal question lists
   * N = the minimum number of questions
   * L = some question list

For example, suppose our input is "a:TTF, b:FFF, c:TTT".  We could choose as L "1?,2?".  Then if the answer is "F,F", then we have identified b.  But if the answer is "T,T", both a and b are possible.  Therefore "1?,2?" is not in Q.  But "2?,3?" is.  So is "2?,c?".  So N <= 2.

Now we can ask some interesting (to me) questions
1. How do you generate a minimal question list?
2. Or what about the set of all tied-for-minimal question lists?
3. How can you tell whether it's possible to create a question list that does not use identity attributes?
4. Is there a way to find a minimal question list that also uses minimal identity attributes?

# A bit of analysis
A few early results:
1. If C=0 the minimal question list is the empty set.  At first I thought this was an error, but my code disagreed, and so I had to think about it some more.
2. If C = 1 the minimal question list is the empty set.
3. 2^N >= C.  Or N >= log-base-2 C.  Three questions only have 8 possible outcomes, so those aren't enough questions for 12 creatures.
4. If we allow identity questions, N <= C-1.
5. Another way to ask the main question is, What are the smallest subsets of Z's columns that have all unique entries?

In case some smarty is already thinking, "well, you know...", let's indulge in an aside about composite questions:
1. If we allow identity questions AND composite questions, then this quickly devolves to a binary search, As an example, for a-h: { "Is it a, b, c, or d?", "Is it a, b, e, or f?", "Is it a, c, e, or g?" } is a sufficient and minimal list.
2. In the variation where identity questions are not allowed but composite questions are, then if any solution exists, then it's possible to reduce it to a binary search with enough ANDs, ORs, and NOTs.  This isn't intuitively obvious, but the proof is simple, and omitted, because this aside is already off topic.
3. Existence of a composite solution implies existence of a (probably longer) non-composite solution.  Off-topic proof omitted.
It's probably obvious I'm not that interested in composite questions.

# This is all great, but wasn't there supposed to be code?
Here's some things to code
   * A representation for Z
   * Make Z's representation serializable/Create a file format/DSL.
   * Add some validation
   * A representation of L
   * Methods that return whether any give L is sufficient, and whether it contains identity attributes.
   * A brute-force method that finds solutions.
   * Attempt to find a faster algorithm.

# Implementation Notes
   * More time getting gradle and eclipse to play nice than coding.  Not entirely successful.
   * Tried object streams and serializable but Writer is working well enough for now.
   * Considered CSV, YAML, but the format I ended up with is simple.
   * If this were a real program we'd add names to creatures and text to attributes.
   * I never know the rules for flush and close.  Gotta refresh.
   * I am super inconsistent about spacing around parentheses.
  
