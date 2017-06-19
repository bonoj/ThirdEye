package com.hk47.realityoverlay.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.utils.PlacesUtilities;

public class SearchActivity extends AppCompatActivity {

    private ImageButton mCafesButton;
    private ImageButton mRestaurantsButton;
    private ImageButton mBarsButton;
    private ImageButton mParksButton;
    private ImageButton mBookstoresButton;
    private ImageButton mGalleriesButton;
    private ImageButton mMoviesButton;
    private ImageButton mLodgingsButton;
    private ImageButton mGroceriesButton;
    private ImageButton mAtmsButton;
    private ImageButton mPharmaciesButton;
    private ImageButton mTransitButton;

    private boolean mCafesAreActivated;
    private boolean mRestaurantsAreActivated;
    private boolean mBarsAreActivated;
    private boolean mParksAreActivated;
    private boolean mBookstoresAreActivated;
    private boolean mGalleriesAreActivated;
    private boolean mMoviesAreActivated;
    private boolean mLodgingsAreActivated;
    private boolean mGroceriesAreActivated;
    private boolean mAtmsAreActivated;
    private boolean mPharmaciesAreActivated;
    private boolean mTransitIsActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCafesButton = (ImageButton) findViewById(R.id.cafes_button);
        mRestaurantsButton = (ImageButton) findViewById(R.id.restaurants_button);
        mBarsButton = (ImageButton) findViewById(R.id.bars_button);
        mParksButton = (ImageButton) findViewById(R.id.parks_button);
        mBookstoresButton = (ImageButton) findViewById(R.id.bookstores_button);
        mGalleriesButton = (ImageButton) findViewById(R.id.galleries_button);
        mMoviesButton = (ImageButton) findViewById(R.id.movies_button);
        mLodgingsButton = (ImageButton) findViewById(R.id.lodgings_button);
        mGroceriesButton = (ImageButton) findViewById(R.id.groceries_button);
        mAtmsButton = (ImageButton) findViewById(R.id.atms_button);
        mPharmaciesButton = (ImageButton) findViewById(R.id.pharmacies_button);
        mTransitButton = (ImageButton) findViewById(R.id.transit_button);

        initializeButtons();
    }

    private void buildReturnIntent() {
        String currentTypesString = "";
        Intent intent = getIntent();
        if (intent != null) {
            currentTypesString = intent.getStringExtra(Constants.REFINE_SEARCH_INTENT);
        }
        String typesString = PlacesUtilities.getTypesString(
                getApplicationContext(),
                getSharedPreferences(getString(R.string.preferences_types_key), MODE_PRIVATE));
        if (!typesString.equals(currentTypesString)) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.SEARCH_RETURN_INTENT, typesString);
            setResult(Constants.SEARCH_RESULT_CODE, returnIntent);
        }
    }

    @Override
    public void onBackPressed() {
        buildReturnIntent();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                buildReturnIntent();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeButtons() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.preferences_types_key), MODE_PRIVATE);
        mCafesAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_cafe), true);
        setButtonBackground(
                mCafesAreActivated,
                mCafesButton,
                R.drawable.activated_button_cafe);
        mRestaurantsAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_restaurant), true);
        setButtonBackground(
                mRestaurantsAreActivated,
                mRestaurantsButton,
                R.drawable.activated_button_restaurant);
        mBarsAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_bar), false);
        setButtonBackground(
                mBarsAreActivated,
                mBarsButton,
                R.drawable.activated_button_bar);
        mParksAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_park), false);
        setButtonBackground(
                mParksAreActivated,
                mParksButton,
                R.drawable.activated_button_park);
        mBookstoresAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_bookstore), false);
        setButtonBackground(
                mBookstoresAreActivated,
                mBookstoresButton,
                R.drawable.activated_button_bookstore);
        mGalleriesAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_gallery), false);
        setButtonBackground(
                mGalleriesAreActivated,
                mGalleriesButton,
                R.drawable.activated_button_gallery);
        mMoviesAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_movie), false);
        setButtonBackground(
                mMoviesAreActivated,
                mMoviesButton,
                R.drawable.activated_button_movie);
        mLodgingsAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_lodging), false);
        setButtonBackground(
                mLodgingsAreActivated,
                mLodgingsButton,
                R.drawable.activated_button_lodging);
        mGroceriesAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_grocery), false);
        setButtonBackground(
                mGroceriesAreActivated,
                mGroceriesButton,
                R.drawable.activated_button_grocery);
        mAtmsAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_atm), false);
        setButtonBackground(
                mAtmsAreActivated,
                mAtmsButton,
                R.drawable.activated_button_atm);
        mPharmaciesAreActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_pharmacy), false);
        setButtonBackground(
                mPharmaciesAreActivated,
                mPharmaciesButton,
                R.drawable.activated_button_pharmacy);
        mTransitIsActivated =
                sharedPreferences.getBoolean(getString(R.string.preferences_type_transit), false);
        setButtonBackground(
                mTransitIsActivated,
                mTransitButton,
                R.drawable.activated_button_transit);
    }

    private void setButtonBackground(boolean isActivated,
                                   ImageButton button,
                                   int drawableValue) {
        if (isActivated) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(getResources().getDrawable(drawableValue));
            } else {
                button.setBackgroundResource(drawableValue);
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.deactivated_button));
            } else {
                button.setBackgroundResource(R.drawable.deactivated_button);
            }
        }
    }

    private void setTypesPreference(String type, boolean isActivated) {
        SharedPreferences.Editor editor = getSharedPreferences(
                getString(R.string.preferences_types_key), MODE_PRIVATE).edit();
        editor.putBoolean(type, isActivated);
        editor.apply();
    }

    private void showToast(boolean isActivated, String type) {
        String toastText;
        if (isActivated) {
            toastText = getString(R.string.adding) + " " + type + "!";
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
        } else {
            toastText = getString(R.string.removing) + " " + type + "!";
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
        }
    }

    public void onCafesButtonClicked(View view) {
        mCafesAreActivated = !mCafesAreActivated;
        setButtonBackground(
                mCafesAreActivated,
                mCafesButton,
                R.drawable.activated_button_cafe);
        setTypesPreference(getString(R.string.preferences_type_cafe), mCafesAreActivated);
        showToast(mCafesAreActivated, getString(R.string.cafes));
    }

    public void onRestaurantsButtonClicked(View view) {
        mRestaurantsAreActivated = !mRestaurantsAreActivated;
        setButtonBackground(
                mRestaurantsAreActivated,
                mRestaurantsButton,
                R.drawable.activated_button_restaurant);
        setTypesPreference(getString(R.string.preferences_type_restaurant), mRestaurantsAreActivated);
        showToast(mRestaurantsAreActivated, getString(R.string.restaurants));
    }

    public void onBarsButtonClicked(View view) {
        mBarsAreActivated = !mBarsAreActivated;
        setButtonBackground(
                mBarsAreActivated,
                mBarsButton,
                R.drawable.activated_button_bar);
        setTypesPreference(getString(R.string.preferences_type_bar), mBarsAreActivated);
        showToast(mBarsAreActivated, getString(R.string.bars));
    }

    public void onParksButtonClicked(View view) {
        mParksAreActivated = !mParksAreActivated;
        setButtonBackground(
                mParksAreActivated,
                mParksButton,
                R.drawable.activated_button_park);
        setTypesPreference(getString(R.string.preferences_type_park), mParksAreActivated);
        showToast(mParksAreActivated, getString(R.string.parks));
    }

    public void onBookstoresButtonClicked(View view) {
        mBookstoresAreActivated = !mBookstoresAreActivated;
        setButtonBackground(
                mBookstoresAreActivated,
                mBookstoresButton,
                R.drawable.activated_button_bookstore);
        setTypesPreference(getString(R.string.preferences_type_bookstore), mBookstoresAreActivated);
        showToast(mBookstoresAreActivated, getString(R.string.bookstores));
    }

    public void onGalleriesButtonClicked(View view) {
        mGalleriesAreActivated = !mGalleriesAreActivated;
        setButtonBackground(
                mGalleriesAreActivated,
                mGalleriesButton,
                R.drawable.activated_button_gallery);
        setTypesPreference(getString(R.string.preferences_type_gallery), mGalleriesAreActivated);
        showToast(mGalleriesAreActivated, getString(R.string.galleries));
    }

    public void onMoviesButtonClicked(View view) {
        mMoviesAreActivated = !mMoviesAreActivated;
        setButtonBackground(
                mMoviesAreActivated,
                mMoviesButton,
                R.drawable.activated_button_movie);
        setTypesPreference(getString(R.string.preferences_type_movie), mMoviesAreActivated);
        showToast(mMoviesAreActivated, getString(R.string.movies));
    }

    public void onLodgingsButtonClicked(View view) {
        mLodgingsAreActivated = !mLodgingsAreActivated;
        setButtonBackground(
                mLodgingsAreActivated,
                mLodgingsButton,
                R.drawable.activated_button_lodging);
        setTypesPreference(getString(R.string.preferences_type_lodging), mLodgingsAreActivated);
        showToast(mLodgingsAreActivated, getString(R.string.lodgings));
    }

    public void onGroceriesButtonClicked(View view) {
        mGroceriesAreActivated = !mGroceriesAreActivated;
        setButtonBackground(
                mGroceriesAreActivated,
                mGroceriesButton,
                R.drawable.activated_button_grocery);
        setTypesPreference(getString(R.string.preferences_type_grocery), mGroceriesAreActivated);
        showToast(mGroceriesAreActivated, getString(R.string.groceries));
    }

    public void onAtmsButtonClicked(View view) {
        mAtmsAreActivated = !mAtmsAreActivated;
        setButtonBackground(
                mAtmsAreActivated,
                mAtmsButton,
                R.drawable.activated_button_atm);
        setTypesPreference(getString(R.string.preferences_type_atm), mAtmsAreActivated);
        showToast(mAtmsAreActivated, getString(R.string.atms));
    }

    public void onPharmaciesButtonClicked(View view) {
        mPharmaciesAreActivated = !mPharmaciesAreActivated;
        setButtonBackground(
                mPharmaciesAreActivated,
                mPharmaciesButton,
                R.drawable.activated_button_pharmacy);
        setTypesPreference(getString(R.string.preferences_type_pharmacy), mPharmaciesAreActivated);
        showToast(mPharmaciesAreActivated, getString(R.string.pharmacies));
    }

    public void onTransitButtonClicked(View view) {
        mTransitIsActivated = !mTransitIsActivated;
        setButtonBackground(
                mTransitIsActivated,
                mTransitButton,
                R.drawable.activated_button_transit);
        setTypesPreference(getString(R.string.preferences_type_transit), mTransitIsActivated);
        showToast(mTransitIsActivated, getString(R.string.transit));
    }
}
