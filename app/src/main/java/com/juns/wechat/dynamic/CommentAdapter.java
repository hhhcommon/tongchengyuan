package com.juns.wechat.dynamic;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.bean.CommentBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.utils.SmileUtils;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiajun on 2016/5/14.
 */
public class CommentAdapter extends BaseRecyclerViewAdapter {

    public CommentAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        CommentBean commentBean = (CommentBean) getData(position);
        String user1 = commentBean.getCommentUserName();
        String user2 = commentBean.getReplyUserName();
        CharSequence content = SmileUtils.getInstance().getSmiledText(commentBean.getContent());
        SpannableStringBuilder builder;
        if (TextUtils.isEmpty(user2))
            builder = addClickablePart(user1, content);
        else
            builder = addClickablePart(user1, user2, content);
        if (builder != null) {
            holder.tvComment.setVisibility(View.VISIBLE);
            holder.tvComment.setText(builder, TextView.BufferType.SPANNABLE);
        } else {
            holder.tvComment.setVisibility(View.GONE);
            holder.tvComment.setText("");
        }
        super.setOnItemClickListener(holder, position);

           /* adapter.setOnDefaultClickListener(new OnDefaultClickListener() {
                @Override
                public void onItemClick(int sub) {
                    if (listener != null) {
                        listener.onItemReplyClick(p, sub);
                    }
                }
            });*/
    }

    private SpannableStringBuilder addClickablePart(String cNike, CharSequence content) {
        if (TextUtils.isEmpty(cNike)) {
            return null;
        }
        String str = cNike + ":";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        setSpan(ssb, str.indexOf(cNike), cNike, null);
        return ssb.append(content);
    }

    private SpannableStringBuilder addClickablePart(String rNike, String cNike, CharSequence content) {
        if (TextUtils.isEmpty(rNike) || TextUtils.isEmpty(cNike)) {
            return null;
        }
        String split = "回复";
        String str = rNike + split + cNike + ":";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        setSpan(ssb, str.indexOf(rNike), rNike, null);
        setSpan(ssb, rNike.length() + split.length(), cNike, null);
        return ssb.append(content);
    }

    private void setSpan(SpannableStringBuilder ssb, int start, final String name, final String account) {
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int nikeColoer = mContext.getResources().getColor(R.color.blue);
                ds.setColor(nikeColoer); // 设置文本颜色
                // 去掉下划线
                ds.setUnderlineText(false);
            }
        }, start, start + name.length(), 0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_comment)
        TextView tvComment;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
