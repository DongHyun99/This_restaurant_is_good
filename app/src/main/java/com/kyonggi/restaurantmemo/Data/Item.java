package com.kyonggi.restaurantmemo.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "item")
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "grade")
    private float grade;//평점

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private Long price;

    @ColumnInfo(name = "imgPath")
    private String imgPath;

    @ColumnInfo(name = "description")
    private String description;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public float getGrade(){
        return grade;
    }
    public String getName(){
        return name;
    }
    public String getImgPath(){
        return imgPath;
    }
    public String getDescription(){
        return description;
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
    public void setDescription(String data){
        description= data;
    }


}
