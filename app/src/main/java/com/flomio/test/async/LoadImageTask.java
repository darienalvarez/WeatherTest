package com.flomio.test.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class LoadImageTask extends AbstractTask<String, Void, Bitmap> {

    private static final String TAG = "LoadImageFromNet";

    @Override
    protected Bitmap doInBackground(String... urls) {
        return loadImageFromNetwork(urls[0]);
    }

    /**
     * Gets an image from an URL
     */
    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)
                    new URL(url).getContent());
        } catch (Exception e) {
            Log.e(TAG, "can not load image", e);
            mException = e;
        }
        return bitmap;
    }
}
