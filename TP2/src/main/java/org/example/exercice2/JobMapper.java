package org.example.exercice2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class JobMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

        String[] tokens = value.toString().split(" ");
        if (tokens.length >= 8) {
            String ip = tokens[0];
            String response = tokens[8];
            if(response.equals("200")){
                context.write(new Text(ip), new IntWritable(1));
            }

        }
    }
}