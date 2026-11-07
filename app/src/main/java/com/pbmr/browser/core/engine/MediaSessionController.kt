package com.pbmr.browser.core.engine

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.MediaSession

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
        session.mediaSessionDelegate = object : MediaSession.Delegate {
            override fun onActivated(session: GeckoSession, mediaSession: MediaSession) {
                isPlayingMedia = true
            }

            override fun onDeactivated(session: GeckoSession, mediaSession: MediaSession) {
                isPlayingMedia = false
            }

            override fun onPlay(session: GeckoSession, mediaSession: MediaSession) {
                isPlayingMedia = true
            }

            override fun onPause(session: GeckoSession, mediaSession: MediaSession) {
                isPlayingMedia = false
            }

            override fun onStop(session: GeckoSession, mediaSession: MediaSession) {
                isPlayingMedia = false
            }

            override fun onFullscreen(
                session: GeckoSession,
                mediaSession: MediaSession,
                fullscreen: Boolean,
                meta: MediaSession.ElementMetadata?
            ) {
                isVideoFullscreen = fullscreen
            }
        }
    }
}
