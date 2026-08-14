
TIP : make sure go thro agoda prepation document provided , communicate your thought process proprely 

Coding Round 


Q1: Given an array, find all pairs of elements whose difference is equal to the minimum absolute difference in array. input = [1,3,10,7,5] output = [[1,3], [5,7]] (as minimum absolute diff in any 2 elemnents of array is 2)

Q2: Given an array, for each element, find the distance (gap/index difference) to the next smaller element on its right. This problem can be solved using monotonic stack.

simply price/discount related question. 


Reassign priorities input: [1,4,8,4] output: [1,2,3,2] Solved using treemap and hashset

Molecule Weight Calculator
Given 3 atoms with weights (C -> 12, H -> 1, O -> 8), you're given a chemical formula. Need to compute the total weight. e.g CH4 = 16, H(CH4)2 = 33


1. Monotonic stack based problem involving nearest smaller element.
2. Given a array of priorities, give ranks of every element in priorities. Higher numbers should get higher ranks. 


1. Some pairs with min absolute difference = K, basic question
2. Count palindromic substrings = medium to hard. solved passed all test cases but could not optimize.
3. Question related to binary search very similar to koko eating bananas. Solved at one go.


 Variation of Stock Buy/Sell 
A DP one (similar to Jump Game)


Variation of 726. Number of Atoms where you have to calculate the weight of the molecule given a fixed mapping of the weight of each atom. 

Questions – 2 medium-level LeetCode-style problems Topics – Backtracking & Stacks

* Problem 1: Variation of binary search/sorting.
* Problem 2: Variation of Jump Game (DP)


Platform Round
The questions were around Web protocols, Security, Optimizations, Event Loop

Improve the system like adding gateway , rate limiting , horizontal scalling , DB replication , ideomptency , low latency , avoiding contenation using distributed locks
code review based on solid principles , desing patterns etc
like POST /promo/updatestaus was given , instead of put
create api was missing
<img width="1736" height="1000" alt="thi" src="https://github.com/user-attachments/assets/b0be56db-df0f-4032-8b1d-a05b6abca020" />

￼


Initially was given a problem where data is being supplied to multiple airline aggregators and asked to analyze the design.
It had issues like lack of rate limiting, incorrect api methods, lack of horizontal scaling, lack of DB replication, etc. Idea is to point out the issues and have a discussion with the interviewer.
This went on for 40 minutes, after which I was asked some general questions on Software Testing, Rolling out to limited traffic, etc.
The last 10 - 15 minutes had a code review question where you're given a code snippet (language of your choice) and you need to review the code and highlight issues (ex - Leveraging strategy - factory pattern to avoid if-else chains, etc).



This was a bit different round. Test you on system design, rest API design, code reviews and QA round.
System Design: They will provide a design and you have to figure out what can you improve. Some booking/data fetching system related to booking, like adding LB, rate limiter, messsage queue (based on design whatever you get)
Connectivity Management: Like if you feel the rest APIs could be improved, go ahead and do that, types of authentication you can have and all of that side, how you manage a robust, secure connectivity session
Code review: Related to the booking API, there would be a basic implementation, have to correct that or provide suggestions. I suggested strategy pattern and proper error and exception handling here.


Given a badly designed system → asked to improve it Code review + DB/tech choices Lots of “how / what / why / why not” grilling.

Design Round with a Manager - HackerRank Platform
* Problem 1: Given a problem statement and partially completed design, you are supposed to give a complete HLD and tell what is the issue in the current design. Come up with APIs, similar to normal system design round.
* Problem 2: Given a code block and you have to understand the code and do a code review, checks how quickly you can understand the code and how do you review a code.


Architecture Round

Agoda Search engine
https://dev.to/sumedhbala/hotel-search-system-design-5ea4


Building a flight aggregation system.
https://medium.com/@abhishekranjandev/building-a-flight-reservation-system-like-expedia-9df3874c9960


Design Ride Sharing App like Uber, Lyft

To Come up with the Solution of their internal System around Aggregation of data at scale, with lots of internal constraints which i don't rememeber. 
Lots of focus around technology choices. 
This is not a Design round, its more about experiences 



1. Similar to flash sale but related to concert booking. They test your overall design skills like whether you can talk about trade offs, knowledge, bottlenecks and scale. Start small, tell the interviewer you would consider scale as you move along.
2. Also they do ask a lot about metrics and telemetry all along. Be good with that. Brownie points here as well.


This question was not a traditional System Design round as what their HR told me. Was more of a Data Engineering design where you had a supermarket system to track SKUs of items in stock across 100s of stores.
There are two streams:
* stock, items coming in at ~1M QPS(item_id, number_of_items either +/- indicating buy/sell, quantity_before, timestamp) from stores.
* camera, contains images of items on shelfs taken everytime there are changes on the store ~100K QPS.
You had a database with metadata about the items and stores. The question is how to generate some signals that are given from these data which are features fed to a downstream ML model. You were given some signals that had to be calculated using states of items and joined with the metadata. Some were tenporal in nature. The catch was you didnt have to calculate features for each item event. Just store them in Redis/DB and then build the inputs on eahc camera event.
Focus was on:
* scale
* data quality
* handling deduplication
Question completely threw me off, seemed too domain specific and I had to spend a lot of time understanding how the items and camera signals are coming. Didn't have time to solve data quality and thought process was not coherent.


A hotel booking/search system similar to booking with an emphasis on proximity-based search.
Alex XU chapter



Round 4 - Culture Fit

Typical questions around technical leadership, handling conflicts, pushing back on design decisions, etc.


1. Longest round in all.
2. Ask about mentorship, mistakes, experiment, trade off, conflict, etc, etc. Overall: Agoda recruiters are pretty good with detailed feedback and how to prepare for interviews each and every round I mean. They provide docs and links to study for.


* All possible behavioral questions can be asked and was asked, how do you handle team, pressure situation, time constraints, relocation etc.


HR - prelim any Discussion after Tech interviews

Visa Buffer and notice period alignment
Can my wife work on Dependent visa
Progressive Tax
Schooling/childcare
Some estimate on the costing of apartment and where do people usually stay working in Agoda
30 days accommodation [hotel or a condo?] [fooding consideration]
Travelling reimbursement timeline 
Joining Bonus payout
Annual Bonus payout and what is the general tend to this
Joining date [Can join in start of Dec]
Family Health insurance amount


Expections
"Thanks. Based on my experience and the relocation, I'd ideally be targeting the upper end of the Staff range. I was thinking around 240–250K. Is there flexibility?"

If you ask me today:
"At what Agoda base salary would you personally feel confident telling me that the Bangkok move makes financial sense?"
My answer is:
250K THB/month
And importantly, I wouldn't require 250K to accept.
I'd use this decision rule:
250K → move confidently.
240–245K → very strong case to move.
225K → don't reject; negotiate and then decide based on family insurance + tax treatment + actual Bangkok budget.
And if Agoda gives you 250K + 19% bonus + family insurance + relocation, I would consider that a very good Staff Engineer relocation package for your specific situation, even without a competing offer.




HR - negotiations


240K THB/month base
600K+ THB joining bonus
19–20% bonus
Comprehensive family health insurance


I would negotiate toward:
* Base: 250K THB/month
* Joining bonus: 500 THB
* Bonus: 20%
* Comprehensive family health insurance
* Full relocation for all three of you


One additional thought
If they are firm on 225K, I would be more willing to negotiate the joining bonus than push aggressively on base. A larger one-time payment (for example 750K–1M THB) can largely offset your relocation and setup costs in the first year, while keeping you aligned with their salary bands. That approach often has a better chance of succeeding than asking for a much higher base.
