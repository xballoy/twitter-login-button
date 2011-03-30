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

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.Context;
import android.os.Bundle;

/***
 * OAuth for Twitter
 * 
 * @author Xavier Balloy
 * 
 */
public class TwitterAuth {

	private static final String REQUEST_TOKEN_URL = "https://twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "https://twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://twitter.com/oauth/authorize";

	private String callbackUrl;
	private String token;
	private String tokenSecret;

	private CommonsHttpOAuthConsumer consumer;
	private CommonsHttpOAuthProvider provider;

	/***
	 * Create a new Twitter object Go to https://dev.twitter.com/ to get a
	 * consumer key and a consumer secret
	 * 
	 * @param consumerKey
	 *            the Consumer Key of the application
	 * @param consumerSecret
	 *            the Consumer Secret of the application
	 * @param callbackUrl
	 *            the Callback URL of the application
	 */
	public TwitterAuth(String consumerKey, String consumerSecret,
			String callbackUrl) {
		this.callbackUrl = callbackUrl;

		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL,
				ACCESS_TOKEN_URL, AUTHORIZE_URL);
	}

	public String getToken() {
		return this.token;
	}

	protected void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return this.tokenSecret;
	}

	protected void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	protected String getAuthorizeUrl() throws OAuthMessageSignerException,
			OAuthNotAuthorizedException, OAuthExpectationFailedException,
			OAuthCommunicationException {
		return provider.retrieveRequestToken(consumer, callbackUrl);
	}

	protected void retrieveAccessToken(String oauthVerifier)
			throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		provider.retrieveAccessToken(consumer, oauthVerifier);
		this.token = consumer.getToken();
		this.tokenSecret = consumer.getTokenSecret();
	}

	protected void autorize(Context context, String url, DialogListener listener) {
		new TwitterDialog(context, url, callbackUrl, listener).show();
	}

	public boolean isSessionValid() {
		return getToken() != null && getTokenSecret() != null;
	}

	protected static interface DialogListener {
		public void onComplete(Bundle values);
	}

	public static interface AuthListener {
		public void onAuthSucceed();

		public void onLogoutSucceed();

		public void onError(String error);
	}

}