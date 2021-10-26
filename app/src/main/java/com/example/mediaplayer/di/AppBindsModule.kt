package com.example.mediaplayer.di

import com.example.mediaplayer.json.DefaultTrackCatalog
import com.example.mediaplayer.json.TrackCatalog
import com.example.mediaplayer.res.AppResources
import com.example.mediaplayer.res.AppResourcesImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppBindsModule {

    @Binds
    fun bindAppResources(appResources: AppResourcesImpl): AppResources

    @Binds
    @Singleton
    fun bindTrackCatalog(trackCatalog: DefaultTrackCatalog): TrackCatalog
}