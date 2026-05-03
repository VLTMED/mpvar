package app.marlboroadvance.mpvar.preferences

import app.marlboroadvance.mpvar.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Segment
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PictureInPictureAlt
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.outlined.Flip
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Segment
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a customizable button in the player controls.
 * Now includes an icon for the preference UI.
 */
enum class PlayerButton(
  val icon: ImageVector,
) {
  BACK_ARROW(Icons.AutoMirrored.Outlined.ArrowBack),
  VIDEO_TITLE(Icons.Outlined.Title),
  BOOKMARKS_CHAPTERS(Icons.Outlined.Bookmarks),
  PLAYBACK_SPEED(Icons.Outlined.Speed),
  DECODER(Icons.Outlined.Memory),
  SCREEN_ROTATION(Icons.Outlined.ScreenRotation),
  FRAME_NAVIGATION(Icons.Outlined.Camera),
  VIDEO_ZOOM(Icons.Outlined.ZoomIn),
  PICTURE_IN_PICTURE(Icons.Outlined.PictureInPictureAlt),
  ASPECT_RATIO(Icons.Outlined.AspectRatio),
  LOCK_CONTROLS(Icons.Outlined.LockOpen),
  AUDIO_TRACK(Icons.Outlined.Audiotrack),
  SUBTITLES(Icons.Outlined.Subtitles),
  MORE_OPTIONS(Icons.Outlined.MoreVert),
  CURRENT_CHAPTER(Icons.Outlined.Bookmarks), // <-- CHANGED ICON
  REPEAT_MODE(Icons.Outlined.Repeat),
  SHUFFLE(Icons.Outlined.Shuffle),
  MIRROR(Icons.Outlined.Flip),
  VERTICAL_FLIP(Icons.Outlined.Flip),
  AB_LOOP(Icons.Outlined.Autorenew),
  CUSTOM_SKIP(Icons.Outlined.FastForward),
  BACKGROUND_PLAYBACK(Icons.Outlined.Headset),
  NONE(Icons.Outlined.Bookmarks),
}

/**
 * A list of all buttons that the user can choose from in the customization menu.
 * Excludes NONE (placeholder) and constant buttons (BACK_ARROW, VIDEO_TITLE).
 */
val allPlayerButtons =
  PlayerButton.values().filter {
    it != PlayerButton.NONE &&
      it != PlayerButton.BACK_ARROW &&
      it != PlayerButton.VIDEO_TITLE
  }

/**
 * Gets the human-readable label for a player button.
 * TODO: You must add these string resources to your `strings.xml` file.
 */
@Composable
fun getPlayerButtonLabel(button: PlayerButton): String =
  when (button) {
    PlayerButton.BACK_ARROW -> "سهم الرجوع"
    PlayerButton.VIDEO_TITLE -> "عنوان الفيديو"
    PlayerButton.BOOKMARKS_CHAPTERS -> "الفصول / الإشارات"
    PlayerButton.PLAYBACK_SPEED -> "سرعة التشغيل"
    PlayerButton.DECODER -> "وحدة فك الترميز"
    PlayerButton.SCREEN_ROTATION -> "تدوير الشاشة"
    PlayerButton.FRAME_NAVIGATION -> "التنقل بين الإطارات"
    PlayerButton.VIDEO_ZOOM -> "تكبير الفيديو"
    PlayerButton.PICTURE_IN_PICTURE -> "صورة داخل صورة"
    PlayerButton.ASPECT_RATIO -> "نسبة العرض للارتفاع"
    PlayerButton.LOCK_CONTROLS -> "قفل عناصر التحكم"
    PlayerButton.AUDIO_TRACK -> "المسار الصوتي"
    PlayerButton.SUBTITLES -> "الترجمة"
    PlayerButton.MORE_OPTIONS -> "المزيد من الخيارات"
    PlayerButton.CURRENT_CHAPTER -> "الفصل الحالي"
    PlayerButton.REPEAT_MODE -> "وضع التكرار"
    PlayerButton.SHUFFLE -> "تشغيل عشوائي"
    PlayerButton.MIRROR -> "قلب أفقي"
    PlayerButton.VERTICAL_FLIP -> "قلب عمودي"
    PlayerButton.AB_LOOP -> "تكرار A-B"
    PlayerButton.CUSTOM_SKIP -> "تخطي مخصص"
    PlayerButton.BACKGROUND_PLAYBACK -> "التشغيل في الخلفية"
    PlayerButton.NONE -> "بلا"
  }
