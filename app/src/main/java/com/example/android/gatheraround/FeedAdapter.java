package com.example.android.gatheraround;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.example.android.gatheraround.data.MyEventsDatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.zxing.BarcodeFormat.QR_CODE;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder>{

    private List<Events> feedlist;
    Calculations calculations = new Calculations();
    Context mContext;
    Gson gson = new Gson();
    BottomSheetBehavior bottomSheetBehavior;
    View bottomSheet;
    private DatabaseHelper dbHelper;
    private MyEventsDatabaseHelper myEvents;
    Intent mainActivityIntent;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView
                nameText,
                finishDateText,
                startDateText,
                summaryText,
                locationText,
                categoryText,
                summaryTitle;
        public LinearLayout eventNameTopLayout,summaryLayout;
        public CardView card;

        public MyViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.event_name);
            finishDateText = view.findViewById(R.id.event_time);
            startDateText = view.findViewById(R.id.event_date);
            summaryText = view.findViewById(R.id.event_summary);
            locationText = view.findViewById(R.id.event_location);
            categoryText = view.findViewById(R.id.event_category);
            card = view.findViewById(R.id.CardViewItem);
            eventNameTopLayout = view.findViewById(R.id.event_name_top_layout);

            summaryTitle = view.findViewById(R.id.summaryTitleTextView);
            summaryLayout = view.findViewById(R.id.summaryLinearLayout);

        }
    }


    public FeedAdapter(List<Events> feedlistinput, Context Context) {
        this.feedlist = feedlistinput;
        this.mContext = Context;
        myEvents  = new MyEventsDatabaseHelper(mContext);
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Events events = feedlist.get(position);

        String startDate = calculations.concatenate(events.getDate(), false, true)[0];
        String finishDate = calculations.concatenate(events.getDate(), false, true)[1];

        holder.startDateText.setText(startDate);
        holder.finishDateText.setText(finishDate);
        holder.summaryText.setText(events.getEventSummary());
        holder.locationText.setText(events.getLocationName());
        holder.categoryText.setText(events.getCategory());
        holder.nameText.setText(events.getName());

        final int position1 = position;

        holder.locationText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                LatLng location = events.getLocation();
                final CameraPosition camLocation  = CameraPosition.builder().target(location).zoom(18).build();
                MapFragmenttab.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camLocation));
            }
        });

        holder.summaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSummaryDialog(events.getEventSummary());
            }
        });

        holder.summaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSummaryDialog(events.getEventSummary());
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LayoutInflater mLayoutInflater;
                mLayoutInflater = LayoutInflater.from(mContext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflater.inflate(R.layout.canceldelete_editor,null);

                final Button cancelButton = mView.findViewById(R.id.cancelAction);
                final Button deleteButton = mView.findViewById(R.id.deleteEntryAction);
                final Button qrGenButton = mView.findViewById(R.id.qrAction);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();
                dbHelper = new DatabaseHelper(mContext);
                final SQLiteDatabase db = dbHelper.getWritableDatabase();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        notifyDataSetChanged();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor eventCursor = dbHelper.getAllEvents();

                        for(eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                            // The Cursor is now set to the right position

                            Log.v("These are the entries:",eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)));

                        }

                        if((myEvents.checkforExistingEvent(events.getGlobalId()))){
                            Log.v("DeletingNow","event: " + events.getGlobalId() + "server:" + DatabaseHelper.COL_GLOBALID);
                            int CheckOutID = dbHelper.checkoutID(events.getGlobalId());
                            db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + CheckOutID, null);
                            DataSenderToServer dataSenderToServer = new DataSenderToServer();
                            dataSenderToServer.eraseEntry(events.getGlobalId());

                        }else{
                            db.delete
                                    (DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_GLOBALID + " = " + events.getGlobalId(), null);
                        }

                        dialog.dismiss();

                        mainActivityIntent = new Intent(mContext,mapfeed.class);
                        mContext.startActivity(mainActivityIntent);

                    }
                });
                qrGenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater mLayoutInflator;
                        mLayoutInflator = LayoutInflater.from(mContext);
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        View mView = mLayoutInflator.inflate(R.layout.qrgenerated,null);

                        final ImageView qrImage = mView.findViewById(R.id.qrImage);
                        Button savetoPhone = mView.findViewById(R.id.saveQrButton);

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        try{
                            String tempString = events.getGlobalId();
                            BitMatrix bitMatrix = multiFormatWriter.encode("gatheraround/" + tempString,  BarcodeFormat.QR_CODE,500,500);

                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);

                            int textSize = 25;
                            int paddingBottom = 10;
                            int textPaddingLeft = 25;
                            int iconSize = 75;

                            int height = qrCodeBitmap.getHeight() + Math.max(textSize, iconSize) + paddingBottom;
                            int width = qrCodeBitmap.getWidth();

                            Bitmap completeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                            Bitmap iconBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.gatheraround_ic, null);

                            Bitmap iconResize = Bitmap.createScaledBitmap(iconBitmap, iconSize, iconSize, false);

                            Canvas canvas = new Canvas(completeBitmap);

                            canvas.drawARGB(255, 255, 255, 255);

                            canvas.drawBitmap(qrCodeBitmap, 0, 0, null);

                            canvas.drawBitmap(iconResize, textPaddingLeft, paddingBottom + qrCodeBitmap.getHeight() - textSize, null);

                            Paint paint = new Paint();
                            paint.setColor(Color.BLACK);
                            paint.setTextSize((float)textSize);

                            String eventNameText;
                            eventNameText = events.getName();

                            canvas.drawText(eventNameText, textPaddingLeft + iconResize.getWidth() + textPaddingLeft, paddingBottom + qrCodeBitmap.getHeight() + (iconResize.getHeight() - textSize) / 2, paint);

                            int [] allpixels = new int [completeBitmap.getHeight() * completeBitmap.getWidth()];

                            completeBitmap.getPixels(allpixels, 0, completeBitmap.getWidth(), 0, 0, completeBitmap.getWidth(), completeBitmap.getHeight());

                            for(int i = 0; i < allpixels.length; i++)
                            {
                                if(allpixels[i] == Color.BLACK)
                                {
                                    allpixels[i] = Color.rgb(18,153,112);
                                }
                            }

                            completeBitmap.setPixels(allpixels,0,completeBitmap.getWidth(),0, 0, completeBitmap.getWidth(),completeBitmap.getHeight());

                            qrImage.setImageBitmap(completeBitmap);

                        }catch (WriterException e){
                            e.printStackTrace();
                        }

                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();

                        savetoPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BitmapDrawable drawable = (BitmapDrawable) qrImage.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
                                String mImageName="MI_"+ timeStamp +".jpg";
                                createDirectoryAndSaveFile(bitmap,mImageName);
                                Toast.makeText(mContext,"Saved Image to Phone",Toast.LENGTH_SHORT).show();

                            }
                        });

                        dialog.show();

                    }
                });
                return true;
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetCreator(events);
            }
        });

    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/GatherAround");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/storage/emulated/0/Pictures/GatherAround/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/storage/emulated/0/Pictures/GatherAround/"), fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return feedlist.size();
    }
    private void makeSummaryDialog(String info){
        LayoutInflater mLayoutInflater;
        mLayoutInflater = LayoutInflater.from(mContext);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        View mView = mLayoutInflater.inflate(R.layout.summarydialog, null);

        TextView textView = mView.findViewById(R.id.mainText);
        textView.setText(info);

        textView.setMovementMethod(new ScrollingMovementMethod());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void bottomSheetCreator( Events events){
        final Events nowevents = events;

        View bottomSheet = MapFragmenttab.bottomSheet;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet.getLayoutParams().height = MapFragmenttab.maincontent.getHeight();

        MapFragmenttab.eventNamebot.setText(nowevents.getName());

        String date1 = calculations.concatenate(nowevents.getDate(),false,false)[0];
        String date2 = calculations.concatenate(nowevents.getDate(),false,false)[1];

        MapFragmenttab.eventDatebot.setText(date1);
        MapFragmenttab.eventTimebot.setText(date2);
        MapFragmenttab.eventLocationbot.setText(nowevents.getLocationName());
        MapFragmenttab.eventSummarybot.setText(nowevents.getEventSummary());
        MapFragmenttab.eventfollowersbot.setText(nowevents.getParticipants()+"");


        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

    }
}
