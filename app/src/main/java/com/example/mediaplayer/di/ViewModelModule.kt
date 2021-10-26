package com.example.mediaplayer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mediaplayer.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindPlayerFragmentViewModel(homeViewModel: MainViewModel) : ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory : AppViewModelFactory) : ViewModelProvider.Factory


}