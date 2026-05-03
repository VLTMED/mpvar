package app.marlboroadvance.mpvar.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.marlboroadvance.mpvar.database.converters.NetworkProtocolConverter
import app.marlboroadvance.mpvar.database.dao.NetworkConnectionDao
import app.marlboroadvance.mpvar.database.dao.PlaybackStateDao
import app.marlboroadvance.mpvar.database.dao.PlaylistDao
import app.marlboroadvance.mpvar.database.dao.RecentlyPlayedDao
import app.marlboroadvance.mpvar.database.dao.VideoMetadataDao
import app.marlboroadvance.mpvar.database.entities.PlaybackStateEntity
import app.marlboroadvance.mpvar.database.entities.PlaylistEntity
import app.marlboroadvance.mpvar.database.entities.PlaylistItemEntity
import app.marlboroadvance.mpvar.database.entities.RecentlyPlayedEntity
import app.marlboroadvance.mpvar.database.entities.VideoMetadataEntity
import app.marlboroadvance.mpvar.domain.network.NetworkConnection

@Database(
  entities = [
    PlaybackStateEntity::class,
    RecentlyPlayedEntity::class,
    VideoMetadataEntity::class,
    NetworkConnection::class,
    PlaylistEntity::class,
    PlaylistItemEntity::class,
  ],
  version = 8,
  exportSchema = true,
)
@TypeConverters(NetworkProtocolConverter::class)
abstract class MpvArDatabase : RoomDatabase() {
  abstract fun videoDataDao(): PlaybackStateDao

  abstract fun recentlyPlayedDao(): RecentlyPlayedDao

  abstract fun videoMetadataDao(): VideoMetadataDao

  abstract fun networkConnectionDao(): NetworkConnectionDao

  abstract fun playlistDao(): PlaylistDao
}
