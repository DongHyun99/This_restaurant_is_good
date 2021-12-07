package com.kyonggi.restaurantmemo.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kyonggi.restaurantmemo.Data.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Query("SELECT * FROM item")
    List<Item> getAll();

    // 별이 많은 순서대로 정렬
    @Query("SELECT * FROM item ORDER BY grade DESC")
    List<Item> getAllByGrade();

    // 맛집 이름으로 가져오기
    @Query("SELECT * FROM item WHERE name = :name")
    List<Item> getItemByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    // 전체 데이터삭제
    @Query("DELETE FROM item")
    void deleteAll();
}
