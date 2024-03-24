# Exercices MapReduce

Ce projet contient deux exercices d'application de MapReduce développés avec Apache Hadoop.

## Structure du Projet
<img width="449" alt="14" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/fce6358e-78f5-4cb5-b7bd-c3385024eb8c">
   


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

### Job2

1. Code source du Driver
```java
    Configuration conf = new Configuration();
        conf.set("year", args[2]);
        Job job = Job.getInstance(conf, "prix_total_Ventes_Ville_par_année");

        job.setJarByClass(Driver.class);
        job.setMapperClass(JobMapper.class);
        job.setReducerClass(JobReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(TextInputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
```
   
3. Code source du JobMapper
   ```java
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
<img width="289" alt="11" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/7efe863d-44b1-4dcc-a1e9-e1a95f9adae7">




6. Execution
   ```bash
    hadoop jar TP2-1.0-SNAPSHOT.jar org.example.exercice1.job2.Driver /ventes.txt /output2 2021
    ```
  <img width="666" alt="12" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/ebe0fa37-802b-44ee-9674-1b05ae969942">


7. Résultats
   ```bash
   hdfs dfs -cat /output2/part-r-00000
   ```
<img width="290" alt="13" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/2cd034c3-37e4-473b-97d6-68742ad9387a">


## Exercice 2

1. Code source du Driver
```java
   public class Driver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "journaux_Web");

        job.setJarByClass(Driver.class);
        job.setMapperClass(JobMapper.class);
        job.setReducerClass(JobReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(TextInputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
```
   
3. Code source du JobMapper
   ```java
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

4. Code source du JobReducer
   ```java
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

5. Créer un fichier avec un contenu :

   ```bash
   hdfs dfs -touchz /web.txt
   hdfs dfs -appendToFile - web.txt
   ```
<img width="453" alt="15" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/1ea545f7-64c7-4793-a6b9-be8832a39bd0">




6. Execution
   ```bash
    hadoop jar TP2-1.0-SNAPSHOT.jar org.example.exercice1.job1.Driver /web.txt /resultats
    ```
  <img width="669" alt="16" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/ec3a7cb1-9667-4edd-b4e1-c17852a51138">


7. Résultats
   ```bash
   hdfs dfs -cat /resultats/part-r-00000
   ```
<img width="354" alt="17" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/0b41f63b-9c3e-4455-b9b0-3aebd27018c1">
