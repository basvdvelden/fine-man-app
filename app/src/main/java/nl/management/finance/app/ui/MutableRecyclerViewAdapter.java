package nl.management.finance.app.ui;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import nl.management.finance.app.R;

public interface MutableRecyclerViewAdapter {
    void deleteItem(int pos);

    Context getContext();

    default void showUndoSnackBar(View view, String text, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, action);
        snackbar.show();
    }

    interface OnDelete<T> {
        void delete(T obj);

        void undoDelete(T obj);
    }
}
