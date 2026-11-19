CHAPTER 9: DESIGN A WEB CRAWLER

Step 1 - Understand the problem and establish design scope

Sample Questions -:
```
Candidate: What is the main purpose of the crawler? Is it used for search engine indexing,
data mining, or something else?
Interviewer: Search engine indexing.
Candidate: How many web pages does the web crawler collect per month?
Interviewer: 1 billion pages.
Candidate: What content types are included? HTML only or other content types such as
PDFs and images as well?
Interviewer: HTML only.
Candidate: Shall we consider newly added or edited web pages?
Interviewer: Yes, we should consider the newly added or edited web pages.
Candidate: Do we need to store HTML pages crawled from the web?
Interviewer: Yes, up to 5 years
Candidate: How do we handle web pages with duplicate content?
Interviewer: Pages with duplicate content should be ignored.
Above are some of the sample questions that you can ask your interviewer.
```

NFR
```
• Scalability: The web is very large. There are billions of web pages out there. Web
crawling should be extremely efficient using parallelization.
• Robustness: The web is full of traps. Bad HTML, unresponsive servers, crashes,
malicious links, etc. are all common. The crawler must handle all those edge cases.
• Politeness: The crawler should not make too many requests to a website within a short
time interval.
• Extensibility: The system is flexible so that minimal changes are needed to support new
content types. For example, if we want to crawl image files in the future, we should not
need to redesign the entire system.
```

Back of the envelope estimation
```
The following estimations are based on many assumptions, and it is important to
communicate with the interviewer to be on the same page.
• Assume 1 billion web pages are downloaded every month.
• QPS: 1,000,000,000 / 30 days / 24 hours / 3600 seconds = ~400 pages per second.
• Peak QPS = 2 * QPS = 800
• Assume the average web page size is 500k.
• 1-billion-page x 500k = 500 TB storage per month. If you are unclear about digital
storage units, go through “Power of 2” section in Chapter 2 again.
• Assuming data are stored for five years, 500 TB * 12 months * 5 years = 30 PB. A 30 PB
storage is needed to store five-year content.
```

Step 2 - Propose high-level design and get buy-in








