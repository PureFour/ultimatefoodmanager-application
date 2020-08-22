package com.raddyr.products.data.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.raddyr.products.R
import kotlinx.android.parcel.Parcelize
import java.util.*
import javax.annotation.Nullable

@Parcelize
data class ProductCard(
    var barcode: String? = null,
    var name: String? = null,
    var brand: String? = null,
    var photoUrl: String? = null,
    var category: Category? = null,
    var price: Price? = null,
    var measurementUnit: MeasurementUnit? = null,
    var totalQuantity: Float? = null,
    var nutriments: Nutriments? = null
) : Parcelable, ProductData {
    fun setBarcode(barcode: String?) = apply { this@ProductCard.barcode = barcode }
    fun setName(name: String?) = apply { this@ProductCard.name = name }
    fun setBrand(brand: String?) = apply { this@ProductCard.brand = brand }
    fun setPhotoUrl(photoUrl: String?) = apply { this@ProductCard.photoUrl = photoUrl }
    fun setCategory(category: Category?) = apply { this@ProductCard.category = category }
    fun setPrice(price: Price?) = apply { this@ProductCard.price = price }
    fun setMeasurementUnit(measurementUnit: MeasurementUnit?) =
        apply { this@ProductCard.measurementUnit = measurementUnit }

    fun setTotalQuantity(totalQuantity: Float?) =
        apply { this@ProductCard.totalQuantity = totalQuantity }

    fun setNutriments(nutriments: Nutriments?) = apply { this@ProductCard.nutriments = nutriments }
}

@Parcelize
data class Product(
    @Nullable var uuid: UUID? = null,
    var quantity: Float? = null,
    var metadata: ProductMetadata? = null,
    var productCard: ProductCard? = null
) : Parcelable, ProductData {
    fun setUuid(uuid: UUID?) = apply { this@Product.uuid = uuid }
    fun setQuantity(quantity: Float?) = apply { this@Product.quantity = quantity }
    fun setProductCard(productCard: ProductCard?) = apply { this@Product.productCard = productCard }
    fun setMetadata(metadata: ProductMetadata?) = apply { this@Product.metadata = metadata }
}

@Parcelize
data class ProductMetadata(
    @SerializedName("synchronized")
    var isSynchronized: Boolean? = null,
    var toBeDeleted: Boolean? = null,
    var expiryDate: String? = null,
    var createdDate: String? = null,
    var shared: Boolean? = null
) : Parcelable {
    fun setExpiryDate(expiryDate: String?) = apply { this@ProductMetadata.expiryDate = expiryDate }
    fun setToBeDeleted(toBeDeleted: Boolean?) =
        apply { this@ProductMetadata.toBeDeleted = toBeDeleted }

    fun setSynchronized(isSynchronized: Boolean?) =
        apply { this@ProductMetadata.isSynchronized = isSynchronized }

    fun setCreatedDate(createdDate: String?) =
        apply { this@ProductMetadata.createdDate = createdDate }

    fun setShared(shared: Boolean?) = apply { this@ProductMetadata.shared = shared }
}

@Parcelize
data class Price(
    val value: Float?,
    val currency: String?
) : Parcelable

@Parcelize
data class Nutriments(
    var carbohydrates: Float? = 0f,
    var energy: Float? = 0f,
    var fat: Float? = 0f,
    var fiber: Float? = 0f,
    var insatiableFat: Float? = 0f,
    var salt: Float? = 0f,
    var saturatedFat: Float? = 0f,
    var sodium: Float? = 0f,
    var sugars: Float? = 0f
) : Parcelable {
    fun setCarbohydrates(carbohydrates: Float?) =
        apply { this@Nutriments.carbohydrates = carbohydrates }

    fun setEnergy(energy: Float?) = apply { this@Nutriments.energy = energy }
    fun setFat(fat: Float?) = apply { this@Nutriments.fat = fat }
    fun setFiber(fiber: Float?) = apply { this@Nutriments.fiber = fiber }
    fun setInsatiableFat(insatiableFat: Float?) =
        apply { this@Nutriments.insatiableFat = insatiableFat }

    fun setSalt(salt: Float?) = apply { this@Nutriments.salt = salt }
    fun setSaturatedFat(saturatedFat: Float?) =
        apply { this@Nutriments.saturatedFat = saturatedFat }

    fun setSodium(sodium: Float?) = apply { this@Nutriments.sodium = sodium }
    fun setSugars(sugars: Float?) = apply { this@Nutriments.sugars = sugars }
}

enum class MeasurementUnit(val unit: Int) : Enums {
    KG(R.string.kg), G(R.string.g), ML(R.string.ml), L(R.string.l), NOT_FOUND(R.string.not_found);

    override fun getKey() = unit

    companion object {
        fun getDefaultCategoriesList() = listOf(
            KG, G, ML, L
        )
    }
}

enum class Category(@StringRes val category: Int) : Enums {
    NOT_FOUND(R.string.not_found),
    PLANT_BASED_FOODS(R.string.plant_based_foods),
    DAIRIES(R.string.dairies),
    GROCERIES(R.string.groceries),
    BEVERAGES(R.string.beverages),
    SNACKS(R.string.snacks);

    override fun getKey() = category

    companion object {
        fun getDefaultCategoriesList() =
            listOf(GROCERIES, PLANT_BASED_FOODS, DAIRIES, BEVERAGES, SNACKS)
    }
}

enum class Currency(@StringRes val currency: Int) : Enums {
    PLN(R.string.pln),
    EUR(R.string.eur),
    DOL(R.string.dol);

    override fun getKey() = currency

    companion object {
        fun getDefaultCurrenciesList() =
            listOf(
                PLN, EUR, DOL
            )
    }
}

interface Enums {
    fun getKey(): Int
}
