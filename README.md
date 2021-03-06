ImageMask
==========
[ ![Bintray download](https://api.bintray.com/packages/bigint/maven/fr.bigint.imagemask/images/download.svg) ](https://bintray.com/bigint/maven/fr.bigint.imagemask/_latestVersion)

Android library to map clickable areas on an `ImageView`.
Specify your clickable areas using drawables.

## Example of use

You wish to map touch events on image areas in Android: this library is made for you!

You can map clicks on areas defined by drawable masks.

|                        Image to map                        |                            head mask                             |                            arms mask                             |                            body mask                             |                            legs mask                             |
|:----------------------------------------------------------:|:----------------------------------------------------------------:|:----------------------------------------------------------------:|:-----------------------------------------------------------------:|:----------------------------------------------------------------:|
| ![Image to map](app/src/main/res/drawable-nodpi/droid.png) | ![Clickable area mask](app/src/main/res/drawable-nodpi/head.png) | ![Clickable area mask](app/src/main/res/drawable-nodpi/arms.png) | ![Clickable area mask](app/src/main/res/drawable-nodpi/body.png) | ![Clickable area mask](app/src/main/res/drawable-nodpi/legs.png) |

In this example each part of Android's droid body would be clickable independently.
<br><br><br>

## Getting started

Include the dependency in `build.gradle`:
```groovy
dependencies {
    compile 'fr.bigint.imagemask:imagemask:1.0.2'
}
```
## Usage
First, create a `MaskMapImageView` and the mapping going along with it. Then listen to touch events.

## Mask types

There is 4 types of mask creation:
* **opacity**: create a mask from non transparent pixels. Every pixel that has an alpha value
greater than 0 will be part of the created mask.
* **transparency**: create a mask from transparent pixels. Every pixel that has an alpha value
of 0 will be part of the created mask.
* **includeColor**: create a mask from pixels that have a specific color.
* **excludeColor**: create a mask from pixels that do not have the chosen color.

>  **Note:** **opacity** is the deafult behaviour.

<br>

## Create mapping in XML
Create a MaskMapImageView from XML layout file.
```xml
<fr.bigint.imagemask.MaskMapImageView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:scaleType="fitCenter"
	android:src="@drawable/droid"
	mask:masks="@array/image_masks"/>
```
Attribute `masks` references an array of drawables. Let's create it in a custom XML file called
`mappings.xml`. You can create this file in `res/values` folder for example.
```xml
<resources>
	<integer-array name="image_masks">
		<item>@drawable/head</item>
		<item>@drawable/arms</item>
		<item>@drawable/body</item>
		<item>@drawable/legs</item>
	</integer-array>
</resources>
```
All opaque areas of the drawables from this array will be clickable areas in the created ImageView.

<br>

## Create mapping programmatically

### Opacity mask (default)
```java
MaskDefinition[] maskDefinitions = {
	new MaskDefinition(R.drawable.head, MaskConfig.getOpacityMaskConfig(1), "head"),
	new MaskDefinition(R.drawable.arms, MaskConfig.getOpacityMaskConfig(1), "arms"),
	new MaskDefinition(R.drawable.body, MaskConfig.getOpacityMaskConfig(1), "body"),
	new MaskDefinition(R.drawable.legs, MaskConfig.getOpacityMaskConfig(1), "legs"),
};
maskMapImageView.setMasks(maskDefinitions);
```

### Color mask

Here is an example of how to create the same mapping masks as before but using a single drawable
with **includeColor** configuration.

|                        Image to map                        |                             Color mask Drawable                           |
|:----------------------------------------------------------:|:-------------------------------------------------------------------------:|
| ![Image to map](app/src/main/res/drawable-nodpi/droid.png) | ![Color mask Drawable](app/src/main/res/drawable-nodpi/droid_mapping.png) |

We specify which area of the drawable is used to map clicks using colors.

```java
MaskDefinition[] maskDefinitions = {
	new MaskDefinition(
		R.drawable.droid_mapping,
		MaskConfig.getIncludeColorMaskConfig(0xFFFF0000, 1f), //< include red
		"head"),
	new MaskDefinition(
		R.drawable.droid_mapping,
		MaskConfig.getIncludeColorMaskConfig(0xFF00FF00, 1f), //< include green
		"arms"),
	new MaskDefinition(
		R.drawable.droid_mapping,
		MaskConfig.getIncludeColorMaskConfig(0xFF0000FF, 1f), //< include blue
		"body"),
	new MaskDefinition(
		R.drawable.droid_mapping,
		MaskConfig.getIncludeColorMaskConfig(0xFFFFFF00, 1f), //< include yellow
		"legs"),
};
maskMapImageView.setMasks(maskDefinitions);
```

<br>

## Listen to mapped touch events
Register an `OnMaskTouchListener` to listen to mapped touch events. You can identify which mask
has been touched relying on `mask` which is the drawable id used to create the mask and `tag` which
is the tag associated to the mask at creation time.

> **Note:** Tags can only be specified programmaticaly

```java
maskMapImageView.setOnMaskTouchListener(new MaskMapImageView.OnMaskTouchListener() {
	@Override
	public void onMaskPressed(int mask, Object tag) {
	}

	@Override
	public void onMaskUnpressed(int mask, Object tag) {
	}
	
	@Override
	public void onMaskClick(int mask, Object tag) {
	}
});
```



-------------------------------------------------------------------------------------------------
<br>

## Masks configuration

```xml
<fr.bigint.imagemask.MaskMapImageView
		android:id="@+id/mask_mapping_image_view"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:scaleType="fitCenter"
		android:src="@drawable/droid"
		mask:masks="@array/droid_masks"
		mask:mask_type="include_color"
		mask:mask_color="#FFFF0000"
		mask:mask_weight="0.5"
		mask:mask_showDebugOverlay="true"/>
```

* **masks** is a reference to the array of drawables
  contained in `mappings.xml`. These drawables will
  be used by the view to generate bit masks in order to map
  touch events. If this attribute is not defined there will
  be no mapping generated by the view.

* **mask_type** defines the way masks will
  be created from drawables. This attribute can take 4 values:
  `opacity`, `transparency`, `includeColor` or `excludeColor`.
  Default value: `opacity`.

* **mask_color** defines the color used to create masks
  when **mask_type** is set to `includeColor` or `excludeColor`.
  Default value: #00000000.

* **mask_showDebugOverlay**: if true a debug overlay will
  be printed onto the image. It represents masks as semi-transparent
  overlays of different colors see the method `MaskMapImageView.setDebugOverlayEnabled(boolean)`
  for details. Default value is false.

* **mask_weight** defines the relative weight of the mask
  in memory. By default the weight is 1.0 which means that 1 pixel
  of the drawable supplied to create the mask will occupy 1 bit in
  memory. You can set the weight in ]0, 1] interval in which case
  the mask will occupy (weight) * 1 bit per pixel.
> **Warning:** the precision of the mask will decrease accordingly. Make sure your mask
  is still correct using the debug overlay.

|               Without debug overlay             |                    weight = 1                  |                    weight = 0.25                  |                    weight = 0.08                  |                    weight = 0.02                  |
|:-------------------------------------------------:|:------------------------------------------------------:|:------------------------------------------------------:|:------------------------------------------------------:|:------------------------------------------------------:|
|![No debug overlay](readme-res/nooverlay.png)|![debug overlay weight = 1](readme-res/overlayw1.png)|![debug overlay weight = 0.25](readme-res/overlayw025.png)|![debug overlay weight = 0.08](readme-res/overlayw008.png)|![debug overlay weight = 0.02](readme-res/overlayw002.png)|

For more details on masks configuration please refer to javadoc and try to play around with the sample app.

<br>

## Implementation details

Compared to other image mapping implementations using
Bitmaps to map clicks, this library uses much less memory. By creating bit masks, which occupy only
1 bit per pixel at most, this library uses at least 32 times less memory. You can save even more
memory setting masks' weight to less than 1. Developers out there that have worked with Bitmaps on
Android know how much memory savings can represent a great deal on Android.