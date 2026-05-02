package app.marlboroadvance.mpvar.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import app.marlboroadvance.mpvar.R

/**
 * Thmanyah Sans — Arabic-first variable-weight family (5 weights)
 * للنصوص العادية، العناوين الصغيرة، والتسميات
 */
val ThmanyahSansFontFamily = FontFamily(
    Font(R.font.thmanyah_light,   FontWeight.Light),
    Font(R.font.thmanyah_regular, FontWeight.Normal),
    Font(R.font.thmanyah_medium,  FontWeight.Medium),
    Font(R.font.thmanyah_bold,    FontWeight.Bold),
    Font(R.font.thmanyah_black,   FontWeight.Black),
)

/**
 * Thmanyah Serif Display — Arabic display family (2 weights)
 * للعناوين الكبيرة والعناصر البارزة — يدعم الأحرف المرسلة
 */
val ThmanyahSerifDisplayFontFamily = FontFamily(
    Font(R.font.thmanyahserifdisplay_bold,  FontWeight.Bold),
    Font(R.font.thmanyahserifdisplay_black, FontWeight.Black),
)

// توافق مع الكود السابق
val ThmanyahFontFamily = ThmanyahSansFontFamily

/**
 * تفعيل الأحرف المرسلة (Stylistic Alternates) عبر خاصية OpenType "salt"
 * يُظهر الحروف المرسلة والكاشيدة المائلة في المواضع المناسبة داخل الكلمة
 * مطابق لتوصيات دليل ثمانية https://font.thmanyah.com
 */
private const val SALT = "salt 1"

/**
 * Typography شاملة بخطوط ثمانية — عربي أولاً
 * - Display / Headline كبير: Thmanyah Serif Display (أحرف مرسلة مفعّلة)
 * - Headline صغير / Title / Body / Label: Thmanyah Sans
 * - letterSpacing = 0.sp في جميع الأنماط (الكشيدة العربية لا تتوافق مع letterSpacing اللاتيني)
 * - lineHeight مُرحَّبة للنص العربي
 */
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = ThmanyahSerifDisplayFontFamily,
        fontWeight = FontWeight.Black,
        fontFeatureSettings = SALT,
        fontSize = 57.sp,
        lineHeight = 72.sp,
        letterSpacing = 0.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = ThmanyahSerifDisplayFontFamily,
        fontWeight = FontWeight.Black,
        fontFeatureSettings = SALT,
        fontSize = 45.sp,
        lineHeight = 58.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = ThmanyahSerifDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontFeatureSettings = SALT,
        fontSize = 36.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = ThmanyahSerifDisplayFontFamily,
        fontWeight = FontWeight.Black,
        fontFeatureSettings = SALT,
        fontSize = 32.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = ThmanyahSerifDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontFeatureSettings = SALT,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontFeatureSettings = SALT,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontFeatureSettings = SALT,
        fontSize = 22.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontFeatureSettings = SALT,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontFeatureSettings = SALT,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontFeatureSettings = SALT,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontFeatureSettings = SALT,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Light,
        fontFeatureSettings = SALT,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontFeatureSettings = SALT,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontFeatureSettings = SALT,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = ThmanyahSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontFeatureSettings = SALT,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)
