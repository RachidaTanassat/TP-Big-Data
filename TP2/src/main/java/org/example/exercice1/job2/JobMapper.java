package org.example.exercice1.job2;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class JobMapper extends Mapper<LongWritable, Text,Text,DoubleWritable> {
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {

        String[] tokens = value.toString().split(" ");
        if (tokens.length == 4) {
            String date = tokens[0];
            String city = tokens[1];
            double price = Double.parseDouble(tokens[3]);

            String year = context.getConfiguration().get("year");

            if (year.equals(date.split("/")[2])) {
                context.write(new Text(city), new DoubleWritable(price));
            }
        }
    }
}