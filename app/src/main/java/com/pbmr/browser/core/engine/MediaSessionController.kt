package com.pbmr.browser.core.engine

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import org.mozilla.geckoview.GeckoSession

/**
 * Manages background media playback and Android Picture-in-Picture (PiP) mode.
 */
object MediaSessionController {

    private var isPlayingMedia = false
    private var isVideoFullscreen = false

    fun notifyMediaPlaying(playing: Boolean, isVideo: Boolean = false) {
        isPlayingMedia = playing
        if (!playing) {
            isVideoFullscreen = false
        }
    }

    fun notifyFullscreenChanged(fullscreen: Boolean) {
        isVideoFullscreen = fullscreen
    }

    /**
     * Called on Android Activity onUserLeaveHint() to trigger Picture-in-Picture
     * if video playback is currently active.
     */
    fun handleUserLeaveHint(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isPlayingMedia || isVideoFullscreen) {
                val aspectRatio = Rational(16, 9)
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build()
                return try {
                    activity.enterPictureInPictureMode(params)
                } catch (e: Exception) {
                    false
                }
            }
        }
        return false
    }

    /**
     * Applies GeckoView media backgrounding properties so playback does not freeze on tab switch.
     */
    fun configureMediaSession(session: GeckoSession) {
        // In GeckoView, media session delegates can receive playing states
        session.mediaSessionDelegate = object : GeckoSession.MediaSession.Delegate {
            override fun onActivated(session: GeckoSession, mediaSession: GeckoSession.MediaSession) {
                isPlayingMedia = true
            }

            override fun onDeactivated(session: GeckoSession, mediaSession: GeckoSession.MediaSession) {
                isPlayingMedia = false
            }

            override fun onPlaybackStateChanged(
                session: GeckoSession,
                mediaSession: GeckoSession.MediaSession,
                state: Int
            ) {
                isPlayingMedia = (state == GeckoSession.MediaSession.PlaybackState.PLAYING)
            }
        }
    }
}
