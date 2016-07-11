package fr.bigint.imagemask;

import android.support.annotation.ColorInt;

/**
 * Describes how to generate a {@link Mask} from a
 * {@link android.graphics.drawable.Drawable Drawable}.
 * <ul>
 * <li>
 * <em>{@link #type}</em> describes what colors of the drawable
 * will be in the generated mask.
 * </li>
 * <li>
 * <em>{@link #weight}</em> describes the
 * relative weight of the generated mask in memory. By default the
 * weight is 1.0 which means that 1 pixel of the drawable supplied
 * to create the mask will occupy 1 bit in memory. You can set the
 * weight in ]0, 1] interval in which case the mask will occupy
 * (weight) * 1 bit per pixel. Warning: the precision of the mask
 * will decrease accordingly.
 * </li>
 * <li>
 * <em>{@link #color}</em> describes which color to use when
 * {@link #type} is {@link #INCLUDE_COLOR_MASK} or
 * {@link #EXCLUDE_COLOR_MASK}.
 * </li>
 * </ul>
 *
 * @author Félicien Brochu
 * @version 1.0
 */
public class MaskConfig {
	/**
	 * Creates a mask from non transparent pixels.
	 * Every pixel that as an alpha value greater
	 * than 0 will be part of the created mask.
	 *
	 * @see #getOpacityMaskConfig(float)
	 */
	public static final int OPACITY_MASK = 0x00000001;

	/**
	 * Creates a mask from transparent pixels.
	 * Every pixel that as an alpha value of 0
	 * will be part of the created mask.
	 *
	 * @see #getTransparencyMaskConfig(float)
	 */
	public static final int TRANSPARENCY_MASK = 0x00000002;

	/**
	 * Creates a mask from pixels that have
	 * a specific color. Choose the color by
	 * setting {@link #color}.
	 *
	 * @see #getIncludeColorMaskConfig(int, float)
	 */
	public static final int INCLUDE_COLOR_MASK = 0x00000003;

	/**
	 * Creates a mask from pixels that do not have
	 * the chosen color. Choose the color by setting
	 * {@link #color}.
	 *
	 * @see #getExcludeColorMaskConfig(int, float)
	 */
	public static final int EXCLUDE_COLOR_MASK = 0x00000004;


	/**
	 * type of mask creation. Takes values in
	 * {@link #OPACITY_MASK},
	 * {@link #TRANSPARENCY_MASK},
	 * {@link #INCLUDE_COLOR_MASK},
	 * {@link #EXCLUDE_COLOR_MASK}.
	 */
	public final int type;

	/**
	 * Relative weight of the mask in memory. By default the weight is
	 * 1.0 which means that 1 pixel of the drawable supplied to create
	 * the mask will occupy 1 bit in memory. You can set the weight in
	 * ]0, 1] interval in which case the mask will occupy
	 * (weight) * 1 bit per pixel. Warning: the precision of the mask
	 * will decrease accordingly.
	 */
	public final float weight;

	/**
	 * Color to use when {@link #type} is {@link #INCLUDE_COLOR_MASK}
	 * or {@link #EXCLUDE_COLOR_MASK}.
	 */
	@ColorInt
	public final int color;

	/**
	 * Creates a default mask config. Type is
	 * {@link #OPACITY_MASK}, weight is 1.0 .
	 */
	public MaskConfig() {
		this(OPACITY_MASK, 1, 0x00000000);
	}

	/**
	 * Creates a mask config of a given type.
	 * Color is #00000000, weight is 1.0 .
	 *
	 * @param type {@link #type}
	 */
	public MaskConfig(int type) {
		this(type, 1, 0x00000000);
	}

	/**
	 * Creates a mask config of a given type
	 * and weight. Color is #00000000.
	 *
	 * @param type   {@link #type}
	 * @param weight {@link #weight}
	 */
	public MaskConfig(int type, float weight) {
		this(type, weight, 0x00000000);
	}

	/**
	 * Creates a mask config.
	 *
	 * @param type   {@link #type}
	 * @param weight {@link #weight}
	 * @param color  {@link #color}
	 */
	public MaskConfig(int type, float weight, @ColorInt int color) {
		this.type = type;
		this.weight = weight;
		this.color = color;
	}

	/**
	 * Creates a mask config of type {@link #OPACITY_MASK}
	 * and given weight.
	 *
	 * @param weight {@link #weight}
	 * @return a mask config of type {@link #OPACITY_MASK}
	 */
	public static MaskConfig getOpacityMaskConfig(float weight) {
		return new MaskConfig(OPACITY_MASK, weight);
	}

	/**
	 * Creates a mask config of type {@link #TRANSPARENCY_MASK}
	 * and given weight.
	 *
	 * @param weight {@link #weight}
	 * @return a mask config of type {@link #TRANSPARENCY_MASK}
	 */
	public static MaskConfig getTransparencyMaskConfig(float weight) {
		return new MaskConfig(TRANSPARENCY_MASK, weight);
	}

	/**
	 * Creates a mask config of type {@link #INCLUDE_COLOR_MASK}
	 * and given color and weight.
	 *
	 * @param color  {@link #color}
	 * @param weight {@link #weight}
	 * @return a mask config of type {@link #INCLUDE_COLOR_MASK}
	 */
	public static MaskConfig getIncludeColorMaskConfig(@ColorInt int color, float weight) {
		return new MaskConfig(INCLUDE_COLOR_MASK, weight, color);
	}

	/**
	 * Creates a mask config of type {@link #EXCLUDE_COLOR_MASK}
	 * and given color and weight.
	 *
	 * @param color  {@link #color}
	 * @param weight {@link #weight}
	 * @return a mask config of type {@link #EXCLUDE_COLOR_MASK}
	 */
	public static MaskConfig getExcludeColorMaskConfig(@ColorInt int color, float weight) {
		return new MaskConfig(EXCLUDE_COLOR_MASK, weight, color);
	}
}
