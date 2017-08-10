package kz.sekeww.findgooglebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Askhat on 6/10/2016.
 */

public class BooksAdapter extends BaseAdapter{

    private static final String TAG = "ADAPTERACTIVITY";
    Context context;
    JSONArray books;
    LayoutInflater inflater;
    ImageLoader myImageLoader;
    RequestQueue myRequestQueue;

    public BooksAdapter(Context context, JSONArray books) {
        this.context = context;
        this.books = books;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myRequestQueue = Volley.newRequestQueue(context);

        myImageLoader = new ImageLoader(myRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String,Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return books.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_book_item,null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolder.subtitleTextView = (TextView) convertView.findViewById(R.id.subtitleTextView);
            viewHolder.coverNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.coverNetworkImageView);
            viewHolder.authorTextView = (TextView) convertView.findViewById(R.id.authorTextView);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject book = books.getJSONObject(position);
            JSONObject volumeInfo = book.getJSONObject("volumeInfo");

            String title = volumeInfo.getString("title");
            String subtitle = volumeInfo.getString("subtitle");

            viewHolder.titleTextView.setText(title);
            viewHolder.subtitleTextView.setText(subtitle);

            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            String thumbnailString = imageLinks.getString("thumbnail");
            Log.d(TAG,"image is " + thumbnailString);

            viewHolder.coverNetworkImageView.setImageUrl(thumbnailString, myImageLoader);

            JSONArray authors = volumeInfo.getJSONArray("authors");

            viewHolder.authorTextView.setText(authors.toString().replace("[","").replace("]","").replace("\"",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
    private class ViewHolder{
        TextView titleTextView;
        TextView subtitleTextView;
        NetworkImageView coverNetworkImageView;
        TextView authorTextView;
    }
}
