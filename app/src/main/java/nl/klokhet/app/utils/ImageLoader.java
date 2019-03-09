package nl.klokhet.app.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoader {
    public static void loadImageWithListener(@NonNull ImageView imageView, String imageUri, @NonNull RequestListener<Drawable> listener) {
        GlideApp.with(imageView.getContext())
                .load(imageUri)
                .listener(listener)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageCircularWithListener(@NonNull ImageView imageView, String imageUri, @NonNull RequestListener<Drawable> listener) {
        GlideApp.with(imageView.getContext())
                .load(imageUri)
                .listener(listener)
                .apply(RequestOptions.circleCropTransform())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImage(@NonNull ImageView imageView, String imageUri) {
        GlideApp.with(imageView.getContext())
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageCenter(@NonNull ImageView imageView, String imageUri) {
        GlideApp.with(imageView.getContext())
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
    }

    public static void loadImageCircular(@NonNull ImageView imageView, String imageUri) {
        GlideApp.with(imageView.getContext())
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
