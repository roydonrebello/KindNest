// Generated by view binder compiler. Do not edit!
package com.kamui.fooddonation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.kamui.fooddonation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddEmployeeBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText etContactNo;

  @NonNull
  public final EditText etEmpName;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final Button selectImageButton;

  @NonNull
  public final Button submit;

  @NonNull
  public final TextView tvContactNo;

  @NonNull
  public final TextView tvEmpName;

  private ActivityAddEmployeeBinding(@NonNull LinearLayout rootView, @NonNull EditText etContactNo,
      @NonNull EditText etEmpName, @NonNull ImageView imageView, @NonNull Button selectImageButton,
      @NonNull Button submit, @NonNull TextView tvContactNo, @NonNull TextView tvEmpName) {
    this.rootView = rootView;
    this.etContactNo = etContactNo;
    this.etEmpName = etEmpName;
    this.imageView = imageView;
    this.selectImageButton = selectImageButton;
    this.submit = submit;
    this.tvContactNo = tvContactNo;
    this.tvEmpName = tvEmpName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddEmployeeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddEmployeeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_employee, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddEmployeeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.et_contact_no;
      EditText etContactNo = ViewBindings.findChildViewById(rootView, id);
      if (etContactNo == null) {
        break missingId;
      }

      id = R.id.et_emp_name;
      EditText etEmpName = ViewBindings.findChildViewById(rootView, id);
      if (etEmpName == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.select_image_button;
      Button selectImageButton = ViewBindings.findChildViewById(rootView, id);
      if (selectImageButton == null) {
        break missingId;
      }

      id = R.id.submit;
      Button submit = ViewBindings.findChildViewById(rootView, id);
      if (submit == null) {
        break missingId;
      }

      id = R.id.tv_contact_no;
      TextView tvContactNo = ViewBindings.findChildViewById(rootView, id);
      if (tvContactNo == null) {
        break missingId;
      }

      id = R.id.tv_emp_name;
      TextView tvEmpName = ViewBindings.findChildViewById(rootView, id);
      if (tvEmpName == null) {
        break missingId;
      }

      return new ActivityAddEmployeeBinding((LinearLayout) rootView, etContactNo, etEmpName,
          imageView, selectImageButton, submit, tvContactNo, tvEmpName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
