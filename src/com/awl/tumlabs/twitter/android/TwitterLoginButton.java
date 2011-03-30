/*
 * Copyright 2011 Atos Wordline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.awl.tumlabs.twitter.android;

import com.awl.tumlabs.twitter.android.TwitterAuth.AuthListener;
import com.awl.tumlabs.twitter.android.TwitterAuth.DialogListener;
import com.twitter.android.R;

import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/***
 * Create a login button to sign in with Twitter
 * 
 * @author Xavier Balloy
 * 
 */
public class TwitterLoginButton extends ImageButton {

	private static final String TAG = TwitterLoginButton.class.getSimpleName();

	private TwitterAuth twitter;
	private Activity activity;
	private AuthListener listener;

	public TwitterLoginButton(Context context) {
		super(context);
	}

	public TwitterLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/***
	 * Initialize the button
	 * 
	 * @param activiy
	 *            The activity where the button is set
	 * @param twitter
	 *            The Twitter used to store the token and token secret
	 */
	public void init(final Activity activiy, final TwitterAuth twitter,
			final AuthListener listener) {
		this.twitter = twitter;
		this.activity = activiy;
		this.listener = listener;

		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);

		TwitterSessionStore.restore(twitter, getContext());

		setImageResource(twitter.isSessionValid() ? R.drawable.twitter_logout
				: R.drawable.twitter_login);
		drawableStateChanged();

		setOnClickListener(new ButtonOnClickListener());
	}

	private final class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (twitter.isSessionValid()) {
				Log.d(TAG, "Session valid: disconnection");
				TwitterSessionStore.clear(twitter, getContext());
				setImageResource(R.drawable.twitter_login);
				listener.onLogoutSucceed();
			} else {
				try {
					Log.d(TAG, "Session not valid: connection");
					String authorizeUrl = twitter.getAuthorizeUrl();
					twitter.autorize(activity, authorizeUrl,
							new LoginDialogListener());
				} catch (OAuthMessageSignerException e) {
					Log.e(TAG, e.getMessage());
					listener.onError(e.getMessage());
				} catch (OAuthNotAuthorizedException e) {
					Log.e(TAG, e.getMessage());
					listener.onError(e.getMessage());
				} catch (OAuthExpectationFailedException e) {
					Log.e(TAG, e.getMessage());
					listener.onError(e.getMessage());
				} catch (OAuthCommunicationException e) {
					Log.e(TAG, e.getMessage());
					listener.onError(e.getMessage());
				}
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {
			try {
				// Get the OAuth Verifier and retrieve the token
				String oauthVerifier = values.getString(OAuth.OAUTH_VERIFIER);
				twitter.retrieveAccessToken(oauthVerifier);
				TwitterSessionStore.save(twitter, getContext());

				setImageResource(R.drawable.twitter_logout);
				listener.onAuthSucceed();
			} catch (OAuthMessageSignerException e) {
				Log.e(TAG, e.getMessage());
				listener.onError(e.getMessage());
			} catch (OAuthNotAuthorizedException e) {
				Log.e(TAG, e.getMessage());
				listener.onError(e.getMessage());
			} catch (OAuthExpectationFailedException e) {
				Log.e(TAG, e.getMessage());
				listener.onError(e.getMessage());
			} catch (OAuthCommunicationException e) {
				Log.e(TAG, e.getMessage());
				listener.onError(e.getMessage());
			}
		}
	}
}
