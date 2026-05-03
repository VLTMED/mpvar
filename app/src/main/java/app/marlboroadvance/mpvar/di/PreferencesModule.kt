package app.marlboroadvance.mpvar.di

import app.marlboroadvance.mpvar.database.MpvArDatabase
import app.marlboroadvance.mpvar.preferences.AdvancedPreferences
import app.marlboroadvance.mpvar.preferences.AppearancePreferences
import app.marlboroadvance.mpvar.preferences.AudioPreferences
import app.marlboroadvance.mpvar.preferences.BrowserPreferences
import app.marlboroadvance.mpvar.preferences.DecoderPreferences
import app.marlboroadvance.mpvar.preferences.FoldersPreferences
import app.marlboroadvance.mpvar.preferences.GesturePreferences
import app.marlboroadvance.mpvar.preferences.PlayerPreferences
import app.marlboroadvance.mpvar.preferences.SettingsManager
import app.marlboroadvance.mpvar.preferences.SubtitlesPreferences
import app.marlboroadvance.mpvar.preferences.preference.AndroidPreferenceStore
import app.marlboroadvance.mpvar.preferences.preference.PreferenceStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val PreferencesModule =
  module {
    single { AndroidPreferenceStore(androidContext()) }.bind(PreferenceStore::class)

    single { AppearancePreferences(get()) }
    singleOf(::PlayerPreferences)
    singleOf(::GesturePreferences)
    singleOf(::DecoderPreferences)
    singleOf(::SubtitlesPreferences)
    singleOf(::AudioPreferences)
    singleOf(::AdvancedPreferences)
    single { BrowserPreferences(get(), androidContext()) }
    singleOf(::FoldersPreferences)
    singleOf(::SettingsManager)
  }
