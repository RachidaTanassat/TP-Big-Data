package org.example.exercice2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class JobReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        int totalRequests=0;
        Iterator<IntWritable> it = values.iterator();
        while (it.hasNext()){
            totalRequests+=it.next().get();
        }
        context.write(key, new IntWritable(totalRequests));

    }
}


