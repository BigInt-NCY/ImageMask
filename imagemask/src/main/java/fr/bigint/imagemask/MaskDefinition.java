package fr.bigint.imagemask;

import android.support.annotation.DrawableRes;

/**
 * Use this class to build {@link Mask masks}.
 *
 * @author Felicien Brochu
 * @version 1.0
 * @see Mask#Mask(MaskDefinition)
 */
public class MaskDefinition {
	/**
	 * Drawable from which the mask will be built.
	 */
	@DrawableRes
	public final int drawable;

	/**
	 * Configuration of the mask.
	 */
	public final MaskConfig maskConfig;

	/**
	 * Tag to link to the created {@link Mask}.
	 *
	 * @see Mask#getTag()
	 */
	public final Object tag;


	/**
	 * Constructs a MaskDefinition.
	 *
	 * @param drawable   {@link #drawable}
	 * @param maskConfig {@link #maskConfig}
	 * @param tag        {@link #tag}
	 */
	public MaskDefinition(@DrawableRes int drawable, MaskConfig maskConfig, Object tag) {
		this.drawable = drawable;
		this.maskConfig = maskConfig;
		this.tag = tag;
	}
}
