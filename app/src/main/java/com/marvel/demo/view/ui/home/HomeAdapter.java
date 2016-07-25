package com.marvel.demo.view.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marvel.demo.R;
import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.helper.ViewHelper;
import com.marvel.demo.view.ui.widgets.BaseRecyclerAdapter;

import butterknife.BindView;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class HomeAdapter
        extends BaseRecyclerAdapter<HomeAdapter.CategoryViewHolder, Comic> {


    private OnCallback mCallback;

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflate(parent, R.layout.item_comic);
        return new CategoryViewHolder(view);
    }

    protected void setAnimation(View viewToAnimate, int position) {

    }

    public class CategoryViewHolder extends BaseRecyclerAdapter.BaseViewHolder<Comic>
            implements View.OnClickListener {

        @BindView(R.id.textview_title)
        TextView mTextViewTitle;

        @BindView(R.id.imageview_comic)
        ImageView mImageViewComic;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void populate(Comic item, int position) {

            Context context = mImageViewComic.getContext();
            mTextViewTitle.setText(item.title);

            Glide.with(context)
                    .load(item.getURLThumbnail())
                    .centerCrop()
                    .into(mImageViewComic);

        }

        @Override
        public void onClick(View v) {

            ViewHelper.blockViewShortTime(v);
            if (mCallback == null) return;

            Comic comic = getItem(getAdapterPosition());
            mCallback.onComicSelected(comic, mTextViewTitle);
        }
    }

    public void setCallback(OnCallback callback) {
        this.mCallback = callback;
    }

    public interface OnCallback {

        void onComicSelected(Comic comic, View viewAnimation);

    }

}
