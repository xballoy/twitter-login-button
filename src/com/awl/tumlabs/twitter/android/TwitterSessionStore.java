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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/***
 * Safe for Token and Token Secret
 * 
 * @author Xavier Balloy (A504884)
 * 
 */
public class TwitterSessionStore {

	private static final String TOKEN = "token";
	private static final String TOKEN_SECRET = "tokenSecret";
	private static final String KEY = "twitter-session";

	/***
	 * Save the Token and Token Secret
	 * 
	 * @param session
	 *            Twitter object with the token and token secret
	 * @param context
	 *            The context of the application
	 * @return
	 */
	public static boolean save(TwitterAuth session, Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, session.getToken());
		editor.putString(TOKEN_SECRET, session.getTokenSecret());
		return editor.commit();
	}

	/***
	 * Restore the token and token secret
	 * 
	 * @param session
	 *            Twitter object to fill with the token and token secret
	 * @param context
	 *            The context of the application
	 * @return true if the token and token secret are defined
	 */
	public static boolean restore(TwitterAuth session, Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		session.setToken(savedSession.getString(TOKEN, null));
		session.setTokenSecret(savedSession.getString(TOKEN_SECRET, null));
		return session.isSessionValid();
	}

	/***
	 * Clear the token and token secret
	 * 
	 * @param context
	 *            The context of the application
	 */
	public static void clear(TwitterAuth session, Context context) {
		session.setToken(null);
		session.setTokenSecret(null);

		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
	}

}
