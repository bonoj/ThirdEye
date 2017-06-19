package com.hk47.realityoverlay.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.CustomerReview;
import com.hk47.realityoverlay.data.PlaceDetails;
import com.hk47.realityoverlay.utils.PlaceDetailsUtilities;
import com.hk47.realityoverlay.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceDetailsActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private LinearLayout mDetailsContainer;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mPhotoImageView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mContactTextView;
    private TextView mHoursTextView;
    private ImageView mHoursExpander;
    private boolean mIsExpanded;
    private TextView mExpandedHoursTextView;
    private ImageView mStar1ImageView;
    private ImageView mStar2ImageView;
    private ImageView mStar3ImageView;
    private ImageView mStar4ImageView;
    private ImageView mStar5ImageView;
    private TextView mReviewsTextView;
    private View mReviewsDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        mProgressBar = (ProgressBar) findViewById(R.id.details_loading_indicator);
        mErrorTextView = (TextView) findViewById(R.id.details_error_text_view);
        mDetailsContainer = (LinearLayout) findViewById(R.id.details_container);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        mPhotoImageView = (ImageView) findViewById(R.id.details_photo_image_view);
        mNameTextView = (TextView) findViewById(R.id.details_name_text_view);
        mAddressTextView = (TextView) findViewById(R.id.details_address_text_view);
        mContactTextView = (TextView) findViewById(R.id.details_contact_text_view);
        mHoursTextView = (TextView) findViewById(R.id.details_hours_text_view);
        mHoursExpander = (ImageView) findViewById(R.id.details_hours_expander);
        mExpandedHoursTextView = (TextView) findViewById(R.id.details_expanded_hours_text_view);
        mStar1ImageView = (ImageView) findViewById(R.id.details_star_1);
        mStar2ImageView = (ImageView) findViewById(R.id.details_star_2);
        mStar3ImageView = (ImageView) findViewById(R.id.details_star_3);
        mStar4ImageView = (ImageView) findViewById(R.id.details_star_4);
        mStar5ImageView = (ImageView) findViewById(R.id.details_star_5);
        mReviewsTextView = (TextView) findViewById(R.id.details_reviews_text_view);
        mReviewsDivider = findViewById(R.id.details_reviews_divider);

        Intent detailsIntent = getIntent();
        if (detailsIntent != null) {
            String place_id = detailsIntent.getStringExtra(Constants.DETAILS_INTENT_PLACE_ID);
            int icon_id = detailsIntent.getIntExtra(Constants.DETAILS_INTENT_ICON_ID, 0);

            getDetails(place_id, icon_id);
        }
    }

    private void getDetails(final String place_id, final int icon_id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                PlaceDetailsUtilities.getDetailsUrlString(place_id),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        PlaceDetails placeDetails = PlaceDetailsUtilities.processDetailsJson(response);
                        placeDetails.setIcon_id(icon_id);

                        // Set header
                        String restaurantName = placeDetails.getName();
                        mNameTextView.setText(restaurantName);
                        setHeaderColors(getApplicationContext(), icon_id);

                        // Set photo
                        String photo_reference = placeDetails.getPhoto_reference();
                        if (photo_reference != null) {
                            Glide.with(getApplicationContext())
                                    .load(PlaceDetailsUtilities.getPhotoUrlString(photo_reference))
                                    .centerCrop()
                                    .into(mPhotoImageView);
                        } else {
                            setPlaceholderImage(icon_id);
                            int placeholderDimension =
                                    (int) getResources().getDimension(R.dimen.standard_icon);
                            mPhotoImageView.requestLayout();
                            mPhotoImageView.getLayoutParams().width = placeholderDimension;
                            mPhotoImageView.getLayoutParams().height = placeholderDimension;
                        }

                        // Set address
                        String formatted_address = placeDetails.getFormatted_address();
                        if (formatted_address.equals("")) {
                            formatted_address = getString(R.string.address_unavailable);
                        }
                        mAddressTextView.setText(formatted_address);

                        // Set contact information
                        String formatted_phone_number = placeDetails.getFormatted_phone_number();
                        if (formatted_phone_number.equals("")) {
                            formatted_phone_number = getString(R.string.contact_unavailable);
                        }
                        mContactTextView.setText(formatted_phone_number);
                        String international_phone_number = placeDetails.getInternational_phone_number();
                        if (!international_phone_number.equals("")) {
                            setContactClickable(placeDetails.getInternational_phone_number());
                        }

                        // Set hours of operation
                        boolean open_now = placeDetails.getOpen_now();
                        mHoursTextView.setText(getOpenStatusString(open_now));
                        String[] weekday_text = placeDetails.getWeekday_text();
                        if (weekday_text != null) {
                            StringBuilder hoursBuilder = new StringBuilder();
                            for (int i = 0; i < weekday_text.length; i++) {
                                hoursBuilder.append(weekday_text[i]);
                                hoursBuilder.append("\n");
                            }
                            String weekday_text_string = hoursBuilder.toString();
                            setExpanderClickable(weekday_text_string);
                        } else {
                            LinearLayout hoursContainer = (LinearLayout) findViewById(R.id.details_hours_container);
                            hoursContainer.setVisibility(View.GONE);
                        }

                        // Set rating
                        double rating = placeDetails.getRating();
                        if (rating == 0.0 &&
                                formatted_address.equals(getString(R.string.address_unavailable))) {
                            LinearLayout ratingContainer = (LinearLayout) findViewById(R.id.details_rating_container);
                            ratingContainer.setVisibility(View.GONE);
                        } else {
                            setRatingStars(rating);
                        }

                        // Set reviews
                        ArrayList<CustomerReview> customerReviews = placeDetails.getCustomerReviews();
                        if (customerReviews != null) {
                            if (customerReviews.size() > 0) {
                                mReviewsDivider.setVisibility(View.VISIBLE);
                                mReviewsTextView.setText(
                                        PlaceDetailsUtilities.getReviewsText(
                                                getApplicationContext(),
                                                restaurantName,
                                                customerReviews));
                            }
                        }
                        showDetailsContainer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError ||
                                error instanceof NetworkError ||
                                error instanceof TimeoutError) {
                            mErrorTextView.setText(getString(R.string.connection_unavailable));
                        } else {
                            mErrorTextView.setText(getString(R.string.service_unavailable));
                        }
                        showErrorTextView();
                    }
                }
        );

        // Get details for this place_id from Google Places API
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void showDetailsContainer() {
        mProgressBar.setVisibility(View.GONE);
        mAppBarLayout.setVisibility(View.VISIBLE);
        mDetailsContainer.setVisibility(View.VISIBLE);
    }

    private void showErrorTextView() {
        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void setHeaderColors(Context context, int icon_id) {
        int backgroundColor;
        int statusBarColor;
        switch (icon_id) {
            case Constants.CAFE_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.cafeBrown);
                statusBarColor = ContextCompat.getColor(context, R.color.cafeStatusBar);
                break;
            case Constants.RESTAURANT_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.restaurantRed);
                statusBarColor = ContextCompat.getColor(context, R.color.restaurantStatusBar);
                break;
            case Constants.BAR_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.barGreen);
                statusBarColor = ContextCompat.getColor(context, R.color.barStatusBar);
                break;
            case Constants.PARK_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.parkGreen);
                statusBarColor = ContextCompat.getColor(context, R.color.parkStatusBar);
                break;
            case Constants.BOOKSTORE_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.bookstoreBlue);
                statusBarColor = ContextCompat.getColor(context, R.color.bookstoreStatusBar);
                break;
            case Constants.GALLERY_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.galleryPurple);
                statusBarColor = ContextCompat.getColor(context, R.color.galleryStatusBar);
                break;
            case Constants.MOVIE_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.moviePurple);
                statusBarColor = ContextCompat.getColor(context, R.color.movieStatusBar);
                break;
            case Constants.LODGING_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.lodgingOrange);
                statusBarColor = ContextCompat.getColor(context, R.color.lodgingStatusBar);
                break;
            case Constants.GROCERY_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.groceryGreen);
                statusBarColor = ContextCompat.getColor(context, R.color.groceryStatusBar);
                break;
            case Constants.ATM_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.atmGold);
                statusBarColor = ContextCompat.getColor(context, R.color.atmStatusBar);
                break;
            case Constants.PHARMACY_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.pharmacyRed);
                statusBarColor = ContextCompat.getColor(context, R.color.pharmacyStatusBar);
                break;
            case Constants.TRANSIT_ID:
                backgroundColor = ContextCompat.getColor(context, R.color.transitBlue);
                statusBarColor = ContextCompat.getColor(context, R.color.transitStatusBar);
                break;
            default:
                backgroundColor = ContextCompat.getColor(context, R.color.defaultBlack);
                statusBarColor = ContextCompat.getColor(context, R.color.defaultStatusBar);
                break;
        }
        mCollapsingToolbarLayout.setBackgroundColor(backgroundColor);
        mCollapsingToolbarLayout.setContentScrimColor(backgroundColor);
        mNameTextView.setBackgroundColor(backgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }
    }

    private void setPlaceholderImage(int icon_id) {
        switch (icon_id) {
            case Constants.CAFE_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_local_cafe_black_48dp);
                break;
            case Constants.RESTAURANT_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_restaurant_black_48dp);
                break;
            case Constants.BAR_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_local_bar_black_48dp);
                break;
            case Constants.PARK_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_nature_people_black_48dp);
                break;
            case Constants.BOOKSTORE_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_import_contacts_black_48dp);
                break;
            case Constants.GALLERY_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_palette_black_48dp);
                break;
            case Constants.MOVIE_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_local_movies_black_48dp);
                break;
            case Constants.LODGING_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_hotel_black_48dp);
                break;
            case Constants.GROCERY_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_shopping_cart_black_48dp);
                break;
            case Constants.ATM_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_local_atm_black_48dp);
                break;
            case Constants.PHARMACY_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_local_pharmacy_black_48dp);
                break;
            case Constants.TRANSIT_ID:
                mPhotoImageView.setImageResource(R.drawable.ic_directions_bus_black_48dp);
                break;
            default:
                mPhotoImageView.setImageResource(R.drawable.ic_location_on_black_48dp);
                break;
        }
    }

    private void setContactClickable(final String international_phone_number) {
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        mContactTextView.setBackgroundResource(outValue.resourceId);
        mContactTextView.setTextColor(Color.BLUE);
        mContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + international_phone_number));
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(callIntent);
                }
            }
        });
    }

    private String getOpenStatusString(boolean open_now) {
        String openStatus;
        if (open_now) {
            openStatus = getString(R.string.open_now);
        } else {
            openStatus = getString(R.string.closed);
        }
        return openStatus;
    }

    private void setExpanderClickable(final String weekday_text_string) {
        mIsExpanded = false;
        mExpandedHoursTextView.setText(weekday_text_string);
        mHoursExpander.setVisibility(View.VISIBLE);
        mHoursExpander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsExpanded = !mIsExpanded;
                if (mIsExpanded) {
                    mExpandedHoursTextView.setVisibility(View.VISIBLE);
                    mHoursExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    mExpandedHoursTextView.setVisibility(View.GONE);
                    mHoursExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
    }

    private void setRatingStars(double rating) {
        if (rating >= 0.5 && rating < 1) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_half_black_24dp);
        } else if (rating >= 1 && rating < 1.5) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
        } else if (rating >= 1.5 && rating < 2) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_half_black_24dp);
        } else if (rating >= 2 && rating < 2.5) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
        } else if (rating >= 2.5 && rating < 3) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_half_black_24dp);
        } else if (rating >= 3 && rating < 3.5) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
        } else if (rating >= 3.5 && rating < 4) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar4ImageView.setImageResource(R.drawable.ic_star_half_black_24dp);
        } else if (rating >= 4 && rating < 4.5) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar4ImageView.setImageResource(R.drawable.ic_star_black_24dp);
        } else if (rating >= 4 && rating < 4.5) {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar4ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar5ImageView.setImageResource(R.drawable.ic_star_half_black_24dp);
        } else {
            mStar1ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar4ImageView.setImageResource(R.drawable.ic_star_black_24dp);
            mStar5ImageView.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getIntent().getFlags() > 0) {
            Intent overlayIntent = new Intent(this, OverlayActivity.class);
            overlayIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(overlayIntent);
        }
    }
}