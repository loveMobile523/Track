package offline.mapbox.trackerapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.ImageRecyclerModel;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageHolder>{

    Context mContext;
    ArrayList<ImageRecyclerModel> itemData = new ArrayList<>();

    public ImageRecyclerAdapter(Context mContext, ArrayList<ImageRecyclerModel> arrayData) {
        super();
        this.mContext = mContext;
        this.itemData = arrayData;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_recycler_image, parent, false);
        ImageHolder viewHolder = new ImageHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        String imageURL = itemData.get(position).getImageURL();

        Bitmap imageBitmap = BitmapFactory.decodeFile(imageURL);

//        holder.imgContent.setImageResource(MainActivity.images[position]);
        holder.imgContent.setImageBitmap(imageBitmap);
    }

    @Override
    public int getItemCount() {
        return this.itemData.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imgContent;
        public ImageView imgHint;

        public ImageHolder(View itemView) {
            super(itemView);
            imgContent = (ImageView) itemView.findViewById(R.id.imgContent);
            imgHint = (ImageView) itemView.findViewById(R.id.imgHint);
        }
    }
}
