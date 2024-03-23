# CountWord

Ce projet contient une application MapReduce simple développée avec Apache Hadoop. L'objectif de cette application est de compter le nombre d'occurrences de chaque mot dans un fichier texte.

## Structure du Projet
- **WordCountDriver:** Classe principale pour lancer le job MapReduce.
- **WordCountMapper:** Classe pour le mapper.
- **WordCountReducer:** Classe pour le reducer.

1. Code source du WordCountDriver
```java
    Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJobName("TP word count");
        job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
```
   
3. Code source du WordCountMapper
   ```java
   public class WordCountMapper extends Mapper<LongWritable, Text , Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text , Text, IntWritable>.Context context) throws IOException, InterruptedException {
        String []mots = value.toString().split(" ");
        for (String m:mots){
            context.write(new Text(m), new IntWritable(1));
        }
    }
   }


4. Code source du WordCountReducer
   ```java
   public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
             int somme=0;
        Iterator<IntWritable> it = values.iterator();
        while (it.hasNext()){
            somme+=it.next().get();
        }
        context.write(key, new IntWritable(somme));
    }
   }

5. Créer un fichier avec un contenu :

   ```bash
   hdfs dfs -touchz /file.txt
   hdfs dfs -appendToFile - file.txt
   ```


6. Quelques modification dans le fichier /opt/hadoop/etc/hadoop/mapred-site.xml
    ```bash
   cat > mapred-site.xml <<EOF
    <configuration>
    <property>
        <name>mapreduce.reduce.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
    </property>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.map.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
    </property>
   </configuration>
   EOF 


8. Execution
   ```bash
   hadoop jar tp_mapReduce-1.0-SNAPSHOT.jar /file.txt /output
   ```
   <img width="668" alt="5" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/6aa2365a-677f-4fec-90e0-9391cb57e18d">
9. Résultats
   ```bash
   hdfs dfs -cat /output/part-r-00000
   ```
   <img width="290" alt="6" src="https://github.com/RachidaTanassat/TP-Big-Data/assets/85264433/f195b776-41f8-418a-90a8-f22204c8a3a4">

   
