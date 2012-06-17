package com.siu.android.tennisparis.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.app.activity.TennisDetailActivity;
import com.siu.android.tennisparis.dao.model.Tennis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisOverlay extends BalloonItemizedOverlay<TennisOverlayItem> {

    private List<TennisOverlayItem> overlayItems = new ArrayList<TennisOverlayItem>();

    public TennisOverlay(Drawable defaultMarker, MapView mapView) {
        super(boundCenterBottom(defaultMarker), mapView);

        // bug fix, see this : http://stackoverflow.com/questions/3755921/problem-with-crash-with-itemizedoverlay
        populate();
    }

    @Override
    protected TennisOverlayItem createItem(int i) {
        return overlayItems.get(i);
    }

    @Override
    public int size() {
        return overlayItems.size();
    }

    @Override
    protected BalloonOverlayView<TennisOverlayItem> createBalloonOverlayView() {
        return new BalloonOverlayView<TennisOverlayItem>(getMapView().getContext(), getBalloonBottomOffset()) {
            @Override
            protected void setupView(Context context, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.tennis_overlay_balloon, parent);
                title = (TextView) v.findViewById(R.id.balloon_item_title);
                snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
            }
        };
    }

    @Override
    protected boolean onBalloonTap(int index, TennisOverlayItem tennisOverlayItem) {
        Intent intent = new Intent(getMapView().getContext(), TennisDetailActivity.class);
        intent.putExtra(TennisDetailActivity.EXTRA_TENNIS, currentFocusedItem.getTennis());
        getMapView().getContext().startActivity(intent);
        return true;
    }

    @Override
    protected void onBalloonOpen(int index) {
        overlayItems.remove(index);
        populate();
    }

    @Override
    protected void onBalloonClose() {
        addOverlayItem(currentFocusedItem);
    }

    public void addOverlayItem(TennisOverlayItem overlayItem) {
        overlayItems.add(overlayItem);
        populate();
    }

    public void addTennises(List<Tennis> tennises) {
        for (Iterator<Tennis> it = tennises.iterator(); it.hasNext(); ) {
            overlayItems.add(new TennisOverlayItem(it.next()));
        }

        populate();
    }

    public List<TennisOverlayItem> getOverlayItems() {
        return overlayItems;
    }

    public static Drawable boundBottomLeft(Drawable drawable) {
        drawable.setBounds(0, -drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth(), 0);
        return drawable;
    }
}
