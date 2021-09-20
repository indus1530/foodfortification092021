package edu.aku.hassannaqvi.bfwfs20210920.ui.sections;

import static edu.aku.hassannaqvi.bfwfs20210920.core.MainApp.form;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;

import edu.aku.hassannaqvi.bfwfs20210920.R;
import edu.aku.hassannaqvi.bfwfs20210920.contracts.TableContracts;
import edu.aku.hassannaqvi.bfwfs20210920.core.MainApp;
import edu.aku.hassannaqvi.bfwfs20210920.database.DatabaseHelper;
import edu.aku.hassannaqvi.bfwfs20210920.databinding.ActivitySectionW4Binding;
import edu.aku.hassannaqvi.bfwfs20210920.ui.EndingActivity;


public class SectionW4Activity extends AppCompatActivity {
    private static final String TAG = "SectionW4Activity";
    ActivitySectionW4Binding bi;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_w4);
        bi.setCallback(this);
        bi.setForm(form);
    }


    private boolean updateDB() {
        db = MainApp.appInfo.getDbHelper();
        long updcount = 0;
        try {
            updcount = db.updatesFormColumn(TableContracts.FormsTable.COLUMN_SW4, form.sW4toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, getString(R.string.upd_db) + e.getMessage());

            Toast.makeText(this, getString(R.string.upd_db) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (updcount > 0) return true;
        else {
            Toast.makeText(this, getString(R.string.upd_db_error), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void btnContinue(View view) {
        if (!formValidation()) return;
        if (updateDB()) {
            finish();
            Intent i = null;
            int childAge = Integer.parseInt(MainApp.child.getAge());
            if (childAge >= 6 && childAge < 24) {
                i = new Intent(this, SectionC1Activity.class).putExtra("complete", true);
            } else if (childAge >= 24 && childAge < 60) {
                i = new Intent(this, SectionC2Activity.class).putExtra("complete", true);
            } else {
                i = new Intent(this, EndingActivity.class).putExtra("complete", true);
            }
            startActivity(i);
        } else Toast.makeText(this, getString(R.string.fail_db_upd), Toast.LENGTH_SHORT).show();
    }


    public void btnEnd(View view) {
        finish();
        startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
    }


    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }


    @Override
    public void onBackPressed() {
        // Toast.makeText(this, "Back Press Not Allowed", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
    }


    /*@Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Press Not Allowed", Toast.LENGTH_SHORT).show();
    }*/


}