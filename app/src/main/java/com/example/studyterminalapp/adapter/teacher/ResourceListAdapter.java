package com.example.studyterminalapp.adapter.teacher;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.bean.Resource;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.RequestManager;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceListAdapter extends BaseAdapter {
    private Context context;
    List<Resource> data;

    public ResourceListAdapter() {
    }

    public ResourceListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<Resource> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data == null ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ResourceListAdapter.ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.resource_item, null);
            viewHolder = new ResourceListAdapter.ViewHolder();
            viewHolder.tvResourceTitle = view.findViewById(R.id.tv_resource_title);
            viewHolder.tvResourceType = view.findViewById(R.id.tv_resource_type);
            viewHolder.btnDeleteResource = view.findViewById(R.id.btn_delete_resource);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ResourceListAdapter.ViewHolder) view.getTag();
        }
        Resource resource = (Resource) getItem(i);
        if (resource != null) {
            String type = "";
            switch (resource.getResourceType()) {
                case "courseware":
                    type = "课件";
                    break;
                case "video":
                    type = "视频";
                    break;
                default:
                    break;
            }
            viewHolder.tvResourceType.setText(type);
            viewHolder.tvResourceTitle.setText(resource.getResourceTitle());
            viewHolder.btnDeleteResource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .content("是否要删除该资源？")
                            .positiveText("是")
                            .negativeText("否")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    HashMap<String, Object> paramsMap = new HashMap<>();
                                    paramsMap.put("rid", resource.getRid());
                                    try {
                                        RequestManager.getInstance().DeleteRequest(paramsMap, Constants.RESOURCE, new RequestManager.ResultCallback() {
                                            @Override
                                            public void onResponse(String responseCode, String response) {
                                                int code = Integer.parseInt(responseCode);
                                                switch (code) {
                                                    case 200:
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                                                data.remove(i);
                                                                notifyDataSetChanged();
                                                            }
                                                        });
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onError(String msg) {
                                                Log.i("Delete Resource", msg);
                                            }
                                        });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }
            });
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, resource.getResourceUrl(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ClassDetailActivity.class);
//                intent.putExtra("chapter", resource);
//                context.startActivity(intent);

            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvResourceType, tvResourceTitle;
        public Button btnDeleteResource;
    }
}
