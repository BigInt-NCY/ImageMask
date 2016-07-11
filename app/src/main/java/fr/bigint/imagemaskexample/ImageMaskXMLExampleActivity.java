package fr.bigint.imagemaskexample;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import fr.bigint.imagemask.MaskMapImageView;

/**
 * Created by Felicien Brochu on 04/07/2016.
 */
public class ImageMaskXMLExampleActivity extends AppCompatActivity implements MaskMapImageView.OnMaskTouchListener {
	private static final String TAG = ImageMaskXMLExampleActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_mask_xml_example);

		final MaskMapImageView maskMapImageView = (MaskMapImageView) findViewById(R.id.mask_mapping_image_view);
		maskMapImageView.setOnMaskTouchListener(this);
	}

	@Override
	public void onMaskPressed(@DrawableRes int mask, Object tag) {
		Log.i(TAG, "mask pressed: " + mask + ", tag: " + tag);
		ImageView overlayImageView = (ImageView) findViewById(R.id.click_overlay_image_view);
		overlayImageView.setImageDrawable(ContextCompat.getDrawable(this, mask));
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
