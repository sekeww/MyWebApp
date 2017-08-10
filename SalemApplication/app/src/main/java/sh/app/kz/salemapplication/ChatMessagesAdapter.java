package sh.app.kz.salemapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by askhat on 19.06.16.
 */
public class ChatMessagesAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> messageList;
    private LayoutInflater inflater;

    public ChatMessagesAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.row_message_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.messageTextView.setText(messageList.get(i).getText());
        Glide.with(context).load(messageList.get(i).getImage()).centerCrop().into(viewHolder.imageView);

        if (messageList.get(i).getType() == ChatMessage.ChatMessageType.RECIEVED) {
            viewHolder.layout.removeAllViews();
            viewHolder.layout.addView(viewHolder.imageView);
            viewHolder.layout.addView(viewHolder.messageTextView);
            viewHolder.messageTextView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        } else {
            viewHolder.layout.removeAllViews();
            viewHolder.layout.addView(viewHolder.messageTextView);
            viewHolder.layout.addView(viewHolder.imageView);
            viewHolder.messageTextView.setBackgroundColor(Color.parseColor("#32CD32"));
            viewHolder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        return view;
    }

    private class ViewHolder {
        LinearLayout layout;
        TextView messageTextView;
        ImageView imageView;

        public ViewHolder(View v) {
            layout = (LinearLayout) v.findViewById(R.id.layout);
            messageTextView = (TextView) v.findViewById(R.id.messageTextView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
    }
}
