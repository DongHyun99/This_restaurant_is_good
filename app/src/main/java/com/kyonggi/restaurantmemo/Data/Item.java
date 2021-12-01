package com.kyonggi.restaurantmemo.Data;

public class Item {
    private float grade;//평점
    private String name;
    private String imgPath;
    private String descripthion;

    public float getGrade(){
        return grade;
    }
    public String getName(){
        return name;
    }
    public String getImgPath(){
        return imgPath;
    }
    public String getDescripthion(){
        return descripthion;
    }
    public void setGrade(float data){
        grade = data;
    }
    public void setName(String data){
        name = data;
    }
    public void setImgPath(String data){
        imgPath = data;
    }
    public void setDescripthion(String data){
        descripthion= data;
    }


}
