package com.hyphenate.easeui.widget.chatrow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;

import java.io.File;

public class EaseChatRowImage extends EaseChatRowFile{

    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }

    
    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
                //set the receive message callback
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                GlideApp.with(BaseApplication.getApplictaion())
                        .load(imgBody.getLocalUrl())
                        .override(180, 180)
                        .into(imageView);
            }
            return;
        }


        String filePath = imgBody.getLocalUrl();
        GlideApp.with(BaseApplication.getApplictaion())
                .load(filePath)
                .override(180, 180)
                .into(imageView);
//        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
//        showImageView(thumbPath, filePath, message);
        handleSendMessage();
    }
    
    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }
    
    @Override
    protected void onBubbleClick() {
        if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            //thumbnail image downloading
            return;
        } else if(imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
            progressBar.setVisibility(View.VISIBLE);
            percentageView.setVisibility(View.VISIBLE);
            // retry download with click event of user
            EMClient.getInstance().chatManager().downloadThumbnail(message);
        }


        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.startActivity(intent);
    }

    /**
     * load image into image view
     * 
     */
//    @SuppressLint("StaticFieldLeak")
//    private void showImageView(final String thumbernailPath, final String localFullSizePath, final EMMessage message) {
//        // first check if the thumbnail image already loaded into cache
//        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
//        if (bitmap != null) {
//            // thumbnail image is already loaded, reuse the drawable
//            imageView.setImageBitmap(bitmap);
//        } else {
//            new AsyncTask<Object, Void, Bitmap>() {
//
//                @Override
//                protected Bitmap doInBackground(Object... args) {
//                    File file = new File(thumbernailPath);
//                    if (file.exists()) {
//                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
//                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
//                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
//                    }
//                    else {
//                        if (message.direct() == EMMessage.Direct.SEND) {
//                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
//                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
//                            } else {
//                                return null;
//                            }
//                        } else {
//                            return null;
//                        }
//                    }
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap image) {
//                    if (image != null) {
//                        imageView.setImageBitmap(image);
//                        EaseImageCache.getInstance().put(thumbernailPath, image);
//                    }
//                }
//            };
//        }
//    }

}
