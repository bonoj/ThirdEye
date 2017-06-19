package com.hk47.realityoverlay.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.NearbyPlace;

import java.util.ArrayList;

public class OverlayDisplayView extends View {

    public interface OnPlaceSelectedListener {
        public void onPlaceSelected(String place_id, int icon_id);
    }

    public OverlayDisplayView(Context context) {
        super(context);

        try {
            mCallback = (OnPlaceSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnPlaceSelectedListener!");
        }

        this.mRestaurantRed = ContextCompat.getColor(context, R.color.restaurantRed);
        this.mCafeBrown = ContextCompat.getColor(context, R.color.cafeBrown);
        this.mBarGreen = ContextCompat.getColor(context, R.color.barGreen);
        this.mParkGreen = ContextCompat.getColor(context, R.color.parkGreen);
        this.mBookstoreBlue = ContextCompat.getColor(context, R.color.bookstoreBlue);
        this.mGalleryPurple = ContextCompat.getColor(context, R.color.galleryPurple);
        this.mMoviePurple = ContextCompat.getColor(context, R.color.moviePurple);
        this.mLodgingOrange = ContextCompat.getColor(context, R.color.lodgingOrange);
        this.mGroceryGreen = ContextCompat.getColor(context, R.color.groceryGreen);
        this.mAtmGold = ContextCompat.getColor(context, R.color.atmGold);
        this.mPharmacyRed = ContextCompat.getColor(context, R.color.pharmacyRed);
        this.mTransitBlue = ContextCompat.getColor(context, R.color.transitBlue);
    }

    private OnPlaceSelectedListener mCallback;

    private float mViewportHeight;
    private float mViewportWidth;
    private boolean mGotViewports;
    private boolean mSetPaints;
    private PointF mTouchPoint;

    private float mVerticalFOV;
    private float mHorizontalFOV;
    private float mAzimuth;
    private float mPitch;
    private float mRoll;

    private ArrayList<NearbyPlace> mNearbyPlaces;
    private ArrayList<NearbyPlace> mQuad1Places = new ArrayList<>();
    private ArrayList<NearbyPlace> mQuad2Places = new ArrayList<>();
    private ArrayList<NearbyPlace> mQuad3Places = new ArrayList<>();
    private ArrayList<NearbyPlace> mQuad4Places = new ArrayList<>();

    private final Bitmap mDefaultIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_on_black_48dp);
    private final Bitmap mRestaurantIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_restaurant_black_48dp);
    private final Bitmap mCafeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_cafe_black_48dp);
    private final Bitmap mBarIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_bar_black_48dp);
    private final Bitmap mParkIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_nature_people_black_48dp);
    private final Bitmap mBookstoreIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_import_contacts_black_48dp);
    private final Bitmap mGalleryIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_palette_black_48dp);
    private final Bitmap mMovieIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_movies_black_48dp);
    private final Bitmap mLodgingIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hotel_black_48dp);
    private final Bitmap mGroceryIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_shopping_cart_black_48dp);
    private final Bitmap mAtmIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_atm_black_48dp);
    private final Bitmap mPharmacyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_pharmacy_black_48dp);
    private final Bitmap mTransitIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_directions_bus_black_48dp);

    private final int mRestaurantRed;
    private final int mCafeBrown;
    private final int mBarGreen;
    private final int mParkGreen;
    private final int mBookstoreBlue;
    private final int mGalleryPurple;
    private final int mMoviePurple;
    private final int mLodgingOrange;
    private final int mGroceryGreen;
    private final int mAtmGold;
    private final int mPharmacyRed;
    private final int mTransitBlue;

    private final float mMargin = getResources().getDimension(R.dimen.canvas_rectangle_right_margin);
    private final float mCornerRadius = getResources().getDimension(R.dimen.canvas_rectangle_corner_radius);
    private final float mOutline = getResources().getDimension(R.dimen.canvas_rectangle_outline);
    private final float mYstep = getResources().getDimension(R.dimen.canvas_y_step);

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mBubble = new RectF();

    public void setVerticalFOV(float verticalFOV) {
        mVerticalFOV = verticalFOV;
    }

    public void setHorizontalFOV(float horizontalFOV) {
        mHorizontalFOV = horizontalFOV;
    }

    public void setAzimuth(float azimuth) {
        mAzimuth = azimuth;
    }

    public void setPitch(float pitch) {
        mPitch = pitch;
    }

    public void setRoll(float roll) {
        mRoll = roll;
    }

    public void setNearbyPlaces(ArrayList<NearbyPlace> nearbyPlaces) {
        mNearbyPlaces = nearbyPlaces;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchPoint = new PointF(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchPoint = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get the viewports only once
        if (!mGotViewports && mVerticalFOV > 0 && mHorizontalFOV > 0) {
            mViewportHeight = canvas.getHeight() / mVerticalFOV;
            mViewportWidth = canvas.getWidth() / mHorizontalFOV;
            mGotViewports = true;
        }

        if (!mGotViewports) {
            return;
        }

        // Set the paints that remain constant only once
        if (!mSetPaints) {
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.canvas_text_size));
            mTextPaint.setColor(Color.BLACK);
            mOutlinePaint.setStyle(Paint.Style.STROKE);
            mOutlinePaint.setStrokeWidth(mOutline);
            mBubblePaint.setStyle(Paint.Style.FILL);
            mSetPaints = true;
        }

        // Center of view
        float x = canvas.getWidth() / 2;
        float y = canvas.getHeight() / 2;

        /*
        * Uncomment line below to allow rotation of display around the center point
        * based on the roll. However, this "feature" is not very intuitive, and requires
        * locking device orientation to portrait or changes the sensor rotation matrix
        * on device rotation. It's really quite a nightmare.
        */
        // canvas.rotate((0.0f - mRoll), x, y);

        float dy = mPitch * mViewportHeight;

        if (mNearbyPlaces != null) {

            // Iterate backwards to draw more distant places first
            for (int i = mNearbyPlaces.size() - 1; i >= 0; i--) {
                NearbyPlace nearbyPlace = mNearbyPlaces.get(i);

                float xDegreesToTarget = mAzimuth - nearbyPlace.getBearingToPlace();
                float dx = mViewportWidth * xDegreesToTarget;
                float iconX = x - dx;
                float iconY = y - dy;
                nearbyPlace.setIconX(iconX);
                nearbyPlace.setIconY(iconY);

                float angleToTarget = xDegreesToTarget;
                if (xDegreesToTarget < 0) {
                    angleToTarget = 360 + xDegreesToTarget;
                }
                if (angleToTarget >= 0 && angleToTarget < 90) {
                    nearbyPlace.setQuadrant(1);
                    mQuad1Places.add(nearbyPlace);
                } else if (angleToTarget >= 90 && angleToTarget < 180) {
                    nearbyPlace.setQuadrant(2);
                    mQuad2Places.add(nearbyPlace);
                } else if (angleToTarget >= 180 && angleToTarget < 270) {
                    nearbyPlace.setQuadrant(3);
                    mQuad3Places.add(nearbyPlace);
                } else {
                    nearbyPlace.setQuadrant(4);
                    mQuad4Places.add(nearbyPlace);
                }
            }
            drawQuadrant(mQuad1Places, canvas);
            drawQuadrant(mQuad2Places, canvas);
            drawQuadrant(mQuad3Places, canvas);
            drawQuadrant(mQuad4Places, canvas);
        }
    }

    private void drawQuadrant(ArrayList<NearbyPlace> placesQuadrant, Canvas canvas) {
        // Limit the number of places to display per quadrant to prevent clutter
        while(placesQuadrant.size() > 8) {
            placesQuadrant.remove(0);
        }
        // Prepare each nearbyPlace and check for touches within the drawNearbyPlace() method
        for (int j = 0; j < placesQuadrant.size(); j++) {
            NearbyPlace nearbyPlace = placesQuadrant.get(j);
            int icon_id = nearbyPlace.getIcon_id();
            Bitmap icon = getIcon(icon_id);

            float iconY = nearbyPlace.getIconY();
            iconY += j * mYstep;
            drawNearbyPlace(
                    nearbyPlace.getIconX(),
                    iconY,
                    canvas,
                    icon,
                    icon_id,
                    nearbyPlace.getName(),
                    nearbyPlace.getDistanceToPlace(),
                    nearbyPlace.getPlace_id());
        }
        placesQuadrant.clear();
    }

    private void drawNearbyPlace(float iconX,
                                 float iconY,
                                 Canvas canvas,
                                 Bitmap icon,
                                 int icon_id,
                                 String name,
                                 String distanceToPlace,
                                 String place_id) {
        float textX = iconX + icon.getWidth();
        float nameY = iconY + icon.getHeight() / 2.4f;
        float distanceY = nameY + icon.getHeight() / 2.1f;

        float left = iconX;
        float top = iconY;
        float bottom = top + icon.getHeight();
        float right = left + icon.getWidth() + mTextPaint.measureText(name) + mMargin;
        mBubble.set(left, top, right, bottom);
        int bubbleColor = getBubbleColor(icon_id);
        mTextPaint.setAlpha(128);
        mBubblePaint.setColor(bubbleColor);
        mBubblePaint.setAlpha(64);
        mOutlinePaint.setColor(bubbleColor);
        mOutlinePaint.setAlpha(32);

        canvas.drawRoundRect(mBubble, mCornerRadius, mCornerRadius, mBubblePaint);
        canvas.drawRoundRect(mBubble, mCornerRadius, mCornerRadius, mOutlinePaint);
        canvas.drawBitmap(icon, iconX, iconY, mTextPaint);
        canvas.drawText(name, textX, nameY, mTextPaint);
        canvas.drawText(distanceToPlace, textX, distanceY, mTextPaint);

        // Listen for touches and set place selection callback
        if (mTouchPoint != null) {
            if (mBubble.contains(mTouchPoint.x, mTouchPoint.y)) {
                mTouchPoint = null;
                mBubblePaint.setAlpha(128);
                canvas.drawRoundRect(mBubble, mCornerRadius, mCornerRadius, mBubblePaint);
                canvas.drawBitmap(icon, iconX, iconY, mTextPaint);
                canvas.drawText(name, textX, nameY, mTextPaint);
                canvas.drawText(distanceToPlace, textX, distanceY, mTextPaint);
                mCallback.onPlaceSelected(place_id, icon_id);
            }
        }
    }

    private int getBubbleColor(int icon_id) {
        int color;
        switch (icon_id) {
            case Constants.RESTAURANT_ID:
                color = mRestaurantRed;
                break;
            case Constants.CAFE_ID:
                color = mCafeBrown;
                break;
            case Constants.BAR_ID:
                color = mBarGreen;
                break;
            case Constants.PARK_ID:
                color = mParkGreen;
                break;
            case Constants.BOOKSTORE_ID:
                color = mBookstoreBlue;
                break;
            case Constants.GALLERY_ID:
                color = mGalleryPurple;
                break;
            case Constants.MOVIE_ID:
                color = mMoviePurple;
                break;
            case Constants.LODGING_ID:
                color = mLodgingOrange;
                break;
            case Constants.GROCERY_ID:
                color = mGroceryGreen;
                break;
            case Constants.ATM_ID:
                color = mAtmGold;
                break;
            case Constants.PHARMACY_ID:
                color = mPharmacyRed;
                break;
            case Constants.TRANSIT_ID:
                color = mTransitBlue;
                break;
            default:
                color = Color.BLACK;
                break;
        }
        return color;
    }

    private Bitmap getIcon(int icon_id) {
        Bitmap icon;
        switch (icon_id) {
            case Constants.RESTAURANT_ID:
                icon = mRestaurantIcon;
                break;
            case Constants.CAFE_ID:
                icon = mCafeIcon;
                break;
            case Constants.BAR_ID:
                icon = mBarIcon;
                break;
            case Constants.PARK_ID:
                icon = mParkIcon;
                break;
            case Constants.BOOKSTORE_ID:
                icon = mBookstoreIcon;
                break;
            case Constants.GALLERY_ID:
                icon = mGalleryIcon;
                break;
            case Constants.MOVIE_ID:
                icon = mMovieIcon;
                break;
            case Constants.LODGING_ID:
                icon = mLodgingIcon;
                break;
            case Constants.GROCERY_ID:
                icon = mGroceryIcon;
                break;
            case Constants.ATM_ID:
                icon = mAtmIcon;
                break;
            case Constants.PHARMACY_ID:
                icon = mPharmacyIcon;
                break;
            case Constants.TRANSIT_ID:
                icon = mTransitIcon;
                break;
            default:
                icon = mDefaultIcon;
                break;
        }
        return icon;
    }
}
