package ru.ancientempires.draws.campaign;

import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.ancientempires.R;
import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptDialog;

public class MyDialog extends MyDialogFragment {

	public void showDialog(GameActivity activity, Bitmap bitmap, String text, ScriptDialog script) {
		Builder builder = new Builder(activity);

		View view = activity.getLayoutInflater().inflate(R.layout.layout_dialog, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageUnit);
		TextView textView = (TextView) view.findViewById(R.id.textUnitName);

		imageView.setImageBitmap(bitmap);
		textView.setText(text);
		builder.setView(view);

		showDialog(activity, builder, script);
	}
}
