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
 * 2018��6��15��09:54:40  zcq
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

    //isRefresh�Ƿ�ˢ��
    public void load(List<SongInfoBean> data, boolean isRefresh) {
        List<SongInfoBean> temp = new ArrayList<>(adapterData);//������
        if (isRefresh) {
            adapterData.clear();
        }
        adapterData.addAll(data);

//        notifyDataSetChanged();
        //��ʱ��֪զ�޸Ķ���0.0
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

    //�����ݱ仯�ģ��������Ҫ����ˢ������Ҫ��д�÷���
    @Override
    public void onBindViewHolder(singHolder holder, final int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //�����ݱ仯
            Bundle payload = (Bundle) payloads.get(0);
            SongInfoBean bean = adapterData.get(position);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "KEY_DESC":
//                        ���������payload������ݣ�����dataҲ���µ� Ҳ������
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

    //���ݱȽϽ���ˢ��
    public static class DiffCallBack extends DiffUtil.Callback {
        private List<SongInfoBean> mOldDatas, mNewDatas;//������

        public DiffCallBack(List<SongInfoBean> mOldDatas, List<SongInfoBean> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        //�����ݼ�size
        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        //�����ݼ�size
        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        //�ж� ���������Ƿ�����ͬ��Item��������Item��Ψһ��id�ֶΣ���������� �ж�id�Ƿ���ȡ�
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldDatas.get(oldItemPosition).getProgress() == mNewDatas.get(newItemPosition).getProgress();
        }

        //�ж���ͬ��item�Ƿ�ı�������һЩ�ֶ�ֵ���ݣ��������������areItemsTheSame()����trueʱ���ŵ��á�
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            SongInfoBean beanOld = mOldDatas.get(oldItemPosition);
            SongInfoBean beanNew = mNewDatas.get(newItemPosition);
//            if (!beanOld.getMusicName().equals(beanNew.getMusicName())) {
//                return false;//��������ݲ�ͬ���ͷ���false
//            }
            if (beanOld.getProgress() != beanNew.getProgress()) {
                return false;//��������ݲ�ͬ���ͷ���false
            }
            return true; //Ĭ������data��������ͬ��
        }

        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // ����ˢ���еĲ��ָ��£�ֻ�޸�item�ı�������ֶΣ��������д���������һ���ֶθı��ˢ������item����
            // Ч����ߡ���������һ�㲻�ðɡ������������д�������Ҫ��дonBindViewHolder�������������ʹ�á�����
            // ��ô����ˢ��ֻ��Ҫ����д�÷�����onBindViewHolder�����������ķ�����
            SongInfoBean oldBean = mOldDatas.get(oldItemPosition);
            SongInfoBean newBean = mNewDatas.get(newItemPosition);

            //����Ƚ�item�����ݣ��仯���򱣴�
            Bundle payload = new Bundle();
            if (oldBean.getProgress() != newBean.getProgress()) {
                payload.putFloat("KEY_DESC", newBean.getProgress());
            }

//            if (oldBean.getPic() != newBean.getPic()) {
//                payload.putInt("KEY_PIC", newBean.getPic());
//            }

            if (payload.size() == 0)//���û�б仯 �ʹ���
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
