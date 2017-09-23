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
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
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
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tamimazmain on 2017/08/29.
 */

class EventListCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private Calculations calculations = new Calculations();
    private Gson gson = new Gson();
    private Context AppContext;
    private DatabaseHelper dbHelper;
    private Context mContext;

    private Intent mainActivityIntent;
    private MyEventsDatabaseHelper myEvents;

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
        TextView finishDateText = view.findViewById(R.id.event_time);
        TextView startDateText = view.findViewById(R.id.event_date);
        TextView summaryText = view.findViewById(R.id.event_summary);
        TextView locationText = view.findViewById(R.id.event_location);
        TextView categoryText = view.findViewById(R.id.event_category);
        CardView card = view.findViewById(R.id.CardViewItem);
        LinearLayout eventNameTopLayout = view.findViewById(R.id.event_name_top_layout);

        TextView summaryTitle = view.findViewById(R.id.summaryTitleTextView);
        LinearLayout summaryLayout = view.findViewById(R.id.summaryLinearLayout);

        myEvents  = new MyEventsDatabaseHelper(mContext);

        final Cursor mCursor = cursor;

        final EventDate eventDate = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_DATE)), EventDate.class);

        String startDate = calculations.concatenate(eventDate, false, true)[0];
        String finishDate = calculations.concatenate(eventDate, false, true)[1];

        if (mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COL_DOESEXISTSONSERVER)) == DatabaseHelper.BOOLEAN_FALSE){
            eventNameTopLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.caution_red));
            Log.i("idNoExist", "id = " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)));

            nameText.setText("(" + context.getResources().getString(R.string.cancel) + ") " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
        }else{
            nameText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
            eventNameTopLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardbackground));
        }

        startDateText.setText(startDate);
        finishDateText.setText(finishDate);
        summaryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY)));
        locationText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATIONNAME)));
        categoryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_CATEGORY)));

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

        summaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSummaryDialog(position);
            }
        });

        summaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSummaryDialog(position);
            }
        });

        summaryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSummaryDialog(position);
            }
        });

        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCursor.moveToPosition(position);
                LayoutInflater mLayoutInflater;
                mLayoutInflater = LayoutInflater.from(AppContext);
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

                        if((myEvents.checkforExistingEvent(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID))))){
                            DataSenderToServer dataSenderToServer = new DataSenderToServer();
                            dataSenderToServer.eraseEntry(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)));
                            db.delete
                                    (DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + mCursor.getInt
                                            (mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)), null);

                        }else{
                            db.delete
                                    (DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + mCursor.getInt
                                            (mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)), null);
                        }

                        dialog.dismiss();

                        mainActivityIntent = new Intent(mContext,MainActivity.class);
                        mContext.startActivity(mainActivityIntent);

                    }
                });
                qrGenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater mLayoutInflator;
                        mLayoutInflator = LayoutInflater.from(AppContext);
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        View mView = mLayoutInflator.inflate(R.layout.qrgenerated,null);

                        final ImageView qrImage = mView.findViewById(R.id.qrImage);
                        Button savetoPhone = mView.findViewById(R.id.saveQrButton);

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        try{
                            BitMatrix bitMatrix = multiFormatWriter.encode("gatheraround/" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)),  BarcodeFormat.QR_CODE,500,500);

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
                            eventNameText = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME));

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

    private void makeSummaryDialog(int position){
        LayoutInflater mLayoutInflater;
        mLayoutInflater = LayoutInflater.from(AppContext);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        View mView = mLayoutInflater.inflate(R.layout.summarydialog, null);

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
}
