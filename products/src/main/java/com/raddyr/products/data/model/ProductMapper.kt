package com.raddyr.products.data.model

import com.raddyr.products.data.db.entity.ProductEntity
import java.util.*

class ProductMapper {

    companion object {
        fun getProduct(productData: ProductData?): Product =
            if (productData is Product) productData else {
                with(productData as ProductEntity) {
                    Product().setUuid(UUID.fromString(uuid))
                        .setProductCard(
                            ProductCard().setBarcode(barcode)
                                .setName(name)
                                .setBrand(brand)
                                .setPhotoUrl(photoUrl)
                                .setCategory(category?.let { Category.valueOf(it) })
                                .setPrice(Price(priceValue, priceCurrency))
                                .setMeasurementUnit(measurementUnit?.let {
                                    MeasurementUnit.valueOf(
                                        it
                                    )
                                })
                                .setTotalQuantity(totalQuantity)
                                .setNutriments(
                                    Nutriments().setCarbohydrates(carbohydrates)
                                        .setEnergy(energy)
                                        .setFat(fat)
                                        .setFiber(fiber)
                                        .setInsatiableFat(insatiableFat)
                                        .setSalt(salt)
                                        .setSaturatedFat(saturatedFat)
                                        .setSodium(sodium)
                                        .setSugars(sugars)
                                )
                        )
                        .setQuantity(quantity)
                        .setMetadata(
                            ProductMetadata().setSynchronized(isSynchronized)
                                .setToBeDeleted(toBeDeleted)
                                .setExpiryDate(expiryDate)
                                .setCreatedDate(createdDate)
                                .setShared(isShared)
                        )
                }
            }

        fun getProductCard(productData: ProductData?): ProductCard =
            if (productData is ProductCard) productData else {
                with(productData as ProductEntity) {
                    ProductCard().setBarcode(barcode)
                        .setName(name)
                        .setBrand(brand)
                        .setPhotoUrl(photoUrl)
                        .setCategory(category?.let { Category.valueOf(it) })
                        .setPrice(Price(priceValue, priceCurrency))
                        .setMeasurementUnit(measurementUnit?.let {
                            MeasurementUnit.valueOf(
                                it
                            )
                        })
                        .setTotalQuantity(totalQuantity)
                        .setNutriments(
                            Nutriments().setCarbohydrates(carbohydrates)
                                .setEnergy(energy)
                                .setFat(fat)
                                .setFiber(fiber)
                                .setInsatiableFat(insatiableFat)
                                .setSalt(salt)
                                .setSaturatedFat(saturatedFat)
                                .setSodium(sodium)
                                .setSugars(sugars)
                        )
                }
            }

        fun mapProductCardToProduct(productCard: ProductCard) =
            Product().setProductCard(productCard).setQuantity(productCard.totalQuantity)

        fun getProductEntity(product: ProductData, isEditOffline: Boolean = false, toDeleted: Boolean = false): ProductEntity =
            with(product as Product) {
                ProductEntity(uuid.toString())
                    .setBarcode(productCard?.barcode)
                    .setName(productCard?.name)
                    .setBrand(productCard?.brand)
                    .setPhotoUrl(productCard?.photoUrl)
                    .setCategory(productCard?.category?.name)
                    .setPriceValue(productCard?.price?.value)
                    .setPriceCurrency(productCard?.price?.currency)
                    .setMeasurementUnit(productCard?.measurementUnit?.name)
                    .setTotalQuantity(productCard?.totalQuantity)
                    .setCarbohydrates(productCard?.nutriments?.carbohydrates)
                    .setEnergy(productCard?.nutriments?.energy)
                    .setFat(productCard?.nutriments?.fat)
                    .setFiber(productCard?.nutriments?.fiber)
                    .setInsatiableFat(productCard?.nutriments?.insatiableFat)
                    .setSalt(productCard?.nutriments?.salt)
                    .setSaturatedFat(productCard?.nutriments?.saturatedFat)
                    .setSodium(productCard?.nutriments?.sodium)
                    .setSugars(productCard?.nutriments?.sugars)
                    .setQuantity(quantity)
                    .setCreatedDate(metadata?.createdDate)
                    .setExpiryDate(metadata?.expiryDate)
                    .setSynchronized(if (isEditOffline) false else metadata?.isSynchronized)
                    .setToBeDeleted( if (toDeleted)true else metadata?.toBeDeleted)
                    .setShared(metadata?.shared)
            }
    }
}