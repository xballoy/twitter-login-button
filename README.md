twitter-login-button
====================
A button to include into the layout of your Android project to easily connect to Twitter by using OAuth.

The Twitter Login Button code includes oauth-signpost libraries that are licensed under Apache 2.0 term.

#Example of utilisation

##Introduction

In this example I will show you how to use the twitter-login-button (TLB)
The target build of the TBL is Android 1.5 so it can be used in every Android project.

##Prerequisite
###Software

Before you begin development with TBL you need to install Android SDK and developers tool.
 * [Android](http://developer.android.com/sdk/index.html)
 * [The Eclipse Plugin](http://developer.android.com/sdk/eclipse-adt.html)

###Installation

Once you have everything installed, open Eclipse and create a new Android Project (File | New | Project ...). We will use this project for the TLB source and then reference it from our app. As such, we need to get the content by selecting Create project from existing source and specifying the TLB directory.

With the TLB project created, we can create the app. Create a new Android Project (the TLB project will stay open in the Eclipse Workspace) from File | New | Project..., using the defaults and populating the require fields.

Once your app project is created, you will need to add a reference to the TLB project. You do this by opening the properties window for our app (File | Properties), selecting the Android item from the list, pressing the Add... button in the Library area and selecting the TLB project created above.

Once the Facebook SDK is referenced, the app manifest needs to be modified to allow the app to make network calls to Facebook. This is accomplished by adding the following to the `AndroidManifest.xml` file in the app project:
```xml
<uses-permission android:name="android.permission.INTERNET">
</uses-permission>
```

###Implementation
####Register your app

In order to use you must create an app in the [Twitter Dev Center](https://dev.twitter.com/).
You need to choose **Browser** as **Application Type**.
You can put anything but a valid URL in the **Callback URL** because it's programmatically redefined. 
Once your app is registered save your Consumer Key and your Consumer Secret.

####Layout

First, you have to add the TLB into your layout as following:
```xml
<com.awl.tumlabs.twitter.android.TwitterLoginButton
android:id="@+id/bLoginTwitter" android:layout_width="wrap_content"
android:layout_height="wrap_content"/>
```

###Activity

In the class associated to your layout you have to initialize the TLB as following.

```java
package com.awl.tumlabs.android;

import android.app.Activity;
import android.os.Bundle;

import com.atos.awl.tumlabs.android.R;
import com.awl.tumlabs.twitter.android.TwitterAuth;
import com.awl.tumlabs.twitter.android.TwitterAuth.AuthListener;
import com.awl.tumlabs.twitter.android.TwitterLoginButton;

public class TwitterExample extends Activity{
  
	private static final String CONSUMER_KEY = "xxx";
	private static final String CONSUMER_SECRET = "xxx";
	private static final String CALLBACK_URL = "myApp:///twitter";
	private static TwitterAuth mTwitterAuth = new TwitterAuth(CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
	
	private TwitterLoginButton twitterLoginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		twitterLoginButton = (TwitterLoginButton) findViewById(R.id.bLoginTwitter);
		twitterLoginButton.init(this, mTwitterAuth, new TwitterAuthListener());
	}
	
	private class TwitterAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLogoutSucceed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(String error) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
```

You can handle the login and logout with `TwitterAuthListener`.
