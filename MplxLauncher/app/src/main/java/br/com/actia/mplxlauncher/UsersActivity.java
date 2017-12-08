package br.com.actia.mplxlauncher;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import java.util.List;

import br.com.actia.mplxlauncher.Model.AppUser;
import br.com.actia.mplxlauncher.Model.UserDataViewModel;

public class UsersActivity extends LifecycleActivity implements View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    public static final String INTENT_USER = "ADMIN_USER";
    public static final int PSW_SIZE = 6;
    private ConstraintLayout mView;
    private UserDataViewModel viewModel;
    private List<AppUser> userList;
    private ViewSwitcher  mViewSwitcher;
    private ListView      mUserListView;
    private ArrayAdapter  mArrayAdapter;

    private TextInputLayout txtinName;
    private TextInputLayout txtinPsw1;
    private TextInputLayout txtinPsw2;
    private TextInputLayout txtinPsw3;

    private AppUser userAdmin;
    private AppUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Bundle bdl = getIntent().getExtras();
        if(bdl != null) {
            userAdmin = (AppUser) bdl.get(INTENT_USER);
        }

        mView = (ConstraintLayout) findViewById(R.id.usersView_id);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.usersViewSwitcher);
        mUserListView = (ListView) findViewById(R.id.userListView);

        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        Button btnSave = (Button) findViewById(R.id.btn_save);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);

        txtinName = (TextInputLayout) findViewById(R.id.username_textin);
        txtinPsw1 = (TextInputLayout) findViewById(R.id.userpsw1_textin);
        txtinPsw2 = (TextInputLayout) findViewById(R.id.userpsw2_textin);
        txtinPsw3 = (TextInputLayout) findViewById(R.id.userpsw3_textin);

        txtinName.getEditText().setOnFocusChangeListener(this);
        txtinPsw1.getEditText().setOnFocusChangeListener(this);
        txtinPsw2.getEditText().setOnFocusChangeListener(this);
        txtinPsw3.getEditText().setOnFocusChangeListener(this);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null) {
                    return;
                }

                if(currentUser.isAdming()) {
                    Snackbar.make(mView, getString(R.string.user_err_delete_admin), Snackbar.LENGTH_LONG).show();
                    return;
                }

                new AlertDialog.Builder(UsersActivity.this)
                        .setTitle(getString(R.string.delete_alert_title))
                        .setMessage(getString(R.string.delete_alert_text))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                viewModel.deleteUserList(currentUser);
                                Snackbar.make(mView, getString(R.string.user_delete_success), Snackbar.LENGTH_LONG).show();
                                clearFields();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldValidation()) {
                    if(currentUser == null) {
                        currentUser = new AppUser();
                    }

                    currentUser.setName(txtinName.getEditText().getText().toString());
                    currentUser.setPassword(txtinPsw1.getEditText().getText().toString());
                    mViewSwitcher.showNext();
                    txtinPsw3.requestFocus();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtinPsw3.getEditText().setText("");
                mViewSwitcher.showPrevious();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPasswordConfimation()) {
                    viewModel.addUserToList(currentUser);
                    Snackbar.make(mView, getString(R.string.user_save_success), Snackbar.LENGTH_LONG).show();
                    clearFields();
                    mViewSwitcher.showPrevious();
                }
                else {
                    Snackbar.make(mView, getString(R.string.user_err_psw_match), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        //GET LIST OF USERS
        viewModel = ViewModelProviders.of(this).get(UserDataViewModel.class);
        viewModel.getUserList(getBaseContext()).observe(this, new Observer<List<AppUser>>() {
            @Override
            public void onChanged(@Nullable List<AppUser> appUsers) {
                if(userList == null) {
                    userList = appUsers;
                }
                else {
                    userList.clear();
                    userList.addAll(appUsers);
                }

                if(userList != null && !userList.isEmpty()) {
                    if(mArrayAdapter == null) {
                        mArrayAdapter = new ArrayAdapter(UsersActivity.this, android.R.layout.simple_list_item_1, userList);
                        mUserListView.setAdapter(mArrayAdapter);
                        mUserListView.setOnItemClickListener(UsersActivity.this);
                    }
                    else {
                        mArrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if( view == txtinName.getEditText() ) {
            validateName();
        }
        else if( view == txtinPsw1.getEditText()) {
           validadePsw1();
        }
        else if( view == txtinPsw2.getEditText()) {
            validadePsw2();
        }
        else if( view == txtinPsw3.getEditText()) {

        }
    }

    private boolean getPasswordConfimation() {
        String typedPassword = txtinPsw3.getEditText().getText().toString();

        return typedPassword.equals(userAdmin.getPassword());
    }

    private boolean fieldValidation() {
        if(validateName() && validadePsw1() && validadePsw2()) {
            return true;
        }

        return false;
    }

    private boolean validateName() {
        boolean ret = false;
        if (txtinName.getEditText().getText().toString().trim().isEmpty()) {
            txtinName.setError(getString(R.string.user_err_empty));
            //txtinName.requestFocus();
        } else {
            txtinName.setErrorEnabled(false);
            ret = true;
        }

        return ret;
    }

    private boolean validadePsw1() {
        if (txtinPsw1.getEditText().getText().toString().trim().isEmpty()) {
            txtinPsw1.setError(getString(R.string.user_err_empty));
            //txtinPsw1.requestFocus();
            return false;
        } else {
            txtinPsw1.setErrorEnabled(false);
        }

        if (txtinPsw1.getEditText().getText().toString().trim().length() != PSW_SIZE) {
            txtinPsw1.setError(getString(R.string.user_err_psw_len));
            //txtinPsw1.requestFocus();
            return false;
        } else {
            txtinPsw1.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validadePsw2(){
        if (!txtinPsw1.getEditText().getText().toString().trim().equals(txtinPsw2.getEditText()
                .getText().toString().trim())) {
            txtinPsw2.setError(getString(R.string.user_err_psw_match));
            //txtinPsw2.requestFocus();
            return false;
        } else {
            txtinPsw2.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validadePsw3(){
        if (txtinPsw3.getEditText().getText().toString().trim().length() != PSW_SIZE) {
            txtinPsw3.setError(getString(R.string.user_err_psw_len));
            //txtinPsw3.requestFocus();
            return false;
        } else {
            txtinPsw3.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * One list Users item was clicked
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clearFields();
        AppUser appUser = userList.get(position);
        if(appUser != null)
            setCurrentUser(appUser);
    }

    private void setCurrentUser(AppUser currentUser) {
        this.currentUser = currentUser;

        this.txtinName.getEditText().setText(currentUser.getName());
        mViewSwitcher.setDisplayedChild(0);
    }

    private void clearFields() {
        currentUser = null;
        txtinName.getEditText().setText("");
        txtinPsw1.getEditText().setText("");
        txtinPsw2.getEditText().setText("");
        txtinPsw3.getEditText().setText("");
    }
}
