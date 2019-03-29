# Animal, Vegetable, or Minimal?
Given a set of creatures, how do you construct the best question list to uniquely identify them?

# Motivation
In the children's game "twenty questions", one asks a series of boolean questions to eventually identify a creature.  It is obvious to anyone who's played it that the questions are sequentially conditional, that is, what one asks for question 3 will depend on the answers to 1 and 2.  (First you ask if it's a fish, then if yes, you ask if it's a shark.)  This project explores the idea that the questions are fixed.  So you have some creatures, and you have some yes/no questions or attributes, and you know the answer to the question for each creature.  Your job is to come up with a list of questions whose answers are guaranteed to identify any creature.

For example, if you have "cat", "bat", "cassowary", and "killdeer", you could ask, "Does it have feathers, and does it fly?".  Then "yes, no" would yield cassowary, while "no, yes" would yield bat.  Now if you add "beaver" to the possibilities, then in order to have enough questions you must either add another piece of information ("Does it have feathers, does it fly, and is it the Oregon state animal?") or default to a degenerate identity fact ( "Does it have feathers, does it fly, and is it a beaver?").

Let's define some terms.  We have C creatures, named a, b, c, etc.  We have A attributes represented by the natural numbers, 1, 2, ... (1-based because I'm a human).  For each creature-attribute pair, we have a boolean result.  This matrix of booleans is our input, Z (um, for zoo?).  We can picture this as a vector of bitmaps.  In addition, for any Z there are C implicit identity attributes (e.g. "a?"), each of which is false for every creature save one.  For brevity we won't include them in representations.  For any Z there is some set Q of question lists, each containing N questions, and each being sufficient to distinguish among all C creatures.  N is the smallest length a sufficient question list can have.

To summarize:
   * C = number of creatures
   * a = some creature
   * A = number of attributes or boolean questions
   * 1? = some attribute or question
   * Z = the truth table for C X A
   * a? = some identity question
   * Q = the set of minimal question lists
   * N = the minimum number of questions
   * L = some question list

For example, suppose our input is "a:TTF, b:FFF, c:TTT".  If we ask, "1?,2?" and the answer is "F,F", then we have identified b.  But if the answer is "T,T", both a and b are possible.  Therefore "1?,2?" is not in Q.  But "2?,3?" is.  So is "2?,c?".  So L <= 2.

Now we can ask some interesting (to me) questions
1. How do you generate a minimal question list?
2. Or what about the set of all tied-for-minimal question lists?
3. How can you tell whether it's possible to create a question list that does not use identity attributes?
4. Is there a way to find a minimal question list that also uses minimal identity attributes?

# A bit of analysis
A few early results:
1. It is an error if C = 0.  Asking, "how many questions must I ask to distinguish among no creatures" makes no sense.
2. If C = 1 the minimal question list is the empty set.
3. 2^N >= C.  Or N >= log-base-2 C.  Three questions only have 8 possible outcomes, so those aren't enough questions for 12 creatures.
4. If we allow identity questions, N <= C-1.
5. Another way to ask the main question is, What are the smallest subset's of Z's columns that have all unique entries?

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
   * Methods that return with any give L is sufficient, and whether it contains identity attributes.
   * A brute-force method that finds solutions.
   * Attempt to find a faster algorithm.

# Implementation Notes
   * More time getting gradle and eclipse to play nice than coding.  Not entirely successful.
   * Tried object streams and serializable but Writer is working well enough for now.
   * Considered CSV, YAML, but the format I ended up with is simple.
   * If this were a real program we'd add names to creatures and text to attributes.
   * I never know the rules for flush and close.  Gotta refresh.
   * I am super inconsistent about spacing around parentheses.
  
