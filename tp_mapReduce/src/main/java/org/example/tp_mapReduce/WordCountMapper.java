package org.example.tp_mapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text , Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text , Text, IntWritable>.Context context) throws IOException, InterruptedException {
        String []mots = value.toString().split(" ");
        for (String m:mots){
            context.write(new Text(m), new IntWritable(1));
        }
    }
}
