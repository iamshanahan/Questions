# Twenty-Something Questions
Given a set of creatures, how do you construct the best question list to uniquely identify them?

# Motivation
In the children's game "twenty questions", one asks a series of boolean questions to eventually identify a creature.  It is obvious to anyone who's played it that the questions are sequentially conditional, that is, what one asks for question 3 will depend on the answers to 1 and 2.  This project explores the idea that the questions are fixed.

For example, if you have "cat", "bat", "cassowary", and "killdeer", you could ask, "Does it have feathers, and does it fly?".  Then "yes, no" would yield cassowary, while "no, yes" would yield bat.  Now if you add "beaver" to the possibilities, then in order to have enough questions you must either add another piece of information ("Does it have feathers, does it fly, and is it the Oregon state animal?") or default to a degenerate identity fact ( "Does it have feathers, does it fly, and is it a beaver?").

Let's define some terms.  We have C creatures, named a, b, c, etc.  We have A attributes represented by the natural numbers, 1, 2, ... (1-based because I'm a human).  For each creature-attribute pair, we have a boolean result.  This matrix of booleans is our input, Z (um, for zoo?).  We can picture this as a vector of bitmaps.  In addition, for any Z there are C implicit identity attributes (e.g. "c?"), each of which is false for every creature save one.  For brevity we won't include them in representations.  For any Z there is some set Q of question lists, each containing N questions, and each being sufficient to distinguish among all C creatures.

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

A few early results:
1. It is an error if C = 0.  Asking, "how many questions must I ask to distinguish among no creatures" makes no sense.
2. If C = 1 the minimal question list is the empty set.
3. 2^N >= C.  Or N >= log-base-2 C.  Three questions only have 8 possible outcomes, so those aren't enough questions for 12 creatures.
4. If we allow identity questions, N <= C-1.

