package com.pinc.android.MB360.insurance.utilities.ui;


import static com.pinc.android.MB360.BuildConfig.DOWNLAOD_BASE_URL;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.insurance.utilities.repository.responseclass.UTILITIESDATum;
import com.pinc.android.MB360.utilities.FileDownloader;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UtilitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<UTILITIESDATum> utilitiesList;
    Activity activity;
    private boolean[] isDownloading;
    UtilDownloadHelper utilDownloadHelper;

    public UtilitiesAdapter(Context context, List<UTILITIESDATum> utilitiesList, Activity activity, UtilDownloadHelper utilDownloadHelper) {
        this.context = context;
        this.utilitiesList = utilitiesList;
        this.activity = activity;
        isDownloading = new boolean[utilitiesList.size()];
        Arrays.fill(isDownloading, false);
        this.utilDownloadHelper = utilDownloadHelper;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_utilities, parent, false);
        return new UtilitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final UTILITIESDATum util = utilitiesList.get(position);

        ((UtilitiesViewHolder) holder).utilities_name.setText(util.getDownloadDisplayName());

        if (isDownloading[position]) {
            ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
            ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.GONE);
        } else {
            ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.GONE);
            ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.VISIBLE);
        }


        switch (util.getFileType()) {
            case "PDF":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_pdf);
                break;
            case "EXCEL":
            case "XLSX":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_excel);
                break;
            case "DOCX":
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_word);
                break;
            default:
                ((UtilitiesViewHolder) holder).utilities_icon.setImageResource(R.drawable.ic_img);
                break;


        }
        ((UtilitiesViewHolder) holder).item_utilities.setOnClickListener(view -> {
            utilDownloadHelper.onStartDownload(position);
            ((UtilitiesViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
            ((UtilitiesViewHolder) holder).utilities_download.setVisibility(View.GONE);
            try {
                if (Build.VERSION.SDK_INT > 32) {
                    context.startActivity(new DownloadFile(context, activity, util.getFileType().toLowerCase(),
                            utilDownloadHelper, position).downloadFilePDF(util.getSysGenFileName(), util.getFilePath()));
                } else {


                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                        context.startActivity(new DownloadFile(context, activity, util.getFileType().toLowerCase(),
                                utilDownloadHelper, position).downloadFilePDF(util.getSysGenFileName(), util.getFilePath()));

                    } else {
                        utilDownloadHelper.requestPermission(position, util);

                    }
                }



            } catch (ActivityNotFoundException e) {
                utilDownloadHelper.onFinishDownload(position);
                Toast.makeText(context, "No Application found to open this file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                utilDownloadHelper.onFinishDownload(position);
            }
        });

        ((UtilitiesViewHolder) holder).utilities_download.setOnClickListener(view -> {
            ((UtilitiesViewHolder) holder).item_utilities.performClick();
        });


    }

    @Override
    public int getItemCount() {
        if (utilitiesList != null) {
            return utilitiesList.size();
        } else {
            return 0;
        }
    }

    public static class UtilitiesViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_utilities;
        TextView utilities_name;
        ImageView utilities_icon, utilities_download;
        ProgressBar downloadProgress;

        public UtilitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            item_utilities = itemView.findViewById(R.id.item_utilities_card);
            utilities_name = itemView.findViewById(R.id.item_utilities_name);
            utilities_icon = itemView.findViewById(R.id.item_utilities_icon);
            utilities_download = itemView.findViewById(R.id.item_utilities_download);
            downloadProgress = itemView.findViewById(R.id.item_download_progress);

        }
    }

    //download file class
    //creates background thread for downloading the files
    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;
        UtilDownloadHelper utilDownloadHelper;
        int position;


        public DownloadFile(Context context, Activity activity, String extension, UtilDownloadHelper utilDownloadHelper, int position) {
            this.context = context;
            this.activity = activity;
            this.extension = extension;
            this.utilDownloadHelper = utilDownloadHelper;
            this.position = position;
        }

        public Intent downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Future<Intent> future = executors.submit(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        utilDownloadHelper.onFinishDownload(position);
                        return UtilDownloader.downloadFileWithoutPermission(DOWNLAOD_BASE_URL + fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        utilDownloadHelper.onFinishDownload(position);
                        return UtilDownloader.downloadFile(DOWNLAOD_BASE_URL + fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }


            });


            try {
                return future.get();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public void startDownloading(int position) {
        isDownloading[position] = true;

    }

    public void finishDownloading(int position) {
        isDownloading[position] = false;

    }
}
