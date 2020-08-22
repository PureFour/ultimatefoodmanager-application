package com.raddyr.products.di

import androidx.lifecycle.ViewModel
import com.raddyr.core.di.viewModel.ViewModelKey
import com.raddyr.products.ui.add.AddProductViewModel
import com.raddyr.products.ui.details.ProductDetailsViewModel
import com.raddyr.products.ui.edit.EditProductViewModel
import com.raddyr.products.ui.list.ProductListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddProductViewModel::class)
    abstract fun addProductViewModel(viewModel: AddProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    abstract fun productDetailsViewModel(viewModel: ProductDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    abstract fun productListViewModel(viewModel: ProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProductViewModel::class)
    abstract fun editProductViewModel(viewModel: EditProductViewModel): ViewModel
}