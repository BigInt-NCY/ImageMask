package fr.bigint.imagemask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

/**
 * This class creates a binary mask that can be used to map
 * an area. The bit mask is created from a
 * {@link android.graphics.drawable.Drawable Drawable}.
 * After constructor call you must call {@link #initAsync(Context)}
 * or {@link #init(Context)} in order to create the bit mask.
 * Then you can test if a point is part of the mask by calling
 * {@link #matches(Point)} method.
 * <p/>
 * For more details on Mask configuration see {@link MaskConfig}
 * and {@link MaskDefinition}.
 *
 * @author Felicien Brochu
 * @version 1.0
 * @see MaskConfig
 * @see MaskDefinition
 */
public class Mask {

	/**
	 * Drawable used to build the mask.
	 */
	private final int mDrawableMask;

	/**
	 * Configuration of how to build the mask.
	 */
	private final MaskConfig mConfig;

	/**
	 * Tag of this mask.
	 */
	private Object mTag;

	/**
	 * Binary mask mapping pixels of the drawable.
	 */
	private byte[] mBitMask;

	/**
	 * Width of {@link #mBitMask}.
	 */
	private int mWidth;
	/**
	 * Height of {@link #mBitMask}.
	 */
	private int mHeight;

	/**
	 * Async task that initializes {@link #mBitMask}.
	 */
	private InitBitMaskTask mInitBitMaskTask;

	/**
	 * Creates a Mask from the given definition.
	 *
	 * @param definition the definition of the mask.
	 * @see #Mask(int, MaskConfig, Object)
	 */
	public Mask(MaskDefinition definition) {
		this(definition.drawable, definition.maskConfig, definition.tag);
	}

	/**
	 * Creates a Mask from the given drawable according
	 * to the given config and associates the given tag
	 * to this Mask.
	 *
	 * @param drawableMask drawable from which the mask
	 *                     is generated
	 * @param config       configuration of how to build
	 *                     the mask.
	 * @param tag          this Mask tag
	 */
	public Mask(int drawableMask, MaskConfig config, Object tag) {
		mDrawableMask = drawableMask;
		mConfig = config;
		mTag = tag;
	}

	/**
	 * Initializes the bit mask asynchronously.
	 *
	 * @param context android context
	 */
	public void initAsync(Context context) {
		cancelInitTask();
		mInitBitMaskTask = new InitBitMaskTask(this, context);
		mInitBitMaskTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	/**
	 * Cancels any async initialization of this Mask.
	 */
	public void cancelInitTask() {
		if (mInitBitMaskTask != null) {
			mInitBitMaskTask.cancel(true);
		}
	}

	/**
	 * Initializes the bit mask synchronously.
	 * It could be slow with large drawables and
	 * high {@link MaskConfig#weight weight}.
	 * Prefer using {@link #initAsync(Context)}.
	 *
	 * @param context android context
	 */
	public synchronized void init(Context context) {
		Drawable drawable = ContextCompat.getDrawable(context, mDrawableMask);

		MaskConfig config = mConfig;
		int width = (int) (drawable.getIntrinsicWidth() * config.weight) + 1;
		int height = (int) (drawable.getIntrinsicHeight() * config.weight) + 1;

		Bitmap bitmap;

		if (drawable instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		} else {
			Bitmap.Config bitmapConfig;
			if (config.type == MaskConfig.OPACITY_MASK || config.type == MaskConfig.TRANSPARENCY_MASK) {
				bitmapConfig = Bitmap.Config.ALPHA_8;
			} else {
				bitmapConfig = Bitmap.Config.ARGB_8888;
			}
			bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
		}

		byte[] bitMask = new byte[(width * height) / 8 + 1];
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		int pixelsSize = bitmapWidth * bitmapHeight;
		int[] pixels = new int[pixelsSize];
		bitmap.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

		float xRatio = ((float) bitmapWidth) / ((float) width);
		float yRatio = ((float) bitmapHeight) / ((float) height);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if ((i * width + j) % 8 == 0) {
					bitMask[(i * width + j) / 8] = 0;
				}
				int xBitmap = (int) (xRatio * (j + 0.5));
				int yBitmap = (int) (yRatio * (i + 0.5));
				boolean masked = isPixelMasked(pixels[yBitmap * bitmapWidth + xBitmap], config);
				bitMask[(i * width + j) / 8] |= (masked ? 1 : 0) << ((i * width + j) % 8);
			}
		}
		mBitMask = bitMask;
		mWidth = width;
		mHeight = height;
	}

	/**
	 * Determine if a pixel of the drawable is part of the mask
	 * according to its color.
	 *
	 * @param pixel  Pixel color.
	 * @param config Mask configuration.
	 * @return true if the pixel belongs to the mask false otherwise
	 */
	private static boolean isPixelMasked(int pixel, MaskConfig config) {
		boolean masked = false;
		int configType = config.type;
		if (configType == MaskConfig.OPACITY_MASK) {
			masked = pixel != 0;
		} else if (configType == MaskConfig.TRANSPARENCY_MASK) {
			masked = pixel == 0;
		} else if (configType == MaskConfig.INCLUDE_COLOR_MASK) {
			masked = pixel == config.color;
		} else if (configType == MaskConfig.EXCLUDE_COLOR_MASK) {
			masked = pixel != config.color;
		}
		return masked;
	}

	/**
	 * Check if the given point is in this mask.
	 *
	 * @param point Point to check. Its coordinates must be
	 *              in interval [0, 1].
	 * @return true if the point belongs to the mask false otherwise
	 */
	public synchronized boolean matches(Point point) {
		return matches(point.x, point.y);
	}

	/**
	 * Check if the given point is in this mask.
	 *
	 * @param x x coordinate. Must be in interval [0, 1].
	 * @param y y coordinate. Must be in interval [0, 1].
	 * @return true if the point belongs to the mask false otherwise
	 */
	public synchronized boolean matches(float x, float y) {
		if (x < 0 || x > 1 || y < 0 || y > 1) {
			return false;
		}
		int scaledX = (int) (x * (float) mWidth);
		int scaledY = (int) (y * (float) mHeight);

		return mBitMask != null && scaledX < mWidth && scaledY < mHeight &&
				(mBitMask[(scaledY * mWidth + scaledX) / 8] & (1 << (scaledY * mWidth + scaledX) % 8)) != 0;
	}

	/**
	 * Returns the drawable that is used to init the mask.
	 *
	 * @return the drawable that is used to init the mask.
	 */
	public int getDrawableMask() {
		return mDrawableMask;
	}

	/**
	 * Returns the configuration of how to build the mask.
	 *
	 * @return the configuration of how to build the mask.
	 */
	public MaskConfig getConfig() {
		return mConfig;
	}

	/**
	 * Returns this Mask's tag.
	 *
	 * @return this Mask's tag.
	 */
	public Object getTag() {
		return mTag;
	}

	/**
	 * Set this Mask's tag.
	 */
	public void setTag(Object tag) {
		mTag = tag;
	}

	/**
	 * Async task that initializes the Mask's bit mask.
	 */
	private static class InitBitMaskTask extends AsyncTask<Void, Void, Void> {
		private final Mask mMask;
		private final Context mContext;

		public InitBitMaskTask(Mask mask, Context context) {
			mMask = mask;
			mContext = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			mMask.init(mContext);
			return null;
		}
	}

	/**
	 * Point on a mask. x and y must be in
	 * interval [0, 1].
	 */
	public static class Point {
		/**
		 * X coordinate. Must be in interval [0, 1].
		 */
		public final float x;
		/**
		 * Y coordinate. Must be in interval [0, 1].
		 */
		public final float y;

		/**
		 * @param x {@link #x}
		 * @param y {@link #y}
		 */
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
