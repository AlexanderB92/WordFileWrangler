# Word Counter

An assignment to create a CLI with some file reading, word counting features

# Setup and Prerequisites

** Maven 3.8.1
** Java 8+

# To build and run tests:
mvn clean install

# To compile standalone JAR:
mvn clean compile assembly:single


* To Generate More Lorem Ipsum
java -jar .\WordFileWrangler-1.0-SNAPSHOT-jar-with-dependencies.jar -generate <filecount> <wordcount> <filename>

* To count and print occurrences (with exclusions)
java -jar .\WordFileWrangler-1.0-SNAPSHOT-jar-with-dependencies.jar -countoccurences <absolute path to dir>

* To print and save exclusions
java -jar .\WordFileWrangler-1.0-SNAPSHOT-jar-with-dependencies.jar -countexcluded <absolute path to dir>

* To create dictionary
java -jar .\WordFileWrangler-1.0-SNAPSHOT-jar-with-dependencies.jar -createdict <absolute path to dir>

* Example of dir path
"C:\Users\Alex\Desktop\WordFileWrangler\static_data\"

# Notes:
Uses hashmaps for counting structure for constant time for updates. Uses hashset for string collections with (1) constant time (for example in exclusions)
Performance is determined mainly by use of the way of reading the file (bufferedreader) and the parsing to get words from lines (long or short)
Spent som time thinking about the file to read. Whether it had linebreaks or not - affects buffersize
Spent some time thinking about sanitizing the file, instead of adding parsing to each buffered line (removing commas etc)
Important assumptions about the format of the files to be read. If it has returns (line breaks) buffered reader is used.
Otherwise, Scanner for reading word by word, but Java may be slow compared to something like C for this.

Possible regex matching depending on sanitizing of files (tokenization etc):
* String[] words = s.split("\\W+") (Alphanumeric repeat until symbol)
* String[] words = s.split("\\s+") (one or more whitespace)
* String[] words = s.split(" ") (one whitespace)

Possible to make more object oriented - for example creating a class for each type of business rule
Assuming that counting the number of excluded words should only yield total excluded count, and not for each (as read on assignment)
Method: createDictionaryLike should perhaps not be in counts
Perhaps counters should each have their own class, if complex enough - or perhaps a controller-like class to separate commands, and the business rules. Model could also contain a 'Task' class, or similar,
Constants like static filenames could be fields.
Can createFileDictionaryLike get faster? (Currently O(n) with optimized hashmap check) - Total is O(n) * 2 - this might be avoidable with a pre ordered list
Errorhandling with empty files and in general
Create a time field in Counter Result, and always pass back performance to the user (time spent)
Tests overwrite results, but should have a cleanup

# Known Bugs
* Ordered approach loses last element
* Only tested on Windows