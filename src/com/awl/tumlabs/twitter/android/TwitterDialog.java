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

import oauth.signpost.OAuth;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.awl.tumlabs.twitter.android.TwitterLoginButton.SessionListener;
import com.twitter.android.R;

/***
 * Create a webview to launch the Twitter website to permit the user to allow
 * the application
 * 
 * @author Xavier Balloy (A504884)
 * 
 */
public class TwitterDialog extends Dialog {

	static final float[] DIMENSIONS_DIFF_LANDSCAPE = { 20, 60 };
	static final float[] DIMENSIONS_DIFF_PORTRAIT = { 40, 60 };
	static final int MARGIN = 4;
	static final int PADDING = 2;
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);

	private String mUrl;
	private SessionListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private String callbackUrl;

	/***
	 * Create a webview to launch the Twitter website to permit the user to
	 * allow the application
	 * 
	 * @param context
	 *            The activity which launch the webview
	 * @param url
	 *            The URL to launch
	 * @param callbackUrl
	 *            The callback URL to come back to the application
	 * @param listener
	 *            The listener to know when the webview ends
	 */
	public TwitterDialog(Context context, String url, String callbackUrl,
			SessionListener listener) {
		super(context);
		this.mUrl = url;
		this.callbackUrl = callbackUrl;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage(getContext().getString(R.string.loading));

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);

		setUpWebView();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int orientation = getContext().getResources().getConfiguration().orientation;
		float[] dimensions = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? DIMENSIONS_DIFF_LANDSCAPE
				: DIMENSIONS_DIFF_PORTRAIT;
		addContentView(
				mContent,
				new LinearLayout.LayoutParams(display.getWidth()
						- ((int) (dimensions[0] * scale + 0.5f)), display
						.getHeight() - ((int) (dimensions[1] * scale + 0.5f))));
	}

	private void setUpWebView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new TwitterWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	private class TwitterWebViewClient extends WebViewClient {

		private final String TAG = TwitterWebViewClient.class.getSimpleName();

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "shouldOverrideUrlLoading: " + url);
			if (url.startsWith(callbackUrl)) {
				Bundle values = Util.parseUrl(url, callbackUrl);
				String oauthVerifier = values.getString(OAuth.OAUTH_VERIFIER);
				mListener.onDialogComplete(oauthVerifier);
				TwitterDialog.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "onPageStarted: " + url);
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.d(TAG, "onPageFinished: " + url);
			super.onPageFinished(view, url);
			mSpinner.dismiss();
		}
	}
}
