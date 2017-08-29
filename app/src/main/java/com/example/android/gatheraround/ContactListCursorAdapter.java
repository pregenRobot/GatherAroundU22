package com.example.android.gatheraround;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;

import org.w3c.dom.Text;

/**
 * Created by tamimazmain on 2017/08/28.
 */

public class ContactListCursorAdapter extends CursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */

    private LayoutInflater cursorInflater;





    public ContactListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.contact_individual_list, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView personName = (TextView) view.findViewById(R.id.personName);
        de.hdodenhof.circleimageview.CircleImageView personImage =
                (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.personImage);

        Log.v("ContactCursorAdapter",cursor.getString(cursor.getColumnIndex(ContactsDatabaseHelper.COL_NAME)));
        personName.setText(cursor.getString(cursor.getColumnIndex(ContactsDatabaseHelper.COL_NAME)));

        int imageResource = cursor.getInt(cursor.getColumnIndex(ContactsDatabaseHelper.COL_IMAGE));
        Drawable myImage = context.getResources().getDrawable(imageResource);

        personImage.setImageDrawable(myImage);

    }
}
