# Exercices MapReduce

Ce projet contient deux exercices d'application de MapReduce développés avec Apache Hadoop.

## Structure du Projet
   


## Exercice 1 
### Job1

1. Code source du Driver
```java
    public class Driver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {


        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJobName("TP Vente");
        job.setJarByClass(Driver.class);
        job.setMapperClass(JobMapper.class);
        job.setReducerClass(JobReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(TextInputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);



    }
}
```
   
3. Code source du JobMapper
   ```java
   public class JobMapper extends Mapper<LongWritable, Text,Text,DoubleWritable> {
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(" ");
        if (tokens.length == 4) {
            String city = tokens[1];
            double price = Double.parseDouble(tokens[3]);
            context.write(new Text(city), new DoubleWritable(price));
        }
    }
   }


4. Code source du JobReducer
   ```java
   public class JobReducer extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        double totalSales = 0.0;
        for (DoubleWritable value : values) {
            totalSales += value.get();
        }
        context.write(key, new DoubleWritable(totalSales));
    }
   }

5. Créer un fichier avec un contenu :

   ```bash
   hdfs dfs -touchz /ventes.txt
   hdfs dfs -appendToFile - ventes.txt
   ```
<img width="324" alt="7" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/e96eaf2e-b70e-4081-a43e-1d17a4d6ae64">



6. Execution
   ```bash
    hadoop jar TP2-1.0-SNAPSHOT.jar org.example.exercice1.job1.Driver /ventes.txt /output1
    ```
   <img width="665" alt="8" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/c39b77ff-34aa-413a-910b-99ad27aa4609">

7. Résultats
   ```bash
   hdfs dfs -cat /output1/part-r-00000
   ```
<img width="294" alt="10" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/00dd9050-6b38-4fd7-b156-1507d7446870">

   
