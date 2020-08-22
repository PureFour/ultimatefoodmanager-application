package com.raddyr.products.data.db.dao

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.raddyr.products.data.db.ProductDatabase
import com.raddyr.products.data.db.entity.ProductEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class ProductDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: ProductDatabase

    private lateinit var dao: ProductDao

    private lateinit var productEntity: ProductEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.productDao()
        setProductEntity()
    }

    private fun setProductEntity() {
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
                isSynchronized = false,
                toBeDeleted = false,
                isShared = true
            )
    }

    @Test
    fun insertProduct() {
        dao.insert(productEntity).blockingGet()
        val products = dao.getAll().blockingGet()
        assertThat(products).contains(productEntity)
    }

    @Test
    fun insertProductsAndGetAll() {
        dao.insert(listOf(productEntity, productEntity)).blockingGet()
        val products = dao.getAll().blockingGet()
        assertThat(products).isEqualTo(listOf(productEntity))
    }
    @Test
    fun getByBarcode() {
        dao.insert(productEntity).blockingGet()
        val product = dao.getByBarcode("12345678").blockingGet()
        assertThat(product).isEqualTo(productEntity)
    }

    @Test
    fun deleteAll() {
        dao.insert(listOf(productEntity, productEntity)).blockingGet()
        dao.deleteAll().blockingGet()
        val products = dao.getAll().blockingGet()
        assertThat(products).isEmpty()
    }

    @Test
    fun getById() {
        dao.insert(productEntity).blockingGet()
        val products = dao.getById("ff59d842-affd-11eb-8529-0242ac130003").blockingGet()
        assertThat(products).isEqualTo(productEntity)
    }

    @Test
    fun getNotSync() {
        dao.insert(productEntity).blockingGet()
        val products = dao.getNotSync().blockingGet()
        assertThat(products).isEqualTo(listOf(productEntity))
    }

    @After
    fun teardown() {
        database.close()
    }
}