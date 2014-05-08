package com.theappbusiness.whoswho.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Fonts {

	private static Fonts fonts = new Fonts();

	private Fonts() {

	}

	public static Fonts getInstance() {
		return fonts;
	}

	public Typeface getRobotoBoldCondensed(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-BoldCondensed.ttf");
	}

	public Typeface getRobotoCondensed(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-Condensed.ttf");
	}

	public Typeface getRobotoLight(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-Light.ttf");
	}
	
	public Typeface getRobotoThin(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-Thin.ttf");
	}	

	public Typeface getRobotoBoldItalic(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-BoldItalic.ttf");
	}

	public Typeface getRobotoMedium(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-Medium.ttf");
	}

	public Typeface getRobotoRegular(AssetManager context) {
		return Typeface.createFromAsset(context, "fonts/Roboto-Regular.ttf");
	}
}
