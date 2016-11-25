/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringweka;

import java.io.File;
import java.util.Scanner;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Gerry
 */
public class Clustering {
    
    private static Instances instances;
    private Clusterer clustererModel;
    private static int clusterer;
    
    public Clustering(){
        instances = null;
        this.clustererModel = null;
        clusterer = 0;
    }
    
    public void setClusterer(int i){
        clusterer = i;
    }
    
    private Instances loadData(String path) throws Exception {
        if (path.endsWith(".arff")) {
            DataSource source = new DataSource("dataset/"+path);
            instances = source.getDataSet();
        } else if (path.endsWith(".csv")) {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("dataset/"+path));
            
            //if there is no header row in csv file
            String[] options = new String[1];
            options[0] = "-H";
            loader.setOptions(options);
            
            instances = loader.getDataSet();
            
            //save ARFF
            String arffFileName = "dataset/"+path.substring(0, path.indexOf('.')).concat(".arff");
            File file = new File(arffFileName);
            if (!file.exists()) {
                ArffSaver saver = new ArffSaver();
                saver.setInstances(instances);
                saver.setFile(file);
                saver.writeBatch();
            }
            
            DataSource source = new DataSource(arffFileName);
            instances = source.getDataSet();
        }
        return instances;
    }
    
    public void buildClusterer(int clusterer, Instances data) throws Exception{
        if (clusterer == 1){
            clustererModel = new MyAgnes(2, "singlelinkage");
        } else if (clusterer == 2){
            clustererModel = new MyAgnes(2, "completelinkage");
        }
        clustererModel.buildClusterer(data);
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        int clustererUse;
        String printCluster;
        Clustering clustering = new Clustering();
        Scanner scan = new Scanner(System.in);
        Clusterer model = null;
       
        MyAgnes myAgnes = new MyAgnes(2, "singlelinkage");

        myAgnes.buildClusterer(clustering.loadData("criminal.csv"));
        printCluster = myAgnes.toString();
    }
    
}
