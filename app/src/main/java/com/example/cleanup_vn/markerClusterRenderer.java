package com.example.cleanup_vn;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class markerClusterRenderer extends DefaultClusterRenderer {

    private static final int MARKER_DIMENSION = 70;
    private final IconGenerator iconGenerator;
    private final ImageView markerImageView;

    public markerClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);

        iconGenerator = new IconGenerator(context);
        markerImageView = new ImageView(context);
        markerImageView.setLayoutParams(new ViewGroup.LayoutParams(MARKER_DIMENSION,MARKER_DIMENSION));

        iconGenerator.setContentView(markerImageView);

    }


    @Override
    protected void onBeforeClusterItemRendered(ClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerImageView.setImageResource((R.drawable.trash_can));

        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon((BitmapDescriptorFactory.fromBitmap(icon)));

        markerOptions.title(item.getTitle());



    }
}

