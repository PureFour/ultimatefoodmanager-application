package com.raddyr.products.data.model

import com.google.common.truth.Truth.assertThat
import com.raddyr.products.data.db.entity.ProductEntity
import org.junit.Before
import org.junit.Test
import java.util.*

class ProductMapperTest {


    lateinit var productEntity: ProductEntity
    lateinit var productFromService: Product

    lateinit var productCard: ProductCard
    lateinit var productCardInProduct: Product

    @Before
    fun setData() {
        productEntity =
            ProductEntity(
                "ff59d842-affd-11eb-8529-0242ac130003",
                "12345678",
                "Przykładowy produkt",
                "Przykładowa firma",
                "www.przykladowezdjecie.pl",
                "SNACKS",
                123.12f,
                "PLN",
                "KG",
                123.12f,
                10f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                "2020-10-10",
                "2020-12-12",
                isSynchronized = true,
                toBeDeleted = false,
                isShared = true
            )
        productFromService =
            Product(
                UUID.fromString("ff59d842-affd-11eb-8529-0242ac130003"),
                10f,
                ProductMetadata(
                    isSynchronized = true,
                    toBeDeleted = false,
                    expiryDate = "2020-12-12",
                    createdDate = "2020-10-10",
                    shared = true
                ),
                ProductCard(
                    "12345678",
                    "Przykładowy produkt",
                    "Przykładowa firma",
                    "www.przykladowezdjecie.pl",
                    Category.SNACKS,
                    Price(123.12f, "PLN"),
                    MeasurementUnit.KG,
                    123.12f,
                    Nutriments(
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                    )
                )
            )

        productCard = ProductCard(
            "12345678",
            "Przykładowy produkt",
            "Przykładowa firma",
            "www.przykladowezdjecie.pl",
            Category.SNACKS,
            Price(123.12f, "PLN"),
            MeasurementUnit.KG,
            123.12f,
            Nutriments(
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
            )
        )
        productCardInProduct = Product(
            null,
            123.12f,
            null,
            ProductCard(
                "12345678",
                "Przykładowy produkt",
                "Przykładowa firma",
                "www.przykladowezdjecie.pl",
                Category.SNACKS,
                Price(123.12f, "PLN"),
                MeasurementUnit.KG,
                123.12f,
                Nutriments(
                    0f,
                    0f,
                    0f,
                    0f,
                    0f,
                    0f,
                    0f,
                    0f,
                    0f,
                )
            )
        )
    }

    @Test
    fun `map product entity to product from service`() {
        assertThat(ProductMapper.getProduct(productEntity)).isEqualTo(productFromService)
    }

    @Test
    fun `map product from service to product entity`() {
        assertThat(ProductMapper.getProductEntity(productFromService)).isEqualTo(productEntity)
    }

    @Test
    fun `map productCard to product`() {
        assertThat(ProductMapper.mapProductCardToProduct(productCard)).isEqualTo(productCardInProduct)
    }

    @Test
    fun `map product entity to productCard`() {
        assertThat(ProductMapper.getProductCard(productEntity)).isEqualTo(productCard)
    }
}