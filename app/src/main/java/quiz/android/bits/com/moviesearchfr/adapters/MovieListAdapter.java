package quiz.android.bits.com.moviesearchfr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import quiz.android.bits.com.moviesearchfr.models.MovieItem;
import quiz.android.bits.com.moviesearchfr.R;
import quiz.android.bits.com.moviesearchfr.utils.Utils;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    private Context context;
    private Utils utils;
    private String[] names;

    private List<MovieItem> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, plot, cast;
        public ImageView thumbnail;
        public RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            cast = view.findViewById(R.id.cast);
            plot = view.findViewById(R.id.plot);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public MovieListAdapter(Context context, List<MovieItem> movieList) {
        this.context = context;
        this.movieList = movieList;
        utils = new Utils();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final MovieItem movieItem = movieList.get(position);
        holder.title.setText(movieItem.getTitle());
        holder.cast.setText(movieItem.getCast());
        holder.plot.setText(movieItem.getPlot());
        holder.thumbnail.refreshDrawableState();

        if (movieItem.getThumbnail().contains("content://")) {
            //holder.thumbnail.setImageURI(Uri.parse(movieItem.getThumbnail()));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(movieItem.getThumbnail()));
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                holder.thumbnail.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                holder.thumbnail.setBackground(Drawable.createFromStream(context.getAssets().open(movieItem.getThumbnail()), null));
            } catch (IOException e) {
                Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void removeItem(int position) {
        movieList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(MovieItem movieItem, int position) {
        movieList.add(position, movieItem);
        // notify movieItem added by position
        notifyItemInserted(position);
    }

}
