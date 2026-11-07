package com.hann.shopu3.StationeryInventory.Presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.ProductRepository
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductDetailViewModel
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductFormViewModel

class ProductViewModelFactory(
    private val productRepository: ProductRepository,
    private val productId: Int? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProductListViewModel::class.java) ->
            ProductListViewModel(productRepository) as T
        modelClass.isAssignableFrom(ProductDetailViewModel::class.java) ->
            ProductDetailViewModel(productRepository, requireNotNull(productId)) as T
        modelClass.isAssignableFrom(ProductFormViewModel::class.java) ->
            ProductFormViewModel(productRepository, productId) as T
        else -> throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}