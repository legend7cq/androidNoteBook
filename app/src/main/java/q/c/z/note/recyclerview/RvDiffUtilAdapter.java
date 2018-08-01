package q.c.z.note.recyclerview;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import q.c.z.note.R;

/**
 * 2018年6月15日09:54:40  zcq
 */
public class RvDiffUtilAdapter extends RecyclerView.Adapter<RvDiffUtilAdapter.singHolder> {

    private List<SongInfoBean> adapterData;
    private OnclickListener mListener;

    public RvDiffUtilAdapter(List<SongInfoBean> data) {
        adapterData = new ArrayList<>();
        load(data, false);
    }

    public List<SongInfoBean> getAdapterData() {
        return adapterData;
    }

    //isRefresh是否刷新
    public void load(List<SongInfoBean> data, boolean isRefresh) {
        List<SongInfoBean> temp = new ArrayList<>(adapterData);//旧数据
        if (isRefresh) {
            adapterData.clear();
        }
        adapterData.addAll(data);

//        notifyDataSetChanged();
        //暂时不知咋修改动画0.0
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(temp, adapterData), true);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public singHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cell ,parent,false);
        return new singHolder(itemView);
    }

    @Override
    public void onBindViewHolder(singHolder holder, final int position) {
        SongInfoBean songInfoBean = adapterData.get(position);
        holder.tvName.setText(songInfoBean.getName());
        holder.mProgress.setText(songInfoBean.getProgress());
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onclick(v, position);
                }
            });
        }

    }

    //有数据变化的，如果不需要定向刷新则不需要重写该方法
    @Override
    public void onBindViewHolder(singHolder holder, final int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //有数据变化
            Bundle payload = (Bundle) payloads.get(0);
            SongInfoBean bean = adapterData.get(position);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "KEY_DESC":
//                        这里可以用payload里的数据，不过data也是新的 也可以用
                        holder.mProgress.setText(bean.getProgress());
                        break;
//                    case "KEY_PIC":
//                        holder.iv.setImageResource(payload.getInt(key));
//                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    public void setOnclick(OnclickListener listener) {
        mListener = listener;
    }


    public interface OnclickListener {
        void onclick(View view, int postion);
    }

    //数据比较进行刷新
    public static class DiffCallBack extends DiffUtil.Callback {
        private List<SongInfoBean> mOldDatas, mNewDatas;//看名字

        public DiffCallBack(List<SongInfoBean> mOldDatas, List<SongInfoBean> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        //老数据集size
        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        //新数据集size
        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        //判断 两个对象是否是相同的Item。如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldDatas.get(oldItemPosition).getProgress() == mNewDatas.get(newItemPosition).getProgress();
        }

        //判断相同的item是否改变了其中一些字段值数据，这个方法仅仅在areItemsTheSame()返回true时，才调用。
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            SongInfoBean beanOld = mOldDatas.get(oldItemPosition);
            SongInfoBean beanNew = mNewDatas.get(newItemPosition);
//            if (!beanOld.getMusicName().equals(beanNew.getMusicName())) {
//                return false;//如果有内容不同，就返回false
//            }
            if (beanOld.getProgress() != beanNew.getProgress()) {
                return false;//如果有内容不同，就返回false
            }
            return true; //默认两个data内容是相同的
        }

        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // 定向刷新中的部分更新，只修改item改变的数据字段，如果不重写这个方法则一个字段改变会刷新整个item内容
            // 效率最高。。。。。一般不用吧。。。。如果重写这个，则要重写onBindViewHolder三个参数的配合使用。。。
            // 那么定向刷新只需要多重写该方法和onBindViewHolder的三个参数的方法。
            SongInfoBean oldBean = mOldDatas.get(oldItemPosition);
            SongInfoBean newBean = mNewDatas.get(newItemPosition);

            //这里比较item的数据，变化的则保存
            Bundle payload = new Bundle();
            if (oldBean.getProgress() != newBean.getProgress()) {
                payload.putFloat("KEY_DESC", newBean.getProgress());
            }

//            if (oldBean.getPic() != newBean.getPic()) {
//                payload.putInt("KEY_PIC", newBean.getPic());
//            }

            if (payload.size() == 0)//如果没有变化 就传空
                return null;
            return payload;//
        }
    }

    public static class singHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView mProgress;

        public singHolder(View itemView) {
            super(itemView);
            mProgress = itemView.findViewById(R.id.tv_progress);
            tvName = itemView.findViewById(R.id.tv_cell_name);
        }
    }

    private class SongInfoBean {
        private int progress = 0;
        private String name;

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
