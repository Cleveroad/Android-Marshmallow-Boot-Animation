# Android Marshmallow Boot Animation View #

Implementation of [Android Marshmallow Boot Animation](https://dribbble.com/shots/2487137-Daily-UI-076-Loading-Android-Marshmallow-Boot).

![Demo image](/images/animation_demo.gif)

To use LoadingAnimationView first add dependency to your project:
 
```groovy
dependencies {
    compile 'com.cleveroad:androidmanimation:0.9.1'
}
``` 
 
Then you can declare it in your layout file like this:

```XML
    <com.cleveroad.androidmanimation.LoadingAnimationView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/animation"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="16dp"
        app:lav_backgroundColor="@android:color/black"
        app:lav_firstColor="@color/google_red"
        app:lav_secondColor="@color/google_green"
        app:lav_thirdColor="@color/google_blue"
        app:lav_fourthColor="@color/google_yellow"
        app:lav_speedCoefficient="1.0"
        />
```

Pay attention that you need to manually start/pause/stop animation.

```JAVA
    private LoadingAnimationView animation;
    
    ...
    
    @Override
    protected void onResume() {
        super.onResume();
        // user interacts with screen, start animation
        animation.startAnimation();
    }

    @Override
    protected void onPause() {
        // user stops interacting with screen, pause animation, save your battery!
        animation.pauseAnimation();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // user leaves screen. Clean up everything. 
        animation.stopAnimation();
        super.onDestroy();
    }
```

Another way to display loading animation is to use builder and display a dialog:
 
```JAVA
    AnimationDialogFragment fragment = new AnimationDialogFragment.Builder()
            .setBackgroundColor(Color.WHITE)
            .setFirstColor(getResources().getColor(R.color.google_red))
            .setSecondColor(getResources().getColor(R.color.google_green))
            .setThirdColor(getResources().getColor(R.color.google_blue))
            .setFourthColor(getResources().getColor(R.color.google_yellow))
            .setSpeedCoefficient(1.0f)
            .build();
    fragment.show(getSupportFragmentManager(), "Animation");
```

## Changelog

| Version | Changes                         |
| --- | --- |
| v.0.9.1 | Replaced Timer with ValueAnimator |
| v.0.9.0 | First public release            |

#### Migrations from v.0.9.0 to v.0.9.1
* **LoadingAnimationView.getState()** was replaced with **LoadingAnimationView.isRunning()** method.
* All resources marked as private, prefix `lav_` added to their names.
* Minimum SDK version set to 12


<br />
#### Support ####
* * *
If you have any other questions regarding the use of this library, please contact us for support at info@cleveroad.com (email subject: "Android loading animation view. Support request.") 
or 

Use our contacts: 

* [Official site](https://www.cleveroad.com/?utm_source=github&utm_medium=link&utm_campaign=contacts)
* [Facebook account](https://www.facebook.com/cleveroadinc)
* [Twitter account](https://twitter.com/CleveroadInc)
* [Google+ account](https://plus.google.com/+CleveroadInc/)

<br />
#### License ####
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
