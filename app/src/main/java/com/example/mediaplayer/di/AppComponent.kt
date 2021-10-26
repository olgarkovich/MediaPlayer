
package com.example.mediaplayer.di

import android.app.Application
import com.example.mediaplayer.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, AppBindsModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}