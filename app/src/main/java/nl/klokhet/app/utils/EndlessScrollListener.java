package nl.klokhet.app.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotal;
    private boolean mLoading = true;
    private int mVisibleThreshold;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mNeedToLoadMore = true;
    private boolean mEnabled = true;

    private EndlessScrollListener(@NonNull RecyclerView recyclerView, int visibleThreshold) {
        this.mRecyclerView = recyclerView;
        this.mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mVisibleThreshold = visibleThreshold;
        recyclerView.addOnScrollListener(this);
    }

    public static EndlessScrollListener create(@NonNull RecyclerView recyclerView, int visibleThreshold) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        AssertionUtils.assertInstanceOf(manager, LinearLayoutManager.class, "Layout manager");
        return new EndlessScrollListener(recyclerView, visibleThreshold);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);
        if (!mEnabled) {
            return;
        }

        int visibleItemCount = this.mRecyclerView.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (mLoading && mNeedToLoadMore && totalItemCount > mPreviousTotal) {
            mLoading = false;
            mPreviousTotal = totalItemCount;
        }
        if (!mLoading && mNeedToLoadMore && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            // End has been reached
            if (mLoadMoreListener != null) {
                mLoadMoreListener.loadMore();
                mNeedToLoadMore = false;
            }
            mLoading = true;
        }

    }

    public void updateNeedToLoad(final boolean needToLoadMore) {
        mNeedToLoadMore = needToLoadMore;
    }

    public void reset() {
        mLoading = false;
        mNeedToLoadMore = true;
        mPreviousTotal = 0;
    }

    public void enable() {
        mEnabled = true;
    }

    public void disable() {
        mEnabled = false;
    }

    public EndlessScrollListener onLoadMoreListener(@Nullable OnLoadMoreListener onLoadMoreListener) {
        this.mLoadMoreListener = onLoadMoreListener;
        return this;
    }

    @FunctionalInterface
    public interface OnLoadMoreListener {
        void loadMore();
    }
}
