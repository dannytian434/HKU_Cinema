package com.hku.cs_cinema;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModel {

    String Name;
    String Category;
    String Duration;
//    String feature;


    public DataModel(String Name, String Category, String Duration) {
        this.Name=Name;
        this.Category=Category;
        this.Duration=Duration;
//        this.feature=feature;

    }


    public String getName() {
        return Name;
    }


    public String getType() {
        return Category;
    }


    public String getVersion_number() {
        return Duration;
    }


//    public String getFeature() {
//        return feature;
//    }

}
