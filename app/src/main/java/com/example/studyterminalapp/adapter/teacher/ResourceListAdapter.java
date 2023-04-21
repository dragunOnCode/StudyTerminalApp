package com.example.studyterminalapp.adapter.teacher;

import static com.example.studyterminalapp.utils.ResourceConstant.RESOURCE_NOT_FOUND;
import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
import android.os.Environment;
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
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.val;
import rxhttp.wrapper.param.RxHttp;

public class ResourceListAdapter extends BaseAdapter {
    private Context context;
    List<Resource> data;
    private int role; //0学生 1教师 2管理员
    int currentProgress;
    long currentSize;
    long totalSize;
    Thread thread;

    public ResourceListAdapter() {
    }

    public ResourceListAdapter(Context context, int role) {
        this.context = context;
        this.role = role;
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
            if (role == 0) {
                viewHolder.btnDeleteResource.setVisibility(View.INVISIBLE);
                viewHolder.btnDeleteResource.setEnabled(false);
            } else {
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
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, resource.getResourceUrl(), Toast.LENGTH_SHORT).show();
                // 下载资源
                if (StringUtils.equals(resource.getResourceUrl(), RESOURCE_NOT_FOUND)) {
                    ToastUtils.toast("找不到资源");
                    return;
                }
                new MaterialDialog.Builder(context)
                        .content("是否要下载该资源？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String appPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                                String[] split = resource.getResourceUrl().split("\\.");
                                String extension = split[split.length - 1];
                                RxHttp.get(resource.getResourceUrl())
                                        .toDownloadObservable(appPath + "/" + resource.getResourceTitle() + "." + extension)
                                        .onMainProgress(progress -> {
                                            //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                                            currentProgress = progress.getProgress(); //当前进度 0-100
                                            currentSize = progress.getCurrentSize(); //当前已下载的字节大小
                                            totalSize = progress.getTotalSize();     //要下载的总字节大小
                                            Log.e("Download Progress", currentProgress + " | " + currentSize + " : " + totalSize);
                                        })
                                        .subscribe(s -> {                  //s为String类型，这里为文件存储路径
                                            ToastUtils.toast("下载完成");
                                        }, throwable -> {
                                            ToastUtils.toast("下载失败");
                                        });

                                new MaterialDialog.Builder(context)
                                        .title(R.string.tip_download)
                                        .content(R.string.content_downloading)
                                        .contentGravity(GravityEnum.CENTER)
                                        .progress(false, 100, true)
                                        .showListener(
                                                dialogInterface -> {
                                                    updateProgress((MaterialDialog) dialogInterface);
                                                })
                                        .negativeText("取消")
                                        .show();
                            }
                        })
                        .show();
            }
        });
        return view;
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    private void updateProgress(MaterialDialog dialogInterface) {
        final MaterialDialog dialog = dialogInterface;
        startThread(() -> {
            while (dialog.getCurrentProgress() != dialog.getMaxProgress()
                    && !Thread.currentThread().isInterrupted()) {
                if (dialog.isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
                dialog.setProgress(currentProgress);
            }
            runOnUiThread(() -> {
                thread = null;
                dialog.setContent("下载完成");
            });
        });
    }

    class ViewHolder{
        public TextView tvResourceType, tvResourceTitle;
        public Button btnDeleteResource;
    }
}
