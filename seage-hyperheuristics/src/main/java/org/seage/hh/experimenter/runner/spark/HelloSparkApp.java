package org.seage.hh.experimenter.runner.spark;

// import java.util.Arrays;
// import java.util.List;

// import org.apache.spark.SparkConf;
// import org.apache.spark.api.java.JavaRDD;
// import org.apache.spark.api.java.JavaSparkContext;

// /**
//  * Hello Spark App.
//  *
//  */
// public class HelloSparkApp {
//   /**
//    * The main fn.
//    */
//   public static void main(String[] args) {
//     System.out.println("Hello Spark!");

//     SparkConf conf = new SparkConf().setAppName("SEAGE test app");
//     conf.setMaster("local[2]");

//     try (JavaSparkContext sc = new JavaSparkContext(conf)) {

//       List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//       JavaRDD<Integer> distData = sc.parallelize(data);

//       distData.filter(i -> {
//         System.out.println(i);
//         return true;
//       }).count();
//       // ArrayList<Double> a = [{10.0}, 2.0];
//       List<Double> list = Arrays.asList(1.38, 2.56, 4.3);

//       List<Double> l = distData.flatMap(i -> list.iterator()).collect();
//       System.out.println(l);
//       List<Double> l2 = distData.map(i -> 3.2).collect();
//       l2.forEach(i -> System.out.println(i));        

//       Integer sum = distData.reduce((a, i) -> {
//         System.out.println(Thread.currentThread().getName() + " --- " + a + " - " + i);
//         return a + i;
//       });
//       System.out.println(sum);
//     }
//   }
// }
