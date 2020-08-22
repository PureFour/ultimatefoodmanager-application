package com.raddyr.products.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raddyr.products.data.db.entity.ProductEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: List<ProductEntity>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun delete(product:ProductEntity): Single<Unit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductEntity): Single<Long>

    @Query("SELECT * FROM product_table")
    fun getAll(): Single<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE barcode =:barcode")
    fun getByBarcode(barcode: String): Single<ProductEntity>

    @Query("DELETE FROM product_table")
    fun deleteAll(): Single<Int>

    @Query("DELETE FROM product_table WHERE uuid = :id")
    fun deleteById(id: String): Single<Unit>

    @Query("SELECT * FROM product_table WHERE uuid = :id")
    fun getById(id: String): Single<ProductEntity>

    @Query("SELECT * FROM product_table WHERE isSynchronized = 0")
    fun getNotSync(): Single<List<ProductEntity>>
}
