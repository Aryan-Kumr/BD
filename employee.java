package employee;
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
   	 conf.setOutputValueClass(DoubleWritable.class);
   	 FileInputFormat.addInputPath(conf,new Path(args[0]));
   	 FileOutputFormat.setOutputPath(conf,new Path(args[1]));
   	 JobClient.runJob(conf);
    }
}

// package employee;
// import java.io.*;
// import java.util.*;
// import org.apache.hadoop.mapred.*;
// import org.apache.hadoop.io.*;


class mapper extends MapReduceBase implements Mapper<LongWritable , Text , Text , DoubleWritable> 
{
   public void map(LongWritable key, Text value, OutputCollector<Text,DoubleWritable> output ,Reporter r) throws IOException
    {
   	 String[] line = value.toString().split("\\t");
           	// String[] fields = line.trim().split("\\s+"); Split by one or more spaces or tabs
        	if (line.length < 9) { // Ensure there are enough fields
            	return; 
        	}
        	String gender = line[3]; // Assuming gender is in the 4th column (index 3)
        	Double salary = Double.parseDouble(line[8]); // Assuming salary is in the 9th column (index 8)
        	output.collect(new Text(gender), new DoubleWritable(salary));

    }
}

// package employee;
// import java.io.*;
// import java.util.*;
// import org.apache.hadoop.mapred.*;
// import org.apache.hadoop.io.*;

class reducer extends MapReduceBase implements Reducer<Text,DoubleWritable,Text,DoubleWritable> 
{
public void reduce(Text key,Iterator<DoubleWritable> value , OutputCollector<Text,DoubleWritable> output ,Reporter r) throws IOException
{
   	int count = 0;
        	double sum = 0.0;
        	while (value.hasNext()) {
            	sum += value.next().get();
            	count++;
        	}
        	output.collect(new Text(key + " Average"), new DoubleWritable(sum / count));
        	output.collect(new Text(key + " Count"), new DoubleWritable(count));
    	}
	}

