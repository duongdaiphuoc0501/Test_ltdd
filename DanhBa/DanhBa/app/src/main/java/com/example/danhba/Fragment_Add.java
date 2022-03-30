package com.example.danhba;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class Fragment_Add extends Fragment {
    private Button btnBack, btnSave;
    private EditText txtName2, txtPhone, txtMail;
    private Case_Interface case_interface;
    private ConTact conTact;
    private ImageButton btn_Library, btn_camera;
    private ImageView img_user;
    int REQUEST_CODE_CAMERA = 123, REQUEST_CODE_FOLDER = 456;

    public Fragment_Add(Case_Interface case_interface, ConTact conTact) {
        this.case_interface = case_interface;
        this.conTact = conTact;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        SetUp(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void SetUp(View view){
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        txtName2 = (EditText) view.findViewById(R.id.txtName2);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtMail = (EditText) view.findViewById(R.id.txtMail);
        btn_camera = (ImageButton) view.findViewById(R.id.btn_camera);
        btn_Library = (ImageButton) view.findViewById(R.id.btn_Library);
        img_user = (ImageView) view.findViewById(R.id.img_user);

        if(conTact != null){
            txtName2.setText(conTact.getName());
            txtPhone.setText(conTact.getPhone());
            txtMail.setText(conTact.getEmail());
            Bitmap bitmap = BitmapFactory.decodeByteArray(conTact.getImage(), 0, conTact.getImage().length);
            img_user.setImageBitmap(bitmap);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                case_interface.Back_Fragment();
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityFromFragment(Fragment_Add.this
                        ,intent, REQUEST_CODE_CAMERA);
            }
        });

        btn_Library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityFromFragment(Fragment_Add.this
                        ,intent, REQUEST_CODE_FOLDER);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img_user.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                //mảng byte để xuất ra
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArray);
                byte[] hinh = byteArray.toByteArray();
                ConTact conTact_temp = new ConTact(txtName2.getText().toString(),
                        txtPhone.getText().toString(), txtMail.getText().toString(), hinh);
                if(conTact != null){
                    conTact_temp.setId(conTact.getId());
                }
                case_interface.Add_User(conTact_temp, (conTact == null) ? true : false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");//Key mặc định data
            img_user.setImageBitmap(bitmap);
        }
        //chọn hình trong file;
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_user.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
