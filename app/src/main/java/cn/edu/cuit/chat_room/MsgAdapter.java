package cn.edu.cuit.chat_room;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<ProtoMsg.Msg> mMsglist;

    MsgAdapter(List<ProtoMsg.Msg> msgList) {
        mMsglist = msgList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProtoMsg.Msg msg = mMsglist.get(position);

        if (msg.getUserInfo().getName().equals(ChatRoomActivity.name)) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            holder.me.setText(msg.getUserInfo().getName());
        } else{
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.friend.setText(msg.getUserInfo().getName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        private TextView friend;

        private TextView me;

        ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);

            friend = view.findViewById(R.id.friendName);
            me = view.findViewById(R.id.myName);
        }
    }

    @Override
    public int getItemCount() {
        return mMsglist.size();
    }
}