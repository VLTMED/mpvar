package app.marlboroadvance.mpvex.ui.player.controls.components.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.marlboroadvance.mpvex.repository.wyzie.WyzieEpisode
import app.marlboroadvance.mpvex.repository.wyzie.WyzieSeason
import app.marlboroadvance.mpvex.repository.wyzie.WyzieSubtitle
import app.marlboroadvance.mpvex.repository.wyzie.WyzieTmdbResult
import app.marlboroadvance.mpvex.repository.wyzie.WyzieTvShowDetails
import app.marlboroadvance.mpvex.ui.theme.spacing
import app.marlboroadvance.mpvex.utils.media.MediaInfoParser
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// ── Kept for call-site compatibility ─────────────────────────────────────────
sealed class OnlineSubtitleItem {
    data class OnlineTrack(val subtitle: WyzieSubtitle) : OnlineSubtitleItem()
    data class Header(val title: String) : OnlineSubtitleItem()
    object Divider : OnlineSubtitleItem()
}

// ── Dynamic accent helpers ────────────────────────────────────────────────────
private val MaterialTheme.subAccent
    @Composable get() = colorScheme.primary
private val MaterialTheme.subAccentDim
    @Composable get() = colorScheme.onPrimaryContainer
private val MaterialTheme.subAccentBg
    @Composable get() = colorScheme.primary.copy(alpha = 0.08f)
private val MaterialTheme.subAccentBorder
    @Composable get() = colorScheme.primary.copy(alpha = 0.38f)

// ── Main sheet ────────────────────────────────────────────────────────────────
@Composable
fun OnlineSubtitleSearchSheet(
    onDismissRequest: () -> Unit,
    onDownloadOnline: (WyzieSubtitle) -> Unit,
    isSearching: Boolean = false,
    isDownloading: Boolean = false,
    searchResults: ImmutableList<WyzieSubtitle> = emptyList<WyzieSubtitle>().toImmutableList(),
    isOnlineSectionExpanded: Boolean = true,
    onToggleOnlineSection: () -> Unit = {},
    modifier: Modifier = Modifier,
    mediaTitle: String = "",
    mediaSearchResults: ImmutableList<WyzieTmdbResult> = emptyList<WyzieTmdbResult>().toImmutableList(),
    isSearchingMedia: Boolean = false,
    onSearchMedia: (String) -> Unit = {},
    onSelectMedia: (WyzieTmdbResult) -> Unit = {},
    selectedTvShow: WyzieTvShowDetails? = null,
    isFetchingTvDetails: Boolean = false,
    selectedSeason: WyzieSeason? = null,
    onSelectSeason: (WyzieSeason) -> Unit = {},
    seasonEpisodes: ImmutableList<WyzieEpisode> = emptyList<WyzieEpisode>().toImmutableList(),
    isFetchingEpisodes: Boolean = false,
    selectedEpisode: WyzieEpisode? = null,
    onSelectEpisode: (WyzieEpisode) -> Unit = {},
    onClearMediaSelection: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val mediaInfo = remember(mediaTitle) { MediaInfoParser.parse(mediaTitle) }
    var searchQuery by remember { mutableStateOf(mediaInfo.title) }
    // Hold the selected TMDB result so we can display its poster/overview in TvPanel
    var selectedMedia by remember { mutableStateOf<WyzieTmdbResult?>(null) }

    // Auto-trigger once when title changes (NOT on every recomposition of mediaInfo)
    LaunchedEffect(mediaInfo.title) {
        if (mediaInfo.title.isNotBlank()) onSearchMedia(mediaInfo.title)
    }

    val items = remember(searchResults) {
        searchResults.map { OnlineSubtitleItem.OnlineTrack(it) as OnlineSubtitleItem }.toImmutableList()
    }

    val showSuggestions = mediaSearchResults.isNotEmpty()
    val showTvPanel     = selectedTvShow != null
    val showLoading     = isSearching || isFetchingTvDetails
    val showResults     = searchResults.isNotEmpty()

    // ── RTL guard ──────────────────────────────────────────────────────────
    // Force RTL for the entire sheet so every Row/Column aligns correctly
    // regardless of the parent's layout direction.  If the parent is already
    // RTL this is a no-op (same value override does NOT double-flip).
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        GenericTracksSheet(
            tracks = items,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            header = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                ) {

                    // ── Search bar ───────────────────────────────────────
                    SubSearchField(
                        query        = searchQuery,
                        onQueryChange = { v ->
                            searchQuery = v
                            if (v.length >= 2) onSearchMedia(v)
                            if (v.isEmpty()) {
                                selectedMedia = null
                                onClearMediaSelection()
                            }
                        },
                        onSearch = {
                            onSearchMedia(searchQuery)
                            keyboardController?.hide()
                        },
                        onClear = {
                            searchQuery = ""
                            selectedMedia = null
                            onClearMediaSelection()
                        },
                        isBusy   = isSearchingMedia || isSearching || isDownloading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(bottom = 6.dp)
                    )

                    // ── Autocomplete ─────────────────────────────────────
                    AnimatedVisibility(
                        visible = showSuggestions,
                        enter   = expandVertically() + fadeIn(),
                        exit    = shrinkVertically() + fadeOut()
                    ) {
                        SuggestionList(
                            results  = mediaSearchResults,
                            onSelect = { result ->
                                searchQuery = result.title
                                selectedMedia = result
                                onSelectMedia(result)
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .padding(bottom = 6.dp)
                        )
                    }

                    // ── TV panel ─────────────────────────────────────────
                    AnimatedVisibility(
                        visible = showTvPanel,
                        enter   = expandVertically() + fadeIn(),
                        exit    = shrinkVertically() + fadeOut()
                    ) {
                        if (selectedTvShow != null) {
                            TvPanel(
                                tvShow          = selectedTvShow,
                                posterUrl       = selectedMedia?.poster,
                                year            = selectedMedia?.releaseYear,
                                overview        = selectedMedia?.overview,
                                isLoadingDetail = isFetchingTvDetails,
                                selectedSeason  = selectedSeason,
                                onSelectSeason  = onSelectSeason,
                                isLoadingEp     = isFetchingEpisodes,
                                episodes        = seasonEpisodes,
                                selectedEpisode = selectedEpisode,
                                onSelectEpisode = onSelectEpisode,
                                onBack          = onClearMediaSelection,
                                modifier        = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                                    .padding(bottom = 6.dp)
                            )
                        }
                    }

                    // ── Progress ─────────────────────────────────────────
                    if (showLoading) {
                        LinearProgressIndicator(
                            modifier   = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .height(2.dp),
                            color      = MaterialTheme.subAccent,
                            trackColor = MaterialTheme.subAccent.copy(alpha = 0.12f)
                        )
                    }

                    // ── Results header ───────────────────────────────────
                    if (showResults) {
                        val ctx = when {
                            selectedTvShow != null && selectedSeason != null && selectedEpisode != null ->
                                "${selectedTvShow.name} \u00b7 S${selectedSeason.season_number} E${selectedEpisode.episode_number}"
                            else -> searchQuery
                        }
                        ResultsHeader(
                            count    = searchResults.size,
                            context  = ctx,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                }
            },
            track = { item ->
                if (item is OnlineSubtitleItem.OnlineTrack) {
                    SubCard(
                        subtitle   = item.subtitle,
                        onDownload = { onDownloadOnline(item.subtitle) },
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium, vertical = 3.dp)
                    )
                }
            }
        )
    }
}

// ── Search bar ────────────────────────────────────────────────────────────────
@Composable
private fun SubSearchField(
    query:         String,
    onQueryChange: (String) -> Unit,
    onSearch:      () -> Unit,
    onClear:       () -> Unit,
    isBusy:        Boolean,
    modifier:      Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    var focused by remember { mutableStateOf(false) }
    val border  = if (focused) MaterialTheme.subAccent else colors.outlineVariant.copy(alpha = 0.5f)

    // RTL layout: Row starts from the right, so placing Search first puts it
    // on the right (Start), TextField in the middle, and Clear last on the left (End).
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(0.5.dp, border, RoundedCornerShape(14.dp))
            .background(colors.surfaceContainerLow)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ① Search icon — first in code = rightmost (Start) in RTL
        Icon(
            Icons.Default.Search,
            contentDescription = "بحث",
            tint     = colors.onSurfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.size(18.dp)
        )

        // ② Text input — fills remaining space
        androidx.compose.foundation.text.BasicTextField(
            value          = query,
            onValueChange  = onQueryChange,
            modifier       = Modifier
                .weight(1f)
                .onFocusChanged { focused = it.isFocused },
            textStyle = remember(colors.onSurface) {
                TextStyle(
                    color     = colors.onSurface,
                    fontSize  = 14.sp,
                    textAlign = TextAlign.Right
                )
            },
            singleLine      = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            cursorBrush     = androidx.compose.ui.graphics.SolidColor(MaterialTheme.subAccent),
            decorationBox   = { inner ->
                Box(
                    modifier         = Modifier.fillMaxWidth(),
                    // Start = Right edge in RTL
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text      = "ابحث عن فيلم أو مسلسل...",
                            fontSize  = 14.sp,
                            color     = colors.onSurfaceVariant.copy(alpha = 0.4f),
                            textAlign = TextAlign.Right,
                            modifier  = Modifier.fillMaxWidth()
                        )
                    }
                    inner()
                }
            }
        )

        // ③ Trailing icon (clear / spinner) — last in code = leftmost (End) in RTL
        Box(Modifier.size(18.dp), contentAlignment = Alignment.Center) {
            when {
                isBusy -> CircularProgressIndicator(
                    modifier    = Modifier.size(16.dp),
                    strokeWidth = 1.8.dp,
                    color       = MaterialTheme.subAccent
                )
                query.isNotEmpty() -> Icon(
                    Icons.Default.Close,
                    contentDescription = "مسح",
                    tint     = colors.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onClear() }
                )
            }
        }
    }
}

// ── Autocomplete list ─────────────────────────────────────────────────────────
@Composable
private fun SuggestionList(
    results:  ImmutableList<WyzieTmdbResult>,
    onSelect: (WyzieTmdbResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier        = modifier,
        shape           = RoundedCornerShape(14.dp),
        color           = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation  = 2.dp,
        shadowElevation = 0.dp,
        border          = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column {
            results.forEachIndexed { i, r ->
                SuggestionRow(r) { onSelect(r) }
                if (i < results.lastIndex) HorizontalDivider(
                    modifier  = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.5.dp,
                    color     = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun SuggestionRow(result: WyzieTmdbResult, onClick: () -> Unit) {
    val isTV   = result.mediaType == "tv"
    val colors = MaterialTheme.colorScheme

    // RTL visual order: [chevron(End/Left)] [poster] [info(Start/Right)]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Chevron — points right (forward) for RTL interfaces
        Icon(
            Icons.Default.ChevronRight, null,
            tint     = colors.onSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.size(14.dp)
        )

        // Poster
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(colors.surfaceVariant)
                .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (result.poster != null) {
                AsyncImage(
                    model              = result.poster,
                    contentDescription = result.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Movie, null, tint = colors.outlineVariant, modifier = Modifier.size(14.dp))
            }
        }

        // Info — align to Start (which is the RIGHT side in RTL)
        Column(
            modifier            = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text       = result.title,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color      = colors.onSurface,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                textAlign  = TextAlign.Right
            )
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val (bg, fg) = if (isTV) Color(0x1A534AB7) to Color(0xFF534AB7)
                               else      Color(0x1A185FA5) to Color(0xFF185FA5)
                Text(
                    text       = if (isTV) "مسلسل" else "فيلم",
                    fontSize   = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = fg,
                    modifier   = Modifier
                        .clip(CircleShape)
                        .background(bg)
                        .border(0.5.dp, fg.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
                if (result.releaseYear != null) {
                    Box(Modifier.size(3.dp).clip(CircleShape).background(colors.outlineVariant))
                    Text(
                        text  = result.releaseYear,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            if (!result.overview.isNullOrBlank()) {
                Text(
                    text       = result.overview,
                    style      = MaterialTheme.typography.bodySmall,
                    color      = colors.onSurfaceVariant.copy(alpha = 0.55f),
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    textAlign  = TextAlign.Right
                )
            }
        }
    }
}

// ── TV Show panel ─────────────────────────────────────────────────────────────
@Composable
private fun TvPanel(
    tvShow:          WyzieTvShowDetails,
    posterUrl:       String?,
    year:            String?,
    overview:        String?,
    isLoadingDetail: Boolean,
    selectedSeason:  WyzieSeason?,
    onSelectSeason:  (WyzieSeason) -> Unit,
    isLoadingEp:     Boolean,
    episodes:        ImmutableList<WyzieEpisode>,
    selectedEpisode: WyzieEpisode?,
    onSelectEpisode: (WyzieEpisode) -> Unit,
    onBack:          () -> Unit,
    modifier:        Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(14.dp),
        color    = colors.surfaceContainerLow,
        border   = androidx.compose.foundation.BorderStroke(0.5.dp, colors.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column {
            // Title row with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    text       = tvShow.name,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color      = colors.onSurface,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Right
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.surfaceContainerHigh)
                        .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, "رجوع", tint = colors.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
            }

            // ── Poster + meta row ──────────────────────────────────────────
            if (!isLoadingDetail) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment     = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Poster
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 72.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.surfaceVariant)
                            .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (posterUrl != null) {
                            AsyncImage(
                                model              = posterUrl,
                                contentDescription = tvShow.name,
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.Movie, null, tint = colors.outlineVariant, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Meta text (name / year / overview)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text       = tvShow.name,
                            style      = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color      = colors.onSurface,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis,
                            textAlign  = TextAlign.Right
                        )
                        if (year != null) {
                            Text(
                                text       = year,
                                style      = MaterialTheme.typography.labelSmall,
                                color      = colors.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        if (!overview.isNullOrBlank()) {
                            Text(
                                text       = overview,
                                style      = MaterialTheme.typography.bodySmall,
                                color      = colors.onSurfaceVariant.copy(alpha = 0.7f),
                                maxLines   = 2,
                                overflow   = TextOverflow.Ellipsis,
                                lineHeight = 16.sp,
                                textAlign  = TextAlign.Right
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.35f), thickness = 0.5.dp)

            if (isLoadingDetail) {
                LinearProgressIndicator(
                    modifier   = Modifier.fillMaxWidth().height(2.dp),
                    color      = MaterialTheme.subAccent,
                    trackColor = MaterialTheme.subAccent.copy(alpha = 0.1f)
                )
            } else {
                // Controls row: hint | episode pill | season pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val hintText = when {
                        selectedSeason == null  -> "اختر الموسم أولاً"
                        selectedEpisode == null -> "اختر الحلقة الآن"
                        else -> "S${selectedSeason.season_number} \u00b7 Ep ${selectedEpisode.episode_number}"
                    }
                    Text(
                        text       = hintText,
                        style      = MaterialTheme.typography.labelSmall,
                        color      = if (selectedEpisode != null) MaterialTheme.subAccentDim
                                     else colors.onSurfaceVariant.copy(alpha = 0.5f),
                        fontWeight = if (selectedEpisode != null) FontWeight.Medium else FontWeight.Normal,
                        modifier   = Modifier.weight(1f),
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )

                    // Episode pill
                    PillDropdown(
                        mainLabel = selectedEpisode?.let { "Ep ${it.episode_number}" } ?: "الحلقة",
                        subLabel  = selectedEpisode?.let { "الحلقة ${it.episode_number}" } ?: "اختر",
                        enabled   = selectedSeason != null && !isLoadingEp,
                        loading   = isLoadingEp,
                        filled    = selectedEpisode != null
                    ) { dismiss ->
                        episodes.forEachIndexed { i, ep ->
                            DropdownMenuItem(
                                text = {
                                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                                        Text("Ep ${ep.episode_number}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        ep.name?.let {
                                            Text(
                                                text     = it,
                                                style    = MaterialTheme.typography.bodySmall,
                                                color    = colors.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.basicMarquee()
                                            )
                                        }
                                    }
                                },
                                onClick = { onSelectEpisode(ep); dismiss() },
                                leadingIcon = if (ep == selectedEpisode) ({ Icon(Icons.Default.Check, null, tint = MaterialTheme.subAccent, modifier = Modifier.size(14.dp)) }) else null,
                                modifier = Modifier.background(if (ep == selectedEpisode) MaterialTheme.subAccentBg else Color.Transparent)
                            )
                            if (i < episodes.lastIndex) HorizontalDivider(Modifier.padding(horizontal = 10.dp), thickness = 0.5.dp, color = colors.outlineVariant.copy(alpha = 0.3f))
                        }
                    }

                    // Season pill
                    PillDropdown(
                        mainLabel = selectedSeason?.let { "S${it.season_number}" } ?: "الموسم",
                        subLabel  = selectedSeason?.let { "الموسم ${it.season_number}" } ?: "اختر",
                        enabled   = true,
                        loading   = false,
                        filled    = selectedSeason != null
                    ) { dismiss ->
                        tvShow.seasons.forEachIndexed { i, s ->
                            DropdownMenuItem(
                                text = {
                                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                                        Text("S${s.season_number}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text("الموسم ${s.season_number}", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                                    }
                                },
                                onClick = { onSelectSeason(s); dismiss() },
                                leadingIcon = if (s == selectedSeason) ({ Icon(Icons.Default.Check, null, tint = MaterialTheme.subAccent, modifier = Modifier.size(14.dp)) }) else null,
                                modifier = Modifier.background(if (s == selectedSeason) MaterialTheme.subAccentBg else Color.Transparent)
                            )
                            if (i < tvShow.seasons.lastIndex) HorizontalDivider(Modifier.padding(horizontal = 10.dp), thickness = 0.5.dp, color = colors.outlineVariant.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
    }
}

// ── Pill dropdown ─────────────────────────────────────────────────────────────
@Composable
private fun PillDropdown(
    mainLabel:   String,
    subLabel:    String,
    enabled:     Boolean,
    loading:     Boolean,
    filled:      Boolean,
    menuContent: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowDeg by animateFloatAsState(if (expanded) 180f else 0f, label = "arr")
    val colors   = MaterialTheme.colorScheme

    val borderC = if (filled || expanded) MaterialTheme.subAccentBorder else colors.outlineVariant.copy(alpha = 0.5f)
    val bgC     = if (filled || expanded) MaterialTheme.subAccentBg     else colors.surfaceContainerHigh

    Box {
        Row(
            modifier = Modifier
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(0.5.dp, if (enabled) borderC else colors.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                .background(if (enabled) bgC else colors.surfaceContainerLow)
                .then(if (enabled) Modifier.clickable { expanded = true } else Modifier)
                .padding(horizontal = 10.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown, null,
                tint     = if (filled || expanded) MaterialTheme.subAccent else colors.onSurfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.size(13.dp).rotate(arrowDeg)
            )
            if (loading) CircularProgressIndicator(modifier = Modifier.size(11.dp), strokeWidth = 1.4.dp, color = MaterialTheme.subAccent)
            // Start = Right edge in RTL
            Column(horizontalAlignment = Alignment.Start) {
                Text(mainLabel, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (enabled) colors.onSurface else colors.onSurface.copy(0.35f), lineHeight = 14.sp)
                Text(subLabel,  fontSize = 9.sp,  color = if (enabled) colors.onSurfaceVariant.copy(0.55f) else colors.onSurface.copy(0.2f), lineHeight = 11.sp)
            }
        }
        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
            modifier         = Modifier.heightIn(max = 260.dp),
            shape            = RoundedCornerShape(12.dp),
            containerColor   = colors.surfaceContainerHigh
        ) {
            menuContent { expanded = false }
        }
    }
}

// ── Results header ────────────────────────────────────────────────────────────
@Composable
private fun ResultsHeader(count: Int, context: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier              = modifier,
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // "نتائج الترجمة" placed first → sits on the right (Start) in RTL
        Text(
            text       = "نتائج الترجمة",
            style      = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color      = colors.onSurfaceVariant.copy(alpha = 0.65f)
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text  = "$count",
                style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceContainerHigh)
                    .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.5f), CircleShape)
                    .padding(horizontal = 9.dp, vertical = 2.dp)
            )
            if (context.isNotBlank()) Text(
                text     = context,
                style    = MaterialTheme.typography.labelSmall,
                color    = colors.onSurfaceVariant.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Subtitle card ─────────────────────────────────────────────────────────────
@Composable
fun SubCard(subtitle: WyzieSubtitle, onDownload: () -> Unit, modifier: Modifier = Modifier) {
    val colors  = MaterialTheme.colorScheme
    val isMatch = subtitle.matchedRelease != null || subtitle.matchedFilter != null
    val dlCount = subtitle.downloadCount ?: 0

    Surface(
        modifier = modifier.clickable { onDownload() },
        shape    = RoundedCornerShape(12.dp),
        color    = if (isMatch) MaterialTheme.subAccentBg else Color.Transparent,
        border   = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            if (isMatch) MaterialTheme.subAccentBorder else colors.outlineVariant.copy(alpha = 0.45f)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp)) {

            // Top row: download | name | flag
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                        .background(colors.surfaceContainerHigh)
                        .clickable { onDownload() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Download, "تحميل", tint = colors.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
                Text(
                    text       = subtitle.displayName,
                    style      = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color      = colors.onSurface,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Right
                )
                FlagBox(subtitle.flagUrl, subtitle.language ?: "??")
            }

            Spacer(Modifier.height(8.dp))

            // Pill row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (dlCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(Icons.Default.Download, null, tint = colors.onSurfaceVariant.copy(alpha = 0.4f), modifier = Modifier.size(10.dp))
                        Text(fmtCount(dlCount), style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant.copy(alpha = 0.45f))
                    }
                }
                Spacer(Modifier.weight(1f))
                subtitle.source?.let { SPill(it, PStyle.Source) }
                if (subtitle.isHearingImpaired) SPill("وصفية", PStyle.Hi)
                if (isMatch) SPill("متوافق", PStyle.Match, check = true)
                subtitle.format?.let { SPill(it.uppercase(), PStyle.Format) }
            }
        }
    }
}

// ── Flag ──────────────────────────────────────────────────────────────────────
@Composable
private fun FlagBox(flagUrl: String?, code: String) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.size(width = 30.dp, height = 21.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(colors.surfaceVariant)
            .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.45f), RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (!flagUrl.isNullOrBlank()) {
            AsyncImage(flagUrl, code, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            Text(code.take(2).uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = colors.onSurfaceVariant)
        }
    }
}

// ── Pill ──────────────────────────────────────────────────────────────────────
private enum class PStyle { Format, Match, Hi, Source }

@Composable
private fun SPill(text: String, style: PStyle, check: Boolean = false) {
    val colors           = MaterialTheme.colorScheme
    val (bg, fg, border) = when (style) {
        PStyle.Format -> Triple(Color(0x1A185FA5), Color(0xFF185FA5), Color(0x33185FA5))
        PStyle.Match  -> Triple(MaterialTheme.subAccentBg, MaterialTheme.subAccentDim, MaterialTheme.subAccent.copy(alpha = 0.25f))
        PStyle.Hi     -> Triple(Color(0x17854F0B), Color(0xFF854F0B), Color(0x33854F0B))
        PStyle.Source -> Triple(colors.surfaceContainerHigh, colors.onSurfaceVariant.copy(alpha = 0.55f), colors.outlineVariant.copy(alpha = 0.45f))
    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .border(0.5.dp, border, CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (check) Icon(Icons.Default.Check, null, tint = fg, modifier = Modifier.size(9.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = fg)
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
private fun fmtCount(n: Int): String = when {
    n >= 1_000_000 -> "${n / 1_000_000}.${(n % 1_000_000) / 100_000}M"
    n >= 1_000     -> "${n / 1_000}.${(n % 1_000) / 100}k"
    else           -> "$n"
}

// ── Legacy aliases ────────────────────────────────────────────────────────────
@Composable
fun WyzieSubtitleRow(subtitle: WyzieSubtitle, onDownload: () -> Unit, modifier: Modifier = Modifier) =
    SubCard(subtitle, onDownload, modifier)

@Composable
fun TmdbResultRow(result: WyzieTmdbResult, onClick: () -> Unit, modifier: Modifier = Modifier) =
    SuggestionRow(result, onClick)

@Composable
fun SeriesDetailsSection(
    tvShow: WyzieTvShowDetails,
    isFetchingSeasons: Boolean,
    selectedSeason: WyzieSeason?,
    onSelectSeason: (WyzieSeason) -> Unit,
    isFetchingEpisodes: Boolean,
    episodes: ImmutableList<WyzieEpisode>,
    selectedEpisode: WyzieEpisode?,
    onSelectEpisode: (WyzieEpisode) -> Unit,
    onClose: () -> Unit
) = TvPanel(
    tvShow          = tvShow,
    posterUrl       = null,
    year            = null,
    overview        = null,
    isLoadingDetail = isFetchingSeasons,
    selectedSeason  = selectedSeason,
    onSelectSeason  = onSelectSeason,
    isLoadingEp     = isFetchingEpisodes,
    episodes        = episodes,
    selectedEpisode = selectedEpisode,
    onSelectEpisode = onSelectEpisode,
    onBack          = onClose
)
