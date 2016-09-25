package org.softwarelibre.tapleau.haptic.symbols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import co.tanvas.haptics.service.adapter.HapticServiceAdapter;
import co.tanvas.haptics.service.app.HapticApplication;
import co.tanvas.haptics.service.model.HapticMaterial;
import co.tanvas.haptics.service.model.HapticSprite;
import co.tanvas.haptics.service.model.HapticTexture;
import co.tanvas.haptics.service.model.HapticView;

import org.softwarelibre.tapleau.R;

public class BrailleDot {
    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    public BrailleDot(Context context, View view, double x, double y) {
        System.out.println("Current view ID grabbed is " + String.valueOf(view.getId()));
        HapticServiceAdapter serviceAdapter = HapticApplication.getHapticServiceAdapter();
        try {
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);
            mHapticView.setOrientation(orientation);

            Bitmap hapticBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_circle);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);

            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);

            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Set the size and position of the haptic sprite to correspond to the view we created
            mHapticSprite.setSize(324, 317);
            System.out.println("The coordinates for the current dot is (" + x + ", " + y + ")");
            mHapticSprite.setPosition((int) x, (int) y);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
