package fr.bigint.imagemaskexample;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import fr.bigint.imagemask.MaskConfig;
import fr.bigint.imagemask.MaskDefinition;
import fr.bigint.imagemask.MaskMapImageView;

/**
 * Created by Felicien Brochu on 04/07/2016.
 */
public class ImageMaskExampleActivity extends AppCompatActivity implements MaskMapImageView.OnMaskTouchListener {
	private static final String TAG = ImageMaskExampleActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_mask_example);

		MaskMapImageView maskMapImageView = (MaskMapImageView) findViewById(R.id.mask_mapping_image_view);
		maskMapImageView.setOnMaskTouchListener(this);
		MaskDefinition[] maskDefinitions = {
				new MaskDefinition(R.drawable.droid_mapping, MaskConfig.getIncludeColorMaskConfig(0xFFFF0000, 1f), R.drawable.head),
				new MaskDefinition(R.drawable.droid_mapping, MaskConfig.getIncludeColorMaskConfig(0xFF00FF00, 1f), R.drawable.arms),
				new MaskDefinition(R.drawable.droid_mapping, MaskConfig.getIncludeColorMaskConfig(0xFF0000FF, 1f), R.drawable.body),
				new MaskDefinition(R.drawable.droid_mapping, MaskConfig.getIncludeColorMaskConfig(0xFFFFFF00, 1f), R.drawable.legs),
		};
		maskMapImageView.setMasks(maskDefinitions);
		maskMapImageView.setDebugOverlayEnabled(true);
	}

	@Override
	public void onMaskPressed(@DrawableRes int mask, Object tag) {
		Log.i(TAG, "mask pressed: " + mask + ", tag: " + tag);
		Integer overlay = (Integer) tag;
		ImageView overlayImageView = (ImageView) findViewById(R.id.click_overlay_image_view);
		overlayImageView.setImageDrawable(ContextCompat.getDrawable(this, overlay));
	}

	@Override
	public void onMaskUnpressed(@DrawableRes int mask, Object tag) {
		Log.i(TAG, "mask unpressed: " + mask + ", tag: " + tag);
		ImageView overlayImageView = (ImageView) findViewById(R.id.click_overlay_image_view);
		overlayImageView.setImageDrawable(null);
	}

	@Override
	public void onMaskClick(@DrawableRes int mask, Object tag) {
		Log.i(TAG, "mask clicked: " + mask + ", tag: " + tag);
	}
}
