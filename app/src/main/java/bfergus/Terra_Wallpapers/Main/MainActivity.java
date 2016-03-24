package bfergus.Terra_Wallpapers.Main;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import bfergus.Terra_Wallpapers.Faq.FaqActivity;
import bfergus.Terra_Wallpapers. R;
import bfergus.Terra_Wallpapers.Settings.SettingsActivity;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainView, ImageButton.OnClickListener{

    MainPresenter presenter;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.progressTextView)
    TextView progressTV;

    @Bind(R.id.saveWallPaperIBT)
    ImageButton saveWallPaperIBT;

    @Bind(R.id.setWallPaperIBT)
    ImageButton setWallPaperIBT;

    @Bind(R.id.settingsIBT)
    ImageButton settingsIBT;

    @Bind(R.id.helpIBT)
    ImageButton helpIBT;

    @Bind(R.id.Actual_Image_View)
    ImageView actualImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenterImpl(this, this,this);
        saveWallPaperIBT.setOnClickListener(this);
        setWallPaperIBT.setOnClickListener(this);
        settingsIBT.setOnClickListener(this);
        helpIBT.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    public void setImageView(Bitmap bitmap) {

        actualImageView.setImageBitmap(bitmap);
    }

    public void showProgressViews(String msg) {
        progressBar.setVisibility(View.VISIBLE);
        progressTV.setText(msg);
        progressTV.setVisibility(View.VISIBLE);
    }

    public void removeProgressViews() {
        progressBar.setVisibility(View.INVISIBLE);
        progressTV.setVisibility(View.INVISIBLE);
    }

    public void showButtons() {
        saveWallPaperIBT.setVisibility(View.VISIBLE);
        settingsIBT.setVisibility(View.VISIBLE);
        setWallPaperIBT.setVisibility(View.VISIBLE);
        helpIBT.setVisibility(View.VISIBLE);
    }

    public void showToast(String msg) {
        Toast toast  = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void displayAlertDialog(String Title, String Message, String positiveText, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onResume();
            }
        })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }




    @Override
    public void onClick(View v ) {
        switch(v.getId()) {
            case R.id.saveWallPaperIBT:
                presenter.addImageToGallery();
                break;
            case R.id.setWallPaperIBT:
                presenter.startWallpaperLoader();
                break;
            case R.id.settingsIBT:
                presenter.navigateToSettingsScreen();
                break;
            case R.id.helpIBT:
                presenter.navigateToFaqScreen();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
