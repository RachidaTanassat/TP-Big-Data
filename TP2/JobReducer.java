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