# CHAPTER 9: DESIGN A WEB CRAWLER 

## Step 1 - Understand the problem and establish design scope

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

## Step 2 - Propose high-level design and get buy-in

<img width="1432" height="1067" alt="image" src="https://github.com/user-attachments/assets/3dc2e7de-6535-4beb-b379-e2a5f41d9a87" />

Steps
```
Step 1: Add seed URLs to the URL Frontier
Step 2: HTML Downloader fetches a list of URLs from URL Frontier.
Step 3: HTML Downloader gets IP addresses of URLs from DNS resolver and starts
downloading.
Step 4: Content Parser parses HTML pages and checks if pages are malformed.
Step 5: After content is parsed and validated, it is passed to the “Content Seen?” component.
Step 6: “Content Seen” component checks if a HTML page is already in the storage.
• If it is in the storage, this means the same content in a different URL has already been
processed. In this case, the HTML page is discarded.
• If it is not in the storage, the system has not processed the same content before. The
content is passed to Link Extractor.
Step 7: Link extractor extracts links from HTML pages.
Step 8: Extracted links are passed to the URL filter.
Step 9: After links are filtered, they are passed to the “URL Seen?” component.
Step 10: “URL Seen” component checks if a URL is already in the storage, if yes, it is
processed before, and nothing needs to be done.
Step 11: If a URL has not been processed before, it is added to the URL Frontier.

```

## Step 3 - Design deep dive


<img width="1069" height="1982" alt="image" src="https://github.com/user-attachments/assets/d518dc3e-e0df-47f9-9f49-714d2ed974c7" />


• Queue router: It ensures that each queue (b1, b2, … bn) only contains URLs from the
same host.
• Mapping table: It maps each host to a queue.
<img width="1178" height="561" alt="image" src="https://github.com/user-attachments/assets/c4c87305-3acb-4df7-93f3-071e3baddfe0" />

• FIFO queues b1, b2 to bn: Each queue contains URLs from the same host.
• Queue selector: Each worker thread is mapped to a FIFO queue, and it only downloads
URLs from that queue. The queue selection logic is done by the Queue selector.
• Worker thread 1 to N. A worker thread downloads web pages

• Prioritizer: It takes URLs as input and computes the priorities.
• Queue f1 to fn: Each queue has an assigned priority. Queues with high priority are
selected with higher probability.
• Queue selector: Randomly choose a queue with a bias towards queues with higher
priority.

Freshness
Few strategies to optimize freshness are listed as follows:
• Recrawl based on web pages’ update history.
• Prioritize URLs and recrawl important pages first and more frequently.


Storage for URL Frontier
```
We adopted a hybrid approach. 
The majority of URLs are stored on disk, so the storage space
is not a problem. To reduce the cost of reading from the disk and writing to the disk, we
maintain buffers in memory for enqueue/dequeue operations. Data in the buffer is
periodically written to the disk.
```

Robots.txt
```
Robots.txt, called Robots Exclusion Protocol, is a standard used by websites to communicate
with crawlers. It specifies what pages crawlers are allowed to download. Before attempting to
crawl a web site, a crawler should check its corresponding robots.txt first and follow its rules.
To avoid repeat downloads of robots.txt file, we cache the results of the file. The file is
downloaded and saved to cache periodically. Here is a piece of robots.txt file taken from
https://www.amazon.com/robots.txt. Some of the directories like creatorhub are disallowed
for Google bot.
User-agent: Googlebot
Disallow: /creatorhub/*
Disallow: /rss/people/*/reviews
Disallow: /gp/pdp/rss/*/reviews
Disallow: /gp/cdp/member-reviews/
Disallow: /gp/aw/cr/
Besides robots.txt, performance
```

Performance optimization

1. Distributed crawl
2. Cache DNS Resolver - DNS cache keeps the domain
name to IP address mapping and is updated periodically by cron jobs
3. Locality - geographical distribution
4. Short timeout

<img width="1098" height="917" alt="image" src="https://github.com/user-attachments/assets/cd8fed24-e5a7-43b8-a6bc-29faf5e480e4" />

Robustness
```
Besides performance optimization, robustness is also an important consideration. We present
a few approaches to improve the system robustness:
• Consistent hashing: This helps to distribute loads among downloaders. A new
downloader server can be added or removed using consistent hashing. Refer to Chapter 5:
Design consistent hashing for more details.
• Save crawl states and data: To guard against failures, crawl states and data are written to
a storage system. A disrupted crawl can be restarted easily by loading saved states and
data.
• Exception handling: Errors are inevitable and common in a large-scale system. The
crawler must handle exceptions gracefully without crashing the system.
• Data validation: This is an important measure to prevent system errors.
```

Extensibility
PNG Downloader module is plugged-in to download PNG files.
Web Monitor module is added to monitor the web and prevent copyright and trademark
infringements.

<img width="1429" height="973" alt="image" src="https://github.com/user-attachments/assets/9409fee3-297e-4e3d-a04d-f2821dd85351" />


Detect and avoid problematic content

1. Redundant content:  Hashes or checksums help to detect duplication [11].
2. Spider traps : idiaity them and try to avoid them
3. Data noise: excluded if possible.

## Step 4 - Wrap up
```
characteristics of a good crawler: scalability, politeness, extensibility, and robustness. 

points:
• Server-side rendering: Numerous websites use scripts like JavaScript, AJAX, etc to
generate links on the fly. If we download and parse web pages directly, we will not be able
to retrieve dynamically generated links. To solve this problem, we perform server-side
rendering (also called dynamic rendering) first before parsing a page [12].
• Filter out unwanted pages: With finite storage capacity and crawl resources, an anti-spam
component is beneficial in filtering out low quality and spam pages [13] [14].
• Database replication and sharding: Techniques like replication and sharding are used to
improve the data layer availability, scalability, and reliability.
• Horizontal scaling: For large scale crawl, hundreds or even thousands of servers are
needed to perform download tasks. The key is to keep servers stateless.
• Availability, consistency, and reliability: These concepts are at the core of any large
system’s success. We discussed these concepts in detail in Chapter 1. Refresh your
memory on these topics.
• Analytics: Collecting and analyzing data are important parts of any system because data
is key ingredient for fine-tuning.
```
