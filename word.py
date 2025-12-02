from pyspark import SparkContext


sc = SparkContext("local", "Word Character Count")

paragraph = """
This is the first line.
And this is the second line.
Here comes the third line.
"""

rdd = sc.parallelize(paragraph.split("\n"))

num_lines = rdd.count()
print(f"Number of lines: {num_lines}")


word_lengths = rdd.flatMap(lambda line: line.split()) \
                  .map(lambda word: (word, len(word)))


word_lengths_result = word_lengths.collect()
for word, length in word_lengths_result:
    print(f"Word: {word}, Length: {length}")


sc.stop()
