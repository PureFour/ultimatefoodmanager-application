package com.raddyr.products.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raddyr.products.data.model.ProductData

@Entity(tableName = "product_table")
data class ProductEntity (
    @PrimaryKey(autoGenerate = false) var uuid: String,
    @ColumnInfo(name = "barcode") var barcode: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "brand") var brand: String? = null,
    @ColumnInfo(name = "photo_url") var photoUrl: String? = null,
    @ColumnInfo(name = "category") var category: String? = null,
    @ColumnInfo(name = "price_value") var priceValue: Float? = null,
    @ColumnInfo(name = "price_currency") var priceCurrency: String? = null,
    @ColumnInfo(name = "measurement_unit") var measurementUnit: String? = null,
    @ColumnInfo(name = "total_quantity") var totalQuantity: Float? = null,
    @ColumnInfo(name = "quantity") var quantity: Float? = null,
    @ColumnInfo(name = "carbohydrates") var carbohydrates: Float? = null,
    @ColumnInfo(name = "energy") var energy: Float? = null,
    @ColumnInfo(name = "fat") var fat: Float? = null,
    @ColumnInfo(name = "fiber") var fiber: Float? = null,
    @ColumnInfo(name = "insatiable_fat") var insatiableFat: Float? = null,
    @ColumnInfo(name = "salt") var salt: Float? = null,
    @ColumnInfo(name = "saturated_fat") var saturatedFat: Float? = null,
    @ColumnInfo(name = "sodium") var sodium: Float? = null,
    @ColumnInfo(name = "sugars") var sugars: Float? = null,
    @ColumnInfo(name = "created_date") var createdDate: String? = null,
    @ColumnInfo(name = "expiry_date") var expiryDate: String? = null,
    @ColumnInfo(name = "isSynchronized") var isSynchronized: Boolean? = null,
    @ColumnInfo(name = "to_be_deleted") var toBeDeleted: Boolean? = null,
    @ColumnInfo(name = "isShared") var isShared: Boolean? = null
): ProductData {

    fun setUuid(uuid: String?) = apply { this@ProductEntity.uuid = uuid!! }
    fun setBarcode(barcode: String?) = apply { this@ProductEntity.barcode = barcode }
    fun setName(name: String?) = apply { this@ProductEntity.name = name }
    fun setBrand(brand: String?) = apply { this@ProductEntity.brand = brand }
    fun setPhotoUrl(photoUrl: String?) = apply { this@ProductEntity.photoUrl = photoUrl }
    fun setCategory(category: String?) = apply { this@ProductEntity.category = category }
    fun setPriceValue(priceValue: Float?) = apply { this@ProductEntity.priceValue = priceValue }
    fun setPriceCurrency(priceCurrency: String?) = apply { this@ProductEntity.priceCurrency = priceCurrency }
    fun setMeasurementUnit(measurementUnit: String?) = apply { this@ProductEntity.measurementUnit = measurementUnit }
    fun setTotalQuantity(totalQuantity: Float?) = apply { this@ProductEntity.totalQuantity = totalQuantity }
    fun setQuantity(quantity: Float?) = apply { this@ProductEntity.quantity = quantity }
    fun setCarbohydrates(carbohydrates: Float?) = apply { this@ProductEntity.carbohydrates = carbohydrates }
    fun setEnergy(energy: Float?) = apply { this@ProductEntity.energy = energy }
    fun setFat(fat: Float?) = apply { this@ProductEntity.fat = fat }
    fun setFiber(fiber: Float?) = apply { this@ProductEntity.fiber = fiber }
    fun setInsatiableFat(insatiableFat: Float?) = apply { this@ProductEntity.insatiableFat = insatiableFat }
    fun setSalt(salt: Float?) = apply { this@ProductEntity.salt = salt }
    fun setSaturatedFat(saturatedFat: Float?) = apply { this@ProductEntity.saturatedFat = saturatedFat }
    fun setSodium(sodium: Float?) = apply { this@ProductEntity.sodium = sodium }
    fun setSugars(sugars: Float?) = apply { this@ProductEntity.sugars = sugars }
    fun setExpiryDate(expiryDate: String?) = apply { this@ProductEntity.expiryDate = expiryDate }
    fun setToBeDeleted(toBeDeleted: Boolean?) = apply { this@ProductEntity.toBeDeleted = toBeDeleted }
    fun setSynchronized(isSynchronized: Boolean?) = apply { this@ProductEntity.isSynchronized = isSynchronized }
    fun setCreatedDate(createdDate: String?) = apply { this@ProductEntity.createdDate = createdDate }
    fun setShared(isShared: Boolean?) = apply { this@ProductEntity.isShared = isShared }
}