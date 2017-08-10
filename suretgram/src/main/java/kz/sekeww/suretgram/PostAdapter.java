package kz.sekeww.suretgram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Askhat on 6/16/2016.
 */

public class PostAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<Post> posts;
    private Context context;
    private LayoutInflater inflater;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_header_item,parent,false);
            viewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HeaderViewHolder) convertView.getTag();
        }

        viewHolder.usernameTextView.setText(posts.get(position).getUser().getProperty("name").toString());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return posts.size();
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

        ItemViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_post_item,parent,false);
            viewHolder = new ItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        viewHolder.messageTextView.setText(posts.get(position).getMessage());

        Glide.with(context).load(posts.get(position).getFile()).centerCrop().into(viewHolder.postImageView);


        return convertView;
    }

    private class ItemViewHolder {
        TextView messageTextView;
        ImageView postImageView;

        public ItemViewHolder(View v){
            messageTextView = (TextView) v.findViewById(R.id.messageTextView);
            postImageView = (ImageView) v.findViewById(R.id.postImageView);
        }
    }

    private class HeaderViewHolder {
        TextView usernameTextView;

        public HeaderViewHolder(View v){
            usernameTextView = (TextView) v.findViewById(R.id.usernameTextView);
        }
    }
}
