package sh.app.kz.salemapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by askhat on 19.06.16.
 */
public class FriendsAdapter extends BaseAdapter {

    private ArrayList<BackendlessUser> friends;
    private ArrayList<String> fatFriendsId;
    private Context context;
    private LayoutInflater inflater;

    public FriendsAdapter(Context context, ArrayList<BackendlessUser> friends) {
        this.context = context;
        this.friends = friends;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void OnNewMessages(ArrayList<String> sendersId) {

        //fatFriends = senders;
        fatFriendsId = sendersId;


        //sort
//        for(String friend : fatFriendsId)
//        {
//            if(friends.contains(friend))
//            friends.remove(Backendless.UserService.findById(friend));
//        }
//
//        //insert them again
//        for (int i = 0; i < fatFriendsId.size(); i++)
//        {
//            friends.add(i, Backendless.UserService.findById(fatFriendsId.get(i)));
//        }

        //notify
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.row_friend_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nameTextView.setText((String) friends.get(i).getProperty("name"));


        Glide.with(context).load(friends.get(i).getProperty("profileImageURL")).centerCrop().into(viewHolder.imageView);

        if (fatFriendsId != null) {
            for (String fatFriend : fatFriendsId) {
                if (fatFriend.equals(friends.get(i).getObjectId())) {
                    viewHolder.nameTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    viewHolder.nameTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        }


        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public ViewHolder(View v) {

            imageView = (ImageView) v.findViewById(R.id.imageView);
            nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        }
    }
}
