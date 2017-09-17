package com.example.android.gatheraround;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.example.android.gatheraround.data.MyEventsDatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.path;
import static com.example.android.gatheraround.MainActivity.mMap;
import static com.example.android.gatheraround.R.id.m;

/**
 * Created by tamimazmain on 2017/08/29.
 */

public class EventListCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    Calculations calculations = new Calculations();
    Gson gson = new Gson();
    Context AppContext;
    DatabaseHelper dbHelper;
    Context mContext;
    String[] whereClause;
    Intent mainActivityIntent;
    MyEventsDatabaseHelper myEvents;

    public EventListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppContext = context.getApplicationContext();
        mContext = context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.card_view, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameText = view.findViewById(R.id.event_name);
        TextView timeText = view.findViewById(R.id.event_time);
        TextView dateText = view.findViewById(R.id.event_date);
        TextView summaryText = view.findViewById(R.id.event_summary);
        TextView locationText = view.findViewById(R.id.event_location);
        TextView categoryText = view.findViewById(R.id.event_category);
        CardView card = view.findViewById(R.id.CardViewItem);

        myEvents  = new MyEventsDatabaseHelper(mContext);

        final Cursor mCursor = cursor;

        EventDate eventDate = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_DATE)), EventDate.class);

        String date = calculations.concatenate(eventDate,false,true)[0];
        String time = calculations.concatenate(eventDate,false,true)[1];

        Log.i("CardViewTimeText", "text = " + date);
        Log.i("CardViewTimeText", "text = " + time);

        nameText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));

        Log.i("bindView cursor check", "name=" + mCursor.getString((mCursor.getColumnIndex(DatabaseHelper.COL_NAME))));

        dateText.setText(date);
        timeText.setText(time);
        summaryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY)));
        locationText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATIONNAME)));
        categoryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_CATEGORY)));

        /**
         * POSITION!
         * **/

        final int position = mCursor.getPosition();

        locationText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCursor.moveToPosition(position);
                LatLng location = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);
                final CameraPosition camLocation  = CameraPosition.builder().target(location).zoom(18).build();
                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camLocation));
            }
        });

        summaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater mLayoutInflater;
                mLayoutInflater = LayoutInflater.from(AppContext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflater.inflate(R.layout.summarydialog,null);

                TextView textView = mView.findViewById(R.id.mainText);
                mCursor.moveToPosition(position);
                textView.setText(
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY))
                );

                textView.setMovementMethod(new ScrollingMovementMethod());

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCursor.moveToPosition(position);
                whereClause = new String[] {String.valueOf(mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)))};
                Log.v("Where clause:",whereClause[0]);

                Log.i("Event onLongClick", "name=" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));

                LayoutInflater mLayoutInflater;
                mLayoutInflater = LayoutInflater.from(AppContext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflater.inflate(R.layout.canceldelete_editor,null);

                final Button cancelButton = (Button) mView.findViewById(R.id.cancelAction);
                final Button deleteButton = (Button) mView.findViewById(R.id.deleteEntryAction);
                final Button qrGenButton = (Button) mView.findViewById(R.id.qrAction);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();
                dbHelper = new DatabaseHelper(mContext);
                final SQLiteDatabase db = dbHelper.getWritableDatabase();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        notifyDataSetChanged();
                        Log.v("Where clause changed:",whereClause[0]);
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

                        if((myEvents.checkforExistingEvent(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID))))){
                            DataSenderToServer dataSenderToServer = new DataSenderToServer();
                            dataSenderToServer.eraseEntry(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)));
                            int tru = db.delete
                                    (DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + mCursor.getInt
                                            (mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)), null);
                            if(tru == 1){
                                Log.v("Delete:Server ", "SuccessFull!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                            }else{
                                Log.v("Delete:Server ", "Failed!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                            }
                        }else{
                            int tru = db.delete
                                    (DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + mCursor.getInt
                                            (mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)), null);
                            if(tru == 1){
                                Log.v("Delete:Local ", "SuccessFull!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                            }else{
                                Log.v("Delete:Local ", "Failed!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                            }

                        }

                        dialog.dismiss();

                        mainActivityIntent = new Intent(mContext,MainActivity.class);
                        mContext.startActivity(mainActivityIntent);
                        //LL
                    }
                });
                qrGenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater mLayoutInflator;
                        mLayoutInflator = LayoutInflater.from(AppContext);
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        View mView = mLayoutInflator.inflate(R.layout.qrgenerated,null);

                        final ImageView qrImage = (ImageView) mView.findViewById(R.id.qrImage);
                        Button savetoPhone = (Button) mView.findViewById(R.id.saveQrButton);

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        /////


                        /////

                        try{
                            BitMatrix bitMatrix = multiFormatWriter.encode("gatheraround/"+mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)),
                                    BarcodeFormat.QR_CODE,500,500
                                    );

                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                            qrImage.setImageBitmap(bitmap);

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
                                Log.v("SaveToPhone","Method Called!");
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

}
