package app.marlboroadvance.mpvar.preferences

import app.marlboroadvance.mpvar.preferences.preference.PreferenceStore

/**
 * Preferences for folder management
 */
class FoldersPreferences(
  preferenceStore: PreferenceStore,
) {
  // Set of folder paths that should be hidden from the folder list
  val blacklistedFolders = preferenceStore.getStringSet("blacklisted_folders", emptySet())
}
