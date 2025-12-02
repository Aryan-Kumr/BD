// driver
package wordcount;
import java.io.*;
import java.util.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.Path;
public class driver
{
    public static void main(String args[]) throws IOException
    {
   	 JobConf conf=new JobConf(driver.class);
   	 conf.setMapperClass(mapper.class);
   	 conf.setReducerClass(reducer.class);
   	 conf.setOutputKeyClass(Text.class);
   	 conf.setOutputValueClass(IntWritable.class);
   	 FileInputFormat.addInputPath(conf, new Path(args[0]));
   	 FileOutputFormat.setOutputPath(conf,new Path(args[1]));
   	 JobClient.runJob(conf);
    }
}

// mapper
// package wordcount;
// import java.io.*;
// import java.util.*;
// import org.apache.hadoop.mapred.*;
// import org.apache.hadoop.io.*;

public class mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
            throws IOException {
        
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
        
        while (tokenizer.hasMoreTokens()) {
            word.set(tokenizer.nextToken());
            output.collect(word, one);
        }
    }
}

// reducer
// package wordcount;
// import java.io.*;
// import java.util.*;
// import org.apache.hadoop.mapred.*;
// import org.apache.hadoop.io.*;

public class reducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output,
            Reporter reporter) throws IOException {
        
        int sum = 0;
        
        // Sum up the counts for each word
        while (values.hasNext()) {
            sum += values.next().get();
        }
        
        // Emit the word with the total count
        output.collect(key, new IntWritable(sum));
    }
}